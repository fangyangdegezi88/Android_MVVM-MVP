package com.focustech.android.commonlibs.util.picture;

import java.io.Serializable;

public class PicturePropertiesBean implements Serializable {
    private String srcPath;

    private String destPath;

    /**
     * 最大允许宽度
     */
    private float width;

    /**
     * 最大允许高度
     */
    private float height;

    /**
     * 图片压缩后的上限
     */
    private int maxSize = 200 * 1024;

    /**
     * 图片压缩后的下限
     */
    private int minSize = 50 * 1024;

    /**
     * 图片压缩增加/减少率,默认10%.
     */
    private int options = 10;

    /**
     * 默认压缩率
     */
    private int defaultOption = 70;

    public PicturePropertiesBean(String srcPath, String destPath, float width, float height) {
        super();
        this.srcPath = srcPath;
        this.destPath = destPath;
        this.width = width;
        this.height = height;
    }

    public PicturePropertiesBean(String srcPath, String destPath, float width, float height, int maxSize, int minSize,
                                 int options, int defaultOption) {
        super();
        this.srcPath = srcPath;
        this.destPath = destPath;
        this.width = width;
        this.height = height;
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.options = options;
        this.defaultOption = defaultOption;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getOptions() {
        return options;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    public int getDefaultOption() {
        return defaultOption;
    }

    public void setDefaultOption(int defaultOption) {
        this.defaultOption = defaultOption;
    }

}
