package com.focustech.android.photo.myalbum.myalbum;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.focustech.android.commonuis.biz.BaseCommonPresenter;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.MediaDirPickerFragment;
import com.focustech.android.photo.myalbum.MediaPicker;
import com.focustech.android.photo.myalbum.MediaPickerFragment;
import com.focustech.android.photo.myalbum.util.PhotoEvent;
import com.focustech.android.photo.myalbum.MediaPickerActivity;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaEntity;
import com.focustech.android.photo.myalbum.myalbum.event.EventSwapDirectory;

import java.io.File;
import java.util.List;
import java.util.Locale;


/**
 * <Controller>
 *
 * @see MediaPickerFragment  <Context/>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/15]
 * @see [相关类/方法]
 * @since [V1]
 */
public class PhotoPickerPresenter extends BaseCommonPresenter<IPhotoPickerView> {

    private Context context;
    private int maxCount = 1;
    private int originalCount = 0;
    long singleFileMaxSize = 0;
    public PhotoPickerPresenter() {
        super(true); // subscribe EventBus
    }

    /**
     * 接收来自Activity的数据刷新命令
     * @see MediaPickerActivity#loadMediaDirectories(boolean) 数据加载
     * @see PhotoActivityPresenter#setDirs(List) 异步刷新
     * @param event
     */
    public void onEventMainThread(PhotoEvent event) {
        if (event == PhotoEvent.LOAD_PHOTO_DIRECTORIES_SUCCESS) {
            if(mvpView!=null)mvpView.refreshPhotos();
        }
    }

    /**
     * 接收来自Fragment的命令刷新文件夹
     * @see MediaDirPickerFragment#onItemClick(AdapterView, View, int, long)
     * @param command
     */
    public void onEventMainThread(EventSwapDirectory command) {
        if(mvpView!=null)mvpView.swapToDirectory(command.position());
    }

    public void init(Context context, int maxCount, int originalCount,long singleFileMaxSize) {
        this.context = context;
        this.maxCount = maxCount;
        this.originalCount = originalCount;
        this.singleFileMaxSize = singleFileMaxSize;
    }

    /**
     *
     * @param entity
     * @param isCheck
     * @param selectedItemCount
     * @return
     */
    public boolean checkPhotoItem(MediaEntity entity, boolean isCheck, int selectedItemCount) {

        if (!isCheck) {
            if (selectedItemCount + originalCount >= maxCount) {
                if(mvpView!=null)mvpView.alertWaring(formatMaxCountWarning(maxCount));
                return false;
            }

            if (entity.getSize() > singleFileMaxSize) {
                if(mvpView!=null){
                    mvpView.alertWaring(formatMaxSizeWarning(singleFileMaxSize));
                    mvpView.alertWarningBtnText(context.getString(R.string.i_know));
                }
                return false;
            }
        }
        return true;
    }


    private boolean fileSizeBeyond10MB(String path) {
        final File imgFile = new File(path);
        final long fileSizeM = imgFile.length() / 1048576;
        final long fileSizeK = imgFile.length() % 1048576;
        return (fileSizeM == 10 && fileSizeK > 0) || fileSizeM > 10;
    }

    private String formatMaxCountWarning(int maxCount) {
        return String.format(Locale.getDefault(), context.getString(R.string.photo_max_count), maxCount);
    }

    public String formatMaxSizeWarning(long singleFileMaxSize) {
        long _G = singleFileMaxSize / MediaPicker.GB;
        if( _G >= 1){
            return String.format(Locale.getDefault(), context.getString(R.string.choose_file_reach_max_size), _G+"G");
        }else{
            long _M = singleFileMaxSize / MediaPicker.MB;
            return String.format(Locale.getDefault(), context.getString(R.string.choose_file_reach_max_size), _M+"M");
        }

    }

    public int maxCount() {
        return maxCount;
    }

}
