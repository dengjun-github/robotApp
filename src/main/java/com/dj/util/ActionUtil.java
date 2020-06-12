package com.dj.util;

public class ActionUtil {

    //针对投注的Key设置正则
    public static String getRegex4Format(String key, String format) {
        GlobalConstant.Action action = GlobalConstant.Action.findActionByKey(key);
        String regex = "";
        switch (action) {
            case BETS_DA:
            case BETS_DAN:
            case BETS_XIAO:
            case BETS_SHUANG:
            case UPPER_SCORE:
                regex = "^(" + format + "[1-9]\\d*)|([1-9]\\d*" + format + ")$";
                break;
            case LOWER_SCORE:
                regex = "^(" + format + "[1-9]\\d*)|([1-9]\\d*" + format + ")|(" + format + ")$";
                break;
            case QUERY_SCORE:
                regex = "^" + format + "$";
                break;

        }
        return regex;
    }


}
