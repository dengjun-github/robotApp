package com.dj.util;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsonUtil {
    private static final Log log = LogFactory.getLog(JsonUtil.class);

    /**
     * json字符串转换为pojo类
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static  <T> T string2Obj(String str, Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            Object parse = JSONObject.parse(str);
            return (T)JSONObject.toJavaObject((JSON) parse, clazz);
        } catch (Exception e) {
            log.error("Parse String to Object error : "+ e.getMessage() );
            return null;
        }
    }

}

