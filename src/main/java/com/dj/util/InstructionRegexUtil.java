package com.dj.util;

import com.dj.Exception.ClientErrorException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionRegexUtil {


    public static final Map<String, String> INSTRUCT_FORMAT_MAP = new HashMap<>();


    /**
     * 根据解析后的用户信息找到key
     *
     * @param content
     * @return
     * @throws ClientErrorException
     */
    public static Map<String,String> findKeyByMsg(String content) throws ClientErrorException {
        final Map<String,String> keyMap = new HashMap<>();
        //首先解析用户的信息
        parseMsg(content).forEach(msg -> {
            SettingUtils.REGEX_MAP.forEach((k, regexes) -> {
                regexes.stream()
                        .map(Pattern::compile)
                        .map(pattern -> pattern.matcher(msg))
                        .map(Matcher::matches)
                        .forEach(isFind -> {
                            if (isFind) {
                                keyMap.put(k,msg);
                                return;
                            }
                        });
            });
        });


        //若没有找到Key,则抛出异常
        if (keyMap.isEmpty()) throw new ClientErrorException("格式不正确,没有找到Key");
        return keyMap;
    }

    /**
     * 根据用户发出的信息解析出正确的格式
     *
     * @param msg
     * @return 解析后的语句
     */
    public static List<String> parseMsg(String msg) {
        final List<String> contents = new ArrayList<>();
        if (msg.contains("各")) {
            //110各100
            //取"各"前面的字符
            String betGames = StringUtils.substringBefore(msg, "各");
            String money = StringUtils.substringAfter(msg, "各");
            if (msg.contains(",")) {
                Arrays.asList(betGames.split(",")).forEach(betGame -> {
                    contents.add(betGame + money);
                });
            }
        } else if (msg.contains(" ")) contents.addAll(Arrays.asList(msg.split(" ")));
        else if (msg.contains(",")) contents.addAll(Arrays.asList(msg.split(",")));
        else if (msg.contains("，")) contents.addAll(Arrays.asList(msg.split("，")));
        else contents.add(msg);


        return contents;
    }
}
