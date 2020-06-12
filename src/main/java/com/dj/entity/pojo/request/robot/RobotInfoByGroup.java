package com.dj.entity.pojo.request.robot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RobotInfoByGroup {
    private String groupName;
    private String groupAccount;
}
