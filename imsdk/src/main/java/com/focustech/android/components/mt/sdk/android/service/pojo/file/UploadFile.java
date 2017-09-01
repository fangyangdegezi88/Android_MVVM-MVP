package com.focustech.android.components.mt.sdk.android.service.pojo.file;

/**
 * 上传文件
 *
 * @author zhangxu
 */
public class UploadFile {
    private long taskId;
    private String fileName;
    private String type;

    public UploadFile(long taskId, String fileName, String type) {
        this.taskId = taskId;
        this.fileName = fileName;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
