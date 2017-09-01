package com.focustech.android.photo.myalbum.myalbum;

import android.os.Bundle;

import com.focustech.android.commonlibs.bridge.cache.localstorage.FileProperty;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonlibs.util.downloadManage.DownloadListener;
import com.focustech.android.commonlibs.util.downloadManage.DownloadManage;
import com.focustech.android.commonuis.biz.BaseCommonPresenter;
import com.focustech.android.photo.PhotoConstants;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.CourseResUrlFormatter;
import com.focustech.android.photo.myalbum.FILE_SOURCE;
import com.focustech.android.photo.myalbum.myalbum.bean.NetPhoto;
import com.focustech.android.photo.myalbum.util.NetPhotoManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuzaibing on 2016/10/20.
 */
public class NetPhotoBrowserPresenter extends BaseCommonPresenter<INetPhotoBrowserView> implements DownloadListener {

    /**
     * 所有图片ID，默认展示的
     */
    private List<String> mFiles = new ArrayList<>();

    /**
     * 原图（大图）图片ID
     */
    private List<String> mOriginalFiles = new ArrayList<>();


    /**
     * 所有展示的图片资源
     */
    private List<NetPhoto> mPhotos = new ArrayList<>();

    /**
     * 当前选择的页面
     */
    private int mCurrentIndex = -1;

    /**
     * 文件来源
     */
    private int mSource = FILE_SOURCE.TYPE_IMAGE;

    private String downloadUrl;
    private String token;

    public NetPhotoBrowserPresenter(boolean enable) {
        super(enable);
        DownloadManage.getInstance().addListener(this);
    }

    @Override
    public void detachView() {
        DownloadManage.getInstance().removeListener(this);
        super.detachView();
    }

    public void initData(Bundle bundle) {
        if (bundle == null)
            return;
        try {
            downloadUrl = bundle.getString(PhotoConstants.BundleKey.DOWNLOAD_HOST_URL);
            token = bundle.getString(PhotoConstants.BundleKey.TOKEN);
            mFiles = bundle.getStringArrayList(PhotoConstants.BundleKey.NET_PHOTO_FILES_KEY);
            if (GeneralUtils.isNullOrZeroSize(mFiles)) {
                if (mvpView != null) mvpView.openNetPicFail(1);
                return;
            }

            mOriginalFiles = bundle.getStringArrayList(PhotoConstants.BundleKey.NET_ORIGINAL_PHOTO_FILES_KEY);

            for (int i = 0; i < mFiles.size(); i++) {
                NetPhoto netPhoto = new NetPhoto();
                netPhoto.setFileId(mFiles.get(i));
                if (mOriginalFiles != null && !mOriginalFiles.isEmpty()) {
                    String oriFileId = mOriginalFiles.get(i);
                    netPhoto.setOriginalFileId(oriFileId);
                    netPhoto.setExistServerOriFile(true);
                    netPhoto.setHasLoadOriFile(NetPhotoManager.isFileExistById(oriFileId));
                }

                mPhotos.add(netPhoto);
            }

            String mCurrentFileId = bundle.getString(PhotoConstants.BundleKey.NET_PHOTO_CURRENT_FILE_KEY);
            if (GeneralUtils.isNullOrEmpty(mCurrentFileId)) {
                mCurrentIndex = 0;
            } else {
                mCurrentIndex = getIndex(mCurrentFileId);
            }
            mSource = bundle.getInt(PhotoConstants.BundleKey.NET_PHOTO_SOURCE, FILE_SOURCE.TYPE_IMAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (mvpView != null) mvpView.showNetPic(mPhotos, mCurrentIndex, mSource, downloadUrl);
    }

    /**
     * @param mCurrentFileId
     * @return
     */
    private int getIndex(String mCurrentFileId) {
        for (int i = 0; i < mFiles.size(); i++) {
            if (mCurrentFileId.equals(mFiles.get(i))) {
                return i;
            }
        }

        return -1;
    }

    public int getFileSize() {
        return mFiles.size();
    }

    /**
     * 根据索引下载图片
     *
     * @param index
     */
    public void downloadImgByIndex(int index) {
        if (index < 0 || index > mPhotos.size() - 1) {
            return;
        }
        if (mvpView == null) {
            return;
        }

        final NetPhoto netPhoto = mPhotos.get(index);
        final String fileId = netPhoto.getFileId();
        new Thread() {
            @Override
            public void run() {
                if (mSource == FILE_SOURCE.TYPE_FILE) {
                    if (netPhoto.isHasLoadOriFile()) {
                        String imgUrl = CourseResUrlFormatter.formatRawImage(netPhoto.getOriginalFileId(), downloadUrl);
                        DownloadManage.getInstance().download(imgUrl, FileProperty.JPG, netPhoto.getOriginalFileId());
                    } else {
                        String imgUrl = CourseResUrlFormatter.format(fileId, downloadUrl);
                        DownloadManage.getInstance().download(imgUrl, FileProperty.JPG, fileId);
                    }
                } else {
                    String imgUrl = downloadUrl + "/" + fileId;
                    DownloadManage.getInstance().downloadFileWithoutType(imgUrl, fileId, token);
                }
            }
        }.start();
    }

    /**
     * 页面切换后  ui展示逻辑
     *
     * @param index
     */
    public void showImgPageIndex(int index) {
        if (index < 0 || index > mPhotos.size() - 1) {
            return;
        }

        if (mvpView == null) {
            return;
        }
        //显示索引
        if (mPhotos.size() > 1) {
            mvpView.showIndex(index, mPhotos.size());
        }

        //显示原图
        NetPhoto photo = mPhotos.get(index);
        boolean serverHasOriFile = photo.isExistServerOriFile();
        boolean hasLoadOriFile = photo.isHasLoadOriFile();
        if (mSource == FILE_SOURCE.TYPE_FILE) {
            if (!serverHasOriFile) {
                mvpView.showRawImg(false);
            } else if (hasLoadOriFile) {
                mvpView.showRawImg(false);
            } else {
                mvpView.showRawImg(true);
            }
        } else {
            mvpView.showRawImg(false);
        }

        //显示下载按钮
    }

    @Override
    public void downloadSuccess(String url) {
        if (mvpView != null) {
            mvpView.downloadSuccess(R.string.save_photo);
        }
    }

    @Override
    public void downloadFail(String url) {
        if (mvpView != null) {
            mvpView.downloadFail(R.string.save_photo_fail);
        }
    }

    @Override
    public void process(int process) {

    }
}
