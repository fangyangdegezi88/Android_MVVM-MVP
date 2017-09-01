package com.focusteach.android.update.upgrade;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.focustech.android.commonlibs.capability.log.L;


/**
 * <下载成功-弹出安装界面>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/8/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class ApkInstallReceiver extends BroadcastReceiver {

    private static final String TAG = "ApkDownloadManager";
    private static L l = new L(TAG);

    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        printLog(TAG, "download complete, id=" + downloadApkId);
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            ApkUpdateUtils.installApk(context, downloadApkId);
            try {
                ApkDownloadManager.getInstance().exitAppIfNeed();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    private void printLog(String tag,String text){
        l.i(text);
        Log.d(tag,text);
    }
}

