package com.dj.util;

import com.dj.Exception.ClientErrorException;
import javafx.stage.StageStyle;
import lombok.Getter;
import serialize.pojo.Keys;

import java.io.File;

public class GlobalConstant {

    public static Keys.Game PLAYING_GAME = null;

    //是否可以下注
    public static boolean CAN_BET = true;

    public static boolean closeStatus = false;

    public static String IMAGE_URL = "http://192.168.1.107:9999/";

    //图片地址
    public static String IMAGE_PATH = "D:\\bot\\";

    //是否开启游戏
    public static boolean IS_GAME_BEGIN = false;

    public static final File SYS_ICON = new File("src/main/resources/images/sys.png");

    //是否已经开奖
    public static boolean IS_OPEN = false;



    public enum Action{
        UPPER_SCORE("UpperScoreInstruct"),
        LOWER_SCORE("LowerScoreInstruct"),
        QUERY_SCORE("QueryScoreInstruct"),
        QUERY_HiSTORY("QueryHistoryInstruct"),
        QUERY_BILL("QueryBillInstruct"),
        RETURN_WATER("ReturnWaterInstruct"),
        CANCEL_BET("CancelBetInstruct"),
        BETS_DA("BetsDa"),
        BETS_XIAO("BetsXiao"),
        BETS_DAN("BetsDan"),
        BETS_SHUANG("BetsShuang"),
        BETS_LONG("BetsLong"),
        BETS_HU("BetsHu"),
        BETS_HE_OF_LONGHU("BetsHeOfLongHu"),
        BETS_He_OF_DAXIAO("BetsHeOfDaXiao"),
        BETS_DANMA("BetsDanMa"),
        BETS_LIANMA("BetsLianMa"),
        BETS_DINGWEI_CODE("BetsDingWeiOfCode"),
        BETS_DINGWEI_DAXIAO("BetsDingweiOfDaXiao"),
        BETS_DADAN("BetsDaDan"),
        BETS_DASHUANG("BetsDaShuang"),
        BETS_XIAODAN("BetsXiaoDan"),
        BETS_XIAOSHUANG("BetsXiaoShuang"),
        BETS_SAN_ZUHE("BetsSanZuHe"),
        BETS_LIANG_ZUHE("BetsLiangZuHe"),
        BETS_BAOZI("BetsBaoZi"),
        BETS_SHUNZI("BetsShunZi"),
        BETS_BANSHUN("BetsBanShunZi"),
        BETS_ZALIU("BetsZaLiu"),
        BETS_DUIZI("BetsDuiZi"),
        BETS_HEZHI("BetsHeZhi"),
        BETS_JIDA("BetsJiDa"),
        BETS_JIXIAO("BetsJiXiao"),
        BETS_DINGWEI_LONG("BetsDingWeiLong"),
        BETS_DINGWEI_HU("BetsDingWeiHu"),
        BETS_DINGWEI_HE("BetsDingWeiHe");

        @Getter
        private String key;

        Action(String key) {
            this.key = key;
        }

        public static Action findActionByKey(String key){
            for (Action action:
                 Action.values()) {
                if (key.equals(action.key)) return action;
            }
            throw new ClientErrorException("当前动作的key["+key+"]没有找到");
        }
    }


    @Getter
    public enum ImageSize{

       LISHI(575,31),KAIJIANG(500,231);
       private int width;
       private int heightInit;

        ImageSize(int width, int heightInit) {
            this.width = width;
            this.heightInit = heightInit;
        }

        public int getHeight(Integer num){
            return heightInit * num + heightInit;
        }
    }

    @Getter
    public enum StageInfo{
        LOGIN(450.0,358.0,StageStyle.TRANSPARENT,"../../fxml/Login.fxml",null),
        REGISTER(450.0,358.0,StageStyle.TRANSPARENT,"../../fxml/Register.fxml",null),
        BIND(450.0,358.0,StageStyle.TRANSPARENT,"../../fxml/Bind.fxml",null),
        FIND_PASSWORD(450.0,358.0,StageStyle.TRANSPARENT,"../../fxml/FindPassword.fxml",null),
        INDEX(1257.0,1032.0,StageStyle.UNIFIED,"../../fxml/Index.fxml","账单"),
        ACCOUNT(450.0,358.0,StageStyle.TRANSPARENT,"../../fxml/FindPassword.fxml",null),
        find_player(295.0,375.0,StageStyle.UTILITY,"../../fxml/FindOrAddPlayer.fxml","查找或添加用户"),
        MANUAL_OPEN(320.0,456.0,StageStyle.UTILITY,"../../fxml/ManualOpen.fxml",null),
        PERIOD_PROFIT(580.0,537.0,StageStyle.UTILITY,"../../fxml/ProfitByPeriod.fxml",null),
        RENEW(450.0,358.0,StageStyle.TRANSPARENT,"../../fxml/Renew.fxml",null),
        ROBOT_LOGIN(450.0,358.0,StageStyle.TRANSPARENT,"../../fxml/RobotLogin.fxml",null),
        SCORE_HISTORY(674.0,719.0,StageStyle.UTILITY,"../../fxml/ScoreHistory.fxml","上下分详细历史"),
        SCORE_MANAGE(450.0,358.0,StageStyle.UTILITY,"../../fxml/ScoreManage.fxml","上下分管理");

        private double width;
        private double height;
        private StageStyle style;
        private String fxmlPath;
        private String title;

        StageInfo(double width, double height, StageStyle style,String fxmlPath,String title) {
            this.width = width;
            this.height = height;
            this.style = style;
            this.fxmlPath = fxmlPath;
            this.title = title;
        }
    }




    /**
     * 判断游戏是否开始
     */
    public static void gameIsBegan() throws ClientErrorException{
        if (!IS_GAME_BEGIN){
            throw new ClientErrorException("游戏未开始,请先开始游戏");
        }
    }
}
