package com.focustech.android.commonlibs.capability.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * <p/>
 * 持久化存储---写入文件，注意 不要在循环中使用该日志进行打印，防止文件过于大
 * <p/>
 * Created by liuzaibing on 2016/9/22.
 */
public class L {

    private Logger mLogger;

    public L(Class clazz) {
        mLogger = LoggerFactory.getLogger(clazz);
    }

    public L(String tag) {
        mLogger = LoggerFactory.getLogger(tag);
    }

    public void i(String info) {
        if (mLogger.isInfoEnabled())
            mLogger.info(info);
    }

    public void i(LogFormat.LogModule module, LogFormat.Operation operation, String info) {
        if (mLogger.isInfoEnabled())
            mLogger.info(LogFormat.format(module, operation, info));
    }

    public void d(String info) {
        if (mLogger.isDebugEnabled())
            mLogger.debug(info);
    }

    public void d(LogFormat.LogModule module, LogFormat.Operation operation, String info) {
        if (mLogger.isDebugEnabled())
            mLogger.debug(LogFormat.format(module, operation, info));
    }

    public void e(String info) {
        if (mLogger.isErrorEnabled())
            mLogger.error(info);
    }

    public void e(LogFormat.LogModule module, LogFormat.Operation operation, String info) {
        if (mLogger.isErrorEnabled())
            mLogger.error(LogFormat.format(module, operation, info));
    }

    public void e(String info, Throwable throwable) {
        if (mLogger.isErrorEnabled())
            mLogger.error(info, throwable);
    }
}
