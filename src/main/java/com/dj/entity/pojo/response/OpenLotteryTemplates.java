package com.dj.entity.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OpenLotteryTemplates {
    private String header;
    private String body;
    private String foot;
    private String title;
    private String key;
    private boolean status;
}
