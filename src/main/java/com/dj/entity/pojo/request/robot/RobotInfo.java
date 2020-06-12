package com.dj.entity.pojo.request.robot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 机器人账户密码信息、登录群号信息管理Bean
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RobotInfo {

    private Long id;
    // 机器人账户
    private String account;
    // 机器人昵称
    private String nickname;
    // 机器人密码
    private String password;
    // 类型：true=机器人 false=群
    private Boolean type;
    // 状态：true=正在使用、false=未使用
    private Boolean status;
    // 标识该登录信息是哪一个用户的。
    private Long userId;
}
