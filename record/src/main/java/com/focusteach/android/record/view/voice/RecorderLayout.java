package com.focusteach.android.record.view.voice;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 触发录音的按钮
 */
public class RecorderLayout extends LinearLayout {
    /**
     * 提示语：点击录音
     */
    private TextView mTextView;
    /**
     * 录音开始
     */
    private ImageView mRecordButton;
    /**
     * 最后一次点击的时间
     */
    private long mLastClick;

    private OnRecordClickListener onRecordClickListener;

    public RecorderLayout(Context context) {
        super(context);
        init();
    }

    public RecorderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecorderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    /**
     * 初始化UI
     */
    public void init() {
        createComponents();

        mRecordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecordClickListener != null && !isFastClick()) {
                    onRecordClickListener.openRecorder();
                }
            }
        });
    }

    public void setOnRecordClickListener(OnRecordClickListener onRecordClickListener) {
        this.onRecordClickListener = onRecordClickListener;
    }

    /**
     * 创建所需要用的View
     */
    private void createComponents() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        int _45dp = RecorderView.dip2px(getContext(), 45);
        LayoutParams tvLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tvLp.topMargin = _45dp;
        int textColor = Color.parseColor("#666666");
        int _16dp = RecorderView.dip2px(getContext(), 16);
        mTextView = new TextView(getContext());
        mTextView.setText("点击录音");
        mTextView.setTextSize(16);
        mTextView.setTextColor(textColor);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setPadding(0, 0, 0, _16dp);
        addView(mTextView, tvLp);

        int _98dp = RecorderView.dip2px(getContext(), 98);
        mRecordButton = new ImageView(getContext());
        LayoutParams ivLp = new LayoutParams(_98dp,
                _98dp);
        mRecordButton.setImageDrawable(RecorderView.newStartRecordBtnSelector(getContext()));
        addView(mRecordButton, ivLp);
    }

    /**
     * 防止重复点击
     */
    public boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - mLastClick > 0 && now - mLastClick < 300)
            return true;

        mLastClick = now;
        return false;
    }

    public interface OnRecordClickListener {
        public void openRecorder();
    }
}
