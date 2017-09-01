package com.focustech.android.commonlibs.bridge.cache.localstorage;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.focustech.android.commonlibs.bridge.BridgeLifeCycleListener;
import com.focustech.android.commonlibs.capability.cache.FileUtil;
import com.focustech.android.commonlibs.util.BuildConfigUtil;

import java.io.File;

/**
 * <管理本地文件目录>
 * 对本地存储路径做管理，本地存储分为外部存储（SD卡）和内部存储，项目以外部存储优先，如果没有外部存储，则使用内部存储\
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class LocalFileStorageManager implements BridgeLifeCycleListener {

    /**
     * 家长app的工作目录
     */
    private static String FILE_APP_NAME ;

    /**
     * 列表页面图片缓存目录
     */
    private static final String FOLDER_NAME_IMAGE = "image";

    /**
     * 列表页面视频缓存目录
     */
    private static final String FOLDER_NAME_VIDEO = "video";

    /**
     * 列表页面图片缓存--标识加载原图目录
     */
    private static final String FOLDER_NAME_ORIGINAL_IMAGE_SIGN = "originalSign";

    /**
     * 用户头像缓存目录
     */
    private static final String FOLDER_NAME_HEAD = "head";

    /**
     * 版本更新的目录
     */
    private static final String FOLDER_NAME_VERSION_UPDATE = "update";

    /**
     * 日志目录
     */
    private static final String FOLDER_NAME_LOG = "log";

    /**
     * 下载目录
     */
    private static final String FOLDER_DOWNLOAD = "download";
    /**
     * 语音
     */
    private static final String FOLDER_NAME_VOICE = "voice";
    /**
     * diskLrucache 目錄
     */
    private static String FOLDER_DISK_LRUCACHE = "lrucache";
    /**
     * 系统文件夹dcim
     */
    public static String SYS_DIR_DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/";
    /**
     * 系统文件夹pictures
     */
    public static String SYS_DIR_PIC = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/";
    /**
     * 系统文件夹downloads
     */
    public static String SYS_DIR_DOWNLOADS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/";
    /**
     * 系统文件夹documents
     */
    public static String SYS_DIR_DOCUMENTS = null;

    private Context mContext;

    private static LocalFileStorageManager instance;

    /**
     * 单例，供非主进程使用
     *
     * @return
     */
    public static LocalFileStorageManager getInstance() {
        if (null == instance) {
            instance = new LocalFileStorageManager();
        }
        return instance;
    }

    @Override
    public void initOnApplicationCreate(Context context) {
        mContext = context;
        FILE_APP_NAME = (String)BuildConfigUtil.getBuildConfigValue(context,"APP_WORK_DIR");
        createFilePaths();
    }

    /**
     * 新建文件目录
     */
    private void createFilePaths() {
        //创建图片目录
        getCacheImgFilePath();
        //版本更新目录
        getVersionUpdatePath();
        //日志目录
        getVersionLogPath();
        //语音目录
        getVoicePath();
        //下载目录
        getDownloadPath();
        //diskLrucache 内部目录
        getInterDiskCachePath();
        //diskLrucache sd卡目录
        getExtraDiskCachePath();
        //系统文件夹Documents
        getSystemDirDocuments();
    }

    /**
     * 下载目录
     *
     * @return
     */
    public String getDownloadPath() {
        return FileUtil.createNewFile(getAppWorkFilePath() + FOLDER_DOWNLOAD + File.separator);
    }

    /**
     * <根目录缓存目录>
     * <功能详细描述>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getAppWorkFilePath() {
        return FileUtil.getAppWorkPath(mContext) + File.separator + FILE_APP_NAME + File.separator;
    }


    /**
     * <图片缓存目录>
     * <功能详细描述>
     *
     * @return
     */
    public String getCacheImgFilePath() {
        return FileUtil.createNewFile(getAppWorkFilePath() + FOLDER_NAME_IMAGE + File.separator);
    }

    /**
     * <视频缓存目录>
     * <功能详细描述>
     *
     * @return
     */
    public String getCacheVideoFilePath() {
        return FileUtil.createNewFile(getAppWorkFilePath() + FOLDER_NAME_VIDEO + File.separator);
    }


    /**
     * <图片缓存目录下--标识已加载原图目录>
     * <功能详细描述>
     *
     * @return
     */
    public String getCacheImgFileSignPath() {
        return FileUtil.createNewFile(getCacheImgFilePath() + FOLDER_NAME_ORIGINAL_IMAGE_SIGN + File.separator);
    }

    /**
     * <用户头像目录>
     * <功能详细描述>
     *
     * @param userId
     * @return
     */
    public String getUserHeadPath(String userId) {
        return FileUtil.createNewFile(getAppWorkFilePath() + FOLDER_NAME_IMAGE + File.separator + userId + File.separator);
    }


    /**
     * <版本更新目录>
     * <功能详细描述>
     *
     * @return
     */
    public String getVersionUpdatePath() {
        return FileUtil.createNewFile(getAppWorkFilePath() + FOLDER_NAME_VERSION_UPDATE + File.separator);
    }

    /**
     * <日志目录>
     * <功能详细描述>
     *
     * @return
     */
    public String getVersionLogPath() {
        return FileUtil.createNewFile(getAppWorkFilePath() + FOLDER_NAME_LOG + File.separator);
    }

    /**
     * <语音目录>
     * <功能详细描述>
     *
     * @return
     */
    public String getVoicePath() {
        return FileUtil.createNewFile(getAppWorkFilePath() + FOLDER_NAME_VOICE + File.separator);
    }


    /**
     * <内部DiskLrucache目录>
     *
     * @return
     */
    public String getInterDiskCachePath() {
        return FileUtil.createNewFile(FileUtil.getAppInterPath(mContext) + File.separator + FOLDER_DISK_LRUCACHE + File.separator);
    }

    /**
     * <SD DiskLrucache目录>
     *
     * @return
     */
    public String getExtraDiskCachePath() {
        return FileUtil.createNewFile(getAppWorkFilePath() + FOLDER_DISK_LRUCACHE + File.separator);
    }

    @Override
    public void clearOnApplicationQuit() {

    }

    /**
     * 获得系统文献文件夹
     */
    public void getSystemDirDocuments() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SYS_DIR_DOCUMENTS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/";
        } else {
            SYS_DIR_DOCUMENTS = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/";
        }


    }


}