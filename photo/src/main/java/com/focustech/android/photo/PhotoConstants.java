package com.focustech.android.photo;

/**
 * <基础常量>
 *
 * @author yanguozhu
 * @version [版本号, 2016/12/12]
 * @see [相关类/方法]
 * @since [V1]
 */

public class PhotoConstants {
    public static class BundleKey {

        /**
         * 调用相机action key
         */
        public final static String OPEN_CAMERA_ACTION = "open_camera_action_key";
        /**
         * 单张
         */
        public final static String LOCAL_PHOTO_CURRENT_FILE_PATH = "local_photo_current_file_path";
        /**
         * 相册名称
         */
        public final static String LOCAL_PHOTO_TITLE = "local_photo_title";
        /**
         * 集合
         */
        public final static String LOCAL_PHOTO_FILES_PATH = "local_photo_files_path";


        public static final String NET_PHOTO_SOURCE = "net_photo_source";

        public static final String DOWNLOAD_HOST_URL = "download_host_url";

        public static final String NET_PHOTO_FILES_KEY = "net_photo_files_key";

        public static final String NET_ORIGINAL_PHOTO_FILES_KEY = "net_original_photo_files_key";

        public static final String NET_PHOTO_CURRENT_FILE_KEY = "net_photo_current_file_key"; //当前要展示图片的key'

        /**
         * 视频路径 或者 网络地址
         */
        public static final String VIDEO_FILE_PATH_OR_URL = "video_path_or_url";
        /**
         * 视频路径 或者 网络地址
         */
        public static final String VIDEO_FILE_DISPLAY_NAME = "video_display_name";
        /**
         * 自定义拍照 图片地址
         */
        public static final String BROADCAST_CAMERA_PATH = "camera_path";

        /**
         * 是否检测最大15G的视频文件
         */
        public static final String CHECK_SINGLE_FILE_MAX_SIZE = "check_single_file_max_size";
        public static final java.lang.String TOKEN = "token";
    }


    public static class ActivityRequestCode {
        /**
         * 预览视频
         */
        public static final int REQUEST_PREVIEW_VIDEO = 101;

    }

}
