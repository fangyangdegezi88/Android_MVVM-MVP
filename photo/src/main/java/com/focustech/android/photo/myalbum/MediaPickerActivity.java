package com.focustech.android.photo.myalbum;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.android.commonlibs.bridge.cache.localstorage.FileProperty;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonlibs.util.crash.FocusPackage;
import com.focustech.android.commonuis.base.BaseActivity;
import com.focustech.android.commonuis.view.dialog.SFAlertDialog;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.myalbum.IPhotoActivityView;
import com.focustech.android.photo.myalbum.myalbum.ImagePagerCallBack;
import com.focustech.android.photo.myalbum.myalbum.PhotoActivityPresenter;
import com.focustech.android.photo.myalbum.myalbum.bean.Path;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaDirectory;
import com.focustech.android.photo.myalbum.util.mediastore.MediaOptions;
import com.focustech.android.photo.myalbum.util.mediastore.MediaStoreHelper;
import com.focustech.android.photo.myalbum.util.mediastore.NameMap;

import java.util.ArrayList;
import java.util.List;

import static com.focustech.android.photo.myalbum.MediaPicker.DEFAULT_COLUMN_NUMBER;
import static com.focustech.android.photo.myalbum.MediaPicker.DEFAULT_MAX_COUNT;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_CAN_PREVIEW;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_GRID_COLUMN;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_CHOOSE_SINGLE_FILE_MAX_SIZE;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_MAX_COUNT;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_ORIGINAL_PHOTOS_COUNT;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_PREVIEW_ENABLED;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_SHOW_CAMERA;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_SHOW_GIF;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_SHOW_UPLOAD_FOLDER_NAME;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_SHOW_VIDEO;
import static com.focustech.android.photo.myalbum.MediaPicker.KEY_SELECTED_MEDIA;

/**
 * <我的相册>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/15]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MediaPickerActivity extends BaseActivity<PhotoActivityPresenter> implements View.OnClickListener, IPhotoActivityView, ImagePagerCallBack {
    /**
     * 照片选择页
     */
    private MediaPickerFragment mPickerFragment;
    /**
     * 选中图片浏览页
     */
    private MediaPagerFragment mImagePagerFragment;
    /**
     * 图片文件夹选择页
     */
    private MediaDirPickerFragment mPhotoDirPickerFragment;
    /**
     * Fragment容器
     */
    private FrameLayout mFrame;
    /**
     * 底部工具栏
     */
    private RelativeLayout mBottomToolbar;
    /**
     * 预览按钮
     */
    private Button mPreviewBt;
    /**
     * 确定按钮
     */
    private Button mCommitBt;

    /**
     * 展示的云盘内容
     * */
    private LinearLayout mUploadFolderLl;
    private TextView mUploadFolderNameTv;
    /**
     * 是否显示Gif图
     */
    private boolean mShowGif;
    /**
     * 是否展示视频
     * */
    private boolean mShowVideo;

    /**
     * 全局rl
     */
    private RelativeLayout mRootRl;

    private RelativeLayout mBottomBoolbarRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public int getLayoutId() {
        return R.layout.activity_photo_picker;
    }

    @Override
    public void initViews(Context context, View root, Bundle savedInstanceState) {
        mRootRl = (RelativeLayout) findViewById(R.id.rl_content);
        mFrame = (FrameLayout) findViewById(R.id.frame_container);
        mBottomToolbar = (RelativeLayout) findViewById(R.id.bottom_toolbar_rl);
        mPreviewBt = (Button) findViewById(R.id.preview_btn);
        mCommitBt = (Button) findViewById(R.id.commit_btn);
        mBottomBoolbarRl = (RelativeLayout) findViewById(R.id.bottom_toolbar_rl);
        mUploadFolderLl = (LinearLayout) findViewById(R.id.upload_folder_name_ll);
        mUploadFolderNameTv = (TextView) findViewById(R.id.upload_folder_name_tv);
    }

    @Override
    public void initListeners() {
        mPreviewBt.setOnClickListener(this);
        mCommitBt.setOnClickListener(this);
        mBottomBoolbarRl.setOnClickListener(this);
    }

    @Override
    public void initData() {
        presenter = new PhotoActivityPresenter(true);
        presenter.attachView(this);

        mHeader.setActionRightTxt(getString(R.string.cancel));
        mHeader.setActionTitle(getString(R.string.recent_pictures));

        boolean showCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, false);
        boolean showGif = getIntent().getBooleanExtra(EXTRA_SHOW_GIF, false);
        boolean previewEnabled = getIntent().getBooleanExtra(EXTRA_PREVIEW_ENABLED, true);
        boolean canDoPreview = getIntent().getBooleanExtra(EXTRA_CAN_PREVIEW, false);
        boolean showVideo = getIntent().getBooleanExtra(EXTRA_SHOW_VIDEO,false);
        int columnNumber = getIntent().getIntExtra(EXTRA_GRID_COLUMN, DEFAULT_COLUMN_NUMBER);
        int maxCount = getIntent().getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
        int originalCount = getIntent().getIntExtra(EXTRA_ORIGINAL_PHOTOS_COUNT, 0);
        String uploadFolderName = getIntent().getStringExtra(EXTRA_SHOW_UPLOAD_FOLDER_NAME);
        /**
         * 默认为10M
         * */
        long singleFileMaxSize = getIntent().getLongExtra(EXTRA_CHOOSE_SINGLE_FILE_MAX_SIZE,MediaPicker.SINGLE_ATTACHMENT_FILE_MAX_SIZE);

        setShowGif(showGif);
        setShowVideo(showVideo);
        setUploadFolderName(uploadFolderName);

        presenter.init(getResources(), originalCount, maxCount, canDoPreview);

        loadMediaDirectories(showVideo);

        mPickerFragment = (MediaPickerFragment) getSupportFragmentManager().findFragmentByTag("mPicker");
        if (mPickerFragment == null) {
            mPickerFragment = MediaPickerFragment
                    .newInstance(showGif, showCamera, previewEnabled, canDoPreview, columnNumber, maxCount, originalCount,singleFileMaxSize);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_container, mPickerFragment, "mPicker")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    /**
     * 搜索MediaStore中的所有媒体文件
     *
     * 优先展示图片文件-->然后展示视频文件
     */
    private void loadMediaDirectories(final boolean showVideo) {
        final Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, isShowGif());
        MediaOptions.builder(mediaStoreArgs).showPhotoDirAll(true, getString(R.string.recent_pictures))
                .nameDisplay(true, new NameMap(FileProperty.JPG.getPath(), FocusPackage.newBuilder().getAppName()),
                                   new NameMap(Path.weiboPhoto, getString(R.string.folder_name_sina_weibo)),
                                   new NameMap(Path.weixinPhoto, getString(R.string.folder_name_weixin)));
        Log.e("Photo", "====MediaStoreHelper getPhotoDirs");
        MediaStoreHelper.getPhotoDirs(this, mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<MediaDirectory> dirs,MediaOptions options) {
                        if(showVideo){
                            //如果需要展示视频，则增加逻辑
                            MediaStoreHelper.getLocalVideoDirs(MediaPickerActivity.this,options,dirs,
                                    new MediaStoreHelper.VideoResultCallback(){
                                        @Override
                                        public void onResultCallback(List<MediaDirectory> dirs,MediaOptions options) {
                                            options.sort(dirs);
                                            presenter.setDirs(dirs);
                                        }
                                    });
                        }else{
                            options.sort(dirs);
                            presenter.setDirs(dirs);
                        }
                    }
                });
    }

    /**
     * 提供图片路径的数据
     *
     * @return
     */
    public ArrayList<MediaDirectory> dirs() {
        return presenter.dirs();
    }

    @Override
    public void leftBtnClick() {
        if (mImagePagerFragment != null && mImagePagerFragment.isVisible()) {
            mImagePagerFragment.runExitAnimation(new Runnable() {
                public void run() {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                        //显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)
                        mRootRl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        presenter.exitPreview();
                        presenter.preViewFinish();
                    }
                }
            });
        } else if (mPickerFragment != null && mPickerFragment.isVisible()) {
            showDirectoriesFragment();
        } else if (mPhotoDirPickerFragment != null && mPhotoDirPickerFragment.isVisible()) {
            cancelAndQuit();
        }
    }

    @Override
    public void onBackPressed() {
        leftBtnClick();
    }

    /**
     * 取消同时退出相册
     */
    void cancelAndQuit() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void rightBtnClick() {
        cancelAndQuit();
    }

    public boolean isShowGif() {
        return mShowGif;
    }

    public void setShowGif(boolean mShowGif) {
        this.mShowGif = mShowGif;
    }

    public boolean isShowVideo() {
        return mShowVideo;
    }

    public void setShowVideo(boolean mShowVideo) {
        this.mShowVideo = mShowVideo;
    }

    public void setUploadFolderName(String name){
        if(GeneralUtils.isNotNullOrEmpty(name)){
            mUploadFolderLl.setVisibility(View.VISIBLE);
            mUploadFolderNameTv.setText(name);
        }else{
            mUploadFolderLl.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.preview_btn) {
            if (mPickerFragment.getCurrentPreviewPhotos() != null && mPickerFragment.getCurrentPreviewPhotos().size() > 0) {
                //Activity全屏显示，且状态栏被隐藏覆盖掉。
//                    mRootRl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);--状态栏滑动下 会出来
                //隐藏状态栏，同时Activity会伸展全屏显示
                mRootRl.setSystemUiVisibility(View.INVISIBLE);//--状态栏滑动下 会出来
                addImagePagerFragment(MediaPagerFragment.newInstance(mPickerFragment.getCurrentPreviewPhotos(), 0));
            }
        } else if (v.getId() == R.id.commit_btn) {
            if (mPickerFragment != null) {
                if (mPickerFragment.getCurrentMedia() != null && mPickerFragment.getCurrentMedia().size() > 0) {
                    setResult(RESULT_OK, getIntent().putParcelableArrayListExtra(KEY_SELECTED_MEDIA, mPickerFragment.getCurrentMedia()));
                    finish();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }

    /**
     * 显示选择图片页面于最上层
     */
    public void showPhotoPickerFragment() {
        if (mPickerFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .show(mPickerFragment)
                    .hide(mPhotoDirPickerFragment)
                    .commit();
            presenter.exitDirView();
        }
    }

    /**
     * 显示文件夹页面于最上层
     */
    public void showDirectoriesFragment() {
        if (mPhotoDirPickerFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(mPickerFragment)
                    .show(mPhotoDirPickerFragment)
                    .commit();
        } else {
            this.mPhotoDirPickerFragment = new MediaDirPickerFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_container, this.mPhotoDirPickerFragment, "mDirs")
                    .hide(mPickerFragment)
                    .show(mPhotoDirPickerFragment)
                    .commit();
        }
        presenter.enterDirView();
    }

    public void addImagePagerFragment(MediaPagerFragment imagePagerFragment) {
        this.mImagePagerFragment = imagePagerFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, this.mImagePagerFragment)
                .addToBackStack(null)
                .commit();
        presenter.enterPreview(mHeader.getActionTitle());
    }

    public void showReachMaxFileSizeDialog(String content){
        alertWarning(content);
        mLayerHelper.getAlertDialog().setSingleBtnText(getString(R.string.i_know));
    }

    @Override
    public String getName() {
        return getString(R.string.module_album);
    }

    @Override
    public void changeActionBarTitle(String changeTitle) {
        mHeader.setActionTitle(changeTitle);
    }

    @Override
    public void setActionBarVisiable(boolean visible) {
        mHeader.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setToolBar(boolean visible) {
        mBottomToolbar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setCommitBtStyle(boolean enabled, String s) {
        mCommitBt.setEnabled(enabled);
        mCommitBt.setText(s);
    }

    @Override
    public void setPreviewBtn(boolean visible, boolean enabled) {
        mPreviewBt.setVisibility(visible ? View.VISIBLE : View.GONE);
        mPreviewBt.setEnabled(enabled);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.clearDirectories();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.dialog_exit_to_bottom);
    }

    public void alertWarning(String msg) {
        if (mLayerHelper != null)
            mLayerHelper.showAlert(msg, SFAlertDialog.MTDIALOG_THEME.NO_TITLE_ONE);
    }

    public void alertWarningBtnText(String msg){
        if (mLayerHelper != null)
            mLayerHelper.getAlertDialog().setSingleBtnText(msg);
    }

    public void invokeUpdateToolbar(int selectedItemCount,int previewCount) {
        presenter.updateToolbar(selectedItemCount,previewCount);
    }

    public void invokeCommit() {
        mCommitBt.performClick();
    }

    public void invokeUpdateHeaderTitle(int position) {
        presenter.setDirTitle(position);
    }

    @Override
    public void imageClick() {
        presenter.clickedImage();
    }

    @Override
    public void imagePreViewFinish() {
        presenter.preViewFinish();
    }
}
