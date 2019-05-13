package com.richie.utils.common;

import android.util.Log;

/**
 * @author Richie on 2019.05.13
 */
public final class StringUtils {
    private static final String TAG = "StringUtils";

    private StringUtils() {
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(final CharSequence a, final CharSequence b) {
        if (a == b) {
            return true;
        }
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(final CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 字符串转换成整形
     *
     * @param s
     * @param defValue
     * @return
     */
    public static int parseToInt(final String s, final int defValue) {
        if (isEmpty(s)) {
            return defValue;
        } else {
            int value = defValue;
            try {
                value = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                Log.e(TAG, "parseToInt: ", e);
            }
            return value;
        }
    }

    /**
     * 字符串转换成 long
     *
     * @param s
     * @param defValue
     * @return
     */
    public static long parseToLong(final String s, final long defValue) {
        if (isEmpty(s)) {
            return defValue;
        } else {
            long value = defValue;
            try {
                value = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                Log.e(TAG, "parseToLong: ", e);
            }
            return value;
        }
    }

    /**
     * 字符串转换成浮点型
     *
     * @param s
     * @param defValue
     * @return
     */
    public static float parseToFloat(final String s, final float defValue) {
        if (isEmpty(s)) {
            return defValue;
        } else {
            float value = defValue;
            try {
                value = Float.parseFloat(s);
            } catch (NumberFormatException e) {
                Log.e(TAG, "parseToFloat: ", e);
            }
            return value;
        }
    }

    /**
     * 字符串转换成浮点型
     *
     * @param s
     * @param defValue
     * @return
     */
    public static double parseToDouble(final String s, final double defValue) {
        if (isEmpty(s)) {
            return defValue;
        } else {
            double value = defValue;
            try {
                value = Double.parseDouble(s);
            } catch (NumberFormatException e) {
                Log.w(TAG, "parseToDouble: ", e);
            }
            return value;
        }
    }

}
