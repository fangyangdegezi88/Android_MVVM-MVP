package com.focustech.android.photo.myalbum.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.myalbum.ILocalPhotoBrowserView;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * @author zhangzeyu
 * @version [版本号, 2016/11/22]
 * @see [相关类/方法]
 * @since [V1]
 */

public class LocalPhotoAdapter extends PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {

    private Context mContext;

    private List<String> mFiles;


    private RequestManager mGlide;

    private ILocalPhotoBrowserView mvpView;

    public LocalPhotoAdapter(Context mContext, List<String> mFiles, RequestManager mGlide, ILocalPhotoBrowserView mvpView) {
        this.mContext = mContext;
        this.mFiles = mFiles;
        this.mGlide = mGlide;
        this.mvpView = mvpView;
    }

    @Override
    public int getCount() {
        return mFiles.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View photoItem = LayoutInflater.from(mContext).inflate(R.layout.item_local_photo, container, false);
        PhotoView netPhotoPv = (PhotoView) photoItem.findViewById(R.id.local_photo_pv);
        netPhotoPv.setOnPhotoTapListener(this);
        initData(netPhotoPv, position);
        container.addView(photoItem);
        return photoItem;
    }

    private void initData(PhotoView netPhotoPv, int position) {
        String path = mFiles.get(position);

        final Uri uri;
        if (path.startsWith("http")) {
            uri = Uri.parse(path);
        } else {
            uri = Uri.fromFile(new File(path));
        }
        mGlide.load(uri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
//                .placeholder(R.drawable.common_pic_loading_big)
//                .error(R.drawable.common_pic_loading_failure_big)
//                .thumbnail(0.1f)
//                .dontAnimate()
//                .dontTransform()
                .into(netPhotoPv);

    }

    /**
     *
     * 关闭页面
     *
     * @param view
     * @param x
     * @param y
     */
    @Override
    public void onPhotoTap(View view, float x, float y) {
        mvpView.close();
    }
}
