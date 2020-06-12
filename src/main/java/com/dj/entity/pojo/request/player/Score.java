package com.dj.entity.pojo.request.player;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Score {

    private Integer money;

    private boolean is_upper;

    private PlayerBo player;

    public Score(Integer money, boolean is_upper, PlayerBo playerBo) {
        this.money = money;
        this.is_upper = is_upper;
        this.player = playerBo;
    }
}
