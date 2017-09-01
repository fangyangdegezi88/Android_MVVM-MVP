package com.focustech.android.commonuis.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.android.commonlibs.util.DensityUtil;
import com.focustech.android.commonuis.R;


/**
 * <底部选择对话框>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/29]
 * @see [相关类/方法]
 * @since [V1]
 */
public class SFSelectDialog extends Dialog {

    private Context context;
    /**
     * 数据集
     */
    private String[] data;

    /**
     * 当前选中的item
     */
    private int checkPosition = -1;

    /**
     * 数据集容器
     */
    private LinearLayout mListContainer;

    /**
     * 取消按钮
     */
    private TextView mCancel;


    /**
     * 点击监听
     */
    private SFSelectDialogListener listener;

    public SFSelectDialog(Context context) {
        super(context, R.style.FilterDialogStyle);
        setOwnerActivity((Activity) context);
        this.context = context;
        // dialog 停留在底部，并且全屏
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    /**
     * 设置选择框list数据和当前选中的选项
     *
     * @param data     数据集
     * @param position 选中position
     */
    public void setSelectDataAndIndex(String[] data, int position) {
        setSelectData(data);
        setSelectIndex(position);
    }

    /**
     * 设置选择框list数据 不需要显示当前选中项的时候可以使用
     *
     * @param data
     */
    public SFSelectDialog setSelectData(String[] data) {
        this.data = data;
        checkPosition = -1;
        if (null != mListContainer) {
            mListContainer.removeAllViews();
            initView();
        }
        return this;
    }

    /**
     * 当前选中的选项
     *
     * @param position 位置
     */
    public void setSelectIndex(int position) {
        int count = data.length;
        //位置大于数据长度
        if (position >= count) return;
        //将当前选中项隐藏
        if (null != mListContainer && checkPosition >= 0) {
            View view = mListContainer.findViewWithTag(checkPosition + count);
            if (null != view)
                view.setVisibility(View.GONE);
        }

        checkPosition = position;
        //将目标位置显示
        if (null != mListContainer) {
            View view = mListContainer.findViewWithTag(position + count);
            if (null != view)
                view.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 动态添加view
     */
    private void initView() {
        LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(44));
        //TextView的LayoutParams
        LinearLayout.LayoutParams txtParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //分割线的LayoutParams
        LinearLayout.LayoutParams dividerParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(1));
        //选中图片的LayoutParams
        RelativeLayout.LayoutParams imageParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageParam.setMargins(0, 0, DensityUtil.dip2px(15), 0);
        imageParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        int count = data.length;
        for (int i = 0; i < count; i++) {
            //添加item
            RelativeLayout mItem = new RelativeLayout(context);
            mItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            mItem.setLayoutParams(itemParam);
            //item中添加的TextView
            TextView textView = new TextView(context);
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            textView.setLayoutParams(txtParam);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(16);
            textView.setText(data[i]);
            textView.setTextColor(ContextCompat.getColor(context, R.color.tab_txt_sel_color));
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        int tag = (int) v.getTag();
                        //显示当前选中位置
                        setSelectIndex(tag);
                        listener.selectDialogitemClick(tag);
                        dismiss();
                    }
                }
            });
            //item中添加的ImageView
            ImageView checkImg = new ImageView(context);
            checkImg.setLayoutParams(imageParam);
            checkImg.setImageResource(R.drawable.common_icon_selected_alert);
            //控制显示与隐藏
            if (i == checkPosition)
                checkImg.setVisibility(View.VISIBLE);
            else
                checkImg.setVisibility(View.GONE);

            mItem.addView(textView);
            mItem.addView(checkImg);
            checkImg.setTag(i + count);
            //将Item加到LinearLayout中
            mListContainer.addView(mItem);
            //添加分割线
            if (i != count - 1) {
                View divider = new View(context);
                divider.setBackgroundColor(ContextCompat.getColor(context, R.color.app_list_bg_color));
                divider.setLayoutParams(dividerParam);
                mListContainer.addView(divider);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_select_dialog);
        mListContainer = (LinearLayout) findViewById(R.id.view_select_dialog_container);
        mCancel = (TextView) findViewById(R.id.view_select_dialog_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) dismiss();
            }
        });
        initView();
    }

    /**
     * 设置选择监听
     *
     * @param listener
     */
    public SFSelectDialog setListener(SFSelectDialogListener listener) {
        this.listener = listener;
        return this;
    }


    /**
     * 选择item监听
     */
    public interface SFSelectDialogListener {
        //item click
        public void selectDialogitemClick(int position);
    }

}
