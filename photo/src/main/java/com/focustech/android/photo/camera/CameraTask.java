package com.focustech.android.photo.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.focustech.android.commonlibs.bridge.cache.localstorage.LocalFileStorageManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * <图片流task>
 *
 * @author yanguozhu
 * @version [版本号, 2016/7/12]
 * @see [相关类/方法]
 * @since [V1]
 */
public class CameraTask implements Runnable {
    private byte[] data;
    private CameraSurfaceView.CameraWriteCallback callback;

    public CameraTask(byte[] data, CameraSurfaceView.CameraWriteCallback callback) {
        this.data = data;
        this.callback = callback;
    }

    @Override
    public void run() {
        BufferedOutputStream bos = null;
        Bitmap bm = null;
        try {
            // 获得图片
            bm = BitmapFactory.decodeByteArray(data, 0, data.length);

            // 将图片旋转90度
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            matrix.postScale(0.5f,0.5f);
            //生成旋转之后的图片
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            //存储
            LocalFileStorageManager localFileStorageManager = LocalFileStorageManager.getInstance();
            String filePath = localFileStorageManager.getCacheImgFilePath() + System.currentTimeMillis() + ".jpg";//照片保存路径
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);//将图片压缩到流中
            if (null != callback)
                callback.writeSuccess(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            if (null != callback)
                callback.writeFail();
        } finally {
            try {
                bos.flush();//输出
                bos.close();//关闭
                bm.recycle();// 回收bitmap空间
            } catch (Exception e) {
                e.printStackTrace();
                callback.writeFail();
            }
        }
    }
}
