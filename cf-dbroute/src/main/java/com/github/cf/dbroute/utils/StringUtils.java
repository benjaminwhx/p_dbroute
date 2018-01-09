package com.github.cf.dbroute.utils;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-08
 * Time: 8:07 pm
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String substringBetween(String str, String open, String close) {
        if(str != null && open != null && close != null) {
            int start = str.indexOf(open);
            if(start != -1) {
                int end = str.indexOf(close, start + open.length());
                if(end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }
            return null;
        } else {
            return null;
        }
    }
}
