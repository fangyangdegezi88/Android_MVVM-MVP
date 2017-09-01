package com.focustech.android.photo.myalbum.util.mediastore;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.MediaPicker;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaDirectory;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaEntity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;

/**
 * <加载相册数据>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/14]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MediaStoreHelper {
    public final static int INDEX_ALL_PHOTOS = 0;


    public static void getPhotoDirs(Activity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getLoaderManager()
                .restartLoader(20170224, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private WeakReference<Context> context;
        private PhotosResultCallback resultCallback;
        private MediaOptions options;

        public PhotoDirLoaderCallbacks(Context context, PhotosResultCallback resultCallback) {
            this.context = new WeakReference<>(context);
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            options = MediaOptions.getOptions(args);
            String[] likePath = options.getIncludeDirPath();
            return new PhotoDirectoryLoader(context.get(), args.getBoolean(MediaPicker.EXTRA_SHOW_GIF, false), likePath);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
            Log.d("Photo", "====" + Thread.currentThread().getName() + " onLoadFinished");
            if (data == null) return;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        processData(data);
                    } catch (Exception e) {
                        Log.d("Photo", "process photo error.");
                    }
                }
            }, "[ProcessPhotoDataThread]").start();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

        public void processData(Cursor data) {
            List<MediaDirectory> directories = new ArrayList<>();
            MediaDirectory mediaDirectoryAll = new MediaDirectory();
            mediaDirectoryAll.setName(options.dirAllName());
            mediaDirectoryAll.setId(MediaOptions.KEY_DIRALL);
            /*
             * 文件夹Id和在directories中的位置，仅仅用于循环中
             */
            Map<String, Integer> dir_id_index_map = new HashMap<>();
            for (; data.moveToNext(); ) {

                if (data.isClosed()) {
                    Log.d("Photo", "===Attempted to access a cursor after it has been closed.");
                    return;
                }

                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
                long duration = 0;//图片默认为0
                long addtime = data.getLong(data.getColumnIndexOrThrow(DATE_ADDED));

                /*
                  include表示是否包含该照片
                 */
                if (!dir_id_index_map.containsKey(bucketId)) {
                    MediaDirectory mediaDirectory = new MediaDirectory();
                    mediaDirectory.setId(bucketId);
                    mediaDirectory.setName(name);
                    mediaDirectory.setCoverPath(path);
                    mediaDirectory.addPhotoMediaEntity(imageId, path, size, duration, addtime);
                    mediaDirectory.setDateAdded(addtime);
                    /*
                     标记将要添加的相册路径在列表中的位置
                     */
                    dir_id_index_map.put(bucketId, directories.size());
                    directories.add(mediaDirectory);
                } else {
                    int index = dir_id_index_map.get(bucketId);
                    directories.get(index).addPhotoMediaEntity(imageId, path, size, duration, addtime);
                }

                if (options.includeDirAll()) {
                    mediaDirectoryAll.addPhotoMediaEntity(imageId, path, size, duration, addtime);
                }

            }
            if (mediaDirectoryAll.getMediaEntityPaths().size() > 0) {
                mediaDirectoryAll.setCoverPath(mediaDirectoryAll.getMediaEntityPaths().get(0));
            }
            if (options.includeDirAll()) {
                directories.add(INDEX_ALL_PHOTOS, mediaDirectoryAll);
            }
            Log.d("Photo", "====" + Thread.currentThread().getName() + " onLoadFinished before getNewName.");
            /*
             重命名是跳过第一个
             */
            for (int i = 1; i < directories.size(); i++) {
                String oldName = directories.get(i).getName();
                String newName = options.getNewName(oldName, directories.get(i).getCoverPath());
                directories.get(i).setName(newName);
                Log.d("Photo", "===== oldName=" + oldName + ", newName=" + newName);
            }

            Log.d("Photo", "====" + Thread.currentThread().getName() + " onLoadFinished before sort.");
//            options.sort(directories);

            Log.d("Photo", "====" + Thread.currentThread().getName() + " onLoadFinished process end.");
            if (resultCallback != null) {
                resultCallback.onResultCallback(directories, options);
            }
        }
    }

    public interface PhotosResultCallback {
        void onResultCallback(List<MediaDirectory> directories, MediaOptions options);
    }

    /**
     * 获取视频逻辑，不能使用loader 因为一个activity只有一个loader
     **/
    public static void getLocalVideoDirs(final Activity activity, final MediaOptions options, final List<MediaDirectory> directories, final VideoResultCallback resultCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DATE_ADDED + " DESC");
                if (cursor != null) {
                    MediaDirectory videoDirectory = new MediaDirectory();
                    videoDirectory.setName(activity.getString(R.string.video_folder));
                    videoDirectory.setId(MediaOptions.KEY_DIR_VIDEOS);
                    while (cursor.moveToNext()) {
                        boolean initAddTime = false;
                        int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns._ID));
                        String bucketId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.BUCKET_ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME));
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA));
                        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.SIZE));
                        long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION));
                        long addtime = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_ADDED));

                        MediaEntity entity = new MediaEntity(imageId, path, MediaEntity.TYPE_VIDEO, size, duration, addtime);
                        videoDirectory.addMediaEntity(entity);

                        if (options.includeDirAll()) {
                            directories.get(INDEX_ALL_PHOTOS).addMediaEntity(entity);
                        }

                        if (!initAddTime) {
                            videoDirectory.setDateAdded(addtime);
                            initAddTime = true;
                        }
                    }

                    if(videoDirectory.getMediaBeen() != null && !videoDirectory.getMediaBeen().isEmpty()){
                        if (options.includeDirAll()) {
                            directories.add(videoDirectory);
                        }
//                      options.sort(directories);
                        Collections.sort(directories.get(INDEX_ALL_PHOTOS).getMediaBeen());

                        videoDirectory.setCoverPath(videoDirectory.getMediaBeen().get(0).getPath());
                        directories.get(INDEX_ALL_PHOTOS).setCoverPath(videoDirectory.getMediaBeen().get(0).getPath());
                    }
                    if (resultCallback != null) {
                        resultCallback.onResultCallback(directories, options);
                    }
                } else {
                    if (resultCallback != null) {
                        resultCallback.onResultCallback(directories, options);
                    }
                }
            }
        }).start();
    }

    public interface VideoResultCallback {
        void onResultCallback(List<MediaDirectory> directories, MediaOptions options);
    }
}
