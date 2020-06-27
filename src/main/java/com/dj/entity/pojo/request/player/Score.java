package com.dj.entity.pojo.request.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Score {

    private Integer money;

    private boolean is_upper;

    private PlayerBo player;

}
