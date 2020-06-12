package com.dj.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetsOrderVo {
    private Long orderId;
    private String key;
    private String content;
    private Double betsMoney;
    private String account;
    private Double odds;
    private Double odds22;
    private Double odds23;



    @Override
    public String toString() {
        return "BetsOrderVo{" +
                "orderId=" + orderId +
                ", key='" + key + '\'' +
                ", content='" + content + '\'' +
                ", betsMoney=" + betsMoney +
                ", account='" + account + '\'' +
                ", odds=" + odds +
                ", odds22=" + odds22 +
                ", odds23=" + odds23 +
                '}';
    }
}
