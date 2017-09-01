package com.focustech.android.photo.myalbum.util.mediastore;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <英文名和中文名对应>
 *
 * pathPrefix绝对路径
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/21]
 * @see [相关类/方法]
 * @since [V1]
 */
public class NameMap implements Parcelable {
    String parentDir = null;
    String chinese = null;

    /**
     * @param parentDir  最好传sd卡/../xxx/这样以“/”结尾的dir不然会有问题(匹配时用的是startWith)
     * @param chinese
     */
    public NameMap(String parentDir, String chinese) {
        this.parentDir = parentDir;
        this.chinese = chinese;
    }

    protected NameMap(Parcel in) {
        parentDir = in.readString();
        chinese = in.readString();
    }

    public static final Creator<NameMap> CREATOR = new Creator<NameMap>() {
        @Override
        public NameMap createFromParcel(Parcel in) {
            return new NameMap(in);
        }

        @Override
        public NameMap[] newArray(int size) {
            return new NameMap[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parentDir);
        dest.writeString(chinese);
    }
}
