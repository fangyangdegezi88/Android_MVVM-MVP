package com.focustech.android.commonuis.biz;

import android.content.Context;

import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.bridge.BridgeFactory;
import com.focustech.android.commonlibs.bridge.Bridges;
import com.focustech.android.commonlibs.bridge.http.OkHttpManager;
import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.eventbus.EventBusConfig;

/**
 * <*****>
 *
 * @author liuzaibing
 * @version [版本号, 2017/6/14]
 * @see [相关类/方法]
 * @since [V1]
 */
public abstract class BaseCommonPresenter<V extends IMvpView> implements Presenter<V> {

    protected L l = new L(getClass().getSimpleName());

    /**
     * Context 实例 MyApplication
     *
     * */
    protected Context mAppContext;

    protected V mvpView;

    /**
     * 是否开启eventBus
     */
    private boolean isOpen;

    /**
     * okhttp请求工具
     */
    protected OkHttpManager mOkHttpManager;


    public BaseCommonPresenter() {
        mAppContext = BaseApplication.getContext();
    }

    /**
     * @param eventOpen 是否开启eventBus
     */
    public BaseCommonPresenter(boolean eventOpen) {
        this.isOpen = eventOpen;
        mAppContext = BaseApplication.getContext();
    }

    /**
     * EventBus初始化
     */
    private EventBusConfig mEventConf;


    public void attachView(V view) {
        try {
            mvpView = view;
            mOkHttpManager = BridgeFactory.getBridge(Bridges.HTTP,BaseApplication.getContext());
            if (isOpen) {
                mEventConf = EventBusConfig.getInstance(this);
                mEventConf.enable();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void detachView() {
        mvpView = null;
        if (isOpen && null != mEventConf)
            mEventConf.disable();
    }

    @Override
    public String getName() {
        return mvpView == null ? "" : mvpView.getClass().getSimpleName();
    }

}
