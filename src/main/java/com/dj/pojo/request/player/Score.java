package com.dj.pojo.request.player;

import lombok.Data;

@Data
public class Score {

    private Integer money;

    private boolean is_upper;

    private Player player;

    public Score(Integer money, boolean is_upper, Player player) {
        this.money = money;
        this.is_upper = is_upper;
        this.player = player;
    }
}
