package com.focusteach.android.update.constant;

/**
 * <*****>
 *
 * @author liuzaibing
 * @version [版本号, 2017/6/20]
 * @see [相关类/方法]
 * @since [V1]
 */
public enum UpdateEvent {
    /**
     * 需要更新
     */
    NEED_UPGRADE,
    /**
     * 当前版本最新
     */
    UN_NEED_UPGRADE,
    /**
     * 检查更新失败
     */
    CHECK_UPGRADE_FAIL,
    /**
     * 检查升级的流程结束-通知检查密码复杂度
     */
    CHECK_UPGRADE_COMPLETE,
}
