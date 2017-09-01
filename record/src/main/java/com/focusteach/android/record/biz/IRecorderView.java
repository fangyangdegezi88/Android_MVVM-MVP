package com.focusteach.android.record.biz;

import android.text.SpannableString;

import com.focustech.android.commonuis.biz.IMvpView;


/**
 * @author zhangzeyu
 * @version [版本号, 2016/7/11]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface IRecorderView extends IMvpView {

    /**
     * <更新录音时长>
     *
     * @param time
     * @param timeStr
     */
    public void updateRecorderLength(long time, String timeStr);

    /**
     * <更新剩余时间>
     *
     * @param timeStr
     */
    public void updateRemainingTime(SpannableString timeStr);

    /**
     * <提示出错>
     *
     * @param message
     */
    public void alertError(String message);

    /**
     * <更新分贝值(音量)>
     *
     * @param db
     */
    public void updateDb(double db);

    /**
     * <录音时间超长>
     */
    public void recordTimeout();

    /**
     * <提示没有录音权限>
     */
    public void alertNoPermission();

    /**
     * 显示没有权限
     */
    void showNoPer();
}
