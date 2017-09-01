package com.focustech.android.commonuis.base;

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.focustech.android.commonlibs.ActivityManager;
import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.bridge.BridgeFactory;
import com.focustech.android.commonlibs.bridge.Bridges;
import com.focustech.android.commonlibs.bridge.http.OkHttpManager;
import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.util.ActivityUtil;
import com.focustech.android.commonlibs.util.DensityUtil;
import com.focustech.android.commonuis.R;
import com.focustech.android.commonuis.biz.BaseCommonPresenter;
import com.focustech.android.commonuis.view.header.SFActionBar;
import com.focustech.android.commonuis.view.header.SFActionBarStyle;
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

public abstract class BaseMVVMActivity<V extends ViewDataBinding, P extends BaseCommonPresenter> extends AppCompatActivity implements CreateMvvmInit, PublishActivityCallBack, SFActionBar.SFActionBarListener {

    /**
     * DataBinding
     */
    protected V binding;

    /**
     * title
     */
    protected SFActionBarStyle actionBarStyle;

    protected final String TAG = this.getClass().getSimpleName();
    protected L l = new L(TAG);
    /**
     * 交互层
     */
    public PresentationLayerFuncHelper mLayerHelper;

    public P presenter;

    /**
     * SFActionBar 头部信息
     */
    public SFActionBar mHeader;

    /**
     * SFLoadingView 加载动画
     */
    public SFLoadingView mLoadView;

    /**
     * 图片加载器Glide
     */
    protected RequestManager mGlideManager;

    /**
     * 导航栏高度
     */
    private int navigationHeight;

    /**
     * 不可用的高度
     */
    private int notUsableHeight;

    /**
     * 键盘高度
     */
    private int keyboardHeight;

    private int usableHeightPrevious;

    private FrameLayout.LayoutParams frameLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkHasLoading(this)) {
            if (!isDoStatusBarSelf()) {
                ActivityUtil.flymeSetStatusBarLightMode(getWindow(), true, R.color.app_status_bg_color);
                ActivityUtil.mIUISetStatusBarLightMode(getWindow(), true, R.color.app_status_bg_color);
            }
            mLayerHelper = new PresentationLayerFuncHelper(this);
            mGlideManager = Glide.with(this);
            binding = DataBindingUtil.setContentView(this, getLayoutId());
            initHeaderAndLoadView();
            initData(getIntent().getExtras());
            //加入堆栈
            ActivityManager.getInstance().addActivity(this);
        }
    }

    /**
     * 控制键盘弹出和隐藏时的布局变化
     *
     * @param rootLayout
     */
    public void controlKeyboardLayout(final View rootLayout) {
        initNavigationHeight();

        initKeyboardHeight();

        frameLayoutParams = (FrameLayout.LayoutParams) rootLayout.getLayoutParams();

        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                /*
                此写法在普通手机中尚准确，但是在pad和plus手机中存在问题
                int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
                Rect rect = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(rect);
                int contentViewTop = rect.top;
                if (heightDiff <= contentViewTop + navigationHeight) {
                    onKeyBoardHide();
                } else if (heightDiff > contentViewTop + navigationHeight){
                    keyboardHeight = heightDiff - contentViewTop - navigationHeight;
                    onKeyBoardShow(keyboardHeight);
                }*/
                possiblyResizeChildOfContent(rootLayout);
            }
        });
    }

    private void possiblyResizeChildOfContent(View mChildOfContent) {
        int usableHeightNow = getUsableHeight(mChildOfContent);
        Log.w(TAG, "===onGlobalLayout usableHeightNow=" + usableHeightNow + ", usableHeightPrevious=" + usableHeightPrevious);
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            Log.w(TAG, "===onGlobalLayout heightDifference=" + heightDifference + ", usableHeightSansKeyboard=" + usableHeightSansKeyboard);
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                keyboardHeight = heightDifference;
                onKeyBoardShow(keyboardHeight);
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;

                notUsableHeight = heightDifference;

                onKeyBoardHide();
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int getUsableHeight(View mChildOfContent) {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return r.bottom - r.top;
    }

    public void onKeyBoardHide() {

    }

    public void onKeyBoardShow(int keyboardHeight) {

    }

    /**
     * 兼容5.0之后，初始化虚拟导航栏的高度
     */
    private void initNavigationHeight() {
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        if (!hasHomeKey) { //判断没有HOME键时存在导航栏
            Resources resources = getResources();
            navigationHeight = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
        }
        Log.w(TAG, "===initNavigationHeight navigationHeight=" + navigationHeight);
    }

    /**
     * 初始化键盘高度
     */
    private void initKeyboardHeight() {
        // 初始化输入法和底部工具条高度
        if (keyboardHeight == 0) {
            keyboardHeight = Math.min(DensityUtil.dip2px(253), DensityUtil.getYScreenpx(this) / 2); // 估算高度
        }
    }

    /**
     * 获取不可用区域的高度
     *
     * @return
     */
    public int getNotUsableHeight() {
        return notUsableHeight;
    }

    /**
     * 获取键盘高度
     *
     * @return
     */
    public int getKeyboardHeight() {
        return keyboardHeight;
    }

    /**
     * 检查当前进程是否经历过loading页
     * <p>
     * 如果没有经历过loading页则表示当前进程被杀，需要重新经历loading页
     *
     * @param activity 当前activity
     */
    public boolean checkHasLoading(AppCompatActivity activity) {
        if (BaseApplication.isHasLoading() || TextUtils.equals(activity.getClass().getSimpleName(), "LoadingActivity")) {
            return true;
        } else {
            Intent intent = new Intent();
            intent.setAction("com.focusteach.android.loadingactivity");
            startActivity(intent);
            finish();
            return false;
        }
    }

    /**
     * 是否子Activity自己处理状态栏
     *
     * @return 如果自己处理状态栏 返回true
     */
    public boolean isDoStatusBarSelf() {

        return false;
    }

    /**
     * 初始化加载view
     */
    private void initHeaderAndLoadView() {
        try {
            //SFActionBar
            mHeader = (SFActionBar) findViewById(getSfHeaderId());
            if (null != mHeader) {
                mHeader.setSFActionBarListener(this);
            }
            //SFLoadingView
            mLoadView = (SFLoadingView) findViewById(getLoadViewId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        
    }

    @Override
    public void leftBtnClick() {
        finish();
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
    public void startActivity(Class<?> openClass, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    //------------Activity生命周期-------------
    @Override
    protected void onResume() {
        ActivityManager.currentActivityName = this.getClass().getName();
        super.onResume();

        if (!isFragmentActivity())
            MobclickAgent.onPageStart(getName());   //统计页面
        MobclickAgent.onResume(this);   //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isFragmentActivity())
            MobclickAgent.onPageEnd(getName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        //返回操作，如果有对话框，则先隐藏对话框
        if (null != mLayerHelper && mLayerHelper.isShowing()) {
            mLayerHelper.hideProgressDialog();
            mLayerHelper.hideAlert();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(this);

        if (presenter != null) {
            presenter.detachView();
        }
        try {
            OkHttpManager httpManager = BridgeFactory.getBridge(Bridges.HTTP, getApplicationContext());
            httpManager.cancelActivityRequest(TAG);
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    /**
     * 默认不包含fragment 当包含时 重写该方法 返回true
     *
     * @return
     */
    public boolean isFragmentActivity() {
        return false;
    }

    @Override
    public String getName() {
        return TAG;
    }


    public abstract int getSfHeaderId();

    public abstract int getLoadViewId();

    protected SFActionBarStyle getDefaultBarStyle() {
        if (null == actionBarStyle) {
            actionBarStyle = SFActionBarStyle.newBuilder();
        }
        return actionBarStyle;
    }
}
