package com.focustech.android.photo.myalbum.myalbum;

import com.focustech.android.commonuis.biz.IMvpView;
import com.focustech.android.photo.myalbum.MediaDirPickerFragment;

/**
 * <View>
 *
 * @see PhotoDirPickerPresenter  <Controller>
 * @see MediaDirPickerFragment <Context/>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/15]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface IDirectoryView extends IMvpView {

    /**
     * <更新文件夹>
     */
    public void refreshDirs();

}
