package com.dj.util;

import com.dj.Exception.ClientErrorException;
import com.dj.pojo.response.Root;
import com.dj.util.gameUtils.GameUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalConstant {

    //服务器端口
    public static final Integer PORT = 8080;

    //服务器主机
    public static final String HOST = "192.168.1.109";

    //用户token
    public static String USER_TOKEN = "";

    //用户配置
    public static final Root USER_ROOT = new Root();

    /**
     * 请求的路径
     */
    public enum RequstUri{
        LOGIN("/authentication"),
        BIND("/bind"),
        REGISTER("/register"),
        CONFIG("/config"),
        PLAYER_SCORE("/player/score");

        @Getter
        private String uri;

        RequstUri(String uri) {
            this.uri = uri;
        }
    }

    /**
     qq玩家的动作
     */
    public enum Action{
        UP(new ArrayList<String>()), DOWN(new ArrayList<String>()), BET(new ArrayList<String>()),CHECK(new ArrayList<String>());

        @Getter
        @Setter
        private List<String> msgs;

        Action(ArrayList<String> msgs) {
            this.msgs = msgs;
        }

        public static Action witchAction(String msg) throws ClientErrorException {
            if (UP.msgs.contains(msg)) return UP;
            else if (DOWN.msgs.contains(msg)) return DOWN;
            else if (CHECK.msgs.contains(msg)) return CHECK;
            else if (BET.msgs.contains(msg)) return BET;
            else throw new ClientErrorException("格式错误");
        }

        public static void setUp(Root root) {
            root.getInstructs().forEach(instruct -> {

                //上分
                if (instruct.getKey().equals("UpperScoreInstruct")) UP.getMsgs().addAll(Arrays.asList(instruct.getValue().split("|")));
                //下分
                if (instruct.getKey().equals("LowerScoreInstruct")) DOWN.getMsgs().addAll(Arrays.asList(instruct.getValue().split("|")));
                //查分
                if (instruct.getKey().equals("QueryScoreInstruct")) CHECK.getMsgs().add(instruct.getValue());


            });
        }
    }




}
