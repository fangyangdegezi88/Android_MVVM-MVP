package com.focustech.android.commonlibs.capability.request.http;

import android.os.Handler;
import android.util.Log;

import com.focustech.android.commonlibs.BaseConstant;
import com.focustech.android.commonlibs.capability.json.GsonHelper;
import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.commonlibs.capability.request.BaseResp;
import com.focustech.android.commonlibs.capability.request.OkHttpClientHelper;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonlibs.util.device.Device;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * <http公共解析库>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class OkHttpUtil {
    Handler handler = new Handler();

    private final String TAG = OkHttpUtil.class.getSimpleName();

    private L l = new L(TAG);

    private volatile static OkHttpUtil manager;

    public static OkHttpUtil getInstance() {
        if (manager == null) {
            synchronized (OkHttpUtil.class) {
                if (manager == null) {
                    manager = new OkHttpUtil();
                }
            }
        }
        return manager;
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
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.PUT, url, params));
        addRequestUrl(activityName, url);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).put(body).addHeader("User-Agent", Device.getInstance().getUserAgent()).build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz, activityName));
    }
    /*********************************************************** get请求*********************************************************/

    /**
     * 异步Get请求 具体实现
     *
     * @param url             请求url
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncGetEnqueue(String url, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.GET, url, params));
        String constructUrl = constructUrl(url, params);
        Request request = new Request.Builder()
                .get()
                .url(constructUrl).addHeader("User-Agent", Device.getInstance().getUserAgent())
                .build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz));
    }

    /**
     * 异步Get请求 具体实现 没有code
     *
     * @param url             请求url
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncGetEnqueueWithNoCode(String url, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.GET, url, params));
        String constructUrl = constructUrl(url, params);
        Request request = new Request.Builder()
                .get()
                .url(constructUrl).addHeader("User-Agent", Device.getInstance().getUserAgent())
                .build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz, false));
    }

    /**
     * 异步Get请求 具体实现
     *
     * @param url             请求url
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncGetEnqueue(String url, ITRequestResult<T> iTRequestResult, Class<T> clazz, List<Param> params) {
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.GET, url, params));
        String constructUrl = constructUrl(url, params);
        Request request = new Request.Builder()
                .get()
                .url(constructUrl).addHeader("User-Agent", Device.getInstance().getUserAgent())
                .build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz));
    }

    /**
     * 异步Get请求 具体实现（可取消）
     *
     * @param url             请求url
     * @param activityName    请求activityName
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncGetEnqueueByTag(String url, String activityName, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.GET, url, params));
        addRequestUrl(activityName, url);
        String constructUrl = constructUrl(url, params);
        Request request = new Request.Builder()
                .get()
                .url(constructUrl)
                .tag(url).addHeader("User-Agent", Device.getInstance().getUserAgent())
                .build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz, activityName));
    }

    /**
     * 异步Get请求 具体实现（可取消）
     *
     * @param url             请求url
     * @param activityName    请求activityName
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncGetEnqueueByTag(String url, String activityName, ITRequestResult<T> iTRequestResult, Class<T> clazz, boolean resultBackThread, Param... params) {
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.GET, url, params));
        addRequestUrl(activityName, url);
        String constructUrl = constructUrl(url, params);
        Request request = new Request.Builder()
                .get()
                .url(constructUrl)
                .tag(url).addHeader("User-Agent", Device.getInstance().getUserAgent())
                .build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz, activityName, true, resultBackThread));
    }

    /**
     * 异步Get请求 具体实现（可取消），没有code
     *
     * @param url             请求url
     * @param activityName    请求activityName
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncGetEnqueueByTagWithNoCode(String url, String activityName, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.GET, url, params));
        addRequestUrl(activityName, url);
        String constructUrl = constructUrl(url, params);
        Request request = new Request.Builder()
                .get()
                .url(constructUrl)
                .tag(url).addHeader("User-Agent", Device.getInstance().getUserAgent())
                .build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz, activityName, false));
    }

    /**
     * 构造get请求的url
     *
     * @param url    不带参数的url
     * @param params 参数
     * @return 带参数的url
     */
    public String constructUrl(String url, Param... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (params.length != 0) {
            sb.append("?");
        } else {
            return sb.toString();
        }

        for (Param param :
                params) {
            sb.append(param.key + "=" + param.value + "&");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    /**
     * 构造get请求的url
     *
     * @param url    不带参数的url
     * @param params 参数
     * @return 带参数的url
     */
    public String constructUrl(String url, List<Param> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);

        if (params.size() != 0) {
            sb.append("?");
        } else {
            return sb.toString();
        }
        for (Param param :
                params) {
            sb.append(param.key + "=" + param.value + "&");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }


    /*********************************************************** post请求*********************************************************/
    /**
     * 异步POST请求  具体实现
     *
     * @param url             请求url
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncPost(String url, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.POST, url, params));
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param :
                params) {
            builder.add(param.key, param.value);
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).addHeader("User-Agent", Device.getInstance().getUserAgent()).build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz));
    }

    /**
     * 异步POST请求  具体实现（可取消）
     *
     * @param url             请求url
     * @param activityName    请求activityName
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncPostByTag(String url, String activityName, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.POST, url, params));
        addRequestUrl(activityName, url);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param :
                params) {
            builder.add(param.key, param.value);
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).tag(url).addHeader("User-Agent", Device.getInstance().getUserAgent()).build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz, activityName));
    }


    /**
     * 异步DELETE请求  具体实现
     *
     * @param url             请求url
     * @param iTRequestResult 请求回调
     * @param clazz           Class<T>
     * @param params          请求参数
     * @param <T>             泛型模板
     */
    public <T> void requestAsyncDelete(String url, ITRequestResult<T> iTRequestResult, Class<T> clazz, Param... params) {
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.DELETE, url, params));
        String finalUrl = constructUrl(url, params);
        Request request = new Request.Builder()
                .delete()
                .url(finalUrl).addHeader("User-Agent", Device.getInstance().getUserAgent())
                .build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz));
    }
    /*********************************************************** 文件请求*********************************************************/
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
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.POST, url, params));
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Param param :
                params) {
            builder.addFormDataPart(param.key, param.value);
        }
        builder = constructMultipartBuilder(builder, file, key);
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).addHeader("User-Agent", Device.getInstance().getUserAgent()).build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz));
    }

    /**
     * 异步POST请求 单文件上传（可取消）
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
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.POST, url, params));
        addRequestUrl(activityName, url);
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Param param :
                params) {
            builder.addFormDataPart(param.key, param.value);
        }
        builder = constructMultipartBuilder(builder, file, key);
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).tag(url).addHeader("User-Agent", Device.getInstance().getUserAgent()).build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz, activityName));
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
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.POST, url, params));
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Param param :
                params) {
            builder.addFormDataPart(param.key, param.value);
        }
        for (int i = 0; i < files.length; i++) {
            builder = constructMultipartBuilder(builder, files[i], keys[i]);
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).addHeader("User-Agent", Device.getInstance().getUserAgent()).build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz));
    }

    /**
     * 异步POST请求 多文件上传（可取消）
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
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.POST, url, params));
        addRequestUrl(activityName, url);
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Param param :
                params) {
            builder.addFormDataPart(param.key, param.value);
        }
        for (int i = 0; i < files.length; i++) {
            builder = constructMultipartBuilder(builder, files[i], keys[i]);
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).tag(url).addHeader("User-Agent", Device.getInstance().getUserAgent()).build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz, activityName));
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
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.POST, url, params));
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Param param :
                params) {
            builder.addFormDataPart(param.key, param.value);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), files);
        builder.addFormDataPart(key, fileName, requestBody);
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).addHeader("User-Agent", Device.getInstance().getUserAgent()).build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz));
    }

    /**
     * 异步POST请求 单图片上传上传（可取消）
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
        l.i(LogFormat.formatHttp(LogFormat.HTTP_TYPE.POST, url, params));
        addRequestUrl(activityName, url);
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Param param :
                params) {
            builder.addFormDataPart(param.key, param.value);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), files);
        builder.addFormDataPart(key, fileName, requestBody);
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).tag(url).addHeader("User-Agent", Device.getInstance().getUserAgent()).build();
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new TRequestCallBack(iTRequestResult, clazz, activityName));
    }

    /**
     * 构造多部件builer
     *
     * @param builder 当前实例化MultipartBuilder
     * @param file    待上传文件
     * @param key     对应的参数名
     * @return 构造后的MultipartBuilder
     */
    private MultipartBuilder constructMultipartBuilder(MultipartBuilder builder, File file, String key) {
        String name = file.getName();
        RequestBody requestBody = RequestBody.create(MediaType.parse(guessMimeType(name)), file);
        builder.addFormDataPart(key, name, requestBody);
        return builder;
    }

    /**
     * 获取文件类型
     *
     * @param path
     * @return
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 增加请求标志
     *
     * @param activityName
     * @param url
     */
    private void addRequestUrl(String activityName, String url) {
        OkHttpClientHelper.getInstance().addRequest(activityName, url);
    }

    /**
     * 取消正在请求的url
     *
     * @param url 请求url
     */
    public void cancelRequest(String url) {
        OkHttpClientHelper.getInstance().cancelRequest(url);
    }

    /**
     * 取消当前页面正在请求的请求
     *
     * @param activityName
     */
    public void cancelActivityRequest(String activityName) {
        OkHttpClientHelper.getInstance().cancelActivityRequest(activityName);
    }

    /*************************************************************
     * 回调方法
     *********************************************************/
    class TRequestCallBack<T> implements Callback {

        private ITRequestResult<T> mITRequestResult;

        private Class<T> clazz;

        private String notifyMsg = "";

        private int notifyCode = -1;

        private String activityName;

        private T entity;

        /**
         * 返回值带有code字段
         */
        private boolean hasCode = true;

        /**
         * 是否在子线程中返回结果
         */
        private boolean backThreadResult;

        public TRequestCallBack(ITRequestResult<T> mITRequestResult, Class<T> clazz) {
            this.mITRequestResult = mITRequestResult;
            this.clazz = clazz;
        }

        public TRequestCallBack(ITRequestResult<T> mITRequestResult, Class<T> clazz, boolean hasCode) {
            this.mITRequestResult = mITRequestResult;
            this.clazz = clazz;
            this.hasCode = hasCode;
        }

        public TRequestCallBack(ITRequestResult<T> mITRequestResult, Class<T> clazz, String activityName) {
            this.mITRequestResult = mITRequestResult;
            this.clazz = clazz;
            this.activityName = activityName;
        }

        public TRequestCallBack(ITRequestResult<T> mITRequestResult, Class<T> clazz, String activityName, boolean hasCode) {
            this.mITRequestResult = mITRequestResult;
            this.clazz = clazz;
            this.activityName = activityName;
            this.hasCode = hasCode;
        }

        public TRequestCallBack(ITRequestResult<T> mITRequestResult, Class<T> clazz, String activityName, boolean hasCode, boolean backThreadResult) {
            this.mITRequestResult = mITRequestResult;
            this.clazz = clazz;
            this.activityName = activityName;
            this.hasCode = hasCode;
            this.backThreadResult = backThreadResult;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            l.i(LogFormat.LogModule.NET, LogFormat.Operation.HTTP_REQUEST, "request fail, url:" + request.url().toString() + " exception:" + Log.getStackTraceString(e));
            if (!isHaveActivtyName(activityName)) return;
            notifyMsg = OkHttpClientHelper.HttpErrorMsg.NETWORK_ERROR;
            postErrorMsg();
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (!isHaveActivtyName(activityName)) return;
            if (response.isSuccessful()) {
                String result = response.body().string(); //方法只能调用一次
                l.i(LogFormat.LogModule.NET, LogFormat.Operation.HTTP_REQUEST, "url:" + response.request().urlString() + " result:" + result);
                if (hasCode) {
                    final BaseResp res = GsonHelper.toType(result, BaseResp.class);
                    if (res != null) {
                        int code = Integer.valueOf(res.getCode());
                        T t = GsonHelper.toType(res.getValue(), clazz);
                        switch (code) {
                            case BaseConstant.HttpCode.SUCESS_CODE_END:
                                postSucessMsg(t);
                                break;
                            case BaseConstant.HttpCode.TOKEN_EXPIRED:
                            case BaseConstant.HttpCode.LOGIN_EXPIRED:
                                //自动登录
                                EventBus.getDefault().post(BaseConstant.BaseEvent.TOKEN_INVALID);
                                postError(code, t);
                                break;
                            case BaseConstant.HttpCode.MUST_TO_UPGRADE:
                                EventBus.getDefault().post(BaseConstant.BaseEvent.MUST_TO_UPGRADE);
                                postError(code, t);
                                break;
                            default:
                                postError(code, t);
                                break;
                        }
                    } else {
                        notifyMsg = OkHttpClientHelper.HttpErrorMsg.SERVER_ERROR;
                        postErrorMsg();
                    }
                } else {
                    T t = GsonHelper.toType(result, clazz);
                    postSucessMsg(t);
                }
            } else {
                notifyMsg = OkHttpClientHelper.HttpErrorMsg.NETWORK_ERROR;
                postErrorMsg();
            }
        }

        void postError(int code, T t) {
            notifyCode = code;    //服务端code不为0 也可能有数据  code=-1 为错误
            this.entity = t;
            postErrorMsg();
        }

        /**
         * 主线程发送错误消息
         */
        private void postErrorMsg() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //二次验证，防止在onSuccessful中有mvpView是null的情况
                    if (!isHaveActivtyName(activityName)) return;
                    try {
                        mITRequestResult.onCompleted();
                        mITRequestResult.onFailure(notifyMsg, notifyCode, entity);
                    } catch (Exception e) {
                        l.e("postErrorMsg exception:" + e.toString());
                    }
                }
            });
        }

        /**
         * 主线程发送正确消息
         */
        private void postSucessMsg(final T res) {
            if (backThreadResult) {
                //二次验证，防止在onSuccessful中有mvpView是null的情况
                if (!isHaveActivtyName(activityName)) return;
                //onCompleted 方法在主进程调用，onSuccessful方法，在子线程调用
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //二次验证，防止在onSuccessful中有mvpView是null的情况
                        if (!isHaveActivtyName(activityName)) return;
                        try {
                            mITRequestResult.onCompleted();
                        } catch (Exception e) {
                            l.e("postSucessMsg exception:");
                        }
                    }
                });

                try {
                    mITRequestResult.onSuccessful(res);
                } catch (Exception e) {
                    l.e("postSucessMsg exception:");
                }
            } else
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //二次验证，防止在onSuccessful中有mvpView是null的情况
                        if (!isHaveActivtyName(activityName)) return;
                        try {
                            mITRequestResult.onCompleted();
                            mITRequestResult.onSuccessful(res);
                        } catch (Exception e) {
                            l.e("postSucessMsg exception:");
                        }
                    }
                });
        }

        /**
         * 当前activity是否存在
         *
         * @param activityName
         */
        private boolean isHaveActivtyName(String activityName) {
            if (GeneralUtils.isNotNullOrEmpty(activityName)) {
                return OkHttpClientHelper.getInstance().getRequestMap().containsKey(activityName);
            } else {
                return true;
            }
        }
    }

    public void destory() {
        manager = null;
    }
}
