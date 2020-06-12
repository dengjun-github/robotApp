package com.dj.entity.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片设置项
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageSettings {
    private String title;
    private String key;
    private String imageUrl;
    private Integer value;
    private Boolean status;
    private String remark;
}
