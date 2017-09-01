package com.focustech.android.commonlibs.util.downloadManage;

/**
 * <文件下载监听>
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/7/27]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface DownloadListener {
    /**
     * 下载成功
     *
     * @param url 下载的url
     */
    void downloadSuccess(String url);

    /**
     * 下载失败
     *
     * @param url
     */
    void downloadFail(String url);

    /**
     * 下载进度
     *
     * @param process
     */
    void process(int process);
}
