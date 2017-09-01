package com.focustech.android.photo.myalbum.myalbum.bean.mediastore;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * <照片Entity> ---->  改造成一个多媒体类型的对象  可以包括图片，视频，音频等
 * 20170626  ycy修改
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/14]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MediaEntity implements Serializable,Comparable,Parcelable {
    /**
     * 无意义
     * */
    public static final int NULL = 0;
    /**
     * 照片
     * */
    public static final int TYPE_PHOTO = 1;
    /**
     * 视频
     * */
    public static final int TYPE_VIDEO = 2;

    private int id;
    /**
     * 路径
     * */
    private String path;
    /**
     * 当前媒体的类型--需要区分视频和图片
     * */
    private int type;

    /**
     * 文件大小
     * */
    private long size;

    /**
     * 文件播放长度-- 主要是给视频 和 语音用的
     * */
    private long duration;

    private long addTime;

    public MediaEntity() {
    }

    public MediaEntity(int id, String path, int type, long size, long duration, long addTime) {
        this.id = id;
        this.path = path;
        this.type = type;
        this.size = size;
        this.duration = duration;
        this.addTime = addTime;
    }

    protected MediaEntity(Parcel in) {
        id = in.readInt();
        path = in.readString();
        type = in.readInt();
        size = in.readLong();
        duration = in.readLong();
        addTime = in.readLong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaEntity)) return false;

        MediaEntity mediaEntity = (MediaEntity) o;

        return id == mediaEntity.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (int) (((MediaEntity)o).getAddTime() - addTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaEntity> CREATOR = new Creator<MediaEntity>() {
        @Override
        public MediaEntity createFromParcel(Parcel in) {
            return new MediaEntity(in);
        }

        @Override
        public MediaEntity[] newArray(int size) {
            return new MediaEntity[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(path);
        parcel.writeInt(type);
        parcel.writeLong(size);
        parcel.writeLong(duration);
        parcel.writeLong(addTime);
    }
}
