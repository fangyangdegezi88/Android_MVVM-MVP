package com.focustech.android.photo.myalbum.myalbum;

import com.focustech.android.commonuis.biz.IMvpView;
import com.focustech.android.photo.myalbum.myalbum.bean.NetPhoto;

import java.util.List;

/**
 * Created by liuzaibing on 2016/10/20.
 */
public interface INetPhotoBrowserView extends IMvpView {
    /**
     * 打开网络图片浏览器失败
     *
     * @param i 失败类型
     */
    void openNetPicFail(int i);

    /**
     * 显示网络图片
     *  @param files        所有网络图片ID
     * @param currentIndex 当前需要展示的索引
     * @param source       文件来源
     * @param downloadUrl
     */
    void showNetPic(List<NetPhoto> files, int currentIndex, int source, String downloadUrl);

    /**
     * 关闭当前图片浏览器
     */
    void close();

    /**
     * 显示索引
     * */
    void showIndex(int index,int SumCount);

    /**
     * 是否显示原图按钮
     * */
    void showRawImg(boolean visible);

    /**
     * 下载成功
     * */
    void downloadSuccess(int resId);

    /**
     * 下载失败
     * */
    void downloadFail(int resId);

}
