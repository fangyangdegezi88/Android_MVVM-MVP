package com.focustech.android.commonuis.view.header;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;

/**
 * <页面基础公共功能实现>
 *
 * @author yanguozhu
 * @version [版本号, 2017-06-23]
 * @see [相关类/方法]
 * @since [V1]
 */

public class SFActionBarUtil {

    @BindingAdapter({"header"})
    public static void setHeader(SFActionBar bar, SFActionBarStyle header) {
        bar.setActionTitle(header.getTitleText());
        bar.setActionLeftVisible(header.getBackVisible());
        bar.setActionLeftDrawable(header.getBackDrawableId());
        bar.setActionLeftTxt(header.getBackText());
        bar.setActionRightVisible(header.getMoreVisible());
        bar.setActionRightDrawable(header.getMoreDrawableId());
        bar.setActionRightTxt(header.getMoreText());
        bar.setActionLeftTxtSize(header.getBackTextSize());
        bar.setBackChooseIvStatus(header.getBackChooseIvStatus());
        bar.setBackChooseIvDrawableId(header.getBackChooseIvDrawableId());
        bar.setActionRightEnable(header.isRightIsEnable());
        if (0 != header.getBgColorId())
            bar.setActionBgColor(header.getBgColorId());
        if (0 != header.getTxtBgId())
            bar.setActionTitleColor(header.getTxtBgId());
        if (0 != header.getRightTxtColor())
            bar.setRightActionTextColor(header.getRightTxtColor());
    }

    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }

}
