package com.focustech.android.photo.myalbum.myalbum.event;

import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaEntity;

/**
 * <点击后确认是否可以checked的接口>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/18]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface OnItemCheckListener {

    /***
     *
     * @param position 所选图片的位置
     * @param path     所选的图片
     * @param isCheck   当前状态
     * @param selectedItemCount  已选数量
     * @return enable check
     */
    boolean OnItemCheck(int position, MediaEntity path, boolean isCheck, int selectedItemCount);
}
