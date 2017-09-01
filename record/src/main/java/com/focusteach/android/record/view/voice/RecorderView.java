package com.focusteach.android.record.view.voice;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * <录音页面View辅助类>
 *
 *     @see RecorderView#newStartRecordBtnSelector(Context) 开始录音按钮背景
 *     @see RecorderView#newStopRecordBtnSelector(Context) 停止录音按钮背景
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/21]
 * @see [相关类/方法]
 * @since [V1]
 */
public class RecorderView {

    /**
     * 开始录音按钮的selector背景
     * @param context context
     */
    public static StateListDrawable newStartRecordBtnSelector(Context context) {
        GradientDrawable outerRing = newOuterRing(context);
        GradientDrawable pressedInnerRing = newInnerRing(context, true);
        GradientDrawable normalInnerRing = newInnerRing(context, false);

        int ringWidth = dip2px(context, (98 - 76) / 2);
        Drawable[] normal = new Drawable[]{outerRing, normalInnerRing};
        LayerDrawable layerNormal = new LayerDrawable(normal);
        layerNormal.setLayerInset(0, 0, 0, 0, 0);
        layerNormal.setLayerInset(1, ringWidth, ringWidth, ringWidth, ringWidth);

        Drawable[] pressed = new Drawable[]{outerRing, pressedInnerRing};
        LayerDrawable layerPressed = new LayerDrawable(pressed);
        layerPressed.setLayerInset(0, 0, 0, 0, 0);
        layerPressed.setLayerInset(1, ringWidth, ringWidth, ringWidth, ringWidth);

        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed}, layerPressed);
        selector.addState(new int[]{}, layerNormal);

        return selector;
    }

    /**
     * 结束录音按钮的selector背景
     * @param context context
     */
    public static StateListDrawable newStopRecordBtnSelector(Context context) {
        GradientDrawable outerRing = newOuterRing(context);
        GradientDrawable pressedInnerRect = newInnerRect(context, true);
        GradientDrawable normalInnerRect = newInnerRect(context, false);

        int ringWidth = dip2px(context, (98 - 44) / 2);
        Drawable[] normal = new Drawable[]{outerRing, normalInnerRect};
        LayerDrawable layerNormal = new LayerDrawable(normal);
        layerNormal.setLayerInset(0, 0, 0, 0, 0);
        layerNormal.setLayerInset(1, ringWidth, ringWidth, ringWidth, ringWidth);

        Drawable[] pressed = new Drawable[]{outerRing, pressedInnerRect};
        LayerDrawable layerPressed = new LayerDrawable(pressed);
        layerPressed.setLayerInset(0, 0, 0, 0, 0);
        layerPressed.setLayerInset(1, ringWidth, ringWidth, ringWidth, ringWidth);

        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed}, layerPressed);
        selector.addState(new int[]{}, layerNormal);

        return selector;
    }

    /**dp转px**/
    protected static int dip2px(Context context, int dipValue) {
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    /**
     * 描边宽度1px，3倍及以上分辨率2px。
     */
    protected static int commonLineWidth(Context context) {
        float den = context.getResources().getDisplayMetrics().density;
        if (den >= 3) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * 带边框线的外圈
     */
    protected static GradientDrawable newOuterRing(Context context) {
        // 颜色、线条、尺寸
        int strokeColor = Color.parseColor("#dadbdd");
        int fillColor = Color.parseColor("#ffffff");
        int strokeWidth = commonLineWidth(context);
        int _98dp = dip2px(context, 98);

        GradientDrawable shapeOuterRing = new GradientDrawable();
        shapeOuterRing.setShape(GradientDrawable.OVAL);
        shapeOuterRing.setStroke(strokeWidth, strokeColor);
        shapeOuterRing.setColor(fillColor);
        shapeOuterRing.setSize(_98dp, _98dp);

        return shapeOuterRing;
    }

    /**
     * 纯色填充无边框的内圈
     * @param context
     * @return
     */
    protected static GradientDrawable newInnerRing(Context context, boolean pressed) {
        // 颜色、尺寸
        int normalColor = Color.parseColor("#f26314");
        int pressedColor = Color.parseColor("#ff6815");
        int _76dp = dip2px(context, 76);

        GradientDrawable shapeInnerRing = new GradientDrawable();
        shapeInnerRing.setShape(GradientDrawable.OVAL);
        shapeInnerRing.setColor(pressed ? pressedColor : normalColor);
        shapeInnerRing.setSize(_76dp, _76dp);

        return shapeInnerRing;
    }

    /**
     * 纯色填充无边框的内圆角矩形
     * @param context
     * @return
     */
    protected static GradientDrawable newInnerRect(Context context, boolean pressed) {
        // 颜色、尺寸
        int normalColor = Color.parseColor("#f26314");
        int pressedColor = Color.parseColor("#ff6815");
        int _44dp = dip2px(context, 44);
        int _8dp = dip2px(context, 8);

        GradientDrawable shapeInnerRect = new GradientDrawable();
        shapeInnerRect.setShape(GradientDrawable.RECTANGLE);
        shapeInnerRect.setColor(pressed ? pressedColor : normalColor);
        shapeInnerRect.setCornerRadius(_8dp);
        shapeInnerRect.setSize(_44dp, _44dp);

        return shapeInnerRect;
    }
}
