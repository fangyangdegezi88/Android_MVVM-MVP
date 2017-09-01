package com.focustech.android.photo.myalbum.myalbum;

/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/2/23]
 * @see [相关类/方法]
 * @since [V1]
 *
 * PhotoPickActivity --> PhotoPagerFragment  中图片点击后需要控制导航和底部工具栏的隐藏和显示所以用回调
 */
public interface ImagePagerCallBack {
    /**
     * 点击了图片
     * */
    void imageClick();
    /**
     * 预览结束
     * */
    void imagePreViewFinish();
}
