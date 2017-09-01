package com.focusteach.android.update.upgrade;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;

import com.focusteach.android.update.R;
import com.focusteach.android.update.constant.UpdateEvent;
import com.focusteach.android.update.sp.FTSharedPrefUpgrade;
import com.focustech.android.commonlibs.ActivityManager;
import com.focustech.android.commonlibs.BaseAppConfiguration;
import com.focustech.android.commonlibs.BaseConstant;
import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.capability.json.GsonHelper;
import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.capability.request.OkHttpClientHelper;
import com.focustech.android.commonlibs.util.BuildConfigUtil;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonlibs.util.NetworkUtil;
import com.focustech.android.commonlibs.util.TaskUtil;
import com.focustech.android.commonlibs.util.device.Device;
import com.focustech.android.commonuis.view.ToastUtil;
import com.focustech.android.commonuis.view.dialog.SFAlertDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;


/**
 * <Apk升级管理>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/8/5]
 * @see [相关类/方法]
 * @since [V1]
 */
public class ApkUpgradeManager implements SFAlertDialog.SFAlertDialogListener {

    private final L log = new L("ApkUpgradeManager");

    public final static String TAG = "FTAppUpgrade";

    private final static long SIX_HOURS = 1000 * 60 * 60 * 6;
    /**
     * 下载文件名前缀
     */
    public static final String FILE_PREFIX = "mtp_focusedu_v";

    private final String mCurrentVersion = (String) BuildConfigUtil.getBuildConfigValue(BaseApplication.getContext(), "VERSION_NAME");

    private volatile static ApkUpgradeManager mApkUpgradeManager = null;
    /**
     * 升级信息
     */
    private FTSharedPrefUpgrade mFTSharedPrefUpgrade = null;
    /**
     * 升级弹窗
     */
    private SFAlertDialog mAlertDialog = null;

    private static final String PREF_NAME_DOWNAPK = "downloadApk";

    private static final String PREF_NAME_UPGRADE = "myapk_upgrade"; // APK升级

    /**
     * 应用启动以来检查过一次升级
     */
    private boolean mCheckOnce;

    private final Handler handler = new Handler();

    private Timer timer;

    private ApkUpgradeManager() {
        mFTSharedPrefUpgrade = new FTSharedPrefUpgrade(BaseApplication.getContext(), PREF_NAME_UPGRADE);

        EventBus.getDefault().register(this);
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static ApkUpgradeManager getInstance() {
        if (mApkUpgradeManager == null) {
            synchronized (ApkUpgradeManager.class) {
                if (mApkUpgradeManager == null) {
                    mApkUpgradeManager = new ApkUpgradeManager();
                }
            }
        }

        return mApkUpgradeManager;
    }

    /**
     * 从启动到结束,只进行一次的升级检查
     */
    public boolean checkForUpgradeOnlyOnce(String userName) {
        if (mCheckOnce) {
            return false;
        }
        mCheckOnce = true;
        checkForNewVersion(userName);
        return true;
    }

    /**
     * <检查升级>
     */
    public void checkForUpgrade(String userName) {
        if (tokenInValidTime()) {
            Log.d(TAG, "offline check mode.");
            String version = mFTSharedPrefUpgrade.getTargetVersion();
            String action = mFTSharedPrefUpgrade.getUpgradeAction();
            String desc = mFTSharedPrefUpgrade.getUpgradeDesc();
            if (isLatestVersion(mCurrentVersion, version, action)) {
                Log.d(TAG, version + " is the latest version");
            } else { // 之前被取消了或者被杀进程了
                remindUserToUpgrade(version, action, desc);
            }
        } else {
            checkForNewVersion(userName);
        }
    }

    /**
     * <检查新版本-第一步请求>
     *
     * @param isAutoCheck    是否是主动检查升级  如果是主动检查升级 不会走整个升级流程  只会返回检查结果
     * @param isDirectlyDown 是否是直接下载
     */
    public void checkForNewVersion(final boolean isAutoCheck, final boolean isDirectlyDown,String domainName) {
        OkHttpClientHelper.getInstance().getOkHttpClient()
                .newCall(getRequest(getFormattedCheckUpgradeUrl(domainName)))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.w(TAG, request.toString() + e.toString());
                        if (!isAutoCheck)
                            EventBus.getDefault().post(UpdateEvent.CHECK_UPGRADE_FAIL);

                        EventBus.getDefault().post(UpdateEvent.CHECK_UPGRADE_COMPLETE); // 检查升级失败
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            Log.i(TAG, result);
                            CheckNewVersionResponse rep = GsonHelper.toType(result, CheckNewVersionResponse.class);
                            if (rep != null) {

                                if (UpgradeAction.PRE_DOWNLOAD.name().equals(rep.getUpgradeAction())) {
                                    // 暂时不支持预下载
                                    EventBus.getDefault().post(UpdateEvent.UN_NEED_UPGRADE);
                                    return;
                                }

                                String serverVersion = rep.getUpgradeVersion();
                                if (isLatestVersion(mCurrentVersion, serverVersion, rep.getUpgradeAction())) {      //如果当前版本是最新版本
                                    Log.d(TAG, serverVersion + " is the latest version");
                                    if (!isAutoCheck) {         //如果是主动升级
                                        EventBus.getDefault().post(UpdateEvent.UN_NEED_UPGRADE);
                                    } else {
                                        if (isDirectlyDown) {   //如果是主动升级
                                            ToastUtil.showFocusToastWithoutActivity(BaseApplication.getContext(), "当前无升级版本");
                                        }
                                    }
                                    EventBus.getDefault().post(UpdateEvent.CHECK_UPGRADE_COMPLETE); // 不需要升级
                                } else {
                                    if (!isAutoCheck) {
                                        EventBus.getDefault().post(UpdateEvent.NEED_UPGRADE);
                                    }
                                    Log.d(TAG, "you need upgrade to version " + serverVersion);

                                    final String token = GeneralUtils.isNullToEmpty(rep.getToken());
                                    final String version = GeneralUtils.isNullToEmpty(rep.getUpgradeVersion());
                                    final String action = GeneralUtils.isNullToEmpty(rep.getUpgradeAction());
                                    final String desc = GeneralUtils.isNullToEmpty(rep.getDesc());
                                    final String validTime = GeneralUtils.isNullToEmpty(rep.getValidTime());
                                    mFTSharedPrefUpgrade.setUpgradeToken(token);
                                    mFTSharedPrefUpgrade.setTargetVersion(version);
                                    mFTSharedPrefUpgrade.setUpgradeAction(action);
                                    mFTSharedPrefUpgrade.setUpgradeDesc(desc);
                                    mFTSharedPrefUpgrade.setUpgradeValidTime(validTime);
                                    // 记录下最后一次请求成功的时间
                                    mFTSharedPrefUpgrade.setLastTimeCheckForUpgrade(System.currentTimeMillis());
                                    if (isAutoCheck) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                requestUpgradeAdvise(token, version, action, desc, isDirectlyDown);
                                            }
                                        }).start();
                                    }
                                }
                            } else {
                                if (!isAutoCheck)
                                    EventBus.getDefault().post(UpdateEvent.CHECK_UPGRADE_FAIL);
                                EventBus.getDefault().post(UpdateEvent.CHECK_UPGRADE_COMPLETE); // 检查升级失败
                            }
                        } else {
                            if (!isAutoCheck)
                                EventBus.getDefault().post(UpdateEvent.CHECK_UPGRADE_FAIL);
                            EventBus.getDefault().post(UpdateEvent.CHECK_UPGRADE_COMPLETE);  // 检查升级失败
                        }
                    }
                });
    }

    /**
     * 自动检查升级
     */
    private void checkForNewVersion(String userName) {
        checkForNewVersion(true, false,userName);
    }

    /**
     * <检查Token在有效时间内>
     *
     * @return
     */
    private boolean tokenInValidTime() {
        String token = mFTSharedPrefUpgrade.getUpgradeToken();
        if (GeneralUtils.isNotNullOrEmpty(token)) {
//            long validTime = Long.parseLong(mFTSharedPrefUpgrade.getUpgradeValidTime());
            /* 考虑到backend后台有可能回退，所以有效时间改为1min，1min内读上次请求的结果，1min后重发请求 */
            long validTime = 60 * 1000L;
            long lastTimeCheckForUpgrade = mFTSharedPrefUpgrade.getLastTimeCheckForUpgrade();
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastTimeCheckForUpgrade > 0 && nowTime - lastTimeCheckForUpgrade <= validTime) {
                return true;
            }
        }

        return false;
    }

    /**
     * <比较本机版本和服务器最新版本>
     *
     * @param serverVersion
     * @param upgradeAction
     * @return true  不升级； false 升级
     */
    private boolean isLatestVersion(String localVersion, String serverVersion, String upgradeAction) {
        return isLocalAppVersionLatest(localVersion, serverVersion) || UpgradeAction.NONE.name().equals(upgradeAction);
//        return localVersion.equals(serverVersion) || UpgradeAction.NONE.name().equals(upgradeAction);
    }

    /**
     * 形如http://imweb.focusteach.com/tm/upgrade.json?currentVersion=x.x.x.x&userNameWithDomain=${domain}_${userName}
     *
     * @return
     */
    private String getFormattedCheckUpgradeUrl(String userName) {
        String url = BaseAppConfiguration.getCheckCurrentVersionUrl() + mCurrentVersion
                + "&userNameWithDomain=" + getUserNameWithDomain(userName);
        Log.d(TAG, url);
        return url;
    }

    /**
     * <返回userNameWithDomain参数>
     *
     * @return String 形如${domain}_${userName}
     */
    private String getUserNameWithDomain(String userName) {
        return BaseAppConfiguration.getUpgradeDomain() + "_" + userName;
    }

    public ApkUpgradeManager setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
        return this;
    }

    private String currentUserName = null;

    /**
     * <得到当前用户UserName>
     *
     * @return String userName ,如果得不到，则返回"none"
     */
    private String getUserName() {
//        UserSession userSession = UserSession.getInstance();
//        UserBase userBase = userSession.getUserBase();
//        if (userBase != null) {
//            String userName = userBase.getUserName();
//            if (GeneralUtils.isNotNullOrEmpty(userName)) {
//                return userName;
//            }
//        }


        return currentUserName == null ? "none" : currentUserName;
    }

    public void onEventMainThread(BaseConstant.BaseEvent event) {
        if (event == BaseConstant.BaseEvent.MUST_TO_UPGRADE) {
            ToastUtil.showFocusToastWithoutActivity(BaseApplication.getContext(), BaseApplication.getContext().getString(R.string.is_old_please_upgrade));
        }
    }

    /**
     * <请求升级建议>
     *
     * @param token
     * @param version
     * @param action
     * @param desc
     * @param isDirectlyDown
     */
    private void requestUpgradeAdvise(final String token, final String version, final String action, final String desc, final boolean isDirectlyDown) {
        String url = BaseAppConfiguration.getUpdateAdviseUrl() + token;
        Log.d(TAG, url);
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(getRequest(url))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.w(TAG, request.toString() + e.toString());
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            Log.i(TAG, result);
                            CheckUpgradeAdviseResponse rsp = GsonHelper.toType(result, CheckUpgradeAdviseResponse.class);
                            if (rsp != null) {
                                /**
                                 * backend提供可下载的链接有2个，
                                 * 其一是字段中给出的下载地址，
                                 * 其二是用上token拼接的url
                                 */
                                String downloadUrl = rsp.getDownload();
                                String spareDownloadUrl = BaseAppConfiguration.getUpdateDownloadUrl() + token;// 长时间不动，token可能失效
                                mFTSharedPrefUpgrade.setDownloadUrl(downloadUrl);
                                mFTSharedPrefUpgrade.setSpareDownloadUrl(spareDownloadUrl);
                                Log.d(TAG, "the provided download url is " + downloadUrl);
                                if (UpgradePolicy.IMMEDIATE.name().equals(rsp.getPolicy())) {
                                    if (isDirectlyDown) {
                                        linkCheckAndDownload();
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                remindUserToUpgrade(version, action, desc);
                                            }
                                        });
                                    }
                                } else {
                                    Log.w(TAG, "i cannot upgrade immediately result from policy=" + rsp.getPolicy());
                                }
                            } else {
                                Log.w(TAG, "server error.");
                            }
                        } else {
                            Log.w(TAG, "network error:" + response.code());
                        }
                    }
                });
    }

    /**
     * <提示用户升级>
     *
     * @param version
     * @param action
     * @param desc
     */
    private void remindUserToUpgrade(String version, String action, String desc) {
        Context context = BaseApplication.getContext();
        String title = context.getString(R.string.upgrade);
        String content;
        String singleBtn = context.getString(R.string.upgrade_right_now);
        String formatDesc = GeneralUtils.isNullOrEmpty(desc) ? "" : "\n" + desc; // 换行
        int contentGravity = GeneralUtils.isNullOrEmpty(desc) ? Gravity.CENTER : Gravity.LEFT;
        if (action.equals(UpgradeAction.MUST_UPGRADE.name())) {
            /**
             * 发现新版本x.x.x.x，建议您立即升级\n
             * desc...
             */
            content = context.getString(R.string.has_published_new_version) +
                    version + context.getString(R.string.upgrade_immediately) + formatDesc;
            showAlert(title, content, singleBtn, SFAlertDialog.MTDIALOG_THEME.HAS_TITLE_ONE, contentGravity);
        } else if (action.equals(UpgradeAction.ADVISE_UPGRADE.name())) {
            /**
             * 发现新版本x.x.x.x，请立即升级\n
             * desc...
             */
            content = context.getString(R.string.has_published_new_version) +
                    version + context.getString(R.string.advise_update_now)
                    + formatDesc;
            /*
             * 规则：非强制升级在点击稍后再说，那么六小时内不再提醒
             */
            boolean inAboutPage = TextUtils.equals(ActivityManager.getInstance().getLastActivity().getClass().getSimpleName(), "AboutActivity");
            if (inAboutPage || moreThanSixHours(version)) {
                showAlert(title, content, "", SFAlertDialog.MTDIALOG_THEME.HAS_TITLE_TWO, contentGravity);
                mAlertDialog.setOKText(context.getString(R.string.upgrade_right_now));
                mAlertDialog.setCancelText(context.getString(R.string.upgrade_put_off));
            }
        } else if (action.equals(UpgradeAction.PRE_DOWNLOAD.name())) {
            // 暂时不支持预下载
        }
    }

    /**
     * 超过6小时的判断，如果超过6小时，当前时间会被计入sp
     *
     * @param version
     * @return
     */
    private boolean moreThanSixHours(String version) {

        long time = mFTSharedPrefUpgrade.getAdviseUpgradeAlertTime(version);
        long now = System.currentTimeMillis();
        if (now - time > 0 && now - time < SIX_HOURS) { // 6小时内
            EventBus.getDefault().post(UpdateEvent.CHECK_UPGRADE_COMPLETE); // 非强制升级，6小时内不弹窗时
            log.i("advise upgrade->check moreThan six hours. false");
            return false;
        }
        mFTSharedPrefUpgrade.setAdviseUpgradeAlertTime(version, now);
        log.i("advise upgrade->check moreThan six hours. true");
        return true;
    }

    /**
     * 弹窗
     *
     * @param content
     * @param theme
     */
    void showAlert(String title, String content, String btnText, SFAlertDialog.MTDIALOG_THEME theme, int contentGravity) {
        if (null != mAlertDialog && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        mAlertDialog = new SFAlertDialog(ActivityManager.getInstance().getLastActivity(), title, content, btnText, theme, contentGravity);
        mAlertDialog.setSFAlertDialogListener(this);
        /*
         * 注释的代码虽为全局弹窗，但是在小米6.0上不兼容
         */
//            int type;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                type = WindowManager.LayoutParams.TYPE_TOAST;
//            } else {
//                type = WindowManager.LayoutParams.TYPE_PHONE;
//            }
//            mAlertDialog.getWindow().setType(type);// 指定会全局,可以在后台弹出
        mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
        startTokenInvalidTimer(); // 设置检查token
    }

    /**
     * <辅助方法>
     *
     * @param url
     * @return
     */
    Request getRequest(String url) {
        return new Request.Builder()
                .get()
                .url(url).addHeader("User-Agent", Device.getInstance().getUserAgent())
                .build();
    }

    /**
     * <非强制升级-稍后再说>
     * 记录下时间
     */
    @Override
    public void clickCancel(int tag) {
        cancelTokenInvalidTimer();
        EventBus.getDefault().post(UpdateEvent.CHECK_UPGRADE_COMPLETE); // 非强制升级，稍后再说，检查流程结束
    }

    /**
     * <非强制升级-立即升级>
     *
     * @param input
     */
    @Override
    public void clickOk(String input, int tag) {
        cancelTokenInvalidTimer();
        linkCheckAndDownload();
    }

    /**
     * <强制升级立即升级>
     *
     * @param input
     */
    @Override
    public void singleOk(String input, int tag) {
        cancelTokenInvalidTimer();
        linkCheckAndDownload();
    }

    private void linkCheckAndDownload() {
        TaskUtil.schedule(linkCheck, 20, TimeUnit.MILLISECONDS);
    }

    /**
     * 链接
     */
    private Runnable linkCheck = new Runnable() {
        int tryCount = 0;

        @Override
        public void run() {
            if (NetworkUtil.isNetworkConnected()) {
                log.i("linkCheck ok! tryCount=" + tryCount);
                downloadApk();
            } else {
                if (tryCount < 3) {
                    log.i("linkCheck failed! tryCount=" + tryCount);
                    TaskUtil.schedule(this, 600, TimeUnit.MILLISECONDS);
                    tryCount++;
                } else if (tryCount == 3) {
                    BaseApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showFocusToast(ActivityManager.getInstance().getLastActivity(), R.string.common_toast_net_null);
                            /*
                             * 强制升级网络异常就退出程序
                             */
                            if (UpgradeAction.MUST_UPGRADE.name().equals(mFTSharedPrefUpgrade.getUpgradeAction())) {
                                ActivityManager.getInstance().exit();
                            }
                        }
                    });
                    tryCount++;
                }
            }
        }
    };

    /**
     * 升级Token失效后的任务
     */
    private class UptokenInvalidTask extends TimerTask {
        @Override
        public void run() {
            boolean inAboutPage = TextUtils.equals(ActivityManager.getInstance().getLastActivity().getClass().getSimpleName(), "AboutActivity");
            String action = mFTSharedPrefUpgrade.getUpgradeAction();
            if (!inAboutPage && UpgradeAction.ADVISE_UPGRADE.name().equals(action)) {// 非强制升级，最后弹窗时间清零，这样检查完成后，检查时间会再次更新
                String version = mFTSharedPrefUpgrade.getTargetVersion();
                mFTSharedPrefUpgrade.setAdviseUpgradeAlertTime(version, 0L); // 清空当前版本的提示时间，以下次提示时间为准
            }
            /*
             * 由于小米手机长时间休眠，自动断网的特性，重新检查升级更新token并不可行，所以在此时结束进程为好
             */
            ActivityManager.getInstance().exit();
        }
    }

    ;

    /**
     * 解决立即升级窗口长时间放置失效的（20-30min），token失效的问题
     */
    private void startTokenInvalidTimer() {
        cancelTokenInvalidTimer();
        if (timer == null) {
            timer = new Timer("TokenCheckTimer");
            timer.schedule(new UptokenInvalidTask(), TimeUnit.MINUTES.toMillis(10));
        }
    }

    private void cancelTokenInvalidTimer() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

    /**
     * <触发下载Apk>
     */
    private void downloadApk() {
        String apkName = FILE_PREFIX + mFTSharedPrefUpgrade.getTargetVersion() + ".apk";
        String desc = GeneralUtils.isNullToEmpty(mFTSharedPrefUpgrade.getUpgradeDesc());
        String downloadUrl = mFTSharedPrefUpgrade.getDownloadUrl();
        String spareDownloadUrl = mFTSharedPrefUpgrade.getSpareDownloadUrl();
        /*
         *  根据强制非强制的区别，设置是否阻塞用户操作
         */
        if (UpgradeAction.MUST_UPGRADE.name().equals(mFTSharedPrefUpgrade.getUpgradeAction())) {
            ApkDownloadManager.getInstance().setBlockUserAction(true);
        } else {
            ApkDownloadManager.getInstance().setBlockUserAction(false);
        }
        log.i(String.format("downloadApk downloadUrl{%s} spareUrl{%s}",
                downloadUrl != null ? downloadUrl : "null",
                spareDownloadUrl != null ? spareDownloadUrl : "null"));
        if (GeneralUtils.isNotNullOrEmpty(downloadUrl)) {
            if (GeneralUtils.isNotNullOrEmpty(spareDownloadUrl)) {
                ApkDownloadManager.getInstance().downloadApk(apkName, desc, downloadUrl, spareDownloadUrl);
            } else {
                ApkDownloadManager.getInstance().downloadApk(apkName, desc, downloadUrl);
            }
        } else if (GeneralUtils.isNotNullOrEmpty(spareDownloadUrl)) {
            ApkDownloadManager.getInstance().downloadApk(apkName, desc, spareDownloadUrl);
        } else {//app 打开情况下，发布建议升级，点击升级，其实这个时候没有获得升级数据（卸载重装第一次）；漏洞
            String token = mFTSharedPrefUpgrade.getUpgradeToken();
            String version = mFTSharedPrefUpgrade.getTargetVersion();
            String action = mFTSharedPrefUpgrade.getUpgradeAction();
            requestUpgradeAdvise(token, version, action, desc, true);
        }
    }

    /**
     * 判断是否为最新版本方法 将版本号根据.切分为int数组 比较
     *
     * @param localVersion  本地版本号
     * @param onlineVersion 线上版本号
     * @return
     */
    private boolean isLocalAppVersionLatest(String localVersion, String onlineVersion) {
        try {
            if (localVersion.equals(onlineVersion)) {
                return true;
            }
            String[] localArray = localVersion.split("\\.");
            String[] onlineArray = onlineVersion.split("\\.");

            int length = localArray.length < onlineArray.length ? localArray.length : onlineArray.length;

            for (int i = 0; i < length; i++) {
                if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(localArray[i])) {
                    return false;
                } else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(localArray[i])) {
                    return true;
                }
                // 相等 比较下一组值
            }
            return false;
        } catch (Exception e) {
            log.i(e.toString());
            return false;
        }
    }

}
