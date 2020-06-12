package com.dj.robot;

import com.alibaba.fastjson.JSONObject;
import com.dj.Exception.ClientErrorException;
import com.dj.entity.pojo.response.BetResponse;
import com.dj.net.VertxWebClient.RestFul;
import com.dj.util.DozerUtils;
import com.dj.util.HttpResult;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Supplier;

import static com.dj.net.VertxWebClient.httpRequest;
import static com.dj.util.GlobalConstant.*;

/**
 * 群管理机器人
 */
public class GroupRobot {

    public static Bot ROBOT;

    public static long GROUPID;

    public static void set(Bot bot, long groupId){
        ROBOT = bot;
        GROUPID = groupId;
    }

    /**
     * 指定群发送消息
     * @param message
     * @param is_atAll
     */
    public static void sendMessage(String message ,boolean is_atAll) {
        if (is_atAll) {
            ROBOT.getGroup(GROUPID).sendMessage(MessageUtils.newChain(AtAll.INSTANCE).plus("\r").plus(message));
        }else {
            ROBOT.getGroup(GROUPID).sendMessage(MessageUtils.newChain(message));
        }
    }

    public static void sendImg(File file){
        if (file.exists()) {
            final Image image = ROBOT.getGroup(GROUPID).uploadImage(file);
            // 上传一个图片并得到 Image 类型的 Message

            final String imageId = image.getImageId(); // 可以拿到 ID
//            final Image fromId = MessageUtils.newImage(imageId); // ID 转换得到 Image

            ROBOT.getGroup(GROUPID).sendMessage(MessageUtils.newImage(imageId)); // 发送图片
        }

    }

    /**
     * 发送图片
     * @param supplier
     * @return
     */
    public static File sendImg(Supplier<File> supplier) throws ClientErrorException{
        File file = null;
        try{
            file = supplier.get();
            sendImg(file);
        }catch (Exception e ) {
            throw new ClientErrorException("发送["+file.getName()+"]图片失败");
        }
        return file;
    }


    /**
     * 开启群监听
     */
    public static void listen(){
        Events.registerEvents(ROBOT,new GroupListener());
    }

    //机器人请求服务器,并返回数据,将数据打印到群中,监听事件
    public static void player2Server(GroupMessageEvent event, RestFul method, RequstUri uri, Object parm) {
        httpRequest(PORT,HOST,uri.getUri(),(JSONObject)JSONObject.toJSON(parm),USER_TOKEN,method,
                res -> {
                    if (res.succeeded()) {
                        HttpResult result = res.result().body();
                        if (result.isIsok()) {
                            LinkedHashMap data = (LinkedHashMap) result.getData();
                            BetResponse betResponse = DozerUtils.mapper.map(data, BetResponse.class);
                            event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                    .plus("\r")
                                    .plus(betResponse.getTemplate()));
                        } else {
                            event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                    .plus("\r")
                                    .plus(result.getMessage()));
                        }

                    } else {
                        event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                .plus("\r")
                                .plus("连接服务器异常,请稍后再试"));
                    }
                });
    }

    public static void robotRequest2Server( GroupMessageEvent event,RestFul method, RequstUri uri, Object parm) {
        httpRequest(PORT,HOST,uri.getUri(),(JSONObject)JSONObject.toJSON(parm),USER_TOKEN,method,
                res -> {
                    if (res.succeeded()) {
                        HttpResult result = res.result().body();
                        if (result.isIsok()) {
                            event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                    .plus("\r")
                                    .plus(result.getData().toString()));
                        } else {
                            event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                    .plus("\r")
                                    .plus(result.getMessage()));
                        }

                    } else {
                        event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                .plus("\r")
                                .plus("连接服务器异常,请稍后再试"));
                    }
                });
    }


    //机器人请求服务器,并返回数据,将数据打印到群中
    public static void robotRequest2Server(RestFul method, String uri, Object parm,String exMessage) {
        httpRequest(PORT,HOST,uri,(JSONObject)JSONObject.toJSON(parm),USER_TOKEN,method,
                res -> {
                    if (res.succeeded()) {
                        HttpResult result = res.result().body();
                        if (result.isIsok()) {
                            List<String> message = (ArrayList<String>)result.getData();
                            message.forEach(str -> {
                                sendMessage(str,false);
                            });
                        } else {
                            sendMessage(result.getMessage(),false);
                            throw new ClientErrorException(exMessage + ":" + result.getMessage());
                        }

                    } else {
                        sendMessage("连接服务器异常,请重试或联系管理员",false);
                        throw new ClientErrorException(exMessage + ": 连接服务器异常,请重试或联系管理员");
                    }
                });
    }
}
