package com.focusteach.android.update.upgrade;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.net.Uri;
import android.util.Log;

import com.focusteach.android.update.R;
import com.focusteach.android.update.sp.FTSharedPrefDownApk;
import com.focustech.android.commonlibs.ActivityManager;
import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.bridge.cache.localstorage.FileProperty;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonlibs.util.NetworkUtil;
import com.focustech.android.commonuis.view.ToastUtil;
import com.focustech.android.commonuis.view.dialog.SFAlertDialog;

import java.io.File;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <下载Apk>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/8/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class ApkDownloadManager implements SFAlertDialog.SFAlertDialogListener {

    private static final String TAG = "ApkDownloadManager";

    private static final int TAG_BACKGROUND = 2;

    private static final int TAG_REDOWNLOAD = 3;

    private Timer timer;

    /**
     * 最后一次弹出对话框的Activity
     */
    private String lastActivity;

    private static ApkDownloadManager mInstance;


    private static final String PREF_NAME_DOWNAPK = "downloadApk";

    private ApkDownloadManager() {
        mDownloadManager = (DownloadManager) BaseApplication.getContext().getSystemService(Service.DOWNLOAD_SERVICE);
        mFTSharedPrefDownApk = new FTSharedPrefDownApk(BaseApplication.getContext(), PREF_NAME_DOWNAPK);
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static ApkDownloadManager getInstance() {
        if (mInstance == null) {
            synchronized (ApkUpgradeManager.class) {
                if (mInstance == null) {
                    mInstance = new ApkDownloadManager();
                }
            }
        }

        return mInstance;
    }

    /**
     * 官方提供的下载Apk的方式
     */
    private DownloadManager mDownloadManager;

    private FTSharedPrefDownApk mFTSharedPrefDownApk = null;

    private SFAlertDialog mAlertDialog;

    /**
     * 下载时阻止用户的其他操作
     */
    private boolean blockUserAction = false;

    public void setBlockUserAction(boolean blockUserAction) {
        this.blockUserAction = blockUserAction;
    }

    /**
     * 后台工作
     */
    private boolean backgroundWork = false;

    public void downloadApk(String apkName, String desc, String downloadUrl) {
        downloadApk(apkName, desc, downloadUrl, "");
    }

    /**
     * <下载Apk>
     *
     * @param apkName
     * @param desc
     * @param downloadUrl
     * @param spareDownloadUrl
     * @return
     */
    public void downloadApk(String apkName, String desc, String downloadUrl, String spareDownloadUrl) {
        backgroundWork = false;  // 移动到前台

        if (asyncDownloadApkTask(downloadUrl, spareDownloadUrl)) {
            return;
        }

        // 新的任务
        mFTSharedPrefDownApk.setDownloadApkName(apkName);
        mFTSharedPrefDownApk.setDownloadApkDesc(desc);
        mFTSharedPrefDownApk.setCurrentDownloadUrl(downloadUrl);
        mFTSharedPrefDownApk.setSpareDownloadUrl(spareDownloadUrl);

        privateDownloadApk(apkName, desc, downloadUrl, true);

    }

    /**
     * <同步当前下载Apk的任务>
     *
     * @param downloadUrl
     * @param spareDownloadUrl
     * @return
     */
    private boolean asyncDownloadApkTask(String downloadUrl, String spareDownloadUrl) {
        String oldDownloadUrl = mFTSharedPrefDownApk.getCurrentDownloadUrl();
        Long oldDownloadId = mFTSharedPrefDownApk.getCurrentDownloadId();
        if (GeneralUtils.isNotNullOrEmpty(oldDownloadUrl)) {
            if (oldDownloadId != null && oldDownloadId > 0) {
                if (oldDownloadUrl.equals(downloadUrl) || oldDownloadUrl.equals(spareDownloadUrl)) {
                    int[] array = ApkUpdateUtils.getDownloadStatus(mDownloadManager, oldDownloadId);
                    int status = array[0];
                    if (status != -1) {
                        String uri = ApkUpdateUtils.getLocalUri(mDownloadManager, oldDownloadId);
                        if (GeneralUtils.isNullOrEmpty(uri)) { // 老任务但是uri丢失了
                            mDownloadManager.remove(oldDownloadId);
                            return false;
                        }
                        File file = new File(URI.create(uri));
                        if (file.exists()) {
                            switch (status) {
                                case DownloadManager.STATUS_PENDING:
                                    myToast(R.string.wait_for_download, 0);
                                    break;
                                case DownloadManager.STATUS_RUNNING:
                                    myToast(R.string.downloading, 0);
                                    break;
                                case DownloadManager.STATUS_SUCCESSFUL:
                                    break;
                                default:// 失败或者暂停的任务删除
                                    mDownloadManager.remove(oldDownloadId);
                                    return false;
                            }
                            cancelTimer();
                            createTimer();
                            // 还是老的任务
                            DownloadMonitor downloadMonitor = new DownloadMonitor(oldDownloadUrl, oldDownloadId);

                            timer.schedule(downloadMonitor, 0L);
                        } else { // 老任务，但是文件已经被删除了
                            mDownloadManager.remove(oldDownloadId);
                            return false;
                        }
                    } else {
                        // 没找到该任务
                        return false;
                    }
                    // 开始同步
                    return true;
                } else {
                    // 没同步上，移除老的
                    mDownloadManager.remove(oldDownloadId);
                    return false;
                }
            }
        }

        return false;
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void createTimer() {
        if (timer == null) {
            timer = new Timer();
        }
    }

    /**
     * <下载Apk>
     *
     * @param apkName
     * @param desc
     * @param downloadUrl
     * @return
     */
    private long privateDownloadApk(String apkName, String desc, String downloadUrl, boolean toastStart) {
        if (GeneralUtils.isNotNullOrEmpty(downloadUrl)) {
            Log.d(TAG, "request downloadUrl=" + downloadUrl);
//            String encoded = Uri.encode(downloadUrl, "?/:=&");
            Uri reqUri = Uri.parse(downloadUrl);
            Log.d(TAG, "encoded downloadUri=" + reqUri.toString());
            DownloadManager.Request request = new DownloadManager.Request(reqUri);

            String restorePath = FileProperty.APK.getPath() + apkName;
            File restoreFile = new File(restorePath);
            Uri destinationUri = Uri.fromFile(restoreFile);
            Log.d(TAG, "destinationUri=" + destinationUri);

            request.setDestinationUri(destinationUri);
            request.setTitle(BaseApplication.getContext().getString(R.string.app_name));
            if (GeneralUtils.isNotNullOrEmpty(desc))
                request.setDescription(desc);
            request.setVisibleInDownloadsUi(true);
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            /**
             * 表示下载进行中和下载完成的通知栏是否显示。默认只显示下载中通知。
             * VISIBILITY_VISIBLE_NOTIFY_COMPLETED表示下载完成后显示通知栏提示。
             * VISIBILITY_HIDDEN表示不显示任何通知栏提示，这个需要在AndroidMainfest中添加权限android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.
             */
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            // request.setMimeType("application/cn.trinea.download.file");

            if (toastStart) myToast(R.string.start_download, 0);
            long downloadId = mDownloadManager.enqueue(request);
            mFTSharedPrefDownApk.setCurrentDownloadId(downloadId);
            cancelTimer();
            createTimer();
            DownloadMonitor mDownloadMonitor = new DownloadMonitor(downloadUrl, downloadId);

            timer.schedule(mDownloadMonitor, 0L);
            return downloadId;
        }

        return 0L;

    }

    /**
     * 提示开始下载
     */
    private void myToast(final int str, final int progress) {
        BaseApplication.mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (backgroundWork) {
                    if (progress <= 0)
                        ToastUtil.showFocusToast(BaseApplication.getContext(), str);
                } else {
                    doShowDialog(BaseApplication.getContext().getString(str), progress);
                }
            }
        });
    }

    /**
     * <显示提示>
     *
     * @param title
     */
    private void doShowDialog(String title, int progress) {
        Activity current = ActivityManager.getInstance().getLastActivity();
        Log.d(TAG, "doShowDialog from" + current.toString());
        if (!current.toString().equals(lastActivity)) {
            /*
             * 如果之前绑定的Activity不存在了，就需要重新初始化Dialog,不然会报
             *  WindowManager$BadTokenException
             *  Unable to add window -- token android.os.BinderProxy@fce43da is not valid; is your activity running?
             *  这样的类似错误
             */
            if (mAlertDialog != null) {
                mAlertDialog.dismiss();
                mAlertDialog = null;
            }
        }
        if (null == mAlertDialog) {
            lastActivity = ActivityManager.getInstance().getLastActivity().toString();
            mAlertDialog = new SFAlertDialog(ActivityManager.getInstance().getLastActivity(), title);
        } else {
            mAlertDialog.setContent(title);
            mAlertDialog.setTitle(title);
        }

        if (!mAlertDialog.isShowing())
            mAlertDialog.show();


        if (blockUserAction) {
            mAlertDialog.setDialogTheme(SFAlertDialog.MTDIALOG_THEME.NO_TITLE_NONE_PROGRESS);
        } else {
            mAlertDialog.setDialogTheme(SFAlertDialog.MTDIALOG_THEME.NO_TITLE_ONE_PROGRESS);
            if (title.equals(BaseApplication.getContext().getString(R.string.download_failed_cannot_resume))) {
                mAlertDialog.setTag(TAG_REDOWNLOAD);
                mAlertDialog.setSingleBtnText(BaseApplication.getContext().getString(R.string.i_know_download_again));
            } else if (title.equals(BaseApplication.getContext().getString(R.string.download_failed))) {
                mAlertDialog.setTag(-1);
                mAlertDialog.setSingleBtnText(BaseApplication.getContext().getString(R.string.cancel));
            } else {
                mAlertDialog.setTag(TAG_BACKGROUND);
                mAlertDialog.setSingleBtnText(BaseApplication.getContext().getString(R.string.background_download));
            }
        }
        mAlertDialog.setProgress(progress);
        mAlertDialog.setSFAlertDialogListener(this);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(false);
    }

    @Override
    public void clickCancel(int tag) {
        if (tag == TAG_REDOWNLOAD) {
            backgroundWork = false;
            redownloadCurrent();
        } else if (tag == TAG_BACKGROUND) {
            backgroundWork = true;
        } else {
            backgroundWork = false;
        }
    }

    @Override
    public void clickOk(String input, int tag) {

    }

    @Override
    public void singleOk(String input, int tag) {
        if (tag == TAG_REDOWNLOAD) {
            backgroundWork = false;
            redownloadCurrent();
        } else if (tag == TAG_BACKGROUND) {
            backgroundWork = true;
        } else {
            backgroundWork = false;
        }
    }

    /**
     * 重新下载当前的
     */
    private void redownloadCurrent() {
        String currentUrl = mFTSharedPrefDownApk.getCurrentDownloadUrl();
        Log.d(TAG, "restart download url=" + currentUrl);
        if (GeneralUtils.isNotNullOrEmpty(currentUrl)) {
            long newId = privateDownloadApk(mFTSharedPrefDownApk.getDownloadApkName()
                    , mFTSharedPrefDownApk.getDownloadApkDesc(), currentUrl, false);

            mFTSharedPrefDownApk.setCurrentDownloadId(newId);
        }
    }

    /**
     * 检查文件下载状态
     */
    private class DownloadMonitor extends TimerTask {
        private long downloadId = 0;
        private String downloadUrl = null;

        public DownloadMonitor(String downloadUrl, long downloadId) {
            this.downloadUrl = downloadUrl;
            this.downloadId = downloadId;
        }

        @Override
        public void run() {
            if (downloadId > 0) {
                int[] array = ApkUpdateUtils.getDownloadStatus(mDownloadManager, downloadId);
                int status = array[0];
                int fileSize = array[2];
                Log.d(TAG, "status=" + status);
                int progress = 0;
                if (array[2] > 0 && array[1] >= 0) {
                    progress = (int) (array[1] * 100f / array[2]);
                }
                int reason = array[3];
                switch (status) {
                    case -1:
                    case DownloadManager.STATUS_FAILED:
                        switch (reason) {
                            case DownloadManager.ERROR_CANNOT_RESUME:
                                cannotResume();
                                break;
                            case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                                inSufficientSpace();
                                break;
                            default:
                                // 下载失败
                                useSpareDownloadUrl();
                                break;
                        }
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        myToast(R.string.wait_for_install, 100);
                        if (!blockUserAction) {
                            if (mAlertDialog != null) mAlertDialog.dismiss();
                        }
                        // 下载成功
                        ApkUpdateUtils.installApk(ActivityManager.getInstance().getLastActivity(), downloadId);
                        exitAppIfNeed();
                        break;
                    case DownloadManager.STATUS_PAUSED:
                        if (!NetworkUtil.isNetworkConnected()) {// 无网络连接，因为这里的轮训，paused_status会因为数据库更新不及时导致不准，所以这里及时判断一下网络情况
                            myToast(R.string.paused_wait_for_connect, progress);
                        } else {// 下载暂停
                            switch (reason) {
                                case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                                case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                                    myToast(R.string.paused_wait_for_connect, progress);
                                    break;
                                case DownloadManager.PAUSED_WAITING_TO_RETRY:
                                    String mSpareUrl = mFTSharedPrefDownApk.getSpareDownloadUrl();
                                    if (GeneralUtils.isNotNullOrEmpty(mSpareUrl) && fileSize == -1) { /**
                                     * 华为不断重复着STATUS_RUNNING和STATUS_PAUSED，不会跳入失败，所以需要手动干掉
                                     */
                                        useSpareDownloadUrl();
                                    } else {
                                        /**
                                         * fileSize != -1时正正好就是小米魅族等手机手动暂停的情况
                                         * 魅族可以在通知栏点击并停止
                                         * 小米可以在后台点击停止下载
                                         */
                                        myToast(R.string.paused_wait_for_retry, progress);
                                    }
                                    break;
                                default:
                                    myToast(R.string.paused, progress);
                                    break;
                            }
                        }
                        if (timer != null) {
                            timer.schedule(new DownloadMonitor(downloadUrl, downloadId), 1000L);
                        }
                        break;
                    case DownloadManager.STATUS_RUNNING:
                        myToast(R.string.downloading, progress);
                        Log.d(TAG, "progress=" + progress);
                        if (timer != null) {
                            timer.schedule(new DownloadMonitor(downloadUrl, downloadId), 1000L);
                        }
                        break;
                    case DownloadManager.STATUS_PENDING:
                    default:
                        // 下载未成功且不是未找到的状态
                        if (timer != null) {
                            timer.schedule(new DownloadMonitor(downloadUrl, downloadId), 1000L);
                        }
                        break;
                }
            }
        }

        /**
         * 不能恢复下载
         */
        private void cannotResume() {
            if (mDownloadManager != null) {
                mDownloadManager.remove(downloadId);
                Log.d(TAG, "download from url=" + downloadUrl + "\n failed");
                myToast(R.string.download_failed_cannot_resume, 0);
            }
        }

        /**
         * 没有足够的内存空间
         */
        private void inSufficientSpace() {
            if (mDownloadManager != null) {
                mDownloadManager.remove(downloadId);
            }
            Log.d(TAG, "download from url=" + downloadUrl + "\n failed, insufficient space.");
            myToast(R.string.download_failed_insufficient_space, 0);
        }

        /**
         * 使用备用下载地址
         */
        private void useSpareDownloadUrl() {
            if (mDownloadManager != null) {
                mDownloadManager.remove(downloadId);
                Log.d(TAG, "download from url=" + downloadUrl + "\n failed");
                String mSpareUrl = mFTSharedPrefDownApk.getSpareDownloadUrl();
                if (GeneralUtils.isNotNullOrEmpty(mSpareUrl)) {
                    Log.d(TAG, "use spare download url=" + mSpareUrl);
                    // drop original download url
                    mFTSharedPrefDownApk.setCurrentDownloadUrl(mSpareUrl);
                    mFTSharedPrefDownApk.setSpareDownloadUrl("");
                    long newId = privateDownloadApk(mFTSharedPrefDownApk.getDownloadApkName()
                            , mFTSharedPrefDownApk.getDownloadApkDesc(), mSpareUrl, false);

                    mFTSharedPrefDownApk.setCurrentDownloadId(newId);
                } else {
                    myToast(R.string.download_failed, 0);
                }
            }
        }

    }

    public void exitAppIfNeed() {
        if (blockUserAction) {
            ActivityManager.getInstance().exit();
        }
    }

}
