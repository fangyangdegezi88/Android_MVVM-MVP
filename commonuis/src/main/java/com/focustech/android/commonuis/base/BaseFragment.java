package com.focustech.android.commonuis.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.focustech.android.commonlibs.bridge.BridgeFactory;
import com.focustech.android.commonlibs.bridge.Bridges;
import com.focustech.android.commonlibs.bridge.http.OkHttpManager;
import com.focustech.android.commonuis.biz.BaseCommonPresenter;
import com.focustech.android.commonuis.view.loadview.SFLoadingView;
import com.umeng.analytics.MobclickAgent;

/**
 * <页面基础公共功能实现>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/21]
 * @see [相关类/方法]
 * @since [V1]
 */
public abstract class BaseFragment<V extends BaseCommonPresenter> extends Fragment implements CreateInit, PublishActivityCallBack, View.OnClickListener {

    public V presenter;
    /**
     * 布局view
     */
    protected View mRoot = null;

    public final String TAG = this.getClass().getSimpleName();

    /**
     * SFLoadingView 加载动画
     */
    public SFLoadingView mLoadView;

    /**
     * 图片加载器Glide
     */
    protected RequestManager mGlideManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = inflater.inflate(getLayoutId(), container, false);
            if (getActivity() == null || getActivity().isFinishing())
                return mRoot;
            mGlideManager = Glide.with(this);
            // 初始化页面组件
            initViews(getActivity(), mRoot, savedInstanceState);

            //SFLoadingView
            mLoadView = (SFLoadingView) mRoot.findViewById(getSfLoadViewId());
            // 绑定事件监听
            initListeners();
            // 初始化页面数据
            initData();
        } else {
            final ViewParent parent = mRoot.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mRoot);
            }
        }
        return mRoot;
    }

    protected abstract int getSfLoadViewId();

    @Override
    public void onClick(View v) {

    }

    //------------PublishActivityCallBack-----------------

    @Override
    public void startActivity(Class<?> openClass, @Nullable Bundle bundle) {
        Intent intent = new Intent(getActivity(), openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getName());
    }

    @Override
    public void startActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 这个从fragment 跳转的，保证返回的result 先从 fragment 外部的activity走一下
     *
     * @param openClass
     * @param requestCode
     * @param bundle
     */
    public void startActForResultBackToAct(Class<?> openClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        getActivity().startActivityForResult(intent, requestCode);
    }

    //------------Fragmeng生命周期-------------

    @Override
    public void onDestroy() {
        try {
            if (presenter != null) {
                presenter.detachView();
            }
            OkHttpManager httpManager = BridgeFactory.getBridge(Bridges.HTTP, getContext());
            httpManager.cancelActivityRequest(TAG);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean isFragmentActivity() {
        return false;
    }
}