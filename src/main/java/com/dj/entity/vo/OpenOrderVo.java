package com.dj.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenOrderVo {

    private String gameKey;

    private String orderId;

    private String betContent;

    private String gameContent;

    private int betMoney;

    private String odds;

    private String odd22;

    private String odd23;

    private String code;

    private String point;

    private String account;




}
