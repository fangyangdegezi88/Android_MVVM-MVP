package com.focustech.android.photo.myalbum.myalbum;


import com.focustech.android.commonuis.biz.IMvpView;
import com.focustech.android.photo.myalbum.MediaPickerFragment;

/**
 * <View>
 *
 * @see PhotoPickerPresenter <Controller/>
 * @see MediaPickerFragment <Context/>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/15]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface IPhotoPickerView extends IMvpView {
    /**
     * <首次刷新图片>
     */
    public void refreshPhotos();

    /**
     * <切换图片文件夹>
     * @param position
     */
    public void swapToDirectory(int position);

    /**
     * <提示出错>
     * @param msg
     */
    public void alertWaring(String msg);

    /**
     * 按钮的文字
     * */
    void alertWarningBtnText(String text);

}
