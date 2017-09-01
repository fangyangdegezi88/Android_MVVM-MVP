package com.focustech.android.photo.myalbum.myalbum.event;

import android.view.View;

/**
 * <点击图片时回调此接口>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/18]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface OnPhotoClickListener {

    void onClick(View v, int position, boolean showCamera);

    void onReachMaxSize(long singleMaxSize);
}
