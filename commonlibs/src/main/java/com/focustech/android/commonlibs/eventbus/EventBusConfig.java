package com.focustech.android.commonlibs.eventbus;

import de.greenrobot.event.EventBus;

/**
 * @author yanguozhu
 * @version [版本号, 2016/3/8]
 * @see [相关类/方法]
 * @since [V1]
 */
public class EventBusConfig {
    /**
     * 是否开启EventBus监听,默认开启
     */
    private boolean EVENTBUS_OPEN;

    private Object object;

    private EventBusConfig(Object object) {
        this.object = object;
    }

    /**
     * 实例化变量
     *
     * @param object object
     */
    public static EventBusConfig getInstance(Object object) {
        return new EventBusConfig(object);
    }

    /**
     * 开启EventBus监听
     */
    public void enable() {
        if (!EVENTBUS_OPEN) {
            EventBus.getDefault().register(object);
            EVENTBUS_OPEN = true;
        }
    }

    /**
     * 关闭EventBus监听
     */
    public void disable() {
        if (EVENTBUS_OPEN) {
            EventBus.getDefault().unregister(object);
            EVENTBUS_OPEN = false;
            object = null;
        }
    }

}
