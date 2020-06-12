package com.dj.entity.pojo.request.player;

import lombok.Data;

@Data
public class PlayerBo {
    private String account;

    private String nickname;

    private String groupName;

    public PlayerBo(String account, String nickname, String groupName) {
        this.account = account;
        this.nickname = nickname;
        this.groupName = groupName;
    }
}
