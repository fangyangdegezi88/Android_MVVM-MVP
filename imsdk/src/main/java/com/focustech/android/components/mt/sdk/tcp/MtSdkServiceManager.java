package com.focustech.android.components.mt.sdk.tcp;

import android.content.Context;

import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.bridge.BridgeLifeCycleListener;
import com.focustech.android.components.mt.sdk.IBizInvokeService;

/**
 * <麦通后台通信服务管理类>
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/6/22]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MtSdkServiceManager implements BridgeLifeCycleListener {

    private static void initConf(Context context) {
        ServiceUtil.getInstance().initConfig(context);
    }

    /**
     * 获取sdkService
     *
     * @return
     */
    public static IBizInvokeService getSdkService() {
        return ServiceUtil.getInstance().getSdkService();
    }

    /**
     * 綁定Service
     */
    public static void bindSDKService() {
        ServiceUtil.getInstance().bindSDKService(BaseApplication.getContext());
    }

    @Override
    public void initOnApplicationCreate(Context context) {
        initConf(context);
    }

    @Override
    public void clearOnApplicationQuit() {

    }
}
