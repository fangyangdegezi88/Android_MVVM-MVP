package com.focustech.android.commonuis.view.header;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonuis.R;

/**
 * <header>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/27]
 * @see [相关类/方法]
 * @since [V1]
 */
public class SFActionBar extends FrameLayout implements View.OnClickListener {
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * title container
     */
    private RelativeLayout mAction_container;

    private TextView mAction_title, mAction_right_tv, mAction_back;
    private ImageView mAction_right_iv;
    /**
     * 返回按钮container
     */
    private LinearLayout mAction_back_container;
    /**
     * 返回按钮container
     */
    private LinearLayout mAction_more_container;

    /**
     * 标题旁下拉按钮
     */
    private ImageView mChooseIv;

    /**
     * 返回按钮旁的下拉按钮
     */
    private ImageView mBackChooseIv;
    /**
     * 分割线
     */
    private View mDivider;

    private SFActionBarListener mSFActionBarListener;

    public SFActionBar(Context context) {
        super(context);
        init(context);
    }

    public SFActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SFActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public SFActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * @param context 初始化
     */
    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.common_title, null);
        mAction_container = (RelativeLayout) view.findViewById(R.id.common_title_container);
        mAction_title = (TextView) view.findViewById(R.id.tv_title);
        mAction_right_tv = (TextView) view.findViewById(R.id.tv_right);
        mAction_right_iv = (ImageView) view.findViewById(R.id.iv_right);
        mAction_back = (TextView) view.findViewById(R.id.back);
        mAction_back_container = (LinearLayout) view.findViewById(R.id.ll_back);
        mAction_more_container = (LinearLayout) view.findViewById(R.id.ll_more);
        mChooseIv = (ImageView) view.findViewById(R.id.choose_iv);
        mBackChooseIv = (ImageView) view.findViewById(R.id.back_choose_iv);
        mDivider = view.findViewById(R.id.actionbar_divider);
        mChooseIv.setOnClickListener(this);
        mBackChooseIv.setOnClickListener(this);
        mAction_right_tv.setOnClickListener(this);
        mAction_right_iv.setOnClickListener(this);
        mAction_back.setOnClickListener(this);
        mAction_back_container.setOnClickListener(this);
        mAction_more_container.setOnClickListener(this);
        mAction_title.setOnClickListener(this);

        addView(view);
    }

    /**
     * 设置ActionBar的背景颜色
     *
     * @param bgColor 背景色Id
     */
    public void setActionBgColor(int bgColor) {
        mAction_container.setBackgroundColor(ContextCompat.getColor(mContext, bgColor));
        setActionDividerBgColor(bgColor);
    }

    public RelativeLayout getAction_container() {
        return mAction_container;
    }

    /**
     * 设置ActionBar的背景颜色和字体颜色
     *
     * @param bgColor    背景色Id
     * @param titleColor title字体颜色
     */
    public void setActionBgColor(int bgColor, int titleColor) {
        mAction_container.setBackgroundColor(ContextCompat.getColor(mContext, bgColor));
        mAction_title.setTextColor(ContextCompat.getColor(mContext, titleColor));
    }

    /**
     * 标题颜色
     *
     * @param titleColor
     */
    public void setActionTitleColor(int titleColor) {
        mAction_title.setTextColor(ContextCompat.getColor(mContext, titleColor));
    }


    /**
     * 设置ActionBar的返回键信息
     * <p/>
     * 返回按钮
     * 可以为箭头返回 也可以自定义文字
     * 当使用文字时将背景去除
     *
     * @param btnMsg
     */
    public void setActionLeftTxt(String btnMsg) {
        if (GeneralUtils.isNotNullOrEmpty(btnMsg)) {
            mAction_back.setText(btnMsg);
            mAction_back.setBackgroundResource(0);
        }
    }


    public void setActionLeftTxtSize(int dpSize) {
        if (dpSize != 0)
            mAction_back.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dpSize);
    }

    /**
     * 设置返回键的背景图
     *
     * @param drawableId
     */
    public void setActionLeftDrawable(int drawableId) {
        mAction_back.setText("");
        if (drawableId == 0) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mAction_back.setBackground(ContextCompat.getDrawable(mContext, drawableId));
        else
            mAction_back.setBackgroundDrawable(ContextCompat.getDrawable(mContext, drawableId));
    }

    /**
     * 设置ActionBar的返回键是否显示
     *
     * @param visible
     */
    public void setActionLeftVisible(int visible) {
        mAction_back_container.setVisibility(visible);
    }

    /**
     * 设置ActionBar的右键信息
     * 可以为箭头返回 也可以自定义文字
     * 当使用文字时将背景去除
     *
     * @param btnMsg
     */
    public void setActionRightTxt(String btnMsg) {
        if (GeneralUtils.isNotNullOrEmpty(btnMsg)) {
            mAction_right_tv.setVisibility(View.VISIBLE);
            mAction_right_iv.setVisibility(View.GONE);
            mAction_right_tv.setText(btnMsg);
            mAction_right_tv.setBackgroundResource(0);
        }
    }

    /**
     * 设置标题右边是否可用 常用场景 不可提交或确定
     *
     * @param isEnable
     */
    public void setRightTextViewEnableStatus(boolean isEnable) {
        mAction_right_tv.setEnabled(isEnable);
    }

    public void setRightImageViewEnableStatus(boolean isEnable) {
        mAction_right_tv.setEnabled(isEnable);
    }

    /**
     * 设置右上角 按钮 字体颜色
     *
     * @param color
     */
    public void setRightActionTextColor(int color) {
        mAction_right_tv.setTextColor(color);
    }

    /**
     * 设置下拉iv显示和隐藏
     *
     * @param isVisible
     */
    public void setChooseIvStatus(boolean isVisible) {
        mChooseIv.setVisibility(isVisible ? VISIBLE : GONE);
    }

    /**
     * 设置左按钮旁边的筛选按钮显示隐藏
     *
     * @param visible
     */
    public void setBackChooseIvStatus(int visible) {
        mBackChooseIv.setVisibility(visible);
    }

    /**
     * 设置左按钮旁边的筛选按钮背景图
     *
     * @param drawableId
     */
    public void setBackChooseIvDrawableId(int drawableId) {
        if (drawableId != 0)
            mBackChooseIv.setBackgroundResource(drawableId);
    }

    /**
     * 设置右键的背景图
     *
     * @param drawableId
     */
    public void setActionRightDrawable(int drawableId) {
        if (drawableId == 0) return;
        mAction_right_tv.setVisibility(View.GONE);
        mAction_right_iv.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mAction_right_iv.setBackground(ContextCompat.getDrawable(mContext, drawableId));
        else
            mAction_right_iv.setBackgroundDrawable(ContextCompat.getDrawable(mContext, drawableId));
    }

    /**
     * 设置右键的背景图
     *
     * @param drawableId
     */
    public void setActionRightDrawableWithEnable(int drawableId, boolean enable) {

        setActionRightDrawable(drawableId);

        if (enable) {
            mAction_right_iv.setAlpha(1f);
        } else {
            mAction_right_iv.setAlpha(0.3f);
        }
    }


    /**
     * 设置ActionBar的右键是否显示
     *
     * @param visible
     */
    public void setActionRightVisible(int visible) {
        mAction_more_container.setVisibility(visible);
    }

    /**
     * 设置 title的名称
     *
     * @param btnMsg
     */
    public void setActionTitle(String btnMsg) {
        if (GeneralUtils.isNotNullOrEmpty(btnMsg)) {
            mAction_title.setText(btnMsg);
        }
    }

    /**
     * 设置头部分割线背景色
     *
     * @param bgColor
     */
    private void setActionDividerBgColor(int bgColor) {
        mDivider.setBackgroundColor(ContextCompat.getColor(mContext, bgColor));
    }

    /**
     * 返回当前标题名称
     *
     * @return
     */
    public String getActionTitle() {
        if (null != mAction_title)
            return mAction_title.getText().toString();
        return "";
    }


    @Override
    public void onClick(View v) {
        if (null == mSFActionBarListener) return;
        if (v.getId() == R.id.ll_back || v.getId() == R.id.back) {
            mSFActionBarListener.leftBtnClick();
        } else if (v.getId() == R.id.tv_right || v.getId() == R.id.iv_right || v.getId() == R.id.ll_more) {
            mSFActionBarListener.rightBtnClick();
        } else if (v.getId() == R.id.choose_iv) {
            mSFActionBarListener.chooseIvClick(v);
        } else if (v.getId() == R.id.back_choose_iv) {
            mSFActionBarListener.backChooseIvClick(v);
        } else if (v.getId() == R.id.tv_title) {
            if (mSFActionBarListener != null) {
                mSFActionBarListener.chooseIvClick(mChooseIv);
            }
        }
    }

    /**
     * 设置SFActionBar的监听
     *
     * @param SFActionBarListener
     */
    public void setSFActionBarListener(SFActionBarListener SFActionBarListener) {
        mSFActionBarListener = SFActionBarListener;
    }


    public View getActionBackView() {
        return mAction_back_container;
    }

    public View getActionMoreIvView() {
        return mAction_more_container;
    }

    public void setActionRightEnable(boolean rightIsEnable) {
        mAction_right_tv.setEnabled(rightIsEnable);
    }

    /**
     * 获取标题
     * */
    public View getActionTitleView(){
        return mAction_title;
    }

    public void setActionTitleEnable(boolean titleIsEnable) {
        mAction_title.setEnabled(titleIsEnable);
        if(titleIsEnable){
            mAction_title.setOnClickListener(this);
        }
    }

    /**
     * SFActionBar的监听
     */
    public interface SFActionBarListener {
        /**
         * 返回按钮点击事件
         */
        void leftBtnClick();

        /**
         * 更多按钮点击事件
         */
        void rightBtnClick();

        /**
         * 筛选按钮点击事件
         *
         * @param view
         */
        void chooseIvClick(View view);

        /**
         * 左按钮筛选按钮点击事件
         *
         * @param view
         */
        void backChooseIvClick(View view);
    }
}
