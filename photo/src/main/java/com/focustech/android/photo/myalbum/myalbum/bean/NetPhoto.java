package com.focustech.android.photo.myalbum.myalbum.bean;

import java.io.Serializable;

/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2016/12/9]
 * @see [相关类/方法]
 * @since [V1]
 *
 * 加载的网络图片，有默认图和原图的字段
 */
public class NetPhoto implements Serializable{
    /**
     * 默认图的id
     * */
    private String fileId;
    /**
     * 原图的id
     * */
    private String originalFileId;

    /**
     * 是否云端存在原图
     * */
    private boolean existServerOriFile = false;

    /**
     * 是否已加载原图
     * */
    private boolean hasLoadOriFile = false;

    /**
     * 因为现在的加载原图用按钮放在了适配器外部，没有直接方法弄
     *
     * 只能设置一个标记位，如果要加载原图，则设置这个标记为true, 然后刷新adapter，如果加载成功-标记位置为false
     *
     * */
    private boolean needLoadOriFile = false;

    public NetPhoto(){}


    public NetPhoto(String fileId, String originalFileId) {
        this.fileId = fileId;
        this.originalFileId = originalFileId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOriginalFileId() {
        return originalFileId;
    }

    public void setOriginalFileId(String originalFileId) {
        this.originalFileId = originalFileId;
    }

    public boolean isExistServerOriFile() {
        return existServerOriFile;
    }

    public void setExistServerOriFile(boolean existServerOriFile) {
        this.existServerOriFile = existServerOriFile;
    }

    public boolean isHasLoadOriFile() {
        return hasLoadOriFile;
    }

    public void setHasLoadOriFile(boolean hasLoadOriFile) {
        this.hasLoadOriFile = hasLoadOriFile;
    }

    public boolean isNeedLoadOriFile() {
        return needLoadOriFile;
    }

    public void setNeedLoadOriFile(boolean needLoadOriFile) {
        this.needLoadOriFile = needLoadOriFile;
    }
}
