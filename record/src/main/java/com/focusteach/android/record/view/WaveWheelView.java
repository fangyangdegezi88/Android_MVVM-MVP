package com.focusteach.android.record.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.focusteach.android.record.R;
import com.focusteach.android.record.util.queue.FIFOImpl;
import com.focustech.android.commonlibs.util.DensityUtil;


/**
 * 用来展现音频音量变化的直方体波浪
 * @author zhangzeyu
 * @version [版本号, 2016/7/11]
 * @see [相关类/方法]
 * @since [V1]
 */
public class WaveWheelView extends View {
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 边界
     */
    private RectF mBounds;
    /**
     * 宽度
     */
    private float width;
    /**
     * 列宽
     */
    private float mRowWidth;
    /**
     * 高度
     */
    private float height;
    /**
     * 圆角
     */
    private float radius;
    /**
     *  列数
     */
    private int mRowCount = 8;
    /**
     * 边框颜色
     */
    private int mBorderColor = R.color.app_caution_color;
    /**
     * 边框宽度
     */
    private int mBorderWidth = 2;
    /**
     * 单位长度：2dp
     */
    private float unit_2dp;
    /**
     * 先进先出且固定长度List
     */
    private FIFOImpl<Double> mDB = null;
    /**
     * 用来画矩形
     */
    private RectF targetRect;

    /**
     * 反转
     */
    private boolean reverse;

    public WaveWheelView(Context context) {
        this(context, null);
    }

    public WaveWheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(mBorderColor));
        mPaint.setStrokeWidth(mBorderWidth);

        mDB = new FIFOImpl<Double>(mRowCount);
        unit_2dp = DensityUtil.dip2px(this.getContext(),2);
        targetRect = new RectF();
    }

    /**
     * 设置镜像反转,在addDB之前调用此方法
     * @param reverse true视图从最右边向左依次展示分贝
     */
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
    /**
     * 新添加一个分贝值
     *
     */
    public void addDB(double db) {
        mDB.addFirst(db);
        invalidate();
    }

    /**
     * 清除分贝值，分贝值重新归零
     */
    public void clearDB() {
        mDB.clear();
        invalidate();
    }

    /**
     * 获得第n列的分贝值
     * @param row 列
     * @return double 分贝
     */
    public double getDB(int row) {
        double db = 0;
        try {
            db = mDB.get(row);
        } catch (IndexOutOfBoundsException ex) {
        }

        return db;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        for (int i = 0; i < mRowCount; i ++) {
            float rectHeight = computeHeight(getDB(i));
            float marginTop = (unit_2dp * 8 - rectHeight) / 2;
            if (reverse) {
                targetRect.left = mBounds.right - mRowWidth * (2 * i + 1);
                targetRect.top = mBounds.top + marginTop;
                targetRect.right = mBounds.right - mRowWidth * 2 * i;
                targetRect.bottom = marginTop + rectHeight;
            } else {
                targetRect.left = mBounds.left + mRowWidth * 2 * i;
                targetRect.top = mBounds.top + marginTop;
                targetRect.right = mBounds.left + mRowWidth * (2 * i + 1);
                targetRect.bottom = marginTop + rectHeight;
            }
            canvas.drawRoundRect(targetRect, 0, 0, mPaint);
        }
    }

    /**
     * 计算高度
     * @rule:
     * 0dB是人刚能听到的最微弱的声音；
     * 10dB大概为树叶微动；
     * 30dB～40dB是较为理想的安静环境；
     * 超过50dB会影响休息和睡眠
     * 超过70dB会影响学习和工作；
     * 超过90dB会影响听力；
     * @param db db值
     * @return height 高度
     */
    private float computeHeight(double db) {

        float result = unit_2dp * 2;     // 最小高度为4dp
        if (db >= 30 && db < 40) {        // 正常说话为8dp
            result = unit_2dp * 3;
        } else if (db >= 40 && db < 50) {
            result = unit_2dp * 4;
        } else if (db >= 50 && db < 70) {
            result = unit_2dp * 6;
        } else if (db >= 70 && db < 85) {
            result = unit_2dp * 7;
        } else if (db >= 85) {
            result = unit_2dp * 8;          // 最大高度为16dp
        }

        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());

        width = mBounds.right - mBounds.left;
        height = mBounds.bottom - mBounds.top;

        if (width < height) {
            radius = width / 4;
        } else {
            radius = height / 4;
        }

        mRowWidth = width / (2 * mRowCount - 1);
    }
}
