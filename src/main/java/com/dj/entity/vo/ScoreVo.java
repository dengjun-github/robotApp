package com.dj.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreVo {
    private String account;
    private String balance;
    private String money;
    private String nickname;
    private String createTime;
}
