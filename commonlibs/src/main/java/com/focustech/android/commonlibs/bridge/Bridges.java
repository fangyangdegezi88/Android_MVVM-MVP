
package com.focustech.android.commonlibs.bridge;

import com.focustech.android.commonlibs.bridge.cache.localstorage.LocalFileStorageManager;
import com.focustech.android.commonlibs.bridge.http.OkHttpManager;
import com.focustech.android.commonlibs.bridge.recorder.RecorderManager;
import com.focustech.android.commonlibs.bridge.security.SecurityManager;

/**
 * <与Bridge模块一一对应，用以在BridgeFactory获取某个Bridge对应的Key>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class Bridges {
    /**
     * 本地缓存(sd卡存储和手机内部存储)
     */
    public static final String LOCAL_FILE_STORAGE = ".LOCAL_FILE_STORAGE";

//    /**
//     * SharedPreference缓存
//     */
//    public static final String SHARED_PREFERENCE = ".SHARED_PREFERENCE";

    /**
     * 安全
     */
    public static final String SECURITY = ".SECURITY";


    /**
     * CoreService，主要维护功能模块
     */
    public static final String CORE_SERVICE = ".CORE_SERVICE";


//    /**
//     * 数据库模块
//     */
//    public static final String DATABASE = ".DATABASE";

    /**
     * http请求
     */
    public static final String HTTP = ".HTTP";

    /**
     * 录音模块
     */
    public static final String RECORDER = ".RECORDER";


    public enum BridgesManager {
        FILE(LOCAL_FILE_STORAGE, LocalFileStorageManager.class),
//        SP(SHARED_PREFERENCE, FTSharedPrefManager.class),
        SECURIT(SECURITY, SecurityManager.class),
//        CORESERVICE(CORE_SERVICE, MtSdkServiceManager.class),
//        DB(DATABASE, DBManager.class),
        REQUEST(HTTP, OkHttpManager.class),
        RECORD(RECORDER, RecorderManager.class);

        String bridgeKey;
        Class manager;

        BridgesManager(String bridgeKey, Class manager) {
            this.bridgeKey = bridgeKey;
            this.manager = manager;
        }

        public static BridgeLifeCycleListener getListenerByKey(String bridgeKey) {
            BridgeLifeCycleListener result = null;
            for (BridgesManager bridgesEnum : BridgesManager.values()) {
                if (bridgesEnum.bridgeKey.equals(bridgeKey)) {
                    try {
                        result = (BridgeLifeCycleListener) bridgesEnum.manager.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            return result;
        }

        public BridgeLifeCycleListener getInstance() {
            try {
                return (BridgeLifeCycleListener) manager.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
