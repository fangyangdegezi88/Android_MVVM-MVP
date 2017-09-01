package com.focustech.android.components.mt.sdk.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

/**
 * 异步操作上下文，基于Netty的时间轮算法实现
 * <p/>
 * <p/>
 * <pre>
 *     1. 对异步上下文提供支持
 *     2. 支持超时，超时后，移除上下文
 * </pre>
 *
 * @author zhangxu
 */
@SuppressWarnings("unchecked")
public final class AsyncContent {
    private static Logger logger = LoggerFactory.getLogger(AsyncContent.class);
    private static final TimeUnit TIMEOUT_UNIT = TimeUnit.MILLISECONDS;
    // 超时timer，默认1秒一次检测
    private static final Timer TIMEOUT_TIMER = new HashedWheelTimer(1000, TIMEOUT_UNIT);
    private static final Map<String, Object> ASYNC_CONTENT = new ConcurrentHashMap<>();

    private AsyncContent() {
    }

    /**
     * 增加一个异步上下文
     *
     * @param asyncKey
     * @param context
     * @param timeout  单位毫秒
     */
    public static void addContent(String asyncKey, Object context, long timeout, TimeoutHandler timeoutHandler) {
        ASYNC_CONTENT.put(asyncKey, context);
        TIMEOUT_TIMER.newTimeout(new CleanAsyncContentTask(asyncKey, timeoutHandler), timeout, TIMEOUT_UNIT);
    }

    /**
     * 增加一个异步上下文
     *
     * @param asyncKey
     * @param context
     * @param timeout  单位毫秒
     */
    public static void addContent(String asyncKey, Object context, long timeout) {
        ASYNC_CONTENT.put(asyncKey, context);
        TIMEOUT_TIMER.newTimeout(new CleanAsyncContentTask(asyncKey), timeout, TIMEOUT_UNIT);
    }

    /**
     * 清理一个异步上下文
     *
     * @param asyncKey
     */
    public static void cleanContent(String asyncKey) {
        ASYNC_CONTENT.remove(asyncKey);
    }

    /**
     * 清理一个异步上下文
     *
     * @param asyncKey
     */
    public static <T> T getContent(String asyncKey) {
        return (T) ASYNC_CONTENT.get(asyncKey);
    }

    /**
     * 上下文是否存在
     *
     * @param asyncKey
     * @return
     */
    public static boolean exists(String asyncKey) {
        return ASYNC_CONTENT.containsKey(asyncKey);
    }

    /**
     * 清理异步key
     */
    public static void clear() {
        ASYNC_CONTENT.clear();
    }

    public static void stop() {
        ASYNC_CONTENT.clear();
    }

    public interface TimeoutHandler {
        void timeout(String asyncKey, Object asyncContext, Timeout timeout);
    }

    /**
     * 超时任务
     */
    private static class CleanAsyncContentTask implements TimerTask {
        private String asyncKey;
        private TimeoutHandler delegate;

        public CleanAsyncContentTask(String asyncKey) {
            this(asyncKey, null);
        }

        public CleanAsyncContentTask(String asyncKey, TimeoutHandler timeoutHandler) {
            this.asyncKey = asyncKey;
            this.delegate = timeoutHandler;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            if (AsyncContent.exists(asyncKey)) {
                Object context = AsyncContent.getContent(asyncKey);
                AsyncContent.cleanContent(asyncKey);

                if (null != delegate) {
                    delegate.timeout(asyncKey, context, timeout);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("async");
                    }
                }
            }
        }
    }
}
