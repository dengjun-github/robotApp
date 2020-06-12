package com.dj.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderVo {
    //下注内容
    private String betsContent;
    //彩种
    private String gameType;
    //普通赔率
    private Double odds;
    // 22赔率
    private String odds22;
    // 23赔率
    private String odds23;

    private String key;
    //投注金额
    private Integer betMoney;
    //玩家账号
    private String palyerAccount;

}
