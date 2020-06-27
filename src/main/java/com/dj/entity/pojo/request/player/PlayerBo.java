package com.dj.entity.pojo.request.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerBo {
    private String account;

    private String nickname;

    private String groupName;
}
