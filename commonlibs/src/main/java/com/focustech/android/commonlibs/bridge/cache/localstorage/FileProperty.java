package com.focustech.android.commonlibs.bridge.cache.localstorage;

import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.bridge.BridgeFactory;
import com.focustech.android.commonlibs.bridge.Bridges;

/**
 * <文件类型后缀及文件存储路径>
 *
 * @author yanguozhu
 * @version [版本号, 2016/6/22]
 * @see [相关类/方法]
 * @since [V1]
 */

public enum FileProperty {
    //安装文件
    APK(".apk") {
        @Override
        public String getPath() {
            LocalFileStorageManager fileStorageManager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE, BaseApplication.getContext());
            return fileStorageManager.getVersionUpdatePath();
        }
    },
    //图片
    JPG(".jpg") {
        @Override
        public String getPath() {
            LocalFileStorageManager fileStorageManager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE, BaseApplication.getContext());
            return fileStorageManager.getCacheImgFilePath();
        }
    },

    /**
     * 使用Glide 加载图片，无法通过某个文件id，定位是否存在原图
     * <p>
     * 通过每加载一个原图，生成一个标识文件
     */
    JPG_ORIGINAL_FILE_SIGN(".ori") {
        @Override
        public String getPath() {
            LocalFileStorageManager fileStorageManager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE, BaseApplication.getContext());
            return fileStorageManager.getCacheImgFileSignPath();
        }
    },

    //图片
    PNG(".png") {
        @Override
        public String getPath() {
            LocalFileStorageManager fileStorageManager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE, BaseApplication.getContext());
            return fileStorageManager.getCacheImgFilePath();
        }
    },
    //视频
    VEDIO(".mp4") {
        @Override
        public String getPath() {
            LocalFileStorageManager fileStorageManager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE, BaseApplication.getContext());
            return fileStorageManager.getCacheVideoFilePath();
        }
    },
    //语音
    VOICE(".amr") {
        @Override
        public String getPath() {
            LocalFileStorageManager fileStorageManager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE, BaseApplication.getContext());
            return fileStorageManager.getVoicePath();
        }
    };

    /**
     * 文件后缀名
     */
    String suffix;

    FileProperty(String suffix) {
        this.suffix = suffix;
    }

    /**
     * 获取文件前缀
     *
     * @return 文件前缀
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * 获取文件目录地址
     *
     * @return 目录地址
     */
    public String getPath() {
        return "";
    }
}
