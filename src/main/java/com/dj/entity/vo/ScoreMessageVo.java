package com.dj.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.message.GroupMessageEvent;
import serialize.pojo.Finance;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreMessageVo {
    private String message;
    private String type;
    private String content;
    private String nickname;
    private Finance finance;
    private GroupMessageEvent event;
}
