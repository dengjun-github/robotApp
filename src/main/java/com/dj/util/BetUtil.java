package com.dj.util;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BetUtil {

    public static int JI_DA_NUMBER = 0;

    public static int JI_XIAO_NUMBER = 0;

    //利用正则匹配.提取字符串中的汉字
    public static String getChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]*";
        Pattern p = Pattern.compile(regEx);


        Matcher matcher = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }

    //利用正则匹配,提取字符串中的数字
    public static int getNumber(String str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String trim = m.replaceAll("").trim();
        if (!trim.equals("")) return Integer.parseInt(trim);
        return 0;
    }

    /**
     * 给和值,定位大小单双计算下注金额
     *
     * @return
     */
    public static int getMoney4DingWeiDaXiao(String msg) {
        return Integer.parseInt(StringUtils.substringBefore(msg, getChinese(msg)));
    }

    /**
     * 给单码计算下注的号码
     *
     * @return
     */
    public static String getCode4DanMa(String msg) {
        return msg.substring(0, msg.indexOf(msg.indexOf(getChinese(msg))));
    }


    //判断字符串是不是以数字开头
    public static boolean isStartWithNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.charAt(0) + "");
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 给和值计算下注号码
     *
     * @return
     */
    public static int getNumber4Hezhi(String content) {
        if (content.contains("/"))
            return Integer.parseInt(StringUtils.substringAfter(content, getChinese(content)).split("/")[0]);

        else if (content.contains("."))
            return Integer.parseInt(StringUtils.substringAfter(content, getChinese(content)).split(".")[0]);

        else return Integer.parseInt(content.split(BetUtil.getChinese(content))[0]);

    }


    //解析定位龙虎合的位置
    public static List<Integer> findLocal4DingWeiLongHuHe(String content) {
        List<String> list = Arrays.asList("个", "十", "百", "千", "万");
        List<Integer> betLocal = new ArrayList<>();
        //万千虎100
        betLocal.add(list.indexOf(StringUtils.substring(content, 0,1)));
        betLocal.add(list.indexOf(StringUtils.substring(content, 1,2)));
        return betLocal;
    }


    public static void main(String[] args) {
        String msg = "和值28/18";
        int str2 = getNumber4Hezhi(msg);
//        String chinese = getChinese(msg);
//        System.out.println(chinese);
//        int i = msg.indexOf(chinese);
//        System.out.println(i);
//        String str1 = msg.substring(0, msg.indexOf(chinese));
//        String str2 = msg.substring(str1.length() + 1);
        System.out.println(str2);
    }
}
