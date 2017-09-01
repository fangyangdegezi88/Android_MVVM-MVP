package com.focustech.android.photo.myalbum.myalbum.event;

import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaEntity;

/**
 * <点击图片checked状态发生变化后的回调接口>
 *     回调此接口时数据源和UI均已发生变化
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/20]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface OnItemCheckChangedListener {
    /**
     *
     * @param position 所选图片的位置
     * @param path     所选的图片
     * @param isCheck  当前状态
     * @param oldCheck 先前状态
     * @return
     */
    void OnItemCheckChanged(int position, MediaEntity path, boolean isCheck, boolean oldCheck);
}
