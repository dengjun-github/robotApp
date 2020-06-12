package com.dj.entity.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OpenData {

    //玩家的大的数字
    public static final Integer DA = 23;
    //玩家的小的数字
    public static final Integer XIAO = 22;

    public static OpenData NEW_OPEN_DATA = null;

    //上期开奖号码
    private String opencode;
    //上期开奖时间
    private String opentime;
    //上期期号
    private String expect;
    //当期开奖时间
    private String nexttime;
    //当期期号
    private String nextexpect;

//    public static void setNewOpenData(OpenData openData){
//        NEW_OPEN_DATA.setExpect(openData.getExpect());
//        NEW_OPEN_DATA.setOpencode(openData.getOpencode());
//        NEW_OPEN_DATA.setOpentime(openData.getOpentime());
//        NEW_OPEN_DATA.setNexttime(openData.getNexttime());
//        NEW_OPEN_DATA.setNextexpect(openData.getNextexpect());
//    }
    public static List<String> getCodeResultList(OpenData openData){
        String code = openData.getOpencode();
        List<String> codeResult = new ArrayList<>();
        Integer sum = Arrays.asList(code.split(",")).stream()
                .map(Integer::parseInt)
                .reduce(0, Integer::sum);
        if (sum >= DA) {
            codeResult.add("大");
        } else if (sum <= XIAO) {
            codeResult.add("小");
        }
        if (sum % 2 == 0) {
            codeResult.add("双");
        } else {
            codeResult.add("单");
        }
        int parseInt = Integer.parseInt(code.substring(code.length() - 1));
        if (Integer.parseInt(code.substring(0,1))> parseInt){
            codeResult.add("龙");
        }else if (Integer.parseInt(code.substring(0,1))< parseInt){
            codeResult.add("虎");
        }else {
            codeResult.add("和");
        }
        return codeResult;
    }



    public int getSum(OpenData openData){
        return Arrays.asList(openData.getOpencode().split(",")).stream()
                .map(Integer::parseInt)
                .reduce(0, Integer::sum);
    }


    public String getResult2Lishi(){
        final StringBuilder codeResult = new StringBuilder();
        List<String> codeResultList = OpenData.getCodeResultList(this);
        codeResultList.forEach(result ->{
            codeResult.append(result);
            if (codeResultList.indexOf(result) != codeResultList.size()-1){
                codeResult.append("&nbsp;&nbsp;");
            }
        });
        return codeResult.toString();
    }

    public String getResult2Kaijiang(){
        final StringBuilder codeResult = new StringBuilder();
        List<String> codeResultList = OpenData.getCodeResultList(this);
        codeResultList.forEach(result ->{
            if (codeResultList.indexOf(result) == 0){
                codeResult.append(getSum(this));
            }
            if (codeResultList.indexOf(result) != codeResultList.size()){
                codeResult.append("&nbsp;&nbsp;");
            }
            codeResult.append(result);
        });
        return codeResult.toString();
    }
    public String getResult2Index(){
        final StringBuilder codeResult = new StringBuilder();
        List<String> codeResultList = OpenData.getCodeResultList(this);
        codeResultList.forEach(result ->{
            if (codeResultList.indexOf(result) == 0){
                codeResult.append(getSum(this));
            }
            if (codeResultList.indexOf(result) != codeResultList.size()){
                codeResult.append("  ");
            }
            codeResult.append(result);
        });
        return codeResult.toString();
    }

    public String getKaiJiangCode(){
        final StringBuilder code = new StringBuilder();
        List<String> codeList = Arrays.asList(this.getOpencode().split(","));
        codeList.forEach(it -> {
            code.append(it);
            if (codeList.indexOf(it) != codeList.size()-1) {
                code.append("-");
            }
        });
        return code.toString();
    }



    @Override
    public String toString() {
        return "OpenData{" +
                "opencode='" + opencode + '\'' +
                ", opentime='" + opentime + '\'' +
                ", expect='" + expect + '\'' +
                ", nexttime='" + nexttime + '\'' +
                ", nextexpect='" + nextexpect + '\'' +
                '}';
    }
}
