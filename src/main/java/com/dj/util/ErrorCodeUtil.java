package com.dj.util;

public class ErrorCodeUtil {

    public static String getMsgByErroCode(int code){
        String msg = "";


        switch (code){
            case 49:
                msg =  "请求成功";
                break;
            case 50:
                msg =  "请求错误";
                break;
            case 51:
                msg =  "服务器繁忙";
                break;
            case 52:
                msg =  "服务器错误";
                break;
            case 53:
                msg =  "用户名已存在";
                break;
            case 61:
                msg =  "用户名不存在";
                break;
            case 62:
                msg =  "用户密码错误";
                break;
            case 63:
                msg =  "卡不存在";
                break;
            case 64:
                msg =  "卡已激活";
                break;
            case 65:
                msg = "用户时间到期";
                break;
            case 66:
                msg = "订单不存在";
                break;
        }

        return msg;
    }

}
