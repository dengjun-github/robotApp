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
            case BETS_HU:
            case BETS_LONG:
            case BETS_BAOZI:
            case BETS_SAN_ZUHE:
            case BETS_JIDA:
            case BETS_SHUNZI:
            case BETS_BANSHUN:
            case BETS_XIAODAN:
            case BETS_DASHUANG:
            case BETS_HEZHI:
            case BETS_JIXIAO:
            case BETS_ZALIU:
            case BETS_DUIZI:
            case BETS_He_OF_DAXIAO:
            case BETS_LIANG_ZUHE:
            case BETS_XIAOSHUANG:
            case BETS_HE_OF_LONGHU:
            case BETS_DADAN:
            case BETS_DINGWEI_HE:
            case BETS_DINGWEI_HU:
            case BETS_DINGWEI_LONG:
            case UPPER_SCORE:
                regex = "^(" + format + "[1-9]\\d*)|([1-9]\\d*" + format + ")$";
                break;
            case LOWER_SCORE:
                regex = "^(" + format + "[1-9]\\d*)|([1-9]\\d*" + format + ")|(" + format + ")$";
                break;
            case QUERY_SCORE:
            case CANCEL_BET:
            case QUERY_BILL:
            case RETURN_WATER:
            case QUERY_HiSTORY:
                regex = "^" + format + "$";
                break;
            case BETS_DINGWEI_CODE:
                regex = "^[1-5]{1,5}" + format + "[0-9]{1,10}" + format + "[1-9]\\d*$";//   1/0/100,12345/0123456789/100
                break;
            case BETS_DANMA:
                regex = "^[1-5]" + format + "[1-9]\\d*$"; //1/100,2/100
                break;
            case BETS_LIANMA:
                regex = "^[1-5]{1,5}" + format + "[1-9]\\d*$";
                break;
            case BETS_DINGWEI_DAXIAO:
                regex = "^([1-5]" + format + "[1-9]\\d*)|([个十百千万]" + format + "[1-9]\\d*)$";
                break;
        }
        return regex;
    }



}
