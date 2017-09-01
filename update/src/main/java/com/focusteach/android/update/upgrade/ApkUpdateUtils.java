package com.focusteach.android.update.upgrade;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.util.BuildConfigUtil;
import com.focustech.android.commonlibs.util.GeneralUtils;

import java.io.File;

/**
 * <辅助工具类>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/8/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class ApkUpdateUtils {
    private static L l = new L(ApkUpdateUtils.class.getSimpleName());

    private static long lastInstallTime = 0l;

    private static void printLog(String tag,String text){
        l.i(text);
        Log.d(tag,text);
    }

    /**
     * <安装应用>
     *
     * @param context
     * @param downloadApkId
     */
    public synchronized static void installApk(Context context, long downloadApkId) {
        if(downloadApkId <= -1){
            return;
        }

        if (System.currentTimeMillis() - lastInstallTime < 1600 && System.currentTimeMillis() - lastInstallTime > 0) {
            lastInstallTime = System.currentTimeMillis();
            return;
        }

        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Intent install = new Intent(Intent.ACTION_VIEW);
        Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
        Cursor cursor = dManager.query(new Query().setFilterById(downloadApkId));
        cursor.moveToFirst();
        String local_uri = "";
        try{
            //Caused by: android.database.CursorIndexOutOfBoundsException: Index 0 requested, with a size of 0
            local_uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
        }catch (Exception e){
            e.printStackTrace();
        }

        if(GeneralUtils.isNullOrEmpty(local_uri)){
            return;
        }

        Uri uri1 = Uri.parse(local_uri);

        if (downloadFileUri != null) {
            //判断是否是AndroidM以及更高的版本
            if (Build.VERSION.SDK_INT >= 23) {
                File file = new File(uri1.getPath());
                try{
                    if (file.exists()) {
                        printLog("ApkDownloadManager", "FLAG_GRANT_READ_URI_PERMISSION" + downloadFileUri.getPath());
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        String authority  = String.valueOf(BuildConfigUtil.getBuildConfigValue(BaseApplication.getContext(), "APPLICATION_ID")) + ".fileprovider";
                        Uri uri = FileProvider.getUriForFile(context, authority, file);
                        printLog("ApkDownloadManager", "installApk uri=" + uri.toString());
                        install.setDataAndType(uri, "application/vnd.android.package-archive");
                    }
                }catch (Exception e){
                    e.printStackTrace();

                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.setDataAndType(uri1, "application/vnd.android.package-archive");
                }
            } else {
                printLog("ApkDownloadManager", "FLAG_ACTIVITY_NEW_TASK");
                printLog("ApkDownloadManager", "installApk path=" + downloadFileUri.toString());
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(uri1, "application/vnd.android.package-archive");
            }
            context.startActivity(install);
        } else {
            printLog("ApkDownloadManager", "download error");
        }
    }

    /**
     * <获取下载状态>
     *
     * @param dm
     * @param downloadId
     * @return
     */
    public static int[] getDownloadStatus(DownloadManager dm, long downloadId) {
        int[] result = new int[4];
        Query query = new Query().setFilterById(downloadId);
        Cursor c = dm.query(query);
        if (c != null) {
            try {
                // Find the column indexes for the data we require.
                int statusIdx = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int reasonIdx = c.getColumnIndex(DownloadManager.COLUMN_REASON);
                int titleIdx = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
                int fileSizeIdx =
                        c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                int bytesDLIdx =
                        c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);

                if (c.moveToFirst()) {
                    // Extract the data we require from the Cursor.
                    int status = c.getInt(statusIdx);
                    int reason = c.getInt(reasonIdx);
                    String title = c.getString(titleIdx);
                    int fileSize = c.getInt(fileSizeIdx);
                    int bytesDL = c.getInt(bytesDLIdx);
                    // Translate the pause reason to friendly text.
                    String reasonString;
                    switch (reason) {
                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                            reasonString = "Waiting for WiFi";
                            break;
                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                            reasonString = "Waiting for connectivity";
                            break;
                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                            reasonString = "Waiting to retry";
                            break;
                        default:
                            reasonString = "Unknown:" + reason;
                            break;
                    }
                    // Construct a status summary
                    StringBuilder sb = new StringBuilder();
                    sb.append(title).append("\n");
                    sb.append(reasonString).append("\n");
                    sb.append("Downloaded ").append(bytesDL).append(" / ").append(fileSize);

                    // Display the status
                    printLog("ApkDownloadManager", sb.toString());
                    result[0] = status;
                    result[1] = bytesDL;
                    result[2] = fileSize;
                    result[3] = reason;
                    return result;
                }
            } finally {
                c.close();
            }
        }
        result[0] = -1;
        return result;
    }

    /**
     * <获取下载状态>
     *
     * @param dm
     * @param downloadId
     * @return
     */
    public static String getLocalUri(DownloadManager dm, long downloadId) {
        Query query = new Query().setFilterById(downloadId);
        Cursor c = dm.query(query);
        if (c != null) {
            try {
                // Find the column indexes for the data we require.
                int localUriIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                int statusIdx = c.getColumnIndex(DownloadManager.COLUMN_STATUS);

                if (c.moveToFirst()) {
                    // Extract the data we require from the Cursor.
                    int status = c.getInt(statusIdx);
                    String uri = c.getString(localUriIdx);
                    // Construct a status summary

                    return uri;
                }
            } finally {
                c.close();
            }
        }
        return "";
    }


}
