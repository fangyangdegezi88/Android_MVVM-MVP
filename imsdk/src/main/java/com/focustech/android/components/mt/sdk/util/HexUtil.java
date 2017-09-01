package com.focustech.android.components.mt.sdk.util;

/**
 * Hex 工具
 *
 * @author zhangxu
 */
public class HexUtil {
    /**
     * Returns a hexadecimal representation of the given byte array.
     *
     * @param bytes the array to output to an hex string
     * @return the hex representation as a string
     */
    public static String asHex(byte[] bytes) {
        return asHex(bytes, null);
    }

    /**
     * Returns a hexadecimal representation of the given byte array.
     *
     * @param bytes     the array to output to an hex string
     * @param separator the separator to use between each byte in the output
     *                  string. If null no char is inserted between each byte value.
     * @return the hex representation as a string
     */
    public static String asHex(byte[] bytes, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String code = Integer.toHexString(bytes[i] & 0xFF);
            if ((bytes[i] & 0xFF) < 16) {
                sb.append('0');
            }

            sb.append(code);

            if (separator != null && i < bytes.length - 1) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }
}
