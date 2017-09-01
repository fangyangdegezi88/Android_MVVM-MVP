package com.focusteach.android.record.util;

import android.util.Log;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * 录音权限判断类
 * 注意：使用该类时，务必考虑，收集数据的频率在100ms/次，不然就不奏效了
 * @author : zhangzeyu
 * @version : [V1, 2016/7/13]
 */
public class AudioPermissionUtil {

    private final static String TAG = "AudioPermission";
    private boolean check = true;
    private int count = 0;                    // 检查文件长度的次数
    private double vocAuthority[] = new double[10];

    private boolean checkFL = true;         // check length of the file
    private int timesCFL = 0;               // times of checking file length
    private long mLastFileLength = 0;
    /**
     * 收集音频振幅判断录音权限
     * 原理：通过1s内10次音频振幅是否相同
     * @param amp true正常 false无权限
     */
    public boolean compareAmplitude(double amp) {
        if (check) {
            if (count >= 10) {
                int zeroAmpCount = 0;
                Set<Double> set = new HashSet<Double>();
                for (int i = 0; i < count; i++) {
                    /**
                     * the set is not modified if it
                     * already contains the object.
                     */
                    Log.v("amplitude", "=" + vocAuthority[i]);
                    set.add(vocAuthority[i]);
                    if (vocAuthority[i] == 0) {
                        zeroAmpCount ++;
                    }
                }
                if (set.size() == 1) {
                    Log.v(TAG, "NO_PERMISSION:same amplitude");
                    count = 0;
                    vocAuthority = null;
                    vocAuthority = new double[10];
                    return false;
                } else if (zeroAmpCount >= 9) {
                    Log.v(TAG, "NO_PERMISSION:90% amplitude=0.0");
                    count = 0;
                    vocAuthority = null;
                    vocAuthority = new double[10];
                    return false;
                } else {
                    check = false;
                }
            } else {
                vocAuthority[count] = amp;
                count++;
            }
        }

        return true;
    }

    public boolean fileExist(File file) {
        if (!file.exists()) {
            Log.v(TAG, "file.exists()=" + file.exists());
        }
        return file.exists();
    }

    /**
     * 权限检查时，不能因为最开始几次的file.length = 0就认为没权限（比如华为手机）
     * 所以干脆等个10次之后，再去判断file.length
     * @param file
     * @return
     */
    public boolean fileLengthAlwaysZero(File file) {
        if (checkFL) {
            if (timesCFL >= 10) {
                if (mLastFileLength == 0) {
                    Log.v(TAG, "NO_PERMISSION: file Length keep zero.");
                    checkFL = false;
                    timesCFL = 0;
                    return true;
                } else {
                    timesCFL = 0;
                    checkFL = false;
                    mLastFileLength = 0;
                    return false;
                }

            } else {
                Log.d(TAG, "file.length()=" + file.length());
                mLastFileLength = file.length();
                timesCFL++;
                return false;
            }
        }

        return file.length() == 0;
    }

    public void reset() {
        check = true;
        count = 0;
        vocAuthority = null;
        vocAuthority = new double[10];

        checkFL = true;
        timesCFL = 0;
        mLastFileLength = 0;

    }
}
