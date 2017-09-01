package com.focusteach.android.update.upgrade;

/**
 * <升级策略>
 *
 * @version : V1.0.0
 * @描述 :
 * @user : zhangzeyu
 * @date : 2016/3/11
 */
public enum UpgradePolicy {
    /**
     * 立即升级
     */
    IMMEDIATE,
    /**
     * 稍后重试
     */
    WAIT_RETRY,
    /**
     * 立即打包
     */
    WAIT_PACKAGE_RETRY
}
