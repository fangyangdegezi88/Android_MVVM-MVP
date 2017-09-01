package com.focustech.android.commonlibs.bridge.cache;

import android.util.Log;

import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.bridge.BridgeFactory;
import com.focustech.android.commonlibs.bridge.Bridges;
import com.focustech.android.commonlibs.bridge.cache.localstorage.LocalFileStorageManager;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供针对key-value的磁盘存储
 *
 * @author zhangxu
 */
public class KeyValueDiskCache {
    private static final String TAG = KeyValueDiskCache.class.getSimpleName();
    private static final long MAX_SIZE = 1024 * 1024 * 10; // 10MB
    private static final Map<CacheType, DiskLruCache> caches = new ConcurrentHashMap<>();

    /**
     * 文件管理类
     */
    private static LocalFileStorageManager fileStorageManager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE, BaseApplication.getContext());

    private KeyValueDiskCache() {
    }

    /**
     * 重置缓存
     */
    public synchronized static void clear() {
        for (CacheType type : CacheType.values()) {
            get(type, "0");
        }

        Set<CacheType> clearTypes = new HashSet<>();

        for (Map.Entry<CacheType, DiskLruCache> entry : caches.entrySet()) {
            if (entry.getKey().isClearable()) {
                try {
                    entry.getValue().delete();
                    clearTypes.add(entry.getKey());
                } catch (Throwable e) {
                }
            }
        }

        for (CacheType type : clearTypes) {
            caches.remove(type);
        }
    }

    /**
     * 清楚某个类型的缓存
     *
     * @param type
     */
    public synchronized static void clear(CacheType type) {
        for (CacheType type1 : CacheType.values()) {
            get(type1, "0");
        }

        for (Map.Entry<CacheType, DiskLruCache> entry : caches.entrySet()) {
            if (entry.getKey() == type) {
                try {
                    entry.getValue().delete();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        caches.remove(type);
    }

    public synchronized static void set(CacheType type, String key, String value) {
        DiskLruCache cache = null;
        DiskLruCache.Editor editor = null;

        try {
            if (!caches.containsKey(type)) {
                caches.put(type, DiskLruCache.open(getCacheDir(type), 1, 1, MAX_SIZE));
            }

            cache = caches.get(type);
            editor = cache.edit(key);

            if (null != editor) {
                cache.remove(key);
                editor.set(0, value);
            }
        } catch (Exception e) {
        } finally {
            if (null != editor) {
                try {
                    editor.commit();
                } catch (IOException e) {
                }
            }

            if (null != cache) {
                try {
                    cache.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized static String get(CacheType type, String key) {
        String value = null;

        DiskLruCache cache = null;

        try {
            if (!caches.containsKey(type)) {
                caches.put(type, DiskLruCache.open(getCacheDir(type), 1, 1, MAX_SIZE));
            }

            cache = caches.get(type);
            DiskLruCache.Snapshot snapshot = cache.get(key);

            if (null != snapshot) {
                value = snapshot.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cache) {
                try {
                    cache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    /**
     * 清除某条缓存
     * @param type
     * @param key
     */
    public synchronized static void remove(CacheType type, String key) {
        DiskLruCache cache = null;

        try {
            if (!caches.containsKey(type)) {
                caches.put(type, DiskLruCache.open(getCacheDir(type), 1, 1, MAX_SIZE));
            }

            cache = caches.get(type);
            cache.remove(key);

        } catch (Exception e) {
            // TODO 日志
            Log.d("KeyValueDiskCache","failed to delete file * key='" + key + "\'");
        } finally {
            if (null != cache) {
                try {
                    cache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public synchronized static File getCacheDir(CacheType type) {
        File base;
        if (type.isClearable())
            base = new File(fileStorageManager.getInterDiskCachePath() + type.name().toLowerCase() + File.separator);
        else
            base = new File(fileStorageManager.getExtraDiskCachePath() + type.name().toLowerCase() + File.separator);

        return base;
    }


    public enum CacheType {
        /**
         * 图片
         */
        IMAGES(true),
        /**
         * 作业详情
         */
        WORKINFO(true),
        /**
         * 通知
         */
        NOTICES(true),
        /**
         * 音频
         */
        AUDIO(true),
        /**
         * 拍照
         */
        CAMERA(true),
        /**
         * 登录信息和引导页
         */
        LOGIN_INFO(false),
        /**
         * crash上报信息
         */
        CRASH(false),
        LOGS(false),
        ZIPS(false),
        /**
         * 教师通知的草稿、教师调查问卷概览的草稿、教师调查问卷问题list的草稿
         */
        DRAFT(true),
        /**
         * h5页面草稿
         */
        HTML_DRAFT(true);


        private boolean value;

        CacheType(boolean value) {
            this.value = value;
        }

        public boolean isClearable() {
            return value;
        }
    }
}
