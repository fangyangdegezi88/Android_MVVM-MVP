package com.focustech.android.photo.myalbum;

import java.io.File;

/**
 * @author zhangzeyu
 * @version [版本号, 2016/8/26]
 * @see [相关类/方法]
 * @since [V1]
 */
public class CourseResUrlFormatter {

    /**
     * 课程资源图片的获取地址url串
     *
     * @param fileId
     * @return
     */
    public static String format(String fileId, String downloadUrl) {
        return downloadUrl + File.separator + fileId + "?type=1";
    }

    public static String formatRawImage(String rawFileId, String downloadUrl) {
        return downloadUrl + File.separator + rawFileId
                + "?type=1" + "&itemId=" + rawFileId + ".jpg";
    }

    public static String formatCourseExerciseUrl(String fileId, String downloadUrl) {
        return downloadUrl + File.separator + fileId
                + "?type=2"
                + "&itemId=" + fileId + ".jpg";
    }
}
