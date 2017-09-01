package com.focustech.android.commonlibs.capability.request.file.download;

import com.focustech.android.commonlibs.capability.request.file.IFileRequestResult;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * <网络拦截器>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/22]
 * @see [相关类/方法]
 * @since [V1]
 */
public class ProgressInterceptor implements Interceptor {

    private IFileRequestResult listener;

    public ProgressInterceptor(IFileRequestResult listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), listener))
                .build();
    }

    public IFileRequestResult getListener() {
        return listener;
    }
}
