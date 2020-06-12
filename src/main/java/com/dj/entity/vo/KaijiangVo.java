package com.dj.entity.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KaijiangVo {

    private String time;

    private String event;

    private String numbers;

    private String result;
}
