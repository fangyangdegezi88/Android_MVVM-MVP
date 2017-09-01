package com.focustech.android.photo.myalbum.myalbum.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaEntity;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaDirectory;
import com.focustech.android.photo.myalbum.myalbum.event.OnItemCheckChangedListener;
import com.focustech.android.photo.myalbum.myalbum.event.OnItemCheckListener;
import com.focustech.android.photo.myalbum.myalbum.event.OnPhotoClickListener;
import com.focustech.android.photo.myalbum.util.mediastore.MediaStoreHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.focustech.android.photo.myalbum.MediaPicker.SINGLE_UPLOAD_FILE_MAX_SIZE;

/**
 * <照片适配器>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/14]
 * @see [相关类/方法]
 * @since [V1]
 */
public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder> {
    private LayoutInflater inflater;
    private RequestManager glide;

    private OnItemCheckListener onItemCheckListener = null;
    private OnPhotoClickListener onPhotoClickListener = null;
    private View.OnClickListener onCameraClickListener = null;
    private OnItemCheckChangedListener onItemCheckChangedListener = null;


    public final static int ITEM_TYPE_CAMERA = 100;
    public final static int ITEM_TYPE_PHOTO = 101;
    private final static int COL_NUMBER_DEFAULT = 4;

    private boolean hasCamera = true;
    private boolean previewEnable = true;

    private int imageSize;
    private int columnNumber = COL_NUMBER_DEFAULT;

    public long singleFileMaxSize;


    public PhotoGridAdapter(Context context, RequestManager requestManager, ArrayList<MediaDirectory> photoDirectories,long singleFileMaxSize) {
        this.photoDirectories = photoDirectories;
        this.glide = requestManager;
        inflater = LayoutInflater.from(context);
        this.singleFileMaxSize = singleFileMaxSize;
        setColumnNumber(context, columnNumber);
    }

    public PhotoGridAdapter(Context context, RequestManager requestManager, ArrayList<MediaDirectory> photoDirectories, int colNum,long singleFileMaxSize) {
        this(context, requestManager, photoDirectories,singleFileMaxSize);
        setColumnNumber(context, colNum);
    }

    private void setColumnNumber(Context context, int columnNumber) {
        this.columnNumber = columnNumber;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNumber;
    }

    @Override
    public int getItemViewType(int position) {
        return (showCamera() && position == 0) ? ITEM_TYPE_CAMERA : ITEM_TYPE_PHOTO;
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_picker_photo, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(itemView);
        if (viewType == ITEM_TYPE_CAMERA) {
            holder.ivSelected.setVisibility(View.GONE);
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCameraClickListener != null) {
                        onCameraClickListener.onClick(view);
                    }
                }
            });
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {

        if (getItemViewType(position) == ITEM_TYPE_PHOTO) {

            List<MediaEntity> mediaBeen = getCurrentPhotos();
            final MediaEntity mediaEntity;

            if (showCamera()) {
                mediaEntity = mediaBeen.get(position - 1);
            } else {
                mediaEntity = mediaBeen.get(position);
            }

            glide.load(new File(mediaEntity.getPath()))
                    .centerCrop()
                    .dontAnimate()
                    .thumbnail(0.5f)
                    .override(imageSize, imageSize)
                    .into(holder.ivPhoto);

            final boolean isChecked = isSelected(mediaEntity);
            boolean isVideo = mediaEntity.getType() == MediaEntity.TYPE_VIDEO;

            if(isVideo){
                holder.mVideoSignIv.setVisibility(View.VISIBLE);
            }else{
                holder.mVideoSignIv.setVisibility(View.GONE);
            }

            holder.ivSelected.setSelected(isChecked);
            holder.ivPhoto.setSelected(isChecked);

            final boolean beyondMaxSize = mediaEntity.getSize() > singleFileMaxSize;
            if (isChecked || beyondMaxSize) {
                holder.mSupernatantIv.setVisibility(View.VISIBLE);
            } else {
                holder.mSupernatantIv.setVisibility(View.GONE);
            }

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPhotoClickListener != null) {
                        int pos = holder.getAdapterPosition();
                        if (previewEnable) {
                            onPhotoClickListener.onClick(view, pos, showCamera());
                        } else {
                            if(beyondMaxSize){
                                onPhotoClickListener.onReachMaxSize(singleFileMaxSize);
                                return;
                            }

                            holder.mTouchDelegate.performClick();
                        }
                    }
                }
            });
            holder.mTouchDelegate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(beyondMaxSize){
                        if(onPhotoClickListener != null){
                            onPhotoClickListener.onReachMaxSize(singleFileMaxSize);
                        }
                        return;
                    }

                    int pos = holder.getAdapterPosition();
                    boolean isEnable = true;
                    boolean isCheckedOld = isSelected(mediaEntity); // 刷新后的数据

                    if (onItemCheckListener != null) {
                        isEnable = onItemCheckListener.OnItemCheck(pos, mediaEntity, isCheckedOld,
                                getSelectedMediaBeen().size());
                    }
                    if (isEnable) {
                        toggleSelection(mediaEntity);
                        // 修复选择时闪烁的问题
                        boolean isCheckedNow = isSelected(mediaEntity);
                        holder.ivPhoto.setSelected(isCheckedNow);
                        holder.ivSelected.setSelected(isCheckedNow);

                        if (isCheckedNow) {
                            addAnimation(holder.ivSelected);
                            holder.mSupernatantIv.setVisibility(View.VISIBLE);
                        } else {
                            holder.mSupernatantIv.setVisibility(View.GONE);
                        }

                        if (onItemCheckChangedListener != null)
                            onItemCheckChangedListener.OnItemCheckChanged(pos, mediaEntity, isCheckedNow, isCheckedOld);
                    }
                }
            });

        } else {
            holder.ivPhoto.setImageResource(R.drawable.assignment_reply_bt_camera_selector);
        }
    }

    /**
     * 选中按钮点击动画
     */
    private void addAnimation(View view) {
        float[] values = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", values),
                ObjectAnimator.ofFloat(view, "scaleY", values));
        set.setDuration(150);
        set.start();
    }

    @Override
    public int getItemCount() {
        int photosCount =
                photoDirectories.size() == 0 ? 0 : getCurrentPhotos().size();
        if (showCamera()) {
            return photosCount + 1;
        }
        return photosCount;
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private LinearLayout mTouchDelegate;
        private ImageView ivSelected;
        private ImageView mSupernatantIv;
        private ImageView mVideoSignIv;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.photo_iv);
            mTouchDelegate = (LinearLayout) itemView.findViewById(R.id.photo_checkbox_delegate_ll);
            ivSelected = (ImageView) itemView.findViewById(R.id.photo_checkbox_iv);
            mSupernatantIv = (ImageView) itemView.findViewById(R.id.supernatant_iv);
            mVideoSignIv = (ImageView) itemView.findViewById(R.id.video_sign_iv);
        }
    }


    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }


    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }


    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.onCameraClickListener = onCameraClickListener;
    }

    public void setOnItemCheckChangedListener(OnItemCheckChangedListener onItemCheckChangedListener) {
        this.onItemCheckChangedListener = onItemCheckChangedListener;
    }

    /**
     * 获取所有的数据
     * */
    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>(getSelectedItemCount());

        for (MediaEntity mediaEntity : selectedMediaBeen) {
            selectedPhotoPaths.add(mediaEntity.getPath());
        }

        return selectedPhotoPaths;
    }

    /**
     * 获取预览的数据
     * */
    public ArrayList<String> getPreviewPhotoPaths() {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>(getSelectedItemCount());
        for (MediaEntity mediaEntity : selectedMediaBeen) {
            if(mediaEntity.getType() == MediaEntity.TYPE_PHOTO){
                selectedPhotoPaths.add(mediaEntity.getPath());
            }
        }
        return selectedPhotoPaths;
    }


    public void setShowCamera(boolean hasCamera) {
        this.hasCamera = hasCamera;
    }

    public void setPreviewEnable(boolean previewEnable) {
        this.previewEnable = previewEnable;
    }

    public boolean showCamera() {
        return (hasCamera && currentDirectoryIndex == MediaStoreHelper.INDEX_ALL_PHOTOS);
    }

    @Override
    public void onViewRecycled(PhotoViewHolder holder) {
        Glide.clear(holder.ivPhoto);
        super.onViewRecycled(holder);
    }
}
