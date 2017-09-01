package com.focustech.android.photo.myalbum.myalbum.bean.mediastore;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 媒体文件夹 包装体
 * 20170626  ycy修改
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/14]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MediaDirectory implements Serializable {

    private String id;
    private String coverPath;
    private String name;
    private long dateAdded;
    private List<MediaEntity> mediaBeen = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaDirectory)) return false;

        MediaDirectory directory = (MediaDirectory) o;

        boolean hasId = !TextUtils.isEmpty(id);
        boolean otherHasId = !TextUtils.isEmpty(directory.id);

        if (hasId && otherHasId) {
            if (!TextUtils.equals(id, directory.id)) {
                return false;
            }

            return TextUtils.equals(name, directory.name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (TextUtils.isEmpty(id)) {
            if (TextUtils.isEmpty(name)) {
                return 0;
            }

            return name.hashCode();
        }

        int result = id.hashCode();

        if (TextUtils.isEmpty(name)) {
            return result;
        }

        result = 31 * result + name.hashCode();
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<MediaEntity> getMediaBeen() {
        return mediaBeen;
    }

    public void setMediaBeen(List<MediaEntity> mediaBeen) {
        this.mediaBeen = mediaBeen;
    }

    public List<String> getMediaEntityPaths() {
        List<String> paths = new ArrayList<>(mediaBeen.size());
        for (MediaEntity mediaEntity : mediaBeen) {
            paths.add(mediaEntity.getPath());
        }
        return paths;
    }

    public void addPhotoMediaEntity(int id, String path, long size,long duration,long addtime) {
        mediaBeen.add(new MediaEntity(id, path,MediaEntity.TYPE_PHOTO,size,duration,addtime));
    }

    public void addVideoMediaEntity(int id, String path, long size,long duration,long addtime) {
        mediaBeen.add(new MediaEntity(id, path,MediaEntity.TYPE_VIDEO,size,duration,addtime));
    }

    public void addMediaEntity(int id, String path, int type, long size,long duration,long addtime) {
        mediaBeen.add(new MediaEntity(id, path,type,size,duration,addtime));
    }

    public void addMediaEntity(MediaEntity mediaEntity) {
        mediaBeen.add(mediaEntity);
    }

    //用序列化的特点来进行深拷贝。无论有多少个对象引用都可以复制
    public Object deepCopy() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        ObjectInputStream ois = new ObjectInputStream(bis);

        return ois.readObject();
    }

    public MediaDirectory() {
    }
}