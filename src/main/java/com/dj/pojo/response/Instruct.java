package com.dj.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instruct {
    // 标题
    private String title;
    // 键
    private String key;
    // 值 上|上分
    private String value;
    // 开关 true=开 false = 关
    private Boolean status;

    public static void main(String[] args) {

        String str = "查";
        String add  = "查";
        String[] split = str.split("|");

    }
}
