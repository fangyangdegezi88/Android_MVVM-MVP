package com.focustech.android.photo.myalbum.myalbum.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.focustech.android.photo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <图片预览适配>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/19]
 * @see [相关类/方法]
 * @since [V1]
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private List<String> paths = new ArrayList<>();
    private RequestManager mGlide;
    private View.OnClickListener mListener;

    public PhotoPagerAdapter(RequestManager glide, List<String> paths, View.OnClickListener listener) {
        this.paths = paths;
        this.mGlide = glide;
        this.mListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Context context = container.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_picker_photo_pager, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pager);

        final String path = paths.get(position);
        final Uri uri;
        if (path.startsWith("http")) {
            uri = Uri.parse(path);
        } else {
            uri = Uri.fromFile(new File(path));
        }
        mGlide.load(uri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
//                .thumbnail(0.1f)
//                .dontAnimate()
//                .dontTransform()
                .into(imageView);

        if (mListener != null) {
            imageView.setOnClickListener(mListener);
        }

        container.addView(itemView);

        return itemView;
    }


    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        Glide.clear((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}