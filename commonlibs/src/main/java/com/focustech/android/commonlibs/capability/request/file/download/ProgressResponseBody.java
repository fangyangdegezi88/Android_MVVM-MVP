package com.focustech.android.commonlibs.capability.request.file.download;

import com.focustech.android.commonlibs.capability.request.file.IFileRequestResult;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * <页面基础公共功能实现>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/22]
 * @see [相关类/方法]
 * @since [V1]
 */
public class ProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    //    private final ProgressListener progressListener;
    private BufferedSource bufferedSource;
    private IFileRequestResult listener;

    public ProgressResponseBody(ResponseBody responseBody, IFileRequestResult listener) {
        this.responseBody = responseBody;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        try {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        } catch (Exception e) {
            return null;
        }

    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                listener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
