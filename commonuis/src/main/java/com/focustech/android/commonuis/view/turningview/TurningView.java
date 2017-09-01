package com.focustech.android.commonuis.view.turningview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.android.commonuis.R;


/**
 * @author : yincaiyu
 * @version : [v1.0.0, 2016/10/24]
 * @see [相关类/方法]
 * @since [V1]
 * <p>
 * 提交中、设置中的 菊花等待
 */
public class TurningView extends RelativeLayout {
    private TextView mProgressTv;
    private ImageView mProgressIv;

    public TurningView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_common_turn_message, this, true);
        ((Activity) context).addContentView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mProgressIv = (ImageView) findViewById(R.id.iv_progress);
        mProgressTv = (TextView) findViewById(R.id.tv_progress);
        mProgressIv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_roate));
    }

    public void setText(String text) {
        mProgressTv.setText(text);
    }

    public void setText(int id) {
        mProgressTv.setText(id);
    }


}
