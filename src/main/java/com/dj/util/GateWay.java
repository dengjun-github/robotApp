package com.dj.util;


import com.dj.annotation.MyService;
import com.dj.annotation.RequestMessage;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialize.pojo.BetsContent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class GateWay {

    private static final Logger logger = LoggerFactory.getLogger(GateWay.class);

    public Object gateWay(String className, String methodName, BetsContent content) throws InvocationTargetException{
        Object obj = new Object();
        Reflections reflections = new Reflections("com.dj.handler.openHandler");
        //获取带Service注解的类
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(MyService.class);
        for (Class clazz : typesAnnotatedWith
        ) {

            if(!StringUtils.isBlank(className)){
                String simpleName = clazz.getSimpleName();
                if(!StringUtils.equals(simpleName,className)){
                    continue;
                }
            }

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods
            ) {
                //判断带自定义注解MyAnnontation的method
                if (method.isAnnotationPresent(RequestMessage.class)) {
                    RequestMessage annotation = method.getAnnotation(RequestMessage.class);
                    //根据入参WayCode比较method注解上的WayCode,两者值相同才执行该method
                    if (null != annotation.path() && annotation.path().equals(methodName)) {
                        logger.info("Path = " + annotation.path());
                        try{
                            obj = method.invoke(clazz.newInstance(), content);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return obj;
    }
}