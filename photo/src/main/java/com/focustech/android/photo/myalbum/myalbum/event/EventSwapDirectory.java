package com.focustech.android.photo.myalbum.myalbum.event;

/**
 * <相册切换文件夹>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/19]
 * @see [相关类/方法]
 * @since [V1]
 */
public class EventSwapDirectory {
    int position = 0;

    public EventSwapDirectory(int position) {
        this.position = position;
    }

    public int position() {
        return position;
    }
}
