package com.focustech.android.photo.myalbum.myalbum.adapter;

import android.support.v7.widget.RecyclerView;

import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaEntity;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaDirectory;
import com.focustech.android.photo.myalbum.myalbum.event.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @see SelectableAdapter#selectedMediaBeen
 * @see SelectableAdapter#originalPhotos
 * 本次选择图片selectedPhotos和上次选择图片originalPhotos是分开的
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/18]
 * @see [相关类/方法]
 * @since [V1]
 */
public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Selectable {

    private static final String TAG = SelectableAdapter.class.getSimpleName();

    protected ArrayList<MediaDirectory> photoDirectories;
    protected ArrayList<MediaEntity> selectedMediaBeen;
    //初始进入时已选的照片 original selected photos
    protected ArrayList<String> originalPhotos = null;

    public int currentDirectoryIndex = 0;


    public SelectableAdapter() {
        photoDirectories = new ArrayList<>();
        selectedMediaBeen = new ArrayList<>();
    }


    /**
     * Indicates if the item at position where is selected
     *
     * @param mediaEntity Photo of the item to check
     * @return true if the item is selected, false otherwise
     */
    @Override
    public boolean isSelected(MediaEntity mediaEntity) {
        if (originalPhotos != null && originalPhotos.contains(mediaEntity.getPath()) && !selectedMediaBeen.contains(mediaEntity)) {
            selectedMediaBeen.add(mediaEntity);
        }
        return getSelectedMediaBeen().contains(mediaEntity);
    }


    /**
     * Toggle the selection status of the item at a given position
     *
     * @param mediaEntity Photo of the item to toggle the section status for
     */
    @Override
    public void toggleSelection(MediaEntity mediaEntity) {
        if (selectedMediaBeen.contains(mediaEntity)) {
            selectedMediaBeen.remove(mediaEntity);
            if (originalPhotos!=null && originalPhotos.contains(mediaEntity.getPath())){
                originalPhotos.remove(mediaEntity.getPath());
            }
        } else {
            selectedMediaBeen.add(mediaEntity);
        }
    }


    /**
     * Clear the selection status for all items
     */
    @Override
    public void clearSelection() {
        selectedMediaBeen.clear();
    }


    /**
     * Count the selected items
     *获取已经选择的对象数量
     * @return Selected items count
     */
    @Override
    public int getSelectedItemCount() {
        return selectedMediaBeen.size();
    }

    public int getPreviewItemCount() {
        int count = 0;
        for (MediaEntity mediaEntity : selectedMediaBeen) {
            if(mediaEntity.getType() == MediaEntity.TYPE_PHOTO){
                count++;
            }
        }
        return count;
    }


    public void setCurrentDirectoryIndex(int currentDirectoryIndex) {
        this.currentDirectoryIndex = currentDirectoryIndex;
    }


    public List<MediaEntity> getCurrentPhotos() {
        return photoDirectories.get(currentDirectoryIndex).getMediaBeen();
    }


    public List<String> getCurrentPhotoPaths() {
        List<String> currentPhotoPaths = new ArrayList<>(getCurrentPhotos().size());
        for (MediaEntity mediaEntity : getCurrentPhotos()) {
            currentPhotoPaths.add(mediaEntity.getPath());
        }
        return currentPhotoPaths;
    }


    public ArrayList<MediaEntity> getSelectedMediaBeen() {
        return selectedMediaBeen;
    }

}
