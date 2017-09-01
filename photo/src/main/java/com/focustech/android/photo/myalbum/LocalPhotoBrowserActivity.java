package com.focustech.android.photo.myalbum;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonuis.base.BaseActivity;
import com.focustech.android.commonuis.view.ToastUtil;
import com.focustech.android.commonuis.view.overscroll.OverScrollDecoratorHelper;
import com.focustech.android.commonuis.view.overscroll.ViewPagerFixed;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.adapter.LocalPhotoAdapter;
import com.focustech.android.photo.myalbum.myalbum.ILocalPhotoBrowserView;

import java.util.ArrayList;
import java.util.Locale;

import static com.focustech.android.photo.PhotoConstants.BundleKey.LOCAL_PHOTO_CURRENT_FILE_PATH;
import static com.focustech.android.photo.PhotoConstants.BundleKey.LOCAL_PHOTO_FILES_PATH;
import static com.focustech.android.photo.PhotoConstants.BundleKey.LOCAL_PHOTO_TITLE;


/**
 * <本地图片浏览器>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/11/22]
 * @see [相关类/方法]
 * @since [V1]
 */

public class LocalPhotoBrowserActivity extends BaseActivity implements ILocalPhotoBrowserView, ViewPager.OnPageChangeListener {

    /**
     * viewPager
     */
    private ViewPagerFixed mPhotoVpf;

    /**
     * adapter
     */
    private LocalPhotoAdapter mLocalPhotoAdapter;

    /**
     * 索引
     */
    private TextView mCurrentTv;
    private RelativeLayout mToolBar;


    /**
     * 当前位置
     */
    private int mCurrentIndex;

    /**
     * 所有文件路径
     */
    private ArrayList<String> paths;

    @Override
    public void initViews(Context context, View root, Bundle savedInstanceState) {
        mCurrentTv = (TextView) findViewById(R.id.tv_current);
        mPhotoVpf = (ViewPagerFixed) findViewById(R.id.local_photo_vpf);
        mToolBar = (RelativeLayout) findViewById(R.id.bottom_toolbar_rl);
    }

    @Override
    public void initListeners() {
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        String currentPath = bundle.getString(LOCAL_PHOTO_CURRENT_FILE_PATH);
        paths = bundle.getStringArrayList(LOCAL_PHOTO_FILES_PATH);
        String title = bundle.getString(LOCAL_PHOTO_TITLE);
        mHeader.setActionTitle(title);
        if (paths == null || paths.size() == 0) {
            ToastUtil.showFocusToast(this, "没有找到文件路径！");
            mCurrentTv.setText(formatText(-1, 0));
            return;
        }

        if (GeneralUtils.isNullOrEmpty(currentPath)) {
            ToastUtil.showFocusToast(this, "没有找到第一张显示的图片！");
            return;
        }

        initCurrentIndex(currentPath);

        initIndexString();

        initPhotoAdapter();
    }

    /**
     * <初始化预览图片适配器>
     */
    private void initPhotoAdapter() {
        mLocalPhotoAdapter = new LocalPhotoAdapter(this, paths, mGlideManager, this);
        mPhotoVpf.setAdapter(mLocalPhotoAdapter);
        mPhotoVpf.addOnPageChangeListener(this);
        mPhotoVpf.setCurrentItem(mCurrentIndex);
        OverScrollDecoratorHelper.setUpOverScroll(mPhotoVpf);
    }

    /**
     * <初始化index文本>
     */
    private void initIndexString() {
        if(paths.size() == 1){
            mToolBar.setVisibility(View.GONE);
        }else{
            mToolBar.setVisibility(View.VISIBLE);
        }
        mCurrentTv.setText(formatText(mCurrentIndex, paths.size()));
    }

    /**
     * <初始化当前Index>
     *
     * @param currentPath
     */
    private void initCurrentIndex(String currentPath) {
        mCurrentIndex = 0;
        for (int i = 0; i < paths.size(); i++) {
            if (currentPath.equals(paths.get(i))) { // 找到当前图片
                mCurrentIndex = i;
                break;
            }
        }
    }

    /**
     * 显示 x / y 样式文本
     *
     * @param pos
     * @param size
     * @return
     */
    String formatText(int pos, int size) {
        return String.format(Locale.getDefault(), getResources().getString(R.string.local_photo_format), pos + 1, size);
    }

    @Override
    public boolean checkHasLoading(AppCompatActivity activity) {
        return true;
    }

    @Override
    public int getSfHeaderId() {
        return R.id.sf_header;
    }

    @Override
    public int getLoadViewId() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_local_photo_browser;
    }

    @Override
    public String getName() {
        return getString(R.string.preview);
    }

    /**
     * <关闭页面>
     */
    @Override
    public void close() {
        finish();
    }

    /***
     * 配合 OverScrollDecoratorHelper.setUpOverScroll(mPhotoVpf) 起作用
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if ((position == 0 || paths.size() - 2 == position) && positionOffsetPixels > 0) {
            mPhotoVpf.IS_CAN_OVER_SCROLL = false;
        } else {
            mPhotoVpf.IS_CAN_OVER_SCROLL = true;
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        initIndexString();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
