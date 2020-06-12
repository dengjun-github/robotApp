package com.dj.entity.pojo.request.robot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.Bot;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RobotInfoByBot {
    private String id;
    private String nickname;
    private String account;
    private String password;


    public static RobotInfoByBot getInstance(Bot bot) {
        return RobotInfoByBot.builder().account(bot.getId()+"").nickname(bot.getNick()).build();
    }

}
