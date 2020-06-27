package com.dj.robot;

import com.dj.Exception.ClientErrorException;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageUtils;

import java.io.File;
import java.util.function.Supplier;

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

    /**
     * 需要At的对象,并发送消息
     * @param message
     * @param member
     */
    public static void sendMessage(String message, Member member) {
        ROBOT.getGroup(GROUPID).sendMessage(MessageUtils.newChain(new At(member)).plus("\r").plus(message));
    }


    public static void sendImg(File file){
        if (file.exists()) {
            final Image image = ROBOT.getGroup(GROUPID).uploadImage(file);
            // 上传一个图片并得到 Image 类型的 Message

            final String imageId = image.getImageId(); // 可以拿到 ID

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

}
