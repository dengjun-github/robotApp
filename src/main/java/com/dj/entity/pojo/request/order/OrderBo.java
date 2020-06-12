package com.dj.entity.pojo.request.order;

import com.dj.entity.pojo.request.player.PlayerBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderBo {
    private PlayerBo player;
    private List<BetsTypeBo> betsTypes;
    private String content;
    private String period;
    private String gameType;
}