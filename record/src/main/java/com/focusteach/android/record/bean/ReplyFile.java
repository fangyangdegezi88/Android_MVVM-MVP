package com.focusteach.android.record.bean;

import java.io.Serializable;

/**
 * <作业回复文件Bean>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/27]
 * @see [相关类/方法]
 * @since [V1]
 */
public class ReplyFile implements Serializable{

    public static int TYPE_IMG   = 1;   //1: 图片
    public static int TYPE_VOICE = 2;   //2：音频

    private String filePath = "";
    private int fileType    = TYPE_IMG;
    private int second      = 0;

    private boolean hasUploaded = false; // 是否已上传，默认为未上传，仅在新生报名用到
    private String fileId = "";  // 文件id，仅在新生报名用到

    public long taskId() {
        return filePath != null ? filePath.hashCode() : 0;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public boolean isHasUploaded() {
        return hasUploaded;
    }

    public void setHasUploaded(boolean hasUploaded) {
        this.hasUploaded = hasUploaded;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReplyFile replyFile = (ReplyFile) o;

        if (fileType != replyFile.fileType) return false;
        if (second != replyFile.second) return false;
        return filePath != null ? filePath.equals(replyFile.filePath) : replyFile.filePath == null;

    }

    @Override
    public int hashCode() {
        int result = filePath != null ? filePath.hashCode() : 0;
        result = 31 * result + fileType;
        result = 31 * result + second;
        return result;
    }

    @Override
    public String toString() {
        return "ReplyFile{" +
                "filePath='" + filePath + '\'' +
                ", fileType=" + fileType +
                ", second=" + second +
                '}';
    }

    public ReplyFile() {
    }

    public ReplyFile(String filePath, int fileType, int second, boolean hasUploaded, String fileId) {
        this.filePath = filePath;
        this.fileType = fileType;
        this.second = second;
        this.hasUploaded = hasUploaded;
        this.fileId = fileId;
    }

}
