package com.dj.robot;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.SystemDeviceInfoKt;

import java.io.File;

/**
 * 机器人实例
 */
public class MiraiBot {

    public static Bot getInstance(long qq, String password) {
        return BotFactoryJvm.newBot(qq, password, new BotConfiguration() {
            {
                setDeviceInfo(context ->
                        SystemDeviceInfoKt.loadAsDeviceInfo(new File("deviceInfo.json"), context)
                );
            }
        });
    }


}
