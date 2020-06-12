package com.dj.util;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dj.entity.pojo.response.Root;
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

    public static void main(String[] args) {
        String json = "{\"userId\": 1263367239957032960, \"account\": \"dj123\", \"instructs\": [{\"key\": \"UpperScore\", \"title\": \"上分\", \"value\": \"上\", \"status\": true}, {\"key\": \"lowerScore\", \"title\": \"下分\", \"value\": \"下\", \"status\": true}], \"returnTemplates\": [{\"key\": \"UpperOrLowerScoreSuccess\", \"title\": \"上下分成功\", \"value\": \"{昵称}{上下分}:[{分数}]成功!\", \"status\": true}, {\"key\": \"BalanceNotEnough\", \"title\": \"余额不足\", \"value\": \"余额不足\", \"status\": true}]}";
        Root root = string2Obj(json,Root.class);
        System.out.println(root.toString());
    }
}

