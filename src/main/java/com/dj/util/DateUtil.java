package com.dj.util;

import com.dj.Exception.ClientErrorException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final SimpleDateFormat yMdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat Hm = new SimpleDateFormat("HH:mm");
    public static long dateToStamp(String source) {
        Date date = null;
        try {
            date = yMdHms.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String toHm(Date date){
        return Hm.format(date);
    }
    public static String toHm(String date) {
        Date d = null;
        try {
            d = yMdHms.parse(date);
        } catch (ParseException e) {
            throw new ClientErrorException("时间解析异常:" + date);
        }
        return toHm(d);
    }

    public static void main(String[] args) throws ParseException {
        String dateStr = "2020-12-12 23:59:59";
        System.out.println(toHm(dateStr));
    }
}
