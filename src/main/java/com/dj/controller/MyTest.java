package com.dj.controller;

import com.dj.annotation.MyService;
import com.dj.annotation.RequestMessage;

import java.util.Map;

@MyService
public class MyTest {
    @RequestMessage(path = "大")
    public Object hello(Map<String,Object> data){
        if(data != null){
            throw new RuntimeException("Hello Exception");
        }
        return data;
    }
}
