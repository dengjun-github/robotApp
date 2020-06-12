package com.dj.entity.pojo.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultFinanceBo {
    private String gameType;
    private String account;
    private Long orderId;
    private Boolean isWin;
    private Double money;
    private String content;
}
