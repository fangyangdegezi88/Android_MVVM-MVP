package com.focustech.android.photo.myalbum.util;


import com.focustech.android.commonlibs.bridge.cache.localstorage.FileProperty;

import java.io.File;
import java.io.IOException;

/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2016/12/9]
 * @see [相关类/方法]
 * @since [V1]
 *
 * NetPhoto 管理类，
 *
 * 因为Glide 无法从缓存中更具url来找到
 */
public class NetPhotoManager {

    /**
     * 判断某个文件id的标识是否存在
     * @param fileId
     * @return
     */
    public static boolean isFileExistById(String fileId){
        File file = new File(FileProperty.JPG_ORIGINAL_FILE_SIGN.getPath() + fileId);
        if(file.exists()){
            return true;
        }
        return false;
    }

    /**
     * 生成存在某个文件id的标识
     * @param fileId
     * @return
     */
    public static boolean setFileExistById(String fileId){
        try {
            new File(FileProperty.JPG_ORIGINAL_FILE_SIGN.getPath() + fileId).createNewFile();
            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
