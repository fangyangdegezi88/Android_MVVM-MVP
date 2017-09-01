package com.focustech.android.photo.myalbum;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.focustech.android.commonuis.base.BaseFragment;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.myalbum.IDirectoryView;
import com.focustech.android.photo.myalbum.myalbum.PhotoDirPickerPresenter;
import com.focustech.android.photo.myalbum.myalbum.adapter.DirectoryListAdapter;
import com.focustech.android.photo.myalbum.myalbum.event.EventSwapDirectory;

import de.greenrobot.event.EventBus;


/**
 * <照片文件夹陈列及选择>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/15]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MediaDirPickerFragment extends BaseFragment<PhotoDirPickerPresenter> implements IDirectoryView, AdapterView.OnItemClickListener {

    private ListView mDirsLv;

    private DirectoryListAdapter listAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photo_dir_picker;
    }

    @Override
    public void initViews(Context context, View root, Bundle savedInstanceState) {
        mDirsLv = (ListView) root.findViewById(R.id.directories_lv);
    }

    @Override
    public void initListeners() {
        mDirsLv.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        listAdapter  = new DirectoryListAdapter(mGlideManager, ((MediaPickerActivity)getActivity()).dirs());
        mDirsLv.setAdapter(listAdapter);
    }

    @Override
    public String getName() {
        return getString(R.string.photo_dir_picker_name);
    }

    @Override
    public void refreshDirs() {
        if (listAdapter != null)
            listAdapter.notifyDataSetChanged();
    }

    /**
     * 点击切换文件夹
     * @see com.focustech.android.photo.myalbum.myalbum.PhotoPickerPresenter#onEventMainThread(EventSwapDirectory)
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((MediaPickerActivity)getActivity()).showPhotoPickerFragment();
        EventBus.getDefault().post(new EventSwapDirectory(position));
    }


    @Override
    protected int getSfLoadViewId() {
        return 0;
    }
}
