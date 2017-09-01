package com.focusteach.android.record.view.voice;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.focusteach.android.record.R;
import com.focusteach.android.record.bean.ReplyFile;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonuis.view.ToastUtil;

import java.io.File;



/**
 * <语音播放>支持删除</语音播放>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/27]
 * @see [相关类/方法]
 * @since [V1]
 */
public class VoicePlayLayout extends LinearLayout implements View.OnClickListener {
    /**
     * 绑定的路径
     */
    private ReplyFile bindFile;
    /**
     * 语音时长
     */
    private TextView tvLength;
    /**
     * 动画
     */
    private AnimationDrawable anim = null;

    private MyOnClickListener myOnClickListener;

    private final Handler mHandler = new Handler();

    public void setMyOnClickListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
    }

    public VoicePlayLayout(Context context) {
        super(context);
        init(context);
    }

    public VoicePlayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoicePlayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_assignment_reply_voice_player, this, true);
        ImageView imageView = (ImageView) findViewById(R.id.assignment_reply_voice_anim_iv);
        LinearLayout playVoice = (LinearLayout) findViewById(R.id.assignment_reply_play_voice_ll);
        ImageButton delete = (ImageButton) findViewById(R.id.assignment_reply_delete_voice_ib);
        tvLength = (TextView) findViewById(R.id.assignment_reply_voice_duration_tv);

        playVoice.setOnClickListener(this);
        imageView.setOnClickListener(this);
        delete.setOnClickListener(this);

        anim = (AnimationDrawable) imageView.getDrawable();
        stopAnim();
    }

    @Override
    public void onClick(View v) {


        int i = v.getId();
        if (i == R.id.assignment_reply_play_voice_ll || i == R.id.assignment_reply_voice_anim_iv) {
            if (bindFile == null)
                return;

            File target = new File(bindFile.getFilePath());
            if (!target.exists()) {
                ToastUtil.showFocusToast(getContext(), R.string.voice_file_not_exist);
                return;
            }

            synchronized (this) {
                if (myOnClickListener != null)
                    myOnClickListener.onPlayOrStop(bindFile.getFilePath());

                if (anim.isRunning()) {
                    stopAnim();
                } else {
//                    anim.start();
                    startAnim();
                }
            }


        } else if (i == R.id.assignment_reply_delete_voice_ib) {
            stopAnim();
            if (myOnClickListener != null && bindFile != null) {
                myOnClickListener.onDeleteAudio(bindFile);
            }

        }
    }

    /**
     * @return true表示已经清除数据或者第一次初始化
     */
    public boolean allowNewVoice() {
        int visibility = getVisibility();
        return visibility != VISIBLE && bindFile == null;

    }

    public boolean show(ReplyFile audio) {
        bindFile = audio;
        setVoiceLength(audio.getSecond());
        show();
        return true;
    }

    private boolean show() {
        if (bindFile != null && GeneralUtils.isNotNullOrEmpty(bindFile.getFilePath())) {
            setVisibility(VISIBLE);
            return true;
        }

        return false;
    }

    private void startAnim() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (anim != null) {
                    anim.start();
                }
            }
        }, 50);
    }

    public void stopAnim() {
        anim.stop();
        anim.selectDrawable(0);
        mHandler.removeCallbacksAndMessages(null);
    }


    /**
     * <重置并隐藏>
     */
    public void resetAndHide() {
        bindFile = null;
        tvLength.setText("");
        setVisibility(GONE);
    }

    /**
     * <设置语音时长>
     *
     * @param second 秒
     */
    void setVoiceLength(int second) {
        StringBuilder sb = new StringBuilder();
        if (second <= 60) { // 58"
            sb.append(second);
            sb.append("\"");
        } else {
            sb.append(second / 60);
            sb.append("\'");
            sb.append(second % 60);
            sb.append("\"");
        }
        tvLength.setText(sb.toString());
    }

    /**
     * <获得语音文件>
     *
     * @return ReplyFile
     */
    public ReplyFile getAudioFile() {
        return bindFile;
    }

    public interface MyOnClickListener {
        void onPlayOrStop(String bindPath);

        void onDeleteAudio(ReplyFile bindPath);
    }
}
