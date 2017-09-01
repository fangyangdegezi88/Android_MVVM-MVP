package com.focustech.android.photo.myalbum.myalbum.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaDirectory;

import java.util.ArrayList;
import java.util.List;

/**
 * <相册文件夹视图适配器>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/18]
 * @see [相关类/方法]
 * @since [V1]
 */
public class DirectoryListAdapter extends BaseAdapter {

    private List<MediaDirectory> directories = new ArrayList<>();
    private RequestManager glide;

    public DirectoryListAdapter(RequestManager glide, List<MediaDirectory> directories) {
        this.directories = directories;
        this.glide = glide;
    }


    @Override
    public int getCount() {
        return directories.size();
    }


    @Override
    public MediaDirectory getItem(int position) {
        return directories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return directories.get(position).hashCode();
    }


    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
            convertView = mLayoutInflater.inflate(R.layout.item_picker_directory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(directories.get(position));

        return convertView;
    }

    private class ViewHolder {

        public ImageView ivCover;
        public TextView tvName;

        public ViewHolder(View rootView) {
            ivCover = (ImageView) rootView.findViewById(R.id.directory_cover_iv);
            tvName  = (TextView)  rootView.findViewById(R.id.directory_name_tv);
        }

        public void bindData(MediaDirectory directory) {
            glide.load(directory.getCoverPath())
                    .dontAnimate()
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(ivCover);
            displayText(directory.getName(), directory.getMediaBeen().size());
        }

        private void displayText(String name, int count) {
            String alltext = name + "(" + count + ")";
            SpannableString ss = new SpannableString(alltext);
            ss.setSpan(new ForegroundColorSpan(BaseApplication.getContext().getResources().getColor(R.color.app_main_txt_color)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(BaseApplication.getContext().getResources().getColor(R.color.app_comment_color)), name.length(), alltext.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvName.setText(ss);
        }
    }
}
