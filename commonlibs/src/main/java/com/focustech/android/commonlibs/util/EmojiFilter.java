package com.focustech.android.commonlibs.util;

/**
 * 过滤emoji 表情
 *
 * Created by liuzaibing on 2015/10/14.
 */
public class EmojiFilter {

    /**
     * 判断是否是emoji表情
     * @param codePoint
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && ((codePoint < 0xA9) || (codePoint > 0xA9)) && ((codePoint < 0xAE) || (codePoint > 0xAE)) && ((codePoint < 0x20E3) || (codePoint > 0x20E3))
                && ((codePoint < 0x2122) || (codePoint > 0x2122)) && ((codePoint < 0x2196) || (codePoint > 0x2199)) && ((codePoint < 0x23E9) || (codePoint > 0x23EA))
                && ((codePoint < 0x25B6) || (codePoint > 0x25B6)) && ((codePoint < 0x25C0) || (codePoint > 0x25C0)) && ((codePoint < 0x2600) || (codePoint > 0x2601))
                && ((codePoint < 0x2614) || (codePoint > 0x2615)) && ((codePoint < 0x263A) || (codePoint > 0x263A)) && ((codePoint < 0x2648) || (codePoint > 0x2653))
                && ((codePoint < 0x2660) || (codePoint > 0x2660)) && ((codePoint < 0x2663) || (codePoint > 0x2663)) && ((codePoint < 0x2665) || (codePoint > 0x2666))
                && ((codePoint < 0x2668) || (codePoint > 0x2668)) && ((codePoint < 0x267F) || (codePoint > 0x267F)) && ((codePoint < 0x26A0) || (codePoint > 0x26A1))
                && ((codePoint < 0x26BD) || (codePoint > 0x26BE)) && ((codePoint < 0x26C4) || (codePoint > 0x26C4)) && ((codePoint < 0x26CE) || (codePoint > 0x26CE))
                && ((codePoint < 0x26EA) || (codePoint > 0x26EA)) && ((codePoint < 0x26F2) || (codePoint > 0x26F3)) && ((codePoint < 0x26F5) || (codePoint > 0x26F5))
                && ((codePoint < 0x26FA) || (codePoint > 0x26FA)) && ((codePoint < 0x26FD) || (codePoint > 0x26FD)) && ((codePoint < 0x2702) || (codePoint > 0x2702))
                && ((codePoint < 0x2708) || (codePoint > 0x2708)) && ((codePoint < 0x270A) || (codePoint > 0x270A)) && ((codePoint < 0x2728) || (codePoint > 0x2728))
                && ((codePoint < 0x2733) || (codePoint > 0x2734)) && ((codePoint < 0x274C) || (codePoint > 0x274C)) && ((codePoint < 0x2753) || (codePoint > 0x2755))
                && ((codePoint < 0x2757) || (codePoint > 0x2757)) && ((codePoint < 0x2764) || (codePoint > 0x2764)) && ((codePoint < 0x27A1) || (codePoint > 0x27A1))
                && ((codePoint < 0x27BF) || (codePoint > 0x27BF)) && ((codePoint < 0x2B05) || (codePoint > 0x2B07)) && ((codePoint < 0x2B1C) || (codePoint > 0x2B1C))
                && ((codePoint < 0x2B50) || (codePoint > 0x2B50)) && ((codePoint < 0x2B55) || (codePoint > 0x2B55)) && ((codePoint < 0x303D) || (codePoint > 0x303D))
                && ((codePoint < 0x3297) || (codePoint > 0x3297)) && ((codePoint < 0x3299) || (codePoint > 0x3299)) && (codePoint <= 0xD7FF) && (codePoint != 0x270C))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {

        StringBuilder buf = new StringBuilder();
        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (!isEmojiCharacter(codePoint)) {// 如果不包含 则将字符append
                buf.append(codePoint);
            } else {

            }
        }

        return buf.toString();
    }
}
