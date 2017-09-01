package com.focustech.android.components.mt.sdk.communicate;

/**
 * <消息类型>
 *
 * @author yanguozhu
 * @version [版本号, 2016/10/24]
 * @see [相关类/方法]
 * @since [V1]
 */

public enum CommunicationType {
    /**
     * 默认
     */
    DEFAULT(true),
    /**
     * 通知和作业类型
     */
    WORK_NOTICE(true),
    /**
     * 系统透传
     */
    SYSNTY(true),
    /**
     * kickout
     */
    KICKOUT(false);

    /**
     * 对应消息类型，是否显示在通知栏
     */
    public boolean showNotification;

    CommunicationType(boolean showNotification) {
        this.showNotification = showNotification;
    }
}
