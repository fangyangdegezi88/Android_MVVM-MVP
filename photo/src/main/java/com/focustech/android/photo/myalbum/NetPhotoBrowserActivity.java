package com.focustech.android.photo.myalbum;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.focustech.android.commonuis.base.BaseActivity;
import com.focustech.android.commonuis.view.ToastUtil;
import com.focustech.android.commonuis.view.overscroll.OverScrollDecoratorHelper;
import com.focustech.android.commonuis.view.overscroll.ViewPagerFixed;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.adapter.NetPhotoAdapter;
import com.focustech.android.photo.myalbum.myalbum.INetPhotoBrowserView;
import com.focustech.android.photo.myalbum.myalbum.NetPhotoBrowserPresenter;
import com.focustech.android.photo.myalbum.myalbum.bean.NetPhoto;

import java.util.List;
import java.util.Locale;


/**
 * 调用图片浏览器相关参数
 * <p>
 * NET_PHOTO_FILES_KEY 所有的图片ffid 必传
 * <p>
 * NET_PHOTO_CURRENT_FILE_KEY  当前展示的ffid
 * <p>
 * NET_ORIGINAL_PHOTO_FILES_KEY  所有图片的原图  ffid  非必传
 * <p>
 * NET_PHOTO_SOURCE  图片来源 {@link FILE_SOURCE}  非必传  默认为 /services/image/utils
 * <p>
 * DOWNLOAD_HOST_URL   当前下载的URL  必传
 * <p>
 * TOKEN
 * Created by liuzaibing on 2016/10/20.
 */
public class NetPhotoBrowserActivity extends BaseActivity<NetPhotoBrowserPresenter>
        implements INetPhotoBrowserView, ViewPager.OnPageChangeListener, View.OnClickListener {

    /**
     * 切换view
     */
    private ViewPagerFixed mPhotoVpf;

    /**
     * 适配器
     */
    private NetPhotoAdapter mNetPhotoAdapter;

    /**
     * 索引id   1/5
     */
    private TextView mIndexTv;
    /**
     * 下载按钮
     */
    private ImageView mDownloadIv;
    /**
     * 原图按钮
     */
    private TextView mRawImage;

    @Override
    public void initViews(Context context, View root, Bundle savedInstanceState) {
        mPhotoVpf = (ViewPagerFixed) findViewById(R.id.photo_vpf);
        mIndexTv = (TextView) findViewById(R.id.index_tv);
        mDownloadIv = (ImageView) findViewById(R.id.download_photo_iv);
        mRawImage = (TextView) findViewById(R.id.tv_raw_image);
    }

    @Override
    public void initListeners() {
        mDownloadIv.setOnClickListener(this);
        mRawImage.setOnClickListener(this);
    }

    @Override
    public void initData() {
        presenter = new NetPhotoBrowserPresenter(true);
        presenter.attachView(this);

        Bundle bundle = getIntent().getExtras();
        presenter.initData(bundle);
    }

    @Override
    public void setContentViewBefore() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public int getSfHeaderId() {
        return 0;
    }

    @Override
    public int getLoadViewId() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_browser;
    }

    @Override
    public String getName() {
        return getString(R.string.net_image_browser);
    }

    @Override
    public void openNetPicFail(int i) {
        ToastUtil.showFocusToast(this, "打开浏览器失败");
        close();
    }

    @Override
    public void showNetPic(List<NetPhoto> files, int currentIndex, int source, String downloadUrl) {
        mNetPhotoAdapter = new NetPhotoAdapter(this, files, this, source, downloadUrl);
        mPhotoVpf.setAdapter(mNetPhotoAdapter);
        mPhotoVpf.addOnPageChangeListener(this);
        mPhotoVpf.setCurrentItem(currentIndex);
        if (currentIndex == 0) {
            dealActionByIndex(currentIndex);
        }
        OverScrollDecoratorHelper.setUpOverScroll(mPhotoVpf);
    }

    private void dealActionByIndex(int index) {
        mDownloadIv.setTag(index);
        mRawImage.setTag(index);
        presenter.showImgPageIndex(index);
    }

    @Override
    public void close() {
        this.finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if ((position == 0 || presenter.getFileSize() - 2 == position) && positionOffsetPixels > 0) {
            mPhotoVpf.IS_CAN_OVER_SCROLL = false;
        } else {
            mPhotoVpf.IS_CAN_OVER_SCROLL = true;
        }
    }

    @Override
    public void onPageSelected(int index) {
        dealActionByIndex(index);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void showIndex(int index, int SumCount) {
        mIndexTv.setVisibility(View.VISIBLE);
        mIndexTv.setText(String.format(Locale.getDefault(), "%d/%d", index + 1, SumCount));
    }

    @Override
    public void showRawImg(boolean visible) {
        mRawImage.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void downloadSuccess(int resId) {
        ToastUtil.showOkToast(this, resId);
    }

    @Override
    public void downloadFail(int resId) {
        ToastUtil.showFocusToast(this, resId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.download_photo_iv) {
            int index = (int) v.getTag();
            presenter.downloadImgByIndex(index);
        } else if (v.getId() == R.id.tv_raw_image) {
            int index = (int) v.getTag();
            mNetPhotoAdapter.showRawImgByIndex(index);
            mPhotoVpf.removeAllViews();
            mPhotoVpf.setAdapter(mNetPhotoAdapter);
            mPhotoVpf.addOnPageChangeListener(this);
            mPhotoVpf.setCurrentItem(index);
            if (index == 0) {
                dealActionByIndex(index);
            }

            v.setVisibility(View.GONE);
        }
    }
}
