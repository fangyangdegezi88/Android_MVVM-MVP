package com.focustech.android.commonuis.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.focustech.android.commonlibs.bridge.BridgeFactory;
import com.focustech.android.commonlibs.bridge.Bridges;
import com.focustech.android.commonlibs.bridge.http.OkHttpManager;
import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonuis.biz.BaseCommonPresenter;
import com.focustech.android.commonuis.view.header.SFActionBar;
import com.focustech.android.commonuis.view.loadview.SFLoadingView;
import com.umeng.analytics.MobclickAgent;

/**
 * <页面基础公共功能实现>
 *
 * @author yanguozhu
 * @version [版本号, 2017-06-22]
 * @see [相关类/方法]
 * @since [V1]
 */

public abstract class BaseMVVMFragment<V extends ViewDataBinding, P extends BaseCommonPresenter> extends Fragment implements CreateMvvmInit, PublishActivityCallBack, SFActionBar.SFActionBarListener {

    /**
     * DataBinding
     */
    protected V binding;

    /**
     * SFActionBar 头部信息
     */
    public SFActionBar mHeader;

    public P presenter;

    public final String TAG = this.getClass().getSimpleName();
    protected L l = new L(TAG);
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

        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mGlideManager = Glide.with(this);
        // 初始化页面数据
        initData(getArguments());

        initHeaderAndLoadView();
        return binding.getRoot();
    }

    /**
     * 初始化加载view
     */
    private void initHeaderAndLoadView() {
        try {
            //SFActionBar
            mHeader = (SFActionBar) binding.getRoot().findViewById(getSfHeaderId());
            if (null != mHeader) {
                mHeader.setSFActionBarListener(this);
            }
            //SFLoadingView
            mLoadView = (SFLoadingView) binding.getRoot().findViewById(getLoadViewId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public abstract int getSfHeaderId();

    protected abstract int getLoadViewId();

    @Override
    public void onClick(View v) {

    }

    @Override
    public void leftBtnClick() {
    }

    @Override
    public void rightBtnClick() {

    }

    @Override
    public void chooseIvClick(View view) {

    }

    @Override
    public void backChooseIvClick(View view) {

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
    public String getName() {
        return TAG;
    }

}
