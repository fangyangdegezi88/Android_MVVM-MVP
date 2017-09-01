package com.focustech.android.commonlibs.capability.request.file;

import android.os.Handler;
import android.os.Looper;

import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.capability.request.OkHttpClientHelper;
import com.focustech.android.commonlibs.capability.request.file.download.ProgressInterceptor;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonlibs.util.device.Device;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * <页面基础公共功能实现>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/22]
 * @see [相关类/方法]
 * @since [V1]
 */
public class OkFileUtil {

    Handler handler = new Handler(Looper.getMainLooper());

    private volatile static OkFileUtil manager;

    private final String TAG = OkFileUtil.class.getSimpleName();

    private L l = new L(TAG);

    /**
     * @return FileDownloadManager 单例
     */
    public static OkFileUtil getInstance() {
        if (manager == null) {
            synchronized (OkFileUtil.class) {
                if (manager == null) {
                    manager = new OkFileUtil();
                }
            }
        }
        return manager;
    }

    /**
     * @param rootPath     文件地址根目录
     * @param suffix       文件后缀名
     * @param activityName activity名称
     * @param url          文件下载地址
     * @param listener     下载响应
     * @param fileName     文件名称或ffid
     */
    public <T> void downFileByTag(String rootPath, String suffix, String activityName, String url, final IFileRequestResult listener, String fileName) {
        addRequestUrl(activityName, url);
        OkHttpClientHelper.getInstance().getOkHttpClient().networkInterceptors().add(new ProgressInterceptor(listener));
        Request request = new Request.Builder()
                .url(url)
                .tag(url).addHeader("User-Agent", Device.getInstance().getUserAgent())
                .build();
        //文件下载到本地的地址
        String downPath = rootPath + fileName + suffix;
        OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new FileDownCallback(activityName, listener, downPath));
    }

    /**
     * 同步得到InputStream
     *
     * @param url 文件地址
     * @return
     */
    public InputStream synDownFile(String url) {
        Request request = new Request.Builder()
                .url(url)
                .tag(url).addHeader("User-Agent", Device.getInstance().getUserAgent())
                .build();
        try {
            Response response = OkHttpClientHelper.getInstance().getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream is = response.body().byteStream();
                return is;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件下载回调
     */
    private class FileDownCallback<T> implements Callback {

        private IFileRequestResult listener;

        private String activityName;

        private String notifyMsg = "";

        private int notifyCode = -1;

        private T t;

        /**
         * 文件下载地址
         */
        private String filePath;

        FileDownCallback(String activityName, IFileRequestResult listener, String filePath) {
            this.listener = listener;
            this.activityName = activityName;
            this.filePath = filePath;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            l.e(request.toString() + e.toString());
            if (!isHaveActivtyName(activityName)) return;
            notifyMsg = OkHttpClientHelper.HttpErrorMsg.NETWORK_ERROR;
            postErrorMsg();
        }

        @Override
        public void onResponse(Response response) throws IOException {
            try {
                if (response != null && response.isSuccessful()) {
                    //下载完成，保存数据到文件
                    InputStream is = response.body().byteStream();

                    File file = new File(filePath);

                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int hasRead = 0;
                    while ((hasRead = is.read(buf)) > 0) {
                        fos.write(buf, 0, hasRead);
                    }
                    fos.close();
                    is.close();
                    postSucessMsg(null);
                } else
                    postErrorMsg();
            } catch (Exception e) {
                l.e("onResponse exception:",e);
                postErrorMsg();
            } finally {
                removeInter(listener);
            }
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
                        listener.onCompleted();
                        listener.onFailure(notifyMsg, notifyCode, t);
                    } catch (Exception e) {
                        l.e("postErrorMsg exception:",e);
                    }
                }
            });
        }

        /**
         * 主线程发送正确消息
         */
        private void postSucessMsg(final T res) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //二次验证，防止在onSuccessful中有mvpView是null的情况
                    if (!isHaveActivtyName(activityName)) return;
                    try {
                        listener.onCompleted();
                        listener.onSuccessful(res);
                    } catch (Exception e) {
                        l.e("postSucessMsg exception:",e);
                    }
                }
            });
        }
    }


    /**
     * 删除网络监听
     *
     * @param listener
     */
    private void removeInter(IFileRequestResult listener) {
        List<Interceptor> list = OkHttpClientHelper.getInstance().getOkHttpClient().networkInterceptors();
        for (Interceptor item : list) {
            if (item instanceof ProgressInterceptor) {
                ProgressInterceptor in = (ProgressInterceptor) item;
                if (in.getListener() == listener) {
                    list.remove(item);
                    break;
                }
            }
        }
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

    public void destory() {
        manager = null;
    }
}
