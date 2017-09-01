package com.focustech.android.components.mt.sdk.android.service.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.focustech.android.components.mt.sdk.FileType;

import java.io.Serializable;
import java.util.List;

/**
 * 消息元数据
 *
 * @author zhangxu
 */
public class MessageMeta implements Serializable {
    public static final int MULTI_MEDIA_TYPE_TEXT = 0; // 文本
    public static final int MULTI_MEDIA_TYPE_PIC = 1;  // 图片
    public static final int MULTI_MEDIA_TYPE_FILE = 2; // 文件
    public static final int MULTI_MEDIA_TYPE_AUDIO = 3; // 语音
    public static final int MULTI_MEDIA_TYPE_VEDIO = 4; // 视频

    @JSONField(name = "c")
    private CustomMeta customMeta;
    @JSONField(name = "s")
    private String serverMeta;

    @JSONField(serialize = false, deserialize = false)
    public boolean isText() {
        return customMeta == null || customMeta.getMultiMediaType() == MULTI_MEDIA_TYPE_TEXT;
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isPic() {
        return customMeta != null && customMeta.getMultiMediaType() == MULTI_MEDIA_TYPE_PIC;
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isFile() {
        return customMeta != null && customMeta.getMultiMediaType() == MULTI_MEDIA_TYPE_FILE;
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isAudio() {
        return customMeta != null && customMeta.getMultiMediaType() == MULTI_MEDIA_TYPE_AUDIO;
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isVedio() {
        return customMeta != null && customMeta.getMultiMediaType() == MULTI_MEDIA_TYPE_VEDIO;
    }

    public CustomMeta getCustomMeta() {
        return customMeta;
    }

    public void setCustomMeta(CustomMeta customMeta) {
        this.customMeta = customMeta;
    }

    public String getServerMeta() {
        return serverMeta;
    }

    public void setServerMeta(String serverMeta) {
        this.serverMeta = serverMeta;
    }

    public static class CustomMeta {
        @JSONField(name = "t")
        private Integer multiMediaType; // 多媒体类型
        @JSONField(name = "m")
        private List<MultiMediaDescriptor> multiMedias;

        public Integer getMultiMediaType() {
            return multiMediaType;
        }

        public void setMultiMediaType(Integer multiMediaType) {
            this.multiMediaType = multiMediaType;
        }

        public List<MultiMediaDescriptor> getMultiMedias() {
            return multiMedias;
        }

        public void setMultiMedias(List<MultiMediaDescriptor> multiMedias) {
            this.multiMedias = multiMedias;
        }
    }

    public static class MultiMediaDescriptor {
        @JSONField(name = "i")
        private String fileId;
        @JSONField(name = "s")
        private Integer size;
        @JSONField(name = "t")
        private Integer multiMediaType;
        @JSONField(name = "ts")
        private Integer second;
        @JSONField(name = "e")
        private Integer extend;
        @JSONField(name = "f")
        private String file;
        @JSONField(serialize = false, deserialize = false)
        private long uploadTaskId;

        public MultiMediaDescriptor() {
        }

        public MultiMediaDescriptor(Integer multiMediaType, String file) {
            this.file = file;
            this.multiMediaType = multiMediaType;
        }

        public MultiMediaDescriptor(Integer multiMediaType, String file, Integer second) {
            this.file = file;
            this.multiMediaType = multiMediaType;
            this.second = second;
        }

        public MultiMediaDescriptor(Integer multiMediaType, String file, Integer second, Integer extend) {
            this.file = file;
            this.multiMediaType = multiMediaType;
            this.second = second;
            this.extend = extend;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
            this.file = null;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Integer getMultiMediaType() {
            return multiMediaType;
        }

        public void setMultiMediaType(Integer multiMediaType) {
            this.multiMediaType = multiMediaType;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        @JSONField(serialize = false, deserialize = false)
        public String getUploadFileType() {
            String fileType = FileType.FILE_TYPE_ONLINE_FILE;

            switch (multiMediaType) {
                case MULTI_MEDIA_TYPE_PIC:
                    fileType = FileType.FILE_TYPE_PICTURE;
                    break;
                case MULTI_MEDIA_TYPE_FILE:
                    fileType = FileType.FILE_TYPE_ONLINE_FILE;
                    break;
                case MULTI_MEDIA_TYPE_AUDIO:
                    fileType = FileType.FILE_TYPE_VOICE;
                    break;
                case MULTI_MEDIA_TYPE_VEDIO:
                    fileType = FileType.FILE_TYPE_ONLINE_FILE;
                    break;
            }

            return fileType;
        }

        public long getUploadTaskId() {
            return uploadTaskId;
        }

        public void setUploadTaskId(long uploadTaskId) {
            this.uploadTaskId = uploadTaskId;
        }

        public Integer getSecond() {
            return second;
        }

        public void setSecond(Integer second) {
            this.second = second;
        }

        public Integer getExtend() {
            return extend;
        }

        public void setExtend(Integer extend) {
            this.extend = extend;
        }
    }
}
