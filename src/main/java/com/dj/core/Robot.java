package com.dj.core;

import com.dj.Exception.ClientErrorException;
import com.dj.core.event.RobotEvent;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.network.LoginFailedException;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.SystemDeviceInfoKt;

import java.io.File;

public class Robot {

    private int qq;

    private String password;

    public Robot(int qq, String password) {
        this.qq = qq;
        this.password = password;
    }

    public Bot Login() throws ClientErrorException {
        // 使用自定义的配置
        final net.mamoe.mirai.Bot bot = BotFactoryJvm.newBot(qq, password, new BotConfiguration() {
            {
                setDeviceInfo(context ->
                        SystemDeviceInfoKt.loadAsDeviceInfo(new File("deviceInfo.json"), context)
                );
            }
        });
        try {
            bot.login();

        } catch (LoginFailedException e) {
            throw new ClientErrorException(e.getMessage());
        }

        return bot;
    }



}
