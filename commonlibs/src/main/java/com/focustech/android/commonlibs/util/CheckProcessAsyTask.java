package com.focustech.android.commonlibs.util;

import android.content.Context;
import android.os.AsyncTask;

/**
 * @version : V1.0.0
 * @描述 :
 * @user : yincaiyu
 * @date : 2016/7/27
 */
public class CheckProcessAsyTask extends AsyncTask<String,Integer,Boolean> {
    private Context mContext;

    IProcessCheckFinishedListener processCheckFinishedListener;

    public CheckProcessAsyTask(Context context){
        this.mContext = context;
    }

    public void setProcessCheckFinishedListener(
            IProcessCheckFinishedListener processCheckFinishedListener) {
        this.processCheckFinishedListener = processCheckFinishedListener;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean inBackground = ProcessUtils.isRunInBack(this.mContext);
        return inBackground;
    }

    @Override
    protected void onPostExecute(Boolean inBackground) {
        if (processCheckFinishedListener != null){
            processCheckFinishedListener.onProcessCheckFinished(inBackground);
        }
    }

    public interface IProcessCheckFinishedListener {
        void onProcessCheckFinished(boolean inBackground);
    }
}
