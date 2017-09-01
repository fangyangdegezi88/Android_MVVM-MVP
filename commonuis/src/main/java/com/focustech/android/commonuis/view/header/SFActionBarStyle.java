package com.focustech.android.commonuis.view.header;

import android.view.View;


/**
 * <actionbar mvvm对应的module>
 *
 * @author yanguozhu
 * @version [版本号, 2017-06-23]
 * @see [相关类/方法]
 * @since [V1]
 */

public class SFActionBarStyle {

    /**
     * 左图
     */
    public int backDrawableId;
    /**
     * 左文字
     */
    public String backText;

    /**
     * 左文字大小
     */
    public int backTextSize;
    /**
     * 居中title
     */
    public String titleText;
    /**
     * 右图
     */
    public int moreDrawableId;
    /**
     * 右文字
     */
    public String moreText;

    /**
     * 背景颜色
     */
    public int bgColorId;

    /**
     * 文字颜色
     */
    public int txtBgId;

    /**
     * 右侧文字颜色
     */
    private int rightTxtColor;

    /**
     * 右侧按钮是否可用
     */
    private boolean rightIsEnable;

    /**
     * 控制左下拉图标显示隐藏
     */
    public int backChooseIvStatus = View.GONE;

    /**
     * 控制左下拉图背景
     */
    private int backChooseIvDrawableId;

    /**
     * 左视图显示隐藏
     */
    private int backVisible = View.VISIBLE;
    /**
     * 右视图显示隐藏
     */
    private int moreVisible = View.VISIBLE;

    public static SFActionBarStyle newBuilder() {
        return new SFActionBarStyle();
    }

    public int getBackDrawableId() {
        return backDrawableId;
    }

    public SFActionBarStyle setBackDrawableId(int backDrawableId) {
        this.backDrawableId = backDrawableId;
        return this;
    }

    public String getBackText() {
        return backText;
    }


    public SFActionBarStyle setBackText(String backText) {
        this.backText = backText;
        return this;
    }

    public String getTitleText() {
        return titleText;
    }

    public SFActionBarStyle setTitleText(String titleText) {
        this.titleText = titleText;
        return this;
    }

    public int getMoreDrawableId() {
        return moreDrawableId;
    }

    public SFActionBarStyle setMoreDrawableId(int moreDrawableId) {
        this.moreDrawableId = moreDrawableId;
        return this;
    }

    public String getMoreText() {
        return moreText;
    }

    public SFActionBarStyle setMoreText(String moreText) {
        this.moreText = moreText;
        return this;
    }

    public int getBgColorId() {
        return bgColorId;
    }

    public SFActionBarStyle setBgColorId(int bgColorId) {
        this.bgColorId = bgColorId;
        return this;
    }

    public int getTxtBgId() {
        return txtBgId;
    }

    public SFActionBarStyle setTxtBgId(int txtBgId) {
        this.txtBgId = txtBgId;
        return this;
    }

    public int getBackTextSize() {
        return backTextSize;
    }

    public SFActionBarStyle setBackTextSize(int backTextSize) {
        this.backTextSize = backTextSize;
        return this;
    }

    public int getBackChooseIvStatus() {
        return backChooseIvStatus;
    }

    public SFActionBarStyle setBackChooseIvStatus(int backChooseIvStatus) {
        this.backChooseIvStatus = backChooseIvStatus;
        return this;
    }

    public int getBackChooseIvDrawableId() {
        return backChooseIvDrawableId;
    }

    public SFActionBarStyle setBackChooseIvDrawableId(int backChooseIvDrawableId) {
        this.backChooseIvDrawableId = backChooseIvDrawableId;
        return this;
    }

    public int getBackVisible() {
        return backVisible;
    }

    public SFActionBarStyle setBackVisible(int backVisible) {
        this.backVisible = backVisible;
        return this;
    }

    public int getMoreVisible() {
        return moreVisible;
    }

    public SFActionBarStyle setMoreVisible(int moreVisible) {
        this.moreVisible = moreVisible;
        return this;
    }

    public SFActionBarStyle setRightTxtColor(int rightTxtColor) {
        this.rightTxtColor = rightTxtColor;
        return this;
    }

    public int getRightTxtColor() {
        return rightTxtColor;
    }

    public boolean isRightIsEnable() {
        return rightIsEnable;
    }

    public SFActionBarStyle setRightIsEnable(boolean rightIsEnable) {
        this.rightIsEnable = rightIsEnable;
        return this;
    }
}
