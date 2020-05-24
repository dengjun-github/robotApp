package com.dj.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Root {
    // 用户id
    private Long userId;
    //用户名
    private String account;
    // 指令集合
    private List<Instruct> instructs;
    // 返回模板
    private List<ReturnTemplate> returnTemplates;

    public void setRoot(Root root) {
        this.userId = root.getUserId();
        this.account = root.getAccount();
        this.instructs = root.getInstructs();
        this.returnTemplates = root.getReturnTemplates();
    }

    @Override
    public String toString() {
        return "Root{" +
                "userId=" + userId +
                ", account='" + account + '\'' +
                ", instructs=" + instructs +
                ", returnTemplates=" + returnTemplates +
                '}';
    }
}