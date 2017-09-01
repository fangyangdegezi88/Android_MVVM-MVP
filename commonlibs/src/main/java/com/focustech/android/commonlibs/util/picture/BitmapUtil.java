package com.focustech.android.commonlibs.util.picture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.focustech.android.commonlibs.ActivityManager;
import com.focustech.android.commonlibs.util.DensityUtil;

/**
 * <图片公共类>
 * <功能详细描述>
 *
 * @author cyf
 * @version [版本号, 2013-9-17]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BitmapUtil {
    /**
     * <打开指定路径的文件，等比例压缩并调整角度>
     *
     * 超级耗时，不要在主线程调用
     * @param bean
     * @return
     */
    public static Bitmap openScaleImage(PicturePropertiesBean bean) {
        Bitmap bitmap = BaseBitmapUtil.radioCompress(bean);
        bitmap = BaseBitmapUtil.correctPictureAngle(bitmap, bean);
        return bitmap;
    }

    /**
     * <图片质量压缩，并转成比特流>
     *
     * @param bitmap
     * @param bean
     * @return
     */
    public static byte[] compress(Bitmap bitmap, PicturePropertiesBean bean) {
        return BaseBitmapUtil.compressImage(bitmap, false, bean);
    }

    /**
     * 亚索图片
     *
     * @param bitmap
     * @param bean
     * @return
     */
    public static Bitmap compressToBitmap(Bitmap bitmap, PicturePropertiesBean bean) {
        byte[] bitmapBytes = BaseBitmapUtil.compressImage(bitmap, false, bean);
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
        if (bitmap != null) {
            bitmap.recycle();
        }
        return bitmap1;
    }

    /**
     * 计算缩放图片，处理长图
     *
     * @param bitmap
     * @return
     */
    public static Bitmap compressScaleBitmap(Bitmap bitmap, Context context) {
        if (bitmap == null) {
            return null;
        }
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= 0 || height <= 0) {
            return bitmap;
        }

        if (width >= height) {
            return bitmap;
        } else {//高>宽
            int screenHeight;
            if (context instanceof Activity) {
                screenHeight = DensityUtil.getXScreenpx((Activity) context);
            } else {
                screenHeight = DensityUtil.getXScreenpx(ActivityManager.getInstance().getLastActivity());
            }

            if (height <= screenHeight) {
                return bitmap;
            } else {
                float scaleRate = ((float) screenHeight) / height;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleRate, scaleRate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                return bitmap;
            }

        }
    }


}