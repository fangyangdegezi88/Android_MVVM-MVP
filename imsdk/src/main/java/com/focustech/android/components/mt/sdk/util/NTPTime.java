package com.focustech.android.components.mt.sdk.util;

import com.focustech.android.components.mt.sdk.MTRuntime;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * NTP 时间戳
 *
 * @author zhangxu
 */
public class NTPTime {
    private static ScheduledExecutorService threads = Executors.newScheduledThreadPool(1);

    private NTPTime() {
    }

    public static long now() {
        return System.currentTimeMillis() + NTPClient.getInstance().getClockOffset();
    }

    public static void start() {
        threads.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    NTPClient.getInstance().requestTime(MTRuntime.getNTPServer(), MTRuntime.getNTPPort(), 5000);
                } catch (Throwable e) {

                } finally {
                    threads.schedule(this, 1, TimeUnit.MINUTES);
                }
            }
        });
    }
}
