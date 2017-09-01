package com.focustech.android.commonlibs.bridge.http;

import android.content.Context;

import com.focustech.android.commonlibs.bridge.BridgeLifeCycleListener;
import com.focustech.android.commonlibs.bridge.cache.localstorage.FileProperty;
import com.focustech.android.commonlibs.capability.request.OkHttpClientHelper;
import com.focustech.android.commonlibs.capability.request.file.IFileRequestResult;
import com.focustech.android.commonlibs.capability.request.file.OkFileUtil;
import com.focustech.android.commonlibs.capability.request.http.ITRequestResult;
import com.focustech.android.commonlibs.capability.request.http.OkHttpUtil;
import com.focustech.android.commonlibs.capability.request.http.Param;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * <http公共解析库>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class OkHttpManager implements BridgeLifeCycleListener {

    @Override
    public void initOnApplicationCreate(Context context) {
        OkHttpUtil.getInstance();
    }

    /**
     * 异步put请求 具体实现
     *
     * @param url             请求url
     * @param activityName    tag
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncPutByTag(String url, String activityName, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncPutByTag(url, activityName, iTRequestResult, clazz, params);
    }

    /**
     * 异步Get请求 泛型返回
     *
     * @param url             请求url
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncGet(String url, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncGetEnqueue(url, iTRequestResult, clazz, params);
    }

    /**
     * 异步Get请求 泛型返回
     *
     * @param url             请求url
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncGet(String url, ITRequestResult<T> iTRequestResult, Class<T> clazz, List<Param> params) {
        OkHttpUtil.getInstance().requestAsyncGetEnqueue(url, iTRequestResult, clazz, params);
    }

    /**
     * 异步Get请求 带tag(关闭页面则取消请求)
     *
     * @param url             请求url
     * @param activityName    请求activityName
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncGetByTag(String url, String activityName, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncGetEnqueueByTag(url, activityName, iTRequestResult, clazz, params);
    }


    /**
     * 异步Get请求 带tag(关闭页面则取消请求)
     *
     * @param url              请求url
     * @param activityName     请求activityName
     * @param iTRequestResult  请求回调
     * @param clazz            Class<T>
     * @param resultBackThread 子线程返回结果
     * @param params           请求参数
     * @param <T>              泛型模板
     */
    public <T> void requestAsyncGetByTag(String url, String activityName, ITRequestResult<T> iTRequestResult, Class<T> clazz, boolean resultBackThread, Param... params) {
        OkHttpUtil.getInstance().requestAsyncGetEnqueueByTag(url, activityName, iTRequestResult, clazz, resultBackThread, params);
    }

    /**
     * 异步POST请求
     *
     * @param url             请求url
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncPost(String url, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncPost(url, iTRequestResult, clazz, params);
    }

    /**
     * 异步POST请求 带tag(关闭页面则取消请求)
     *
     * @param url             请求url
     * @param activityName    请求activityName
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncPostByTag(String url, String activityName, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncPostByTag(url, activityName, iTRequestResult, clazz, params);
    }


    /**
     * 异步DELETE请求
     *
     * @param url             请求url
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncDelete(String url, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncDelete(url, iTRequestResult, clazz, params);
    }

    /**
     * 异步POST请求 单文件上传
     *
     * @param url             请求url
     * @param file            待上传的文件
     * @param key             待上传的key
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     */
    public <T> void requestAsyncPost(String url, File file, String key, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncPost(url, file, key, iTRequestResult, clazz, params);
    }

    /**
     * 异步POST请求 单文件上传 带tag(关闭页面则取消请求)
     *
     * @param url             请求url
     * @param activityName    请求activityName
     * @param file            待上传的文件
     * @param key             待上传的key
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     */
    public <T> void requestAsyncPostByTag(String url, String activityName, File file, String key, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncPostByTag(url, activityName, file, key, iTRequestResult, clazz, params);
    }

    /**
     * 异步POST请求 多文件上传
     *
     * @param url             请求url
     * @param files           待上传的文件s
     * @param keys            待上传文件的keys
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     */
    public <T> void requestAsyncPost(String url, File[] files, String[] keys, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncPost(url, files, keys, iTRequestResult, clazz, params);
    }

    /**
     * 异步POST请求 多文件上传  带tag(关闭页面则取消请求)
     *
     * @param url             请求url
     * @param activityName    请求activityName
     * @param files           待上传的文件s
     * @param keys            待上传文件的keys
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     */
    public <T> void requestAsyncPostByTag(String url, String activityName, File[] files, String[] keys, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncPostByTag(url, activityName, files, keys, iTRequestResult, clazz, params);
    }

    /**
     * 异步POST请求 单图片上传上传
     *
     * @param url             请求url
     * @param files           待上传图片数组
     * @param fileName        待上传图片名
     * @param key             待上传的key
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     */
    public <T> void requestAsyncPost(String url, byte[] files, String fileName, String key, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncPost(url, files, fileName, key, iTRequestResult, clazz, params);
    }

    /**
     * 异步POST请求 单图片上传上传 带tag(关闭页面则取消请求)
     *
     * @param url             请求url
     * @param activityName    请求activityName
     * @param files           待上传图片数组
     * @param fileName        待上传图片名
     * @param key             待上传的key
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     */
    public <T> void requestAsyncPostByTag(String url, String activityName, byte[] files, String fileName, String key, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        OkHttpUtil.getInstance().requestAsyncPostByTag(url, activityName, files, fileName, key, iTRequestResult, clazz, params);
    }

    /**
     * 文件下载
     *
     * @param type         文件类型
     * @param activityName activity
     * @param url          文件下载地址
     * @param listener     下载回调
     * @param fileName     文件名称或ffid
     */
    public void downloadFile(FileProperty type, String activityName, String url, IFileRequestResult listener, String fileName) {
        OkFileUtil.getInstance().downFileByTag(type.getPath(), type.getSuffix(), activityName, url, listener, fileName);
    }

    /**
     * 同步得到输入流
     *
     * @param url 文件地址
     * @return
     */
    public InputStream synDownloadFile(String url) {
        return OkFileUtil.getInstance().synDownFile(url);
    }


    /**
     * 取消正在请求的url
     *
     * @param url
     */
    public void cancelRequest(String url) {
        OkHttpClientHelper.getInstance().cancelRequest(url);
    }

    /**
     * 取消当前页面正在请求的请求
     *
     * @param activity
     */
    public void cancelActivityRequest(String activity) {
        OkHttpClientHelper.getInstance().cancelActivityRequest(activity);
    }

    @Override
    public void clearOnApplicationQuit() {
        OkHttpUtil.getInstance().destory();
        OkFileUtil.getInstance().destory();
    }
}
