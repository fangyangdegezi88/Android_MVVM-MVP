package com.focusteach.android.record.recorder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * <录音启动辅助>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/28]
 * @see [相关类/方法]
 * @since [V1]
 */
public class OpenRecorder {

    public static int REQUEST_CODE = "OpenRecorder".hashCode();

    public static final String RESULT_AUDIO_LENGTH_SEC = "audio_length_sec";
    /**
     * 传入的Extra的Key，value使用int，用来设置Dialog的高度，单位dp
     */
    public final static String DIALOG_HEIGHT_PX = "dialog_height_px";
    /**
     * 传入的Extra的Key，value使用String，用来设置语音输出文件夹路径，确保路径正确！
     */
    public final static String OUTPUT_FILE_DIR = "output_file_dir_string";
    /**
     * 传入的Extra的Key，value使用int，用来设置最长录音时间，<单位>秒<单位/>
     */
    public final static String AUDIO_MAX_LENGTH = "audio_max_length_int";

    public static OpenRecorderBuilder builder() {
        return new OpenRecorderBuilder();
    }

    public static class OpenRecorderBuilder {
        private Bundle mOptionsBundle;
        private Intent mIntent;

        public OpenRecorderBuilder() {
            mOptionsBundle = new Bundle();
            mIntent = new Intent();
        }

        /**
         * <设置窗口高度>
         */
        public OpenRecorderBuilder height(int px) {
            mOptionsBundle.putInt(DIALOG_HEIGHT_PX, px);
            return this;
        }

        /**
         * <设置输出文件夹>
         */
        public OpenRecorderBuilder outputDir(String path) {
            mOptionsBundle.putString(OUTPUT_FILE_DIR, path);
            return this;
        }

        /**
         * <设置最长录音时间>
         */
        public OpenRecorderBuilder maxDuration(int second) {
            mOptionsBundle.putInt(AUDIO_MAX_LENGTH, second);
            return this;
        }

        public void start(@NonNull Activity from, int requestCode) {
            from.startActivityForResult(getIntent(from), requestCode);
        }

        public void start(@NonNull Activity from) {
            start(from, REQUEST_CODE);
        }

        public void start(@NonNull Context context, @NonNull Fragment from, int requestCode) {
            from.startActivityForResult(getIntent(context), requestCode);
        }

        public void start(@NonNull Context context, @NonNull Fragment from) {
            start(context, from, REQUEST_CODE);
        }

        /**
         * Get Intent to start {@link RecorderActivity}
         *
         * @return Intent for {@link RecorderActivity}
         */
        public Intent getIntent(@NonNull Context context) {
            mIntent.setClass(context, RecorderActivity.class);
            mIntent.putExtras(mOptionsBundle);
            return mIntent;
        }
    }
}
