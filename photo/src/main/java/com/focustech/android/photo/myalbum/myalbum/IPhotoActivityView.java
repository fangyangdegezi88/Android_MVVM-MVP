package com.focustech.android.photo.myalbum.myalbum;


import com.focustech.android.commonuis.biz.IMvpView;
import com.focustech.android.photo.myalbum.MediaPickerActivity;

/**
 * <View>
 *
 * @see PhotoActivityPresenter  <Controller>
 * @see MediaPickerActivity <Context>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/15]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface IPhotoActivityView extends IMvpView {
    /**
     * <修改ActionBar的标题>
     * @param changeTitle
     */
    public void changeActionBarTitle(String changeTitle);

    /**
     * <控制标题栏是否可见>
     * */
    void setActionBarVisiable(boolean visiable);

    /**
     * <设置底部工具栏是否可见>
     * @param visible
     */
    public void setToolBar(boolean visible);

    /**
     * <设置提交按钮的样式>
     * @param enabled 是否可点
     * @param s 内容
     */
    public void setCommitBtStyle(boolean enabled, String s);

    /**
     * <设置预览按钮的样式>
     * @param visible 是否可见
     * @param enabled 是否可点
     */
    public void setPreviewBtn(boolean visible, boolean enabled);

}
