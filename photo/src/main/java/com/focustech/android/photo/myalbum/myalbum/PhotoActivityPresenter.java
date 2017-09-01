package com.focustech.android.photo.myalbum.myalbum;

import android.content.res.Resources;

import com.focustech.android.commonuis.biz.BaseCommonPresenter;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.MediaPickerActivity;
import com.focustech.android.photo.myalbum.util.PhotoEvent;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaDirectory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;


/**
 * <Controller>
 *
 * @see MediaPickerActivity  <Context/>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/15]
 * @see [相关类/方法]
 * @since [V1]
 */
public class PhotoActivityPresenter extends BaseCommonPresenter<IPhotoActivityView> {
    /**
     * 用来取用String
     */
    private Resources res;
    /**
     * 最大容纳量
     */
    private int maxCount;
    /**
     * 已经选中的图片数量
     */
    private int originalCount = 0;
    /**
     * 存储的标题
     */
    private String mLastTitle = "";
    /**
     * 所有文件夹及其路径
     */
    private ArrayList<MediaDirectory> directories = null;

    private boolean canDoPreview = false;

    private boolean mActionBarVisiable = true;
    /**
     * 构造函数：打开EventBus
     */
    public PhotoActivityPresenter(boolean eventOpen) {
        directories = new ArrayList<>();
    }

    public void init(Resources res, int originalCount, int maxCount, boolean canDoPreview) {
        if (mvpView == null)
            return;
        this.res = res;
        this.originalCount = originalCount;
        this.maxCount = maxCount;
        this.canDoPreview = canDoPreview;

        mvpView.setCommitBtStyle(originalCount > 0, formatCommitBt(0, maxCount - originalCount));

        mvpView.setToolBar(maxCount != 1 || canDoPreview);
    }

    private String formatCommitBt(int nowCount, int maxCount) {
        return String.format(Locale.getDefault(), res.getString(R.string.make_sure_format), nowCount, maxCount);
    }

    /**
     * 获取图片数据源
     * @return
     */
    public ArrayList<MediaDirectory> dirs() {
        return directories;
    }

    /**
     * 设置图片数据源
     * 事件接收
     * @see PhotoPickerPresenter#onEventMainThread(PhotoEvent)
     * @see PhotoDirPickerPresenter#onEventMainThread(PhotoEvent)
     * @return
     */
    public void setDirs(List<MediaDirectory> dirs) {
        directories.clear();
        directories.addAll(dirs);
        EventBus.getDefault().post(PhotoEvent.LOAD_PHOTO_DIRECTORIES_SUCCESS);
    }

    /**
     * 清除图片数据源
     * @return
     */
    public void clearDirectories() {
        if (directories == null) {
            return;
        }

        for (MediaDirectory directory : directories) {
            directory.getMediaEntityPaths().clear();
            directory.getMediaBeen().clear();
            directory.setMediaBeen(null);
        }
        directories.clear();
        directories = null;
    }

    /**
     * 根据选择图片数量更新 “预览” 和 “确定” 按钮
     * @param selectedItemCount
     */
    public void updateToolbar(int selectedItemCount,int previewCount) {
        if (mvpView == null)
            return;
        int nowCount = originalCount + selectedItemCount;
        mvpView.setCommitBtStyle(nowCount > 0, formatCommitBt(selectedItemCount, maxCount - originalCount));

        mvpView.setPreviewBtn(true, previewCount > 0);
    }

    /**
     * 进入到预览页面，并且对ActionBar和BottomToolbar做些调整
     * @param actionTitle
     */
    public void enterPreview(String actionTitle) {
        if (mvpView == null)
            return;
        mLastTitle = actionTitle;
        mvpView.changeActionBarTitle(res.getString(R.string.preview));
        mvpView.setPreviewBtn(false, true);
    }

    /**
     * 退出预览页面，同时改变ActionBar的标题和底部工具栏
     */
    public void exitPreview() {
        if (mvpView == null)
            return;
        if (!mLastTitle.isEmpty()) {
            mvpView.changeActionBarTitle(mLastTitle);
        }
        mvpView.setPreviewBtn(true, true);
    }

    /*public void enterDirView(String actionTitle) {
        mLastTitle = actionTitle;
        if(mvpView!=null)mvpView.setToolBar(false);
    }*/

    /**
     * 由最近相册界面-返回文件夹list界面
     * */
    public void enterDirView() {
        mLastTitle = mAppContext.getString(R.string.photo_string);
        mvpView.changeActionBarTitle(mLastTitle);
        if(mvpView!=null)mvpView.setToolBar(false);
    }


    public void exitDirView() {
        if (mvpView == null)
            return;
        if (!mLastTitle.isEmpty()) {
            mvpView.changeActionBarTitle(mLastTitle);
        }

        mvpView.setToolBar(maxCount != 1 || canDoPreview);
    }

    public void setDirTitle(int position) {
        if (directories.size() > position) {
            if(mvpView!=null)mvpView.changeActionBarTitle(directories.get(position).getName());
        }
    }

    /**
     * 点击了图片，控制隐藏和显示
     */
    public void clickedImage(){
        mActionBarVisiable = !mActionBarVisiable;
        if(mvpView != null){
            mvpView.setActionBarVisiable(mActionBarVisiable);
            mvpView.setToolBar(mActionBarVisiable);
        }
    }

    /**
     * 预览模式结束
     */
    public void preViewFinish(){
        mActionBarVisiable = true;
        if(mvpView != null){
            mvpView.setActionBarVisiable(mActionBarVisiable);
            mvpView.setToolBar(mActionBarVisiable);
        }
    }
}
