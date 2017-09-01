package com.focustech.android.commonlibs.application;


import ch.qos.logback.classic.Level;

/**
 * <application类初始化>
 *
 * @author yanguozhu
 * @version [版本号, 2016/12/12]
 * @see [相关类/方法]
 * @since [V1]
 */

public interface InitApplication {

    /**
     * 判断进程
     *
     * @return
     */
    boolean isMainProcess();

    /**
     * 进程初始化数据
     */
    void initData();

    /**
     * 获取权限之后，应用初始化
     */
    void initPermissionData();

    /**
     * 初始化日志
     */
    void initLog(String logPath, String logName, Level level, int days);

    /**
     * 应用初始化的时候，需要屏蔽的进程
     *
     * @return
     */
    String[] getOtherProcess();

    /**
     * 初始化其他进程的操作
     *
     * @param processName
     */
    void initOtherProcess(String processName);


    /**
     * 回收
     */
    void onDestory();

}
