package com.focustech.android.commonlibs;

/**
 * <基础常量>
 *
 * @author yanguozhu
 * @version [版本号, 2016/12/12]
 * @see [相关类/方法]
 * @since [V1]
 */

public class BaseConstant {
    /**
     * 网络请求返回码
     */
    public class HttpCode {
        /**
         * 返回请求成功码
         */
        public static final int SUCESS_CODE_END = 0;

        /**
         * 返回请求失败码
         */
        public static final int FAILED_CODE_END = 000001;

        /**
         * 登录态失效
         */
        public static final int TOKEN_EXPIRED = 10005;

        public static final int LOGIN_EXPIRED = 10011;

        /**
         * 使用此功能必须强制升级
         */
        public static final int MUST_TO_UPGRADE = 60000;
    }

    public enum BaseEvent {
        /**
         * token失效
         */
        TOKEN_INVALID,
        /**
         * 强制升级
         */
        MUST_TO_UPGRADE
    }
}
