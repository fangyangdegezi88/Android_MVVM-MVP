package com.focustech.android.photo.video;

import android.os.CountDownTimer;

/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2016/10/8]
 * @see [相关类/方法]
 * @since [V1]
 */
public class CountTimer extends CountDownTimer {
    /**
     * 是否在运行
     * */
    public boolean mIsRuning = false;
    /**
     * 回调
     * */
    private CountDownCallBack mCallBack;

    /**
     * 参数 millisInFuture 倒计时总时间（如60S，120s等） 参数 countDownInterval 渐变时间（每次倒计1s）
     * <p/>
     * 参数 btn 点击的按钮(因为Button是TextView子类，为了通用我的参数设置为TextView）
     * <p/>
     * 参数 endStrRid 倒计时结束后，按钮对应显示的文字
     */
    /**
     * 参数上面有注释
     */
    public CountTimer(long countTime, CountDownCallBack callBack){
        super(countTime,1000);
        this.mCallBack = callBack;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(mCallBack != null){
            mCallBack.onTick(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        mIsRuning = false;
        if(mCallBack != null){
            mCallBack.onFinish();

        }
    }

    public interface CountDownCallBack{
        void onFinish();
        void onTick(long millisUntilFinished);
    }
}
