
package com.focustech.android.commonlibs.bridge;

import android.content.Context;

import com.focustech.android.commonlibs.util.TaskUtil;

import java.util.LinkedHashMap;
import java.util.concurrent.Executors;


/**
 * <中间连接层>
 * <p>
 * 注意：不要将manager放在单例中作为属性  因为 manager可能会变
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class BridgeFactory {

    private static BridgeFactory model;

    private LinkedHashMap<String, Object> mBridges;

    private BridgeFactory() {
        mBridges = new LinkedHashMap<>();
    }

    public static void init(Context context) {
        model = null;
        BridgeLifeCycleSetKeeper.getInstance().clearOnApplicationQuit();
        TaskUtil.initialize(Executors.newScheduledThreadPool(5));
        model = new BridgeFactory();
        model.initLocalFileStorageManager();
//        model.initPreferenceManager();
        model.initSecurityManager();
//        model.initCoreServiceManager();
        model.initOkHttpManager();
//        model.initDBManager();
        model.initRecorderManager();
    }

    public static void destroy() {
        model.mBridges = null;
        model = null;
    }

    /**
     * 初始化本地存储路径管理类
     */
    private void initLocalFileStorageManager() {
//        LocalFileStorageManager localFileStorageManager = new LocalFileStorageManager();
//        model.mBridges.put(Bridges.LOCAL_FILE_STORAGE, localFileStorageManager);
        BridgeLifeCycleListener localFileStorageManager = Bridges.BridgesManager.FILE.getInstance();
        model.mBridges.put(Bridges.LOCAL_FILE_STORAGE, localFileStorageManager);
        BridgeLifeCycleSetKeeper.getInstance().trustBridgeLifeCycle(localFileStorageManager);
    }
//
//    /**
//     * 初始化SharedPreference管理类
//     */
//    private void initPreferenceManager() {
////        FTSharedPrefManager mFTSharedPrefManager = new FTSharedPrefManager();
////        model.mBridges.put(Bridges.SHARED_PREFERENCE, mFTSharedPrefManager);
//        BridgeLifeCycleListener mFTSharedPrefManager = Bridges.BridgesManager.SP.getInstance();
//        model.mBridges.put(Bridges.SHARED_PREFERENCE, mFTSharedPrefManager);
//        BridgeLifeCycleSetKeeper.getInstance().trustBridgeLifeCycle(mFTSharedPrefManager);
//    }

    /**
     * 网络请求管理类
     */
    private void initOkHttpManager() {
//        OkHttpManager mOkHttpManager = new OkHttpManager();
        BridgeLifeCycleListener mOkHttpManager = Bridges.BridgesManager.REQUEST.getInstance();
        model.mBridges.put(Bridges.HTTP, mOkHttpManager);
        BridgeLifeCycleSetKeeper.getInstance().trustBridgeLifeCycle(mOkHttpManager);
    }

    /**
     * 初始化安全模块
     */
    private void initSecurityManager() {
//        SecurityManager securityManager = new SecurityManager();
        BridgeLifeCycleListener securityManager = Bridges.BridgesManager.SECURIT.getInstance();
        model.mBridges.put(Bridges.SECURITY, securityManager);
        BridgeLifeCycleSetKeeper.getInstance().trustBridgeLifeCycle(securityManager);
    }

//    /**
//     * 初始化Tcp服务
//     */
//    private void initCoreServiceManager() {
////        MtSdkServiceManager mtSdkServiceManager = new MtSdkServiceManager();
//        BridgeLifeCycleListener mtSdkServiceManager = Bridges.BridgesManager.CORESERVICE.getInstance();
//        model.mBridges.put(Bridges.CORE_SERVICE, mtSdkServiceManager);
//        BridgeLifeCycleSetKeeper.getInstance().trustBridgeLifeCycle(mtSdkServiceManager);
//    }

    /**
     * 初始化数据库
     */
    private void initDBManager() {
//        DBManager dbManager = new DBManager();
//        BridgeLifeCycleListener dbManager = Bridges.BridgesManager.DB.getInstance();
//        model.mBridges.put(Bridges.DATABASE, dbManager);
//        BridgeLifeCycleSetKeeper.getInstance().trustBridgeLifeCycle(dbManager);
    }

    /**
     * 初始化录音模块
     */
    private void initRecorderManager() {
//        RecorderManager recorderManager = new RecorderManager();
        BridgeLifeCycleListener recorderManager = Bridges.BridgesManager.RECORD.getInstance();
        model.mBridges.put(Bridges.RECORDER, recorderManager);
        BridgeLifeCycleSetKeeper.getInstance().trustBridgeLifeCycle(recorderManager);
    }

    /**
     * 通过bridgeKey {@link Bridges}来获取对应的Bridge模块
     *
     * @param bridgeKey {@link Bridges}
     * @return
     */
    @SuppressWarnings("unchecked")
//    public static <V extends Object> V getBridge(String bridgeKey) {
//        Object bridge = model.mBridges.get(bridgeKey);
//        if (bridge == null) {
//            throw new NullPointerException("-no defined bridge-");
//        }
//        return (V) bridge;
//    }

    /**
     * 在非主进程中使用，getBridge(String bridgeKey)报空指针错误
     *
     * @param bridgeKey
     * @param context
     * @param <V>
     * @return
     */
    public static <V extends Object> V getBridge(String bridgeKey, Context context) {
        //非主进程
        if (model == null) {
            model = new BridgeFactory();
        }
        Object listener = model.mBridges.get(bridgeKey);
        if (null == listener) {
            //非主进程，比如广播接收里面使用
            BridgeLifeCycleListener manager = Bridges.BridgesManager.getListenerByKey(bridgeKey);
            manager.initOnApplicationCreate(context);
            model.mBridges.put(bridgeKey, manager);
            return (V) manager;
        }
        return (V) listener;
    }
}
