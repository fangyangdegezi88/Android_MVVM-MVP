package com.focustech.android.photo.myalbum.myalbum;

import com.focustech.android.commonuis.biz.BaseCommonPresenter;
import com.focustech.android.photo.myalbum.MediaDirPickerFragment;
import com.focustech.android.photo.myalbum.util.PhotoEvent;

/**
 * <Controller>
 *
 * @see MediaDirPickerFragment  <Context/>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/15]
 * @see [相关类/方法]
 * @since [V1]
 */
public class PhotoDirPickerPresenter extends BaseCommonPresenter<IDirectoryView> {

    public PhotoDirPickerPresenter() {
        super(true); // subscribe EventBus
    }

    public void onEventMainThread(PhotoEvent event) {
        if (event == PhotoEvent.LOAD_PHOTO_DIRECTORIES_SUCCESS) {
            if(mvpView!=null)mvpView.refreshDirs();
        }
    }
}
