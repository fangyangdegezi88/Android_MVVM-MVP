package com.focustech.android.commonlibs.util;

import android.content.Context;

import com.focustech.android.commonlibs.capability.log.L;

import java.lang.reflect.Field;

/**
 * 获取主module中的BuildConfig
 *
 * @author liuzaibing
 * @version [版本号, 2017/6/14]
 * @see [相关类/方法]
 * @since [V1]
 */
public class BuildConfigUtil {

    private static L log = new L(BuildConfigUtil.class.getSimpleName());

    public static Object getBuildConfigValue(Context context, String fieldName) {
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            log.i(clazz.getName());
            Field field = clazz.getDeclaredField(fieldName);
            log.i((String) field.get(null));
            return field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            log.e("getBuildConfigValue,filedName:" + fieldName + "\n error" + e.toString());
        }
        return null;
    }
}
