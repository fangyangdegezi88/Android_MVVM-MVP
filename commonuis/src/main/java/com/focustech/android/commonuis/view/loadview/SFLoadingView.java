package com.focustech.android.commonuis.view.loadview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonuis.R;

/**
 * <智小鹰加载提示框>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/29]
 * @see [相关类/方法]
 * @since [V1]
 */
public class SFLoadingView extends LinearLayout {

    public enum LOADING_STATUS {
        LOADING, //加载中
        ERROR, //错误
        EMPTY,//空数据
        GONE //隐藏
    }

    private Context mContext = null;
    /**
     * 根布局
     */
    private LinearLayout mContainer;

    /**
     * 提示语
     */
    private TextView mTip;
    /**
     * 提示图
     */
    private ImageView mImage;
    /**
     * 重试
     */
    private TextView mRetry;

    /**
     * 无数据的指示线
     */
    private ImageView mNoDataLine;

    /**
     * 当前对话框的状态
     */
    private LOADING_STATUS mCurrStatus;

    /**
     * 监听
     */
    private LoadingRefreshListener lister;


    /**
     * 内容view SFLoadingView所在页面的内容布局容器，将其传进来，直接控制sfloadingview状态同步修改内容布局的状态
     */
    private View mContentView;


    /**
     * 加载动画
     */
    private AnimationDrawable mGifDrawable;

    public SFLoadingView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SFLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public SFLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SFLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mContainer = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.view_sf_loading, this, true);
        if (mContainer != null) {
            mTip = (TextView) mContainer.findViewById(R.id.view_loadding_msg);
            mImage = (ImageView) mContainer.findViewById(R.id.view_loadding_img);
            mRetry = (TextView) mContainer.findViewById(R.id.view_loadding_btn);
            mNoDataLine = (ImageView) mContainer.findViewById(R.id.iv_data_null_arrow);
            mContainer.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 当前状态是错误状态的时候，才会刷新
                    if (null != lister && LOADING_STATUS.ERROR == mCurrStatus) {
                        showLoading(R.string.refresh);
                        lister.doRefresh();
                    }
                }
            });

            showLoading(R.string.refresh);

        }
    }


    /**
     * 加载中...
     *
     * @param msgId 提示语
     */
    public void showLoading(int msgId) {
        mNoDataLine.setVisibility(GONE);
        if (msgId != -1)
            mTip.setText(msgId);
        //当前正在刷新
        if (LOADING_STATUS.LOADING == mCurrStatus)
            return;
        mCurrStatus = LOADING_STATUS.LOADING;
        mRetry.setVisibility(View.INVISIBLE);
        //设置图片背景
        mImage.setBackgroundResource(R.drawable.view_loading_gif);
        mGifDrawable = (AnimationDrawable) mImage.getBackground();
        mGifDrawable.start();

        setVisibility(View.VISIBLE);
        if (GeneralUtils.isNotNull(mContentView))
            mContentView.setVisibility(GONE);
    }

    /**
     * 加载中...
     */
    public void showLoading() {
        showLoading(-1);
    }

    /**
     * 显示错误信息
     *
     * @param msgId 错误提示语id
     */
    public void showErr(int msgId) {
        mTip.setText(msgId);
        showError();
    }

    public void showErr(String msg) {
        mTip.setText(msg);
        showError();
    }

    private void showError() {
        mNoDataLine.setVisibility(GONE);
        stopAnim();

        //当前正在刷新
        if (LOADING_STATUS.ERROR == mCurrStatus)
            return;
        mCurrStatus = LOADING_STATUS.ERROR;
        mRetry.setVisibility(View.VISIBLE);
        //设置图片背景
        mImage.setBackgroundResource(R.drawable.view_loading_err);
        setVisibility(View.VISIBLE);
        if (GeneralUtils.isNotNull(mContentView))
            mContentView.setVisibility(GONE);
    }

    /**
     * 显示错误信息
     *
     * @param msgId   错误提示语Id
     * @param retryId 重试提示语Id
     */
    public void showErr(int msgId, int retryId) {
        mNoDataLine.setVisibility(GONE);
        mRetry.setText(retryId);
        showErr(msgId);
        if (GeneralUtils.isNotNull(mContentView))
            mContentView.setVisibility(GONE);
    }

    public void showEmpty(int msgId) {
        mNoDataLine.setVisibility(GONE);

        stopAnim();

        mTip.setText(msgId);
        //当前正在刷新
        if (LOADING_STATUS.EMPTY == mCurrStatus)
            return;
        mCurrStatus = LOADING_STATUS.EMPTY;
        mRetry.setVisibility(View.INVISIBLE);
        //设置图片背景
        mImage.setBackgroundResource(R.drawable.view_loading_empty);
        setVisibility(View.VISIBLE);
        if (GeneralUtils.isNotNull(mContentView))
            mContentView.setVisibility(GONE);
    }

    /**
     * 显示数据为空并且带箭头指示的UI
     *
     * @param msgId
     */
    public void showEmptyWithLine(String msgId) {
        mNoDataLine.setVisibility(VISIBLE);
        mTip.setText(msgId);
        //当前正在刷新
        if (LOADING_STATUS.EMPTY == mCurrStatus)
            return;
        mCurrStatus = LOADING_STATUS.EMPTY;
        mRetry.setVisibility(View.INVISIBLE);
        //设置图片背景
        mImage.setBackgroundResource(R.drawable.view_loading_empty);
        setVisibility(View.VISIBLE);
        if (GeneralUtils.isNotNull(mContentView))
            mContentView.setVisibility(GONE);
    }

    /**
     * 慎用
     * <p>
     * 通知被撤回后，显示空白的背景页
     */
    public void showEmptyBg() {
        mNoDataLine.setVisibility(GONE);
        stopAnim();

        mTip.setText("");
        //当前正在刷新
        if (LOADING_STATUS.EMPTY == mCurrStatus)
            return;
        mCurrStatus = LOADING_STATUS.EMPTY;
        mRetry.setVisibility(View.INVISIBLE);
        //设置图片背景
        mImage.setVisibility(View.INVISIBLE);
        setVisibility(View.VISIBLE);
        if (GeneralUtils.isNotNull(mContentView))
            mContentView.setVisibility(GONE);
    }


    /**
     * 将加载view 隐藏 GONE
     */
    public void setGone() {
        stopAnim();

        mCurrStatus = LOADING_STATUS.GONE;
        super.setVisibility(View.GONE);
        if (GeneralUtils.isNotNull(mContentView))
            mContentView.setVisibility(VISIBLE);
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == GONE)
            mCurrStatus = LOADING_STATUS.GONE;
        super.setVisibility(visibility);
    }

    /**
     * <停止小鹰快跑动画>
     */
    private void stopAnim() {
        if (null != mGifDrawable && mGifDrawable.isRunning()) {
            mGifDrawable.stop();
            mGifDrawable = null;
        }
    }

    /**
     * 获取当前的状态
     *
     * @return
     */
    public LOADING_STATUS getCurrStatus() {
        return mCurrStatus;
    }

    /**
     * 设置监听
     *
     * @param lister
     */
    public void setRefreshListener(LoadingRefreshListener lister) {
        this.lister = lister;
    }

    /**
     * 描述：刷新
     */
    public interface LoadingRefreshListener {

        /**
         * 自定义SFLoadingView点击刷新事件
         */
        void doRefresh();
    }

    public void setContentView(View contentView) {
        mContentView = contentView;
    }
}
