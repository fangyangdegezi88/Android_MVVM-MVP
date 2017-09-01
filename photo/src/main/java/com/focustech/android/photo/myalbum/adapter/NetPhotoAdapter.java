package com.focustech.android.photo.myalbum.adapter;/**
 * Created by liuzaibing on 2016/10/21.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.util.DensityUtil;
import com.focustech.android.commonlibs.util.picture.BitmapUtil;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.CourseResUrlFormatter;
import com.focustech.android.photo.myalbum.FILE_SOURCE;
import com.focustech.android.photo.myalbum.util.NetPhotoManager;
import com.focustech.android.photo.myalbum.myalbum.INetPhotoBrowserView;
import com.focustech.android.photo.myalbum.myalbum.bean.NetPhoto;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * <网络图片浏览器适配器>
 *
 * @author liuzaibing
 * @version [版本号, 2016/10/21]
 * @see [相关类/方法]
 * @since [V1]
 */
public class NetPhotoAdapter extends PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {

    private L l = new L(NetPhotoAdapter.class.getSimpleName());

    private Context mContext;
    private List<NetPhoto> mPhotos;
    private int mSource = FILE_SOURCE.TYPE_IMAGE;
    private INetPhotoBrowserView mINetPhotoBrowserView;

    private float mScreenPro;   //屏幕高宽比例

    private String downloadUrl;

    public NetPhotoAdapter(Context context, List<NetPhoto> mPhotos, INetPhotoBrowserView iNetPhotoBrowserView, int source, String downloadUrl) {
        mContext = context;
        this.mPhotos = mPhotos;
        mINetPhotoBrowserView = iNetPhotoBrowserView;
        mSource = source;
        this.downloadUrl = downloadUrl;
        getScreenAtr();
    }

    private void getScreenAtr() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        mScreenPro = point.y * 1.0f / point.x;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    /**
     * 通知加载某个index的原图
     *
     * @param index
     */
    public void showRawImgByIndex(int index) {
        NetPhoto photo = mPhotos.get(index);
        photo.setNeedLoadOriFile(true);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View photoItem = LayoutInflater.from(mContext).inflate(R.layout.item_net_photo, container, false);
        PhotoView netPhotoPv = (PhotoView) photoItem.findViewById(R.id.net_photo_pv);
        ProgressBar progressBar = (ProgressBar) photoItem.findViewById(R.id.progressbar);
        LinearLayout mLlimageLoadFail = (LinearLayout) photoItem.findViewById(R.id.ll_img_load_fail);
        netPhotoPv.setOnPhotoTapListener(this);
        initData(netPhotoPv, progressBar, mLlimageLoadFail, position);
        container.addView(photoItem);
        return photoItem;
    }

    private void initData(final PhotoView netPhotoPv, final ProgressBar progressBar, final LinearLayout mLlimageLoadFail, final int position) {
        String fileId = "";
        String imgUrl = "";

        final NetPhoto photo = mPhotos.get(position);

        boolean serverHasOriFile = photo.isExistServerOriFile();
        boolean hasLoadOriFile = photo.isHasLoadOriFile();

        final boolean needLoadRawFile = photo.isNeedLoadOriFile();

        if (mSource == FILE_SOURCE.TYPE_FILE) {
            if (!serverHasOriFile) {
                fileId = photo.getFileId();
                imgUrl = CourseResUrlFormatter.format(fileId,downloadUrl);
            } else if (hasLoadOriFile) {
                imgUrl = CourseResUrlFormatter.formatRawImage(photo.getOriginalFileId(),downloadUrl);
            } else if (needLoadRawFile) {//有原图，且未加载原图,判断是否需要加载原图
                imgUrl = CourseResUrlFormatter.formatRawImage(photo.getOriginalFileId(),downloadUrl);
            } else {
                imgUrl = CourseResUrlFormatter.format(photo.getFileId(),downloadUrl);
            }
        } else {
            imgUrl = downloadUrl + "/" + photo.getFileId();
        }

        final String url = imgUrl;

        SimpleTarget simpleTarget = new SimpleTarget<Object>() {
            @Override
            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                if (resource instanceof GlideBitmapDrawable) {
                    GlideBitmapDrawable glideresource = (GlideBitmapDrawable) resource;
                    setPhotoViewLayout(glideresource, netPhotoPv, progressBar);
                } else if (resource instanceof GlideDrawable) {
                    Glide.with(mContext).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(netPhotoPv);
                }

                loadSuccess(progressBar, mLlimageLoadFail, photo);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                loadFail(progressBar, mLlimageLoadFail, photo);
            }

            @Override
            public void onStart() {
            }
        };

        Glide.with(mContext).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(simpleTarget);

    }

    /**
     * 设置图片加载控件的布局
     *
     * @param resource
     * @param netPhotoPv
     * @param progressBar
     */
    private void setPhotoViewLayout(GlideBitmapDrawable resource, PhotoView netPhotoPv, ProgressBar progressBar) {
        Bitmap loadBp = resource.getBitmap();
        int bitHeight = loadBp.getHeight();
        int bitWidth = loadBp.getWidth();


        float bitmapPro = bitHeight * 1.0f / bitWidth;     //

        ViewGroup.LayoutParams params = netPhotoPv.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        netPhotoPv.setLayoutParams(params);
        progressBar.setVisibility(View.GONE);
        if (bitWidth >= 350) {     //根据具体业务进行判断
            if (mScreenPro > bitmapPro) {
                netPhotoPv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                /**
                 * 这里处理哪些高/宽比特别悬殊的图片  比如说 长图
                 * */
                loadBp = BitmapUtil.compressScaleBitmap(loadBp, mContext);
                netPhotoPv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        } else {
            loadBp = ThumbnailUtils.extractThumbnail(loadBp, DensityUtil.dip2px(mContext, bitWidth), DensityUtil.dip2px(mContext, bitHeight));
            netPhotoPv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        netPhotoPv.setImageBitmap(loadBp);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        mINetPhotoBrowserView.close();
    }

    private void loadSuccess(ProgressBar progressBar, LinearLayout mLlimageLoadFail, final NetPhoto photo) {
        progressBar.setVisibility(View.GONE);
        mLlimageLoadFail.setVisibility(View.GONE);

        if (photo.isNeedLoadOriFile()) {
            NetPhotoManager.setFileExistById(photo.getOriginalFileId());
            photo.setHasLoadOriFile(true);
            photo.setNeedLoadOriFile(false);
        }

    }

    private void loadFail(ProgressBar progressBar, LinearLayout mLlimageLoadFail, final NetPhoto photo) {
        progressBar.setVisibility(View.GONE);
        mLlimageLoadFail.setVisibility(View.VISIBLE);

        if (photo.isNeedLoadOriFile()) {
            photo.setHasLoadOriFile(false);
        }
    }

}
