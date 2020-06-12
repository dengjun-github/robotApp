package com.dj.entity.pojo.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultOrderBo {
    private List<ResultFinanceBo> finances;
    private String code;
    private List<String> results;
    private String prePeriod;

}
