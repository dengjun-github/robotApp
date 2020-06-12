package com.dj.entity.pojo.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BetsTypeBo {
    private String content;
    private String key;
    private Integer money;
}
