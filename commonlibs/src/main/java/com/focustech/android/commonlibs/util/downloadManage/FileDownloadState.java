package com.focustech.android.commonlibs.util.downloadManage;

/**
 * <文件下载的几种状态>
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/7/27]
 * @see [相关类/方法]
 * @since [V1]
 */
public enum FileDownloadState {
    /**
     * 文件正在下载中
     */
    DOWNLOAD_ING,
    /**
     * 文件已经下载完成
     */
    DOWNLOAD_FINISHED,
    /**
     * 文件下载失败
     */
    DOWNLOAD_FAIL,
}
