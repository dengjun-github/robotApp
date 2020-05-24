package com.dj.pojo.request.player;

import lombok.Data;

@Data
public class Player {
    private String account;

    private String nickname;

    private String groupName;

    public Player(String account, String nickname, String groupName) {
        this.account = account;
        this.nickname = nickname;
        this.groupName = groupName;
    }
}
