package com.focustech.android.components.mt.sdk.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.commonlibs.util.TaskUtil;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.IntentAction;
import com.focustech.android.components.mt.sdk.android.service.MessageService;
import com.focustech.android.components.mt.sdk.core.net.MTConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网络连接状态Receiver
 *
 * @author zhangxu
 */
public class ConnectivityReceiver extends BroadcastReceiver {
    private static Logger logger = LoggerFactory.getLogger(ConnectivityReceiver.class);
    private static ConnectivityReceiver instance;
    private static final IntentFilter FILTERS = new IntentFilter();
    private MTConnection connection;
    private MTRuntime.Network last = null;

    private ConnectivityReceiver(MTConnection connection) {
        this.connection = connection;
    }

    static {
        FILTERS.addAction(IntentAction.ANDROID_NET_CONN_CONNECTIVITY_CHANGE);
        //应用长时间息屏，系统可能会将其网络切断，当屏幕重新点亮时，系统不能感知网络连接正常
        FILTERS.addAction(Intent.ACTION_SCREEN_ON);
        FILTERS.addAction(IntentAction.ANDROID_NET_MAYBE_CONN_CONNECTIVITY);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        MTRuntime.Network value = getNetwork(cm);

        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.NET, LogFormat.Operation.NET_STAT, "net state changed to {}"), value.name());
        }

        if (MTRuntime.Network.NULL == value) {
            //connection.close();
        } else {
            // 只要有网络就尝试重练
            connection.onNetWorkActive();
        }

        if (last == value) {
            return;
        }

        MTRuntime.setNetWork(value);
        last = value;

        // 没有网络(应该为网络改变时，不是没有网络)
        TaskUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageService service = ContextHolder.getMessageService();

                if (null != service && null != service.getBizInvokeCallback()) {
                    try {
                        service.getBizInvokeCallback().privateNetworkChanged(MTRuntime.getNetwork().name());
                    } catch (Throwable e) {
                    }
                }
            }
        });
    }


    public static synchronized ConnectivityReceiver getInstance(MTConnection connection) {
        if (null == instance) {
            instance = new ConnectivityReceiver(connection);
        }

        return instance;
    }

    public static IntentFilter getFilters() {
        return FILTERS;
    }

    public static MTRuntime.Network getNetwork(ConnectivityManager cm) {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        MTRuntime.Network value = MTRuntime.Network.NULL;

        if (activeNetwork != null && activeNetwork.isConnected()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                value = MTRuntime.Network.WIFI;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // 手机网络进行运行商的区分
                switch (activeNetwork.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        value = MTRuntime.Network.MOBILE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        value = MTRuntime.Network.MOBILE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        value = MTRuntime.Network.MOBILE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        value = MTRuntime.Network.MOBILE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        value = MTRuntime.Network.MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        value = MTRuntime.Network.MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        value = MTRuntime.Network.MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        value = MTRuntime.Network.MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        value = MTRuntime.Network.MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        value = MTRuntime.Network.MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        value = MTRuntime.Network.MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        value = MTRuntime.Network.MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        value = MTRuntime.Network.MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        value = MTRuntime.Network.MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        value = MTRuntime.Network.MOBILE_4G;
                        break;
                    default:
                        value = MTRuntime.Network.MOBILE_UNKNOWN;
                        break;
                }
            }
        }

        return value;
    }
}
