package com.focustech.android.commonlibs.util.downloadManage;

/**
 * <文件下载状态>
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/7/27]
 * @see [相关类/方法]
 * @since [V1]
 */
public class FileState {

    public FileState(String fileName, FileDownloadState state, String savePath) {
        this.fileName = fileName;
        mState = state;
        this.savePath = savePath;
    }

    /**
     * 文件名
     */
    private String fileName;
    /**
     * 保存路径
     */
    private String savePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileDownloadState getState() {
        return mState;
    }

    public void setState(FileDownloadState state) {
        mState = state;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    /**
     * 文件下载状态
     */
    private FileDownloadState mState;
}
