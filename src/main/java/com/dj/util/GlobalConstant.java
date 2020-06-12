package com.dj.util;

import com.dj.Exception.ClientErrorException;
import com.dj.entity.pojo.response.Root;
import com.dj.entity.vo.ImageVo;
import lombok.Getter;

public class GlobalConstant {

    //服务器端口
    public static final Integer PORT = 8080;

    //服务器主机
    public static final String HOST = "192.168.1.107";

    //用户token
    public static String USER_TOKEN = "";

    //用户配置
    public static Root USER_ROOT = new Root();

    //玩家玩的是哪个彩种
    public static StringBuilder GAME_INDEX = new StringBuilder();
    //
    public static StringBuilder GAME_TYPE = new StringBuilder();

    //是否可以下注
    public static boolean CAN_BET = true;

    public static boolean closeStatus = false;



    //图片地址
    public static String IMAGE_PATH = "D:\\bot\\";

    //是否开启游戏
    public static boolean IS_GAME_BEGIN = false;

    //是否已经开奖
    public static boolean IS_OPEN = false;

    //图片
    public static final ImageVo imageVo = new ImageVo();

    public enum Action{
        UPPER_SCORE("UpperScoreInstruct"),
        LOWER_SCORE("LowerScoreInstruct"),
        QUERY_SCORE("QueryScoreInstruct"),
        BETS_DA("BetsDa"),
        BETS_XIAO("BetsXiao"),
        BETS_DAN("BetsDan"),
        BETS_SHUANG("BetsShuang"),
        BETS_DING_WEI("BetsDingWei");

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

    /**
     * 请求的路径
     */
    public enum RequstUri{
        LOGIN("/authentication"),
        BIND("/bind"),
        REGISTER("/register"),
        CONFIG("/config"),
        PLAYER_SCORE("/player/score"),
        PLAYER_BET("/player/order"),
        PLAYER_BETS_CHECK("/player/bets_check"),
        PLAYER_ORDER("/player/order"),
        PLAYER_RESULT("/player/result"),
        ROBOT_INFO("/robot-info"),
        BILL("/player/bill");

        @Getter
        private String uri;

        RequstUri(String uri) {
            this.uri = uri;
        }
    }

    public enum BotPrivence{
        GROUP,
        DUMMY
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

}
