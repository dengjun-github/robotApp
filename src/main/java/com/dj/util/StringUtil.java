package com.dj.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {


    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     *
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }

    /**
     * 判断字符串是否有重复字符
     * @param s
     * @return
     */
    public static boolean isunique(String s) {
        int index1,index2;
        char[] chars = s.toCharArray();
        for(char c:chars) {
            index1 = s.indexOf(c);
            index2 = s.lastIndexOf(c);
            if(index1 != index2) return false;
        }
        return true;
    }

}
