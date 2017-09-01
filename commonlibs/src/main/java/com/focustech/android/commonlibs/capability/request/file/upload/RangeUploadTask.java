package com.focustech.android.commonlibs.capability.request.file.upload;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonlibs.util.device.Device;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * 异步断点上传的任务
 *
 * @author zhangxu
 */
public class RangeUploadTask extends RequestBody implements Callback, Runnable {

    private final String TAG = RangeUploadTask.class.getSimpleName();

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

    static {
        OK_HTTP_CLIENT.setConnectTimeout(30, TimeUnit.SECONDS);
        OK_HTTP_CLIENT.setWriteTimeout(30, TimeUnit.SECONDS);
        OK_HTTP_CLIENT.setReadTimeout(30, TimeUnit.SECONDS);
    }

    private static final String HEAD_DATA_RANGE = "Data-Range";
    private static final String HEAD_DATA_LENGTH = "Data-Length";
    private static final String HEAD_DATA_ID = "Data-Id";
    private static final String HEAD_DATA_VERSION = "Data-Version";
    private static final String HEAD_USER_AGENT = "User-Agent";

    private static final String FILE_CACHE_DIR = Environment.getExternalStorageState();

    private long taskId;
    private TaskCallback callback;

    private String url;
    private List<KeyValue> formData;
    private String fileFormField;
    private String fileName;

    private RandomAccessFile file = null;
    private Buffer buf = new Buffer();

    private long chunkSize = 0;
    private long offset = 0;
    private long total = 0;
    private long read = 0;

    private String dataVersion = null;
    private String dataId = null;
    private InputStream data;

    private boolean first = true;
    private Call lastCall = null;
    private boolean cancel = false;

    private int dataType = 0; //0 file 1 bitmap
    private byte[] bpByte;

    public RangeUploadTask(long taskId, String url, String fileName, String fileFormField, List<KeyValue> formData, long chunkSize, TaskCallback callback) throws IOException {
        if (chunkSize == 0L)
            chunkSize = 1024 * 256;     //如果上传时MTRuntime里面标记的网络状态为NULL，则上传图片时默认用WIFI上传速度进行尝试

        this.taskId = taskId;
        this.callback = callback;
        this.url = url;
        this.fileName = fileName;
        this.formData = formData;
        this.fileFormField = fileFormField;
        this.chunkSize = chunkSize;

        ensureFileStream();
    }

    public RangeUploadTask(long taskId, String url, String fileName, String fileFormField, List<KeyValue> formData, TaskCallback callback) throws IOException {
        this(taskId, url, fileName, fileFormField, formData, 1024 * 256, callback);
    }

    public RangeUploadTask(long taskId, String url, Bitmap bitmap, String fileName, String fileFormField, List<KeyValue> formData, TaskCallback callback) throws IOException {
        this(taskId, url, bitmap, fileName, fileFormField, formData, 1024 * 256, callback);
    }

    public RangeUploadTask(long taskId, String url, Bitmap bitmap, String fileName, String fileFormField, List<KeyValue> formData, long chunkSize, TaskCallback callback) throws IOException {

        if (chunkSize == 0L)
            chunkSize = 1024 * 256;     //如果上传时MTRuntime里面标记的网络状态为NULL，则上传图片时默认用WIFI上传速度进行尝试

        dataType = 1;

        this.taskId = taskId;
        this.callback = callback;
        this.url = url;
        this.fileName = fileName;
        this.formData = formData;
        this.fileFormField = fileFormField;
        this.chunkSize = chunkSize;

        bpByte = bitmap2byte(bitmap);

        ensureByteStream();
    }

    public RangeUploadTask(long taskId, String url, byte[] bpByte, String fileName, String fileFormField, List<KeyValue> formData, long chunkSize, TaskCallback callback) throws IOException {

        if (chunkSize == 0L)
            chunkSize = 1024 * 256;     //如果上传时MTRuntime里面标记的网络状态为NULL，则上传图片时默认用WIFI上传速度进行尝试

        dataType = 1;

        this.taskId = taskId;
        this.callback = callback;
        this.url = url;
        this.fileName = fileName;
        this.formData = formData;
        this.fileFormField = fileFormField;
        this.chunkSize = chunkSize;

        this.bpByte = bpByte;

        ensureByteStream();
    }

    /**
     * 位图转字节数组
     *
     * @param bp
     * @return
     */
    private byte[] bitmap2byte(Bitmap bp) {
        String suffixImg = null;
        if (fileName.lastIndexOf(".") != -1) {
            suffixImg = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        } else {
            suffixImg = "jpg";
        }

        if (bp == null || bp.isRecycled())
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if ("png".equals(suffixImg) || "PNG".equals(suffixImg)) {
            bp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else {
            bp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        bp.recycle();
        return baos.toByteArray();
    }


    @Override
    public void run() {
        try {
            upload();
        } catch (IOException e) {
            close();
            notifyFailure(e);
        }
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(getMimeType(fileName.substring(fileName.lastIndexOf(".") + 1)));
    }

    @Override
    public long contentLength() {
        long length = chunkSize;

        if (offset + chunkSize > total) {
            length = total - offset;
        }

        return length;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        try {
            Source source = Okio.source(data);
            for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                sink.write(buf, readCount);
            }

            if (null != callback) {
                callback.onProcess(taskId, total, offset);
            }
        } catch (Exception e) {
            notifyFailure(e);
        } finally {
            buf.clear();
            read = 0;
        }
    }

    @Override
    public void onFailure(Request request, IOException e) {
        notifyFailure(e);
        close();
    }

    @Override
    public void onResponse(Response response) throws IOException {
        try {
            String body = new String(response.body().bytes());

            if (response.isSuccessful()) {
                JSONObject object = JSONObject.parseObject(body);
                if (object.getIntValue("code") == 0) {
                    offset += contentLength();

                    if (first) {
                        dataVersion = object.getString("version");
                        dataId = object.getString("fileId");

                        if (GeneralUtils.isNullOrEmpty(dataId)) {
                            //TODO 在文件上传时，如果上传服务器出错，会有返回，但是没有dataId.再次提交能成功。此处仅做防范，后面如果有大神可以尝试解决.上传的range为（0--1），导致服务端出错。
                            throw new Exception(RangeUploadTask.class.getSimpleName() + "-->dataId is null");
                        }
                    }

                    if (!complete()) {
                        upload();
                        first = false;
                    } else {
                        bpByte = null;
                        if (null != callback) {
                            notifySuccessful();
                        }
                    }
                } else {
                    callback.onFailure(taskId, new Exception("RANGE UPLOAD CODE:" + object.getIntValue("code")));
                }
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            callback.onFailure(taskId, e);
        }
    }

    public void cancel() {
        try {
            this.cancel = true;
            close();
            if (null != lastCall && !lastCall.isCanceled()) {
                lastCall.cancel();
            }
        } finally {
            callback.onCanceled(taskId);
        }
    }


    private void notifySuccessful() {
        callback.onSuccessful(taskId, dataId);
    }

    private void upload() throws IOException {
        if (cancel) {
            return;
        }
        if (dataType == 1) {
            ensureByteStream();
        } else {
            ensureFileStream();
            file.seek(offset);
        }

        MultipartBuilder mBuilder = new MultipartBuilder().type(MultipartBuilder.FORM)
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"" + fileFormField + "\"; filename=\"" + fileName + "\""), this);

        for (KeyValue keyValue : formData) {
            mBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + keyValue.getKey() + "\""), RequestBody.create(null, keyValue.getValue()));
        }

        Request.Builder build = new Request.Builder()
                .url(url)
                .header(HEAD_DATA_LENGTH, String.valueOf(total))
                .header(HEAD_DATA_RANGE, getRange());

        if (null != dataId) {
            build.header(HEAD_DATA_ID, dataId).header(HEAD_DATA_VERSION, dataVersion).header(HEAD_USER_AGENT, getUserAgent());
        }

        Request request = build.post(mBuilder.build()).build();
        lastCall = OK_HTTP_CLIENT.newCall(request);
        lastCall.enqueue(this);
    }

    public String getUserAgent() {
        try {
            String temp = URLEncoder.encode(Build.MODEL, "utf-8");
            return temp + "_" + Device.getInstance().getAppVersion();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "MODE ERROR" + "_" + Device.getInstance().getAppVersion();
    }

    private String getRange() {
        long endOffset = offset + contentLength() - 1;
        if (endOffset > total - 1) {
            endOffset = total - 1;
        }
        return offset + "-" + endOffset;
    }

    private boolean complete() {
        return offset == total;
    }

    private void notifyFailure(Exception e) {
        if (null != callback) {
            callback.onFailure(taskId, e);
        }
    }

    private static String getMimeType(String extension) {
        String type = "application/octet-stream";

        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }

        return null == type ? "application/octet-stream" : type;
    }

    private void close() {
        if (null != file) {
            try {
                file.close();
            } catch (Exception e) {
            } finally {
                file = null;
            }
        }
    }

    private void ensureFileStream() throws IOException {
        if (file != null) {
            return;
        }

        this.file = new RandomAccessFile(fileName, "r");
        this.total = file.length();

        data = new InputStream() {
            @Override
            public int read() throws IOException {
                if (read == contentLength()) {
                    return -1;
                }

                int value = file.read();

                if (-1 != value) {
                    read++;
                }

                return value;
            }
        };
    }

    private void ensureByteStream() {
        if (bpByte == null) {
            callback.onFailure(taskId, new Throwable("bitmap to byte is null"));
            return;
        }

        this.total = bpByte.length;

        if (total == 0) {
            callback.onFailure(taskId, new Throwable("total == 0"));
            return;
        }

        data = new ByteArrayInputStream(bpByte, (int) offset, (int) contentLength());
    }

}
