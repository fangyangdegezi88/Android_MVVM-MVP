package com.focustech.android.photo.myalbum;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.focustech.android.photo.R;


/**
 * <辅助启动类>更便捷的打开相册</辅助启动类>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/18]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MediaPicker {
    public static final long KB = 1024 ;
    public static final long MB = KB * 1024;
    public static final long GB = MB * 1024;

    public static final long SINGLE_UPLOAD_FILE_MAX_SIZE = 15 * GB;

    public static final long SINGLE_ATTACHMENT_FILE_MAX_SIZE = 10 * MB;

    public static final int REQUEST_CODE                   = 233;

    public final static int DEFAULT_MAX_COUNT              = 6;
    public final static int DEFAULT_COLUMN_NUMBER          = 4;

    public final static String KEY_SELECTED_MEDIA = "SELECTED_MEDIA";

    public final static String EXTRA_MAX_COUNT             = "MAX_COUNT";
    public final static String EXTRA_SHOW_GIF              = "SHOW_GIF";
    public final static String EXTRA_SHOW_VIDEO            = "SHOW_VIDEO";//是否展示视频
    public final static String EXTRA_SHOW_UPLOAD_FOLDER_NAME = "SHOW_UPLOAD_FOLDER_NAME";//是否展示云盘文件夹名字
    public final static String EXTRA_SHOW_CAMERA           = "SHOW_CAMERA";
    public final static String EXTRA_PREVIEW_ENABLED       = "PREVIEW_ENABLED";
    public final static String EXTRA_GRID_COLUMN           = "column";
    public final static String EXTRA_ORIGINAL_PHOTOS_COUNT = "ORIGINAL_PHOTOS_COUNT";
    public final static String EXTRA_CAN_PREVIEW = "can_preview";
    public final static String EXTRA_CHOOSE_SINGLE_FILE_MAX_SIZE = "CHOOSE_SINGLE_FILE_MAX_SIZE";//供选择的单个文件最大长度

    public static PhotoPickerBuilder builder() {
        return new PhotoPickerBuilder();
    }

    public static class PhotoPickerBuilder {
        private Bundle mPickerOptionsBundle;
        private Intent mPickerIntent;

        public PhotoPickerBuilder() {
            mPickerOptionsBundle = new Bundle();
            mPickerIntent = new Intent();
        }

        /**
         * Send the Intent from an Activity with a custom request code
         *
         * @param activity    Activity to receive result
         * @param requestCode requestCode for result
         */
        public void start(@NonNull Activity activity, int requestCode) {
            activity.startActivityForResult(getIntent(activity), requestCode);
            activity.overridePendingTransition(R.anim.dialog_enter_from_bottom, 0);
        }

        /**
         * Send the Intent with a custom request code
         *
         * @param fragment    Fragment to receive result
         * @param requestCode requestCode for result
         */
        public void start(@NonNull Context context, @NonNull android.support.v4.app.Fragment fragment, int requestCode) {
            fragment.startActivityForResult(getIntent(context), requestCode);
            fragment.getActivity().overridePendingTransition(R.anim.dialog_enter_from_bottom, 0);
        }

        /**
         * Send the Intent with a custom request code
         *
         * @param fragment    Fragment to receive result
         */
        public void start(@NonNull Context context, @NonNull android.support.v4.app.Fragment fragment) {
            fragment.startActivityForResult(getIntent(context), REQUEST_CODE);
            fragment.getActivity().overridePendingTransition(R.anim.dialog_enter_from_bottom, 0);
        }

        /**
         * Get Intent to start {@link MediaPickerActivity}
         *
         * @return Intent for {@link MediaPickerActivity}
         */
        public Intent getIntent(@NonNull Context context) {
            mPickerIntent.setClass(context, MediaPickerActivity.class);
            mPickerIntent.putExtras(mPickerOptionsBundle);
            return mPickerIntent;
        }

        /**
         * Send the crop Intent from an Activity
         *
         * @param activity Activity to receive result
         */
        public void start(@NonNull Activity activity) {
            start(activity, REQUEST_CODE);
        }

        public PhotoPickerBuilder setPhotoCount(int photoCount) {
            mPickerOptionsBundle.putInt(EXTRA_MAX_COUNT, photoCount);
            return this;
        }

        public PhotoPickerBuilder setGridColumnCount(int columnCount) {
            mPickerOptionsBundle.putInt(EXTRA_GRID_COLUMN, columnCount);
            return this;
        }

        /**
         * 设置是否展示gif
         */
        public PhotoPickerBuilder setShowGif(boolean showGif) {
            mPickerOptionsBundle.putBoolean(EXTRA_SHOW_GIF, showGif);
            return this;
        }

        /**
         * 设置是否展示视频
         */
        public PhotoPickerBuilder setShowVideo(boolean showVideo) {
            mPickerOptionsBundle.putBoolean(EXTRA_SHOW_VIDEO, showVideo);
            return this;
        }

        /**
         * 设置展示云盘的文件夹名字
         */
        public PhotoPickerBuilder setShowUploadFolderName(String uploadFolderName) {
            mPickerOptionsBundle.putString(EXTRA_SHOW_UPLOAD_FOLDER_NAME, uploadFolderName);
            return this;
        }

        /**
         * 设置 选择的最大的文件大小 -- 参数为size
         *
         * 上传到云平台的，最大为15G
         * 发送的通知作业选择的图片，最大为10M
         *
         */
        public PhotoPickerBuilder setChoosedSingleFileMaxSize(long singleFileMaxSize) {
            mPickerOptionsBundle.putLong(EXTRA_CHOOSE_SINGLE_FILE_MAX_SIZE, singleFileMaxSize);
            return this;
        }

        /**
         * 设置是否在所有图片相册的第一项显示照相机
         */
        public PhotoPickerBuilder setShowCamera(boolean showCamera) {
            mPickerOptionsBundle.putBoolean(EXTRA_SHOW_CAMERA, showCamera);
            return this;
        }
        /**
         * 输入已选择的图片数量
         */
        public PhotoPickerBuilder setSelectedCount(int selectedCount) {
            mPickerOptionsBundle.putInt(EXTRA_ORIGINAL_PHOTOS_COUNT, selectedCount);
            return this;
        }

        public PhotoPickerBuilder setPreviewEnabled(boolean previewEnabled) {
            mPickerOptionsBundle.putBoolean(EXTRA_PREVIEW_ENABLED, previewEnabled);
            return this;
        }

        /**
         * 当选择图片的最大值为1张时设置该属性，用于控制此时是否可以点击底部“预览”查看此时选中的这一张图片
         * @param canDoPreview 需要能够“预览”时设置为true，不需要则无需设置该属性
         */
        public PhotoPickerBuilder setCanDoPreview(boolean canDoPreview) {
            mPickerOptionsBundle.putBoolean(EXTRA_CAN_PREVIEW, canDoPreview);
            return this;
        }

    }
}
