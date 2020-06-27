package com.dj.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WelfareGrantVo {
    private String startMoney;
    private String endMoney;
    private String ratio;
}
