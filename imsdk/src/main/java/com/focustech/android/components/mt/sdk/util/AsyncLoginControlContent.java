package com.focustech.android.components.mt.sdk.util;

import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqLoginProcessor;

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
 * <页面基础公共功能实现>
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/7/13]
 * @see [相关类/方法]
 * @since [V1]
 */
public class AsyncLoginControlContent {


    private static Logger logger = LoggerFactory.getLogger(AsyncLoginControlContent.class);
    private static final TimeUnit TIMEOUT_UNIT = TimeUnit.MILLISECONDS;
    // 超时timer，默认1秒一次检测
    private static final Timer TIMEOUT_TIMER = new HashedWheelTimer(1000, TIMEOUT_UNIT);
    private static final Map<String, Object> ASYNC_CONTENT = new ConcurrentHashMap<>();

    private AsyncLoginControlContent() {
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
        if (ReqLoginProcessor.LOGIN_KEY.equals(asyncKey)) {
            logger.info("AsyncContent->>clear login key context");
        }
        ASYNC_CONTENT.clear();
        if (!ASYNC_CONTENT.containsKey(asyncKey))
            logger.info("clear asyncKey");
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
            if (AsyncLoginControlContent.exists(asyncKey)) {
                Object context = AsyncLoginControlContent.getContent(asyncKey);
                AsyncLoginControlContent.cleanContent(asyncKey);

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
