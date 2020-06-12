package com.dj.entity.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 投注类型
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BetsType {
    private String key;
    // 下注类型名称
    private String name;
    // 投注格式
    private String format;
    // 通用赔率
    private String odds;
    // 22赔率
    private String odds22;
    // 23赔率
    private String odds23;
    // 玩法最大下注
    private Integer betsMaxMoney;
    // 玩法最小下注
    private Integer betsMinMoney;
    // 单局最大下注
    private Integer betsSingleMaxMoney;
}
