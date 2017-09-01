package com.focustech.android.jni;

/**
 * JNI工具类  ----独立一个项目
 * <p>
 * 如果之后需要用到jni调用的话直接在cpp文件中进行添加
 * <p>
 * 运行后从编译后的文件中直接取得so文件使用即可
 *
 * @author liuzaibing
 * @version [版本号, 2016/11/14]
 * @see [相关类/方法]
 * @since [V1]
 */
public class SFJniUtils {

    static {
        System.loadLibrary("sf-native-lib");
    }

    public static native String stringFromJNI();

    /**
     * 进行模糊---主要处理成毛玻璃效果
     *
     * @param pix bitmap数组
     * @param w   bitmap的宽
     * @param h   bitmap的高
     * @param r   模糊半径
     * @return 处理后的数组
     */
    public static native int[] stackBlur(int[] pix, int w, int h, int r);

    /**
     * 加密密码
     *
     * @param psd 原始密码
     * @return 加密过后的密码
     */
    public static native String encryptPsd(String psd);

    /**
     * 解密密码
     *
     * @param psd 已加密的密码
     * @return 原始密码
     */
    public static native String decryptPsd(String psd);
}
