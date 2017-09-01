package com.focustech.android.commonlibs.util.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <图片操作基础类>
 * <p>
 * 提供一些基础的操作方法
 * 供组合类使用
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/22]
 * @see BitmapUtil
 * @see [相关类/方法]
 * @since [V1]
 */
public class BaseBitmapUtil {
    /**
     * <图片按比例大小压缩方法（根据路径获取图片并压缩）>
     *
     * @return 压缩后图片路径
     */
    static Bitmap radioCompress(PicturePropertiesBean propertiesBean) {
        Bitmap bitmap = null;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(propertiesBean.getSrcPath(), newOpts);
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        //        float minHeight = 800f;//设置为主流手机分辨率800*480

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (height > width && width > propertiesBean.getWidth()) {
            // 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / propertiesBean.getWidth());
        } else if (width > height && height > propertiesBean.getHeight()) {
            // 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / propertiesBean.getHeight());
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(propertiesBean.getSrcPath(), newOpts);
    }

    /**
     * <纠正图片角度>
     *
     * @param image
     * @param bean
     * @return
     */
    static Bitmap correctPictureAngle(Bitmap image, PicturePropertiesBean bean) {
        int degree = getExifOrientation(bean.getSrcPath());
        if (degree > 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degree);
            Bitmap rotateBitmap =
                    Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
            if (rotateBitmap != null) {
                image.recycle();
                image = rotateBitmap;
            }
        }
        return image;
    }

    /**
     * <质量压缩方法>
     *
     * @return 压缩后图片路径
     */
    static byte[] compressImage(Bitmap image, boolean INPUT_RECYCLED, PicturePropertiesBean propertiesBean) {
        if (image != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //图片大于最大值,则压缩,否则不做任何操作
            if (baos.toByteArray().length > propertiesBean.getMaxSize()) {
                baos.reset();
                // 质量压缩方法，首先压缩options的压缩率
                image.compress(Bitmap.CompressFormat.JPEG, propertiesBean.getDefaultOption(), baos);
                // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
                while (baos.toByteArray().length > propertiesBean.getMaxSize()) {
                    baos.reset();// 重置baos即清空baos
                    // 这里压缩defaultOptions%，把压缩后的数据存放到baos中
                    int lastOption = propertiesBean.getDefaultOption() - propertiesBean.getOptions();
                    if (lastOption < 10) {
                        propertiesBean.setDefaultOption(10);// 每次都减少option
                        image.compress(Bitmap.CompressFormat.JPEG, propertiesBean.getDefaultOption(), baos);
                        byte[] bytes = baos.toByteArray();
                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (INPUT_RECYCLED) {
                            image.recycle();
                        }
                        return bytes;
                    } else {
                        propertiesBean.setDefaultOption(lastOption);// 每次都减少option
                        image.compress(Bitmap.CompressFormat.JPEG, propertiesBean.getDefaultOption(), baos);
                    }
                }
                while (baos.toByteArray().length < propertiesBean.getMinSize()) {
                    baos.reset();// 重置baos即清空baos
                    // 这里压缩options%，把压缩后的数据存放到baos中
                    propertiesBean.setDefaultOption(propertiesBean.getDefaultOption() + propertiesBean.getOptions());// 每次都增加option
                    image.compress(Bitmap.CompressFormat.JPEG, propertiesBean.getDefaultOption(), baos);
                }
            }
            byte[] bytes = baos.toByteArray();
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (INPUT_RECYCLED) {
                image.recycle();
            }
            return bytes;
        }

        return null;
    }

    static String restore(byte[] bytes, PicturePropertiesBean propertiesBean) {
        File file = new File(propertiesBean.getDestPath());
        FileOutputStream stream = null;
        String result = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(bytes);
            result = file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // flush*
        if (stream != null) {
            try {
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // close*
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * <得到 图片旋转 的角度>
     * <功能详细描述>
     *
     * @param filePath
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static int getExifOrientation(String filePath) {
        int degree = 0;
        try {
            ExifInterface exif = new ExifInterface(filePath);
            int result = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (result) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;

                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
