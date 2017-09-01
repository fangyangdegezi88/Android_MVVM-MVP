package com.focustech.android.components.mt.sdk.tcp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.focustech.android.commonlibs.BaseAppConfiguration;
import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.util.BuildConfigUtil;
import com.focustech.android.commonlibs.util.TaskUtil;
import com.focustech.android.components.mt.sdk.IBizInvokeCallback;
import com.focustech.android.components.mt.sdk.IBizInvokeService;
import com.focustech.android.components.mt.sdk.android.IntentAction;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * <SDK Service 能力类>
 *
 * @author yanguozhu
 * @version [版本号, 2016-6-24]
 * @see [相关类/方法]
 * @since [V1]
 */
public class ServiceUtil {
    private L l = new L(ServiceUtil.class);
    public static ServiceUtil instance;
    private IBizInvokeService sdkService;
    protected String TAG = ServiceUtil.class.getSimpleName();

    private long delay = 20;
    /**
     * sdk绑定成功
     */
    public static boolean mBindSuccessful;

    public static ServiceUtil getInstance() {
        if (null == instance)
            instance = new ServiceUtil();
        return instance;
    }

    public void initConfig(final Context mContext) {
        // 初始化基本配置信息
        BaseAppConfiguration.init(mContext);
        //初始化sdk中线程池
        initThreadPool();
        // 绑定service
        bindSDKService(mContext);

        //--后台Service进程偶尔会出现挂了的情况，如在小米手机上，此处做一个定时器，定时bindService
        TaskUtil.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                bindSDKService(mContext);
            }
        }, delay, delay, TimeUnit.SECONDS);
    }

    /**
     * 初始化sdk中线程池的大小
     */
    private void initThreadPool() {
        TaskUtil.initialize(Executors.newScheduledThreadPool(4));
//        UploadTaskPoolManager.initialize(new ScheduledThreadPoolExecutor(1));
    }

    /**
     * 绑定SDK
     */
    public void bindSDKService(Context mContext) {
        Intent intent = new Intent(IntentAction.MT_SERVICE_CORE_BOOT);
        intent.setPackage((String) BuildConfigUtil.getBuildConfigValue(BaseApplication.getContext(), "APPLICATION_ID"));
        try {
            mBindSuccessful = mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
            l.i("bind sdk service:" + mBindSuccessful);
        } catch (Throwable e) {
            l.e("bind sdk service fail.", e);
        }
    }

    public void unBindSDKService(Context mContext) {
        mContext.unbindService(connection);
        mBindSuccessful = false;
        sdkService = null;
    }

    /**
     * ServiceConnection
     */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            l.i("sdk service connected.");
            sdkService = IBizInvokeService.Stub.asInterface(service);
            try {
                Class<?> clazz = Class.forName(BuildConfigUtil.getBuildConfigValue(BaseApplication.getContext(), "APPLICATION_ID") + ".sdkservice.SDKCallback");
                Object obj = clazz.newInstance();
                sdkService.syncStart((String) BuildConfigUtil.getBuildConfigValue(BaseApplication.getContext(), "SDK_FILE"), (IBizInvokeCallback) obj);
                l.i("sdk service callback bind successful.");
            } catch (Exception e) {
                l.e("sdk service callback bind fail.", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };

    public IBizInvokeService getSdkService() {
        return sdkService;
    }

}
