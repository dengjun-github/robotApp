package com.dj.entity.pojo.response;

import com.dj.util.BetUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    public static List<String> newCodeResultList = new ArrayList<>();

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

    public static List<String> getCodeResultList(OpenData openData){

        List<String> codeResultList = new ArrayList<>();

        String code = openData.getOpencode();

        List<Integer> codeList = Arrays.asList(code.split(",")).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Integer sum = codeList.stream()
                .reduce(0, Integer::sum);
        if (sum >= DA) codeResultList.add("大");

        else if (sum <= XIAO) codeResultList.add("小");

        if (sum % 2 == 0) codeResultList.add("双");

        else codeResultList.add("单");

        int parseInt = Integer.parseInt(code.substring(code.length() - 1));
        if (Integer.parseInt(code.substring(0,1))> parseInt) codeResultList.add("龙");

        else if (Integer.parseInt(code.substring(0,1))< parseInt) codeResultList.add("虎");

        else codeResultList.add("合");
        Integer[] numbers = {codeList.get(0),codeList.get(1),codeList.get(2)};
        boolean flag = true;
        if(isPair(numbers)) {
            codeResultList.add("前对");
            flag = false;
        }

        if(isLeopard(numbers)) codeResultList.add("前豹");

        else if(isStraight(numbers)) codeResultList.add("前顺");

        else if(isHalfStraight(numbers) && !isPair(numbers)) codeResultList.add("前半顺");

        else if(flag) codeResultList.add("前杂");

        numbers = new Integer[]{codeList.get(1),codeList.get(2),codeList.get(3)};
        flag = true;
        if(isPair(numbers)) {
            codeResultList.add("中对");
            flag = false;
        }

        if(isLeopard(numbers)) codeResultList.add("中豹");

        else if(isStraight(numbers)) codeResultList.add("中顺");

        else if(isHalfStraight(numbers) && !isPair(numbers)) codeResultList.add("中半顺");

        else if(flag) codeResultList.add("中杂");

        numbers = new Integer[]{codeList.get(2),codeList.get(3),codeList.get(4)};
        flag = true;
        if(isPair(numbers)) {
            codeResultList.add("后对");
            flag = false;
        }

        if(isLeopard(numbers))
            codeResultList.add("后豹");
        else if(isStraight(numbers))
            codeResultList.add("后顺");
        else if(isHalfStraight(numbers) && !isPair(numbers))
            codeResultList.add("后半顺");
        else if(flag)
            codeResultList.add("后杂");


        if ( sum >= BetUtil.JI_DA_NUMBER) codeResultList.add("极大");
        if ( sum <= BetUtil.JI_XIAO_NUMBER) codeResultList.add("极小");

        return codeResultList;
    }


    public static void main(String[] args) {
        OpenData build = OpenData.builder().opencode("1,4,3,4,5").build();
//        List<String> codeResultList = getCodeResultList(build);
//        System.out.println(codeResultList.toString());
    }



    /**
     * 三位数豹子的判断
     * @param numbers
     * @return
     */
    private static boolean isLeopard(Integer[] numbers) {
        Set<Integer> stSet = new HashSet<>(Arrays.asList(numbers));
        return stSet.size()==1;
    }

    /**
     * 三位数对子的判断
     * @param numbers
     * @return
     */
    private static boolean isPair(Integer[] numbers) {
        Set<Integer> stSet = new HashSet<>(Arrays.asList(numbers));
        return stSet.size()==2;
    }

    /**
     * 三位数顺子的判断
     * @param numbers
     * @return
     */
    private static boolean isStraight(Integer[] numbers) {
        int sum=numbers[0]+numbers[1];
        int abs=Math.abs(numbers[0]-numbers[1]);
        return (abs==2&&numbers[2]*2==sum)||(abs==1&&(abs==1&&(Math.abs(numbers[2]*2-sum)==3)));
    }

    /**
     * 三位数半顺的判断
     * @param numbers
     * @return
     */
    public static boolean isHalfStraight(Integer[] numbers) {
        if(!isStraight(numbers)) {
            if(Math.abs(numbers[0]-numbers[1]) == 1 || Math.abs(numbers[0]-numbers[2]) == 1 || Math.abs(numbers[1]-numbers[2]) == 1) {
                return true;
            }
        }
        return false;
    }



    public static int getSum(OpenData openData){
        return Arrays.asList(openData.getOpencode().split(",")).stream()
                .map(Integer::parseInt)
                .reduce(0, Integer::sum);
    }


    public String getResult2Lishi(){
        final StringBuilder codeResult = new StringBuilder();

        List<String> codeResultList = getCodeResultList(this).subList(0,3);

        codeResult.append(StringUtils.join(codeResultList,"&nbsp;&nbsp;"));

//        codeResultList.forEach(result ->{
//            codeResult.append(result);
//            if (codeResultList.indexOf(result) != codeResultList.size()-1){
//                codeResult.append("&nbsp;&nbsp;");
//            }
//        });
        return codeResult.toString();
    }

    public String getResult2Kaijiang(){
        final StringBuilder codeResult = new StringBuilder();
        List<String> codeResultList = newCodeResultList.subList(0,3);

//        codeResult.append(StringUtils.join(codeResultList,"&nbsp;&nbsp;"));
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
        List<String> codeResultList = newCodeResultList.subList(0,3);
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
        List<String> codeList = Arrays.asList(this.getOpencode().split(","));
//        codeList.forEach(it -> {
//            code.append(it);
//            if (codeList.indexOf(it) != codeList.size()-1) {
//                code.append("-");
//            }
//        });
        return StringUtils.join(codeList,"-");
    }

    public static void setNewCodeResultList(OpenData openData){
        newCodeResultList = getCodeResultList(openData);

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
