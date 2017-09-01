package com.focustech.android.commonlibs.util.downloadManage;

import android.content.ContentValues;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.bridge.BridgeFactory;
import com.focustech.android.commonlibs.bridge.Bridges;
import com.focustech.android.commonlibs.bridge.cache.localstorage.FileProperty;
import com.focustech.android.commonlibs.bridge.http.OkHttpManager;
import com.focustech.android.commonlibs.capability.request.file.IFileRequestResult;
import com.focustech.android.commonlibs.capability.request.http.OkHttpUtil;
import com.focustech.android.commonlibs.capability.request.http.Param;
import com.focustech.android.commonlibs.util.BuildConfigUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <下载管理类，提供管理下载任务，查询任务进度，任务完成情况回调>
 * <p/>
 * 非线程安全，请在主线程中使用
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/7/27]
 * @see [相关类/方法]
 * @since [V1]
 */
public class DownloadManage {

    /**
     * 单例实现
     */
    private static DownloadManage mDownloadManage = new DownloadManage();

    /**
     * 常见文件头信息
     */
    public final static Map<String, String> FILE_TYPE_MAP = new HashMap<>();

    /**
     * 队列中管理下载任务的最大数
     */
    private static int MAX_QUEUE = 200;

    /**
     * 设置观察者
     */
    private List<DownloadListener> listeners = new ArrayList<>();

    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 用于存放
     */
    private LimitLinkedHashMap<String, FileState> mFileHashMap = new LimitLinkedHashMap<>();

    /**
     * 下载文件 hosturl
     */
    private String mBaseDownloadFileUrl;

    /**
     * 下载图片hosturl
     */
    private String mBaseDownloadImgUrl;


    public static DownloadManage getInstance() {
        return mDownloadManage;
    }

    private DownloadManage() {
    }


    /**
     * 下载
     *
     * @param url      文件地址
     * @param type     文件类型
     * @param fileName 文件名称
     */
    public void download(final String url, final FileProperty type, final String fileName) {
        OkHttpManager mOkHttpManager = BridgeFactory.getBridge(Bridges.HTTP, BaseApplication.getContext());

        /**
         * 如果当前请求下载的内容正在下载中或者已经下载过，不在进行下载
         */
        if (mFileHashMap.containsKey(url) &&
                (FileDownloadState.DOWNLOAD_ING == mFileHashMap.get(url).getState() ||
                        FileDownloadState.DOWNLOAD_FINISHED == mFileHashMap.get(url).getState())) {
            return;
        }

        if (!mFileHashMap.containsKey(url)) {   //如果不包含该url
            mFileHashMap.put(url, new FileState(fileName, FileDownloadState.DOWNLOAD_ING, type.getPath()));
        } else if (mFileHashMap.containsKey(url) && //如果下载过 但是失败了,将状态置为加载中
                FileDownloadState.DOWNLOAD_FAIL == mFileHashMap.get(url).getState()) {
            mFileHashMap.get(url).setState(FileDownloadState.DOWNLOAD_ING);
        }

        mOkHttpManager.downloadFile(type, "", url, new IFileRequestResult() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                //暂时不需要更新的listener
            }

            @Override
            public void onSuccessful(Object entity) {
                for (DownloadListener listener :
                        listeners) {
                    if (mFileHashMap.containsKey(url))      //下载成功移除，使用的地方直接判断是否有文件
                        mFileHashMap.remove(url);
                    listener.downloadSuccess(url);
                }

                if (type == FileProperty.JPG || type == FileProperty.PNG) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    values.put(MediaStore.Images.Media.DATA, type.getPath() + fileName + type.getSuffix());
                    values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);
                    values.put(MediaStore.Images.Media.SIZE, fileName.length());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

                    BaseApplication.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                }
            }

            @Override
            public void onFailure(String errorMsg, int errorCode, Object o) {
                for (DownloadListener listener :
                        listeners) {
                    if (mFileHashMap.containsKey(url))      //如果失败，将状态改为失败状态
                        mFileHashMap.get(url).setState(FileDownloadState.DOWNLOAD_FAIL);
                    listener.downloadFail(url);
                }
            }

            @Override
            public void onCompleted() {

            }
        }, fileName);
    }

    /**
     * 下载文件
     *
     * @param fileId 文件id
     * @param type   文件类型
     */
    public void download(String fileId, String token, FileProperty type) {
        String url = constructDownloadUrl(fileId, type, token);
        download(url, type, fileId);
    }

    /**
     * 下载未知类型文件  暂时支持jpg和png
     *
     * @param imgUrl
     * @param fileId
     */
    public void downloadFileWithoutType(final String imgUrl, String fileId, String token) {
        String fileType = getImgType(imgUrl);
        FileProperty fileProperty = null;
        if ("jpg".equals(fileType)) {
            fileProperty = FileProperty.JPG;
        } else if ("png".equals(fileType)) {
            fileProperty = FileProperty.PNG;
        }
        if (fileProperty == null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (DownloadListener listener :
                            listeners) {
                        listener.downloadFail(imgUrl);
                    }
                }
            });

            return;
        }
        download(fileId, token, fileProperty);
    }

    /**
     * 判断是否已经处理过该文件
     *
     * @param url 文件下载的url
     */
    public boolean fileIsIn(String url) {
        return mFileHashMap.containsKey(url);
    }

    /**
     * 得到文件在文件下载器中的状态
     *
     * @param url 文件下载的url
     * @return 文件当前状态 如果不存在则返回失败让下载
     */
    public FileDownloadState getFileState(String url) {
        if (mFileHashMap.containsKey(url)) {
            return mFileHashMap.get(url).getState();
        }
        return FileDownloadState.DOWNLOAD_FAIL;
    }

    /**
     * 得到文件在文件下载器中的状态
     *
     * @param fileId 文件ID
     * @param type   文件类型
     * @return 文件当前状态 如果不存在则返回失败让下载
     */
    public FileDownloadState getFileState(String fileId, FileProperty type, String token) {
        String url = constructDownloadUrl(fileId, type, token);
        if (mFileHashMap.containsKey(url)) {
            return mFileHashMap.get(url).getState();
        }
        return FileDownloadState.DOWNLOAD_FAIL;
    }

    /**
     * 判断文件是否已经下载
     *
     * @param fileName 文件名称
     * @param type
     * @return
     */
    public boolean fileHasDowload(String fileName, FileProperty type) {
        File file = new File(type.getPath() + fileName + type.getSuffix());
        if (file.exists())
            return true;
        return false;
    }


    /**
     * 构造文件下载地址
     *
     * @param fileId 文件ID
     * @param type   文件类型
     * @return
     */
    public String constructDownloadUrl(String fileId, FileProperty type, String token) {
        String hostUrl = null;
        int typeServer = 0;
        judgeUrl();
        if (FileProperty.VOICE == type) {
//            hostUrl = APPConfiguration.getDownloadFilesURL();
            hostUrl = mBaseDownloadFileUrl;
            typeServer = 4;
        } else if (FileProperty.JPG == type || FileProperty.PNG == type) {
            hostUrl = mBaseDownloadImgUrl;
            typeServer = 4;
        } else {
            hostUrl = mBaseDownloadFileUrl;
            typeServer = 4;
        }
        return constructUrl(hostUrl, fileId, typeServer, token);
    }

    /**
     * 判断URL是否初始化   如果没有 通过反射 初始化
     */
    private void judgeUrl() {
        try {
            Class<?> clazz = Class.forName(BuildConfigUtil.getBuildConfigValue(BaseApplication.getContext(), "APPLICATION_ID") + ".comm.APPConfiguration");

            getFileUrl(clazz);
            getImgUrl(clazz);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getImgUrl(Class<?> clazz) {
        Method getImgUrl = null;
        try {
            getImgUrl = clazz.getDeclaredMethod("getDownloadImgURL");
            getImgUrl.setAccessible(true);
            mBaseDownloadImgUrl = (String) getImgUrl.invoke(null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void getFileUrl(Class<?> clazz) {
        Method getFileUrl = null;
        try {
            getFileUrl = clazz.getDeclaredMethod("getDownloadFilesURL");

            getFileUrl.setAccessible(true);
            mBaseDownloadFileUrl = (String) getFileUrl.invoke(null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 构造文件下载地址
     *
     * @param hostUrl
     * @param fileId
     * @param typeServer
     */
    private String constructUrl(String hostUrl, String fileId, int typeServer, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append(hostUrl);
        sb.append("/");
        sb.append(fileId);
        return OkHttpUtil.getInstance().constructUrl(sb.toString(),
                new Param("token", token),
                new Param("type", typeServer));
    }

    public String getImgType(String imgUrl) {
        OkHttpManager mOkHttpManager = BridgeFactory.getBridge(Bridges.HTTP, BaseApplication.getContext());
        try {
            InputStream inputStream = mOkHttpManager.synDownloadFile(imgUrl);

            return getFileByFile(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过文件流得到文件类型
     *
     * @param is
     * @return
     */
    public String getFileByFile(InputStream is) {
        String filetype = null;
        byte[] b = new byte[50];
        try {
            is.read(b);
            filetype = getFileTypeByStream(b);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filetype;
    }

    /**
     * 通过输入流得到文件
     *
     * @param b
     * @return
     */
    public final static String getFileTypeByStream(byte[] b) {
        String filetypeHex = String.valueOf(getFileHexString(b));
        Iterator<Map.Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
        while (entryiterator.hasNext()) {
            Map.Entry<String, String> entry = entryiterator.next();
            String fileTypeHexValue = entry.getValue();
            if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 将字节文件转成字符串
     *
     * @param b
     * @return
     */
    public final static String getFileHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * 添加观察者
     *
     * @param listener
     */
    public void addListener(DownloadListener listener) {
        listeners.add(listener);
    }

    /**
     * 移除观察者
     *
     * @param listener
     */
    public void removeListener(DownloadListener listener) {
        listeners.remove(listener);
    }

    /**
     * 常见文件头信息
     */
    static {
        FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPEG (jpg)
        FILE_TYPE_MAP.put("png", "89504E47");  //PNG (png)
        FILE_TYPE_MAP.put("gif", "47494638");  //GIF (gif)
        FILE_TYPE_MAP.put("tif", "49492A00");  //TIFF (tif)
        FILE_TYPE_MAP.put("bmp", "424D"); //Windows Bitmap (bmp)
        FILE_TYPE_MAP.put("dwg", "41433130"); //CAD (dwg)
        FILE_TYPE_MAP.put("html", "68746D6C3E");  //HTML (html)
        FILE_TYPE_MAP.put("rtf", "7B5C727466");  //Rich Text Format (rtf)
        FILE_TYPE_MAP.put("xml", "3C3F786D6C");
        FILE_TYPE_MAP.put("zip", "504B0304");
        FILE_TYPE_MAP.put("rar", "52617221");
        FILE_TYPE_MAP.put("psd", "38425053");  //Photoshop (psd)
        FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)
        FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)
        FILE_TYPE_MAP.put("pst", "2142444E");  //Outlook (pst)
        FILE_TYPE_MAP.put("xls", "D0CF11E0");  //MS Word
        FILE_TYPE_MAP.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样
        FILE_TYPE_MAP.put("mdb", "5374616E64617264204A");  //MS Access (mdb)
        FILE_TYPE_MAP.put("wpd", "FF575043"); //WordPerfect (wpd)
        FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("qdf", "AC9EBD8F");  //Quicken (qdf)
        FILE_TYPE_MAP.put("pwl", "E3828596");  //Windows Password (pwl)
        FILE_TYPE_MAP.put("wav", "57415645");  //Wave (wav)
        FILE_TYPE_MAP.put("avi", "41564920");
        FILE_TYPE_MAP.put("ram", "2E7261FD");  //Real Audio (ram)
        FILE_TYPE_MAP.put("rm", "2E524D46");  //Real Media (rm)
        FILE_TYPE_MAP.put("mpg", "000001BA");  //
        FILE_TYPE_MAP.put("mov", "6D6F6F76");  //Quicktime (mov)
        FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); //Windows Media (asf)
        FILE_TYPE_MAP.put("mid", "4D546864");  //MIDI (mid)
    }

    public DownloadManage setBaseDownloadFileUrl(String baseDownloadFileUrl) {
        mBaseDownloadFileUrl = baseDownloadFileUrl;
        return this;
    }

    public DownloadManage setBaseDownloadImgUrl(String baseDownloadImgUrl) {
        mBaseDownloadImgUrl = baseDownloadImgUrl;
        return this;
    }
}
