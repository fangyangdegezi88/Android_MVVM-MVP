package com.focustech.android.photo.myalbum;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.focustech.android.commonlibs.util.DensityUtil;
import com.focustech.android.commonuis.base.BaseFragment;
import com.focustech.android.commonuis.view.ToastUtil;
import com.focustech.android.commonuis.view.recycler.SpacingDecoration;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.myalbum.IPhotoPickerView;
import com.focustech.android.photo.myalbum.myalbum.PhotoPickerPresenter;
import com.focustech.android.photo.myalbum.myalbum.adapter.PhotoGridAdapter;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaEntity;
import com.focustech.android.photo.myalbum.myalbum.event.OnItemCheckChangedListener;
import com.focustech.android.photo.myalbum.myalbum.event.OnItemCheckListener;
import com.focustech.android.photo.myalbum.myalbum.event.OnPhotoClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.focustech.android.photo.myalbum.MediaPicker.DEFAULT_COLUMN_NUMBER;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_CAN_PREVIEW;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_CHOOSE_SINGLE_FILE_MAX_SIZE;
import static com.focustech.android.photo.myalbum.MediaPicker.EXTRA_PREVIEW_ENABLED;

/**
 * <照片陈列及选择>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/15]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MediaPickerFragment extends BaseFragment<PhotoPickerPresenter> implements IPhotoPickerView {
    private final static String EXTRA_CAMERA = "camera";
    private final static String EXTRA_COLUMN = "column";
    private final static String EXTRA_COUNT = "count";
    private final static String EXTRA_GIF = "gif";
    private final static String EXTRA_ORIGIN_COUNT = "origin_count";

    private PhotoGridAdapter photoGridAdapter;
    private RecyclerView mPhotoRv;

    private int SCROLL_THRESHOLD = 100;

    private boolean canDoPreview;

    public static MediaPickerFragment newInstance(boolean showGif, boolean showCamera, boolean preview, boolean canDoPreview, int column, int maxCount, int originalCount,long singleFileMaxSize) {
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_GIF, showGif);
        args.putBoolean(EXTRA_CAMERA, showCamera);
        args.putBoolean(EXTRA_PREVIEW_ENABLED, preview);
        args.putBoolean(EXTRA_CAN_PREVIEW, canDoPreview);
        args.putInt(EXTRA_COLUMN, column);
        args.putInt(EXTRA_COUNT, maxCount);
        args.putInt(EXTRA_ORIGIN_COUNT, originalCount);
        args.putLong(EXTRA_CHOOSE_SINGLE_FILE_MAX_SIZE, singleFileMaxSize);
        MediaPickerFragment fragment = new MediaPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photo_picker;
    }

    @Override
    public void initViews(Context context, View root, Bundle savedInstanceState) {
        mPhotoRv = (RecyclerView) root.findViewById(R.id.photo_picker_photos_rv);
    }

    @Override
    public void initListeners() {
        mPhotoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.d(">>> Picker >>>", "dy = " + dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    mGlideManager.pauseRequests();
                } else {
                    mGlideManager.resumeRequests();
                }
            }
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mGlideManager.resumeRequests();
                }
            }
        });
    }

    @Override
    public void initData() {
        presenter = new PhotoPickerPresenter();
        presenter.attachView(this);

        int originalCount = getArguments().getInt(EXTRA_ORIGIN_COUNT, 0);
        int maxCount = getArguments().getInt(EXTRA_COUNT, 1);

        int column = getArguments().getInt(EXTRA_COLUMN, DEFAULT_COLUMN_NUMBER);
        boolean showCamera = getArguments().getBoolean(EXTRA_CAMERA, true);
        boolean previewEnable = getArguments().getBoolean(EXTRA_PREVIEW_ENABLED, true);
        canDoPreview = getArguments().getBoolean(EXTRA_CAN_PREVIEW, false);

        long singleFileMaxSize = getArguments().getLong(EXTRA_CHOOSE_SINGLE_FILE_MAX_SIZE, MediaPicker.SINGLE_ATTACHMENT_FILE_MAX_SIZE);

        presenter.init(getActivity().getApplicationContext(), maxCount, originalCount,singleFileMaxSize);

        photoGridAdapter = new PhotoGridAdapter(getContext(), mGlideManager, ((MediaPickerActivity)getActivity()).dirs(), column,singleFileMaxSize);
        photoGridAdapter.setShowCamera(showCamera);
        photoGridAdapter.setPreviewEnable(previewEnable);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(column, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mPhotoRv.addItemDecoration(new SpacingDecoration(DensityUtil.dip2px(4), DensityUtil.dip2px(4), false));
        mPhotoRv.setLayoutManager(layoutManager);
        mPhotoRv.setAdapter(photoGridAdapter);
        mPhotoRv.setItemAnimator(new DefaultItemAnimator());

        initAdapterListener();
    }

    private void initAdapterListener() {
        photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onClick(View v, int position, boolean showCamera) {
                final int index = showCamera ? position - 1 : position;

                List<String> photos = photoGridAdapter.getCurrentPhotoPaths();

                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);
                MediaPagerFragment imagePagerFragment =
                        MediaPagerFragment.newInstance(photos, index, screenLocation, v.getWidth(),
                                v.getHeight());

                ((MediaPickerActivity) getActivity()).addImagePagerFragment(imagePagerFragment);
            }

            @Override
            public void onReachMaxSize(long singleFileMaxSize) {
                String text = presenter.formatMaxSizeWarning(singleFileMaxSize);
                ((MediaPickerActivity) getActivity()).showReachMaxFileSizeDialog(text);
            }
        });

        photoGridAdapter.setOnCameraClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showOkToast(getActivity(), "打开相机");
            }
        });

        photoGridAdapter.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean OnItemCheck(int position, MediaEntity path, boolean isCheck, int selectedItemCount) {
                return presenter.checkPhotoItem(path, isCheck, selectedItemCount);
            }
        });

        photoGridAdapter.setOnItemCheckChangedListener(new OnItemCheckChangedListener() {
            @Override
            public void OnItemCheckChanged(int position, MediaEntity path, boolean isCheck, boolean oldCheck) {
                if (presenter.maxCount() == 1 && !canDoPreview) {
                    commitPicture();
                } else
                    updateSelectedCount(photoGridAdapter.getSelectedItemCount(),photoGridAdapter.getPreviewItemCount());
            }
        });
    }

    @Override
    protected int getSfLoadViewId() {
        return R.id.view_loadding_msg;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public String getName() {
        return getString(R.string.photo_picker_name);
    }


    @Override
    public void refreshPhotos() {
        if (photoGridAdapter != null)
            photoGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void swapToDirectory(int position) {
        if (photoGridAdapter != null) {
            photoGridAdapter.clearSelection();  // 更换文件夹时，清空之前的选择
            photoGridAdapter.setCurrentDirectoryIndex(position);
            photoGridAdapter.notifyDataSetChanged();
            updateSelectedCount(photoGridAdapter.getSelectedItemCount(),photoGridAdapter.getPreviewItemCount());
            updateHeaderTitle(position);
        }
    }

    void updateHeaderTitle(int position) {
        ((MediaPickerActivity)getActivity()).invokeUpdateHeaderTitle(position);
    }

    @Override
    public void alertWaring(String msg) {
        ((MediaPickerActivity)getActivity()).alertWarning(msg);
    }

    @Override
    public void alertWarningBtnText(String text) {
        ((MediaPickerActivity)getActivity()).alertWarningBtnText(text);
    }

    void updateSelectedCount(int count,int previewCount) {
        ((MediaPickerActivity)getActivity()).invokeUpdateToolbar(count,previewCount);
    }

    void commitPicture() {
        ((MediaPickerActivity)getActivity()).invokeCommit();
    }

    public ArrayList<String> getCurrentPhotos() {
        if (photoGridAdapter != null) {
            return photoGridAdapter.getSelectedPhotoPaths();
        } else
            return null;
    }

    public ArrayList<String> getCurrentPreviewPhotos() {
        if (photoGridAdapter != null) {
            return photoGridAdapter.getPreviewPhotoPaths();
        } else
            return null;
    }

    public ArrayList<MediaEntity> getCurrentMedia() {
        if (photoGridAdapter != null) {
            return photoGridAdapter.getSelectedMediaBeen();
        } else
            return null;
    }

}
