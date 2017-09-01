package com.focustech.android.commonlibs.capability.request.file;


import com.focustech.android.commonlibs.capability.request.http.ITRequestResult;

/**
 * <页面基础公共功能实现>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/22]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface IFileRequestResult extends ITRequestResult {
    /**
     * 文件下载进度监听
     *
     * @param bytesRead
     * @param contentLength
     * @param done
     */
    void update(long bytesRead, long contentLength, boolean done);
}
