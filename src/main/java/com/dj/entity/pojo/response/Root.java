package com.dj.entity.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    // 下注类型
    private List<BetsType> betsTypes;
    // 设置项
    private List<Settings> settings = new ArrayList<>();
    // 彩种
    private List<Game> games;
    //开奖模板
    private List<OpenLotteryTemplates> openLotteryTemplates;
    // 图片设置项
    private List<ImageSettings> imageSettings = new ArrayList<>();


    public void setRoot(Root root) {
        this.userId = root.getUserId();
        this.account = root.getAccount();
        this.instructs = root.getInstructs();
        this.returnTemplates = root.getReturnTemplates();
        this.betsTypes = root.getBetsTypes();
        this.settings = root.settings;
    }

    @Override
    public String toString() {
        return "Root{" +
                "userId=" + userId +
                ", account='" + account + '\'' +
                ", instructs=" + instructs +
                ", returnTemplates=" + returnTemplates +
                ", betsTypes=" + betsTypes +
                ", settings=" + settings +
                '}';
    }
}