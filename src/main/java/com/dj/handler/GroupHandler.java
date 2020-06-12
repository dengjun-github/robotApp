package com.dj.handler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dj.entity.pojo.response.OpenData;
import com.dj.entity.pojo.response.OpenEvent;
import com.dj.entity.vo.KaijiangVo;
import com.dj.entity.vo.LishiVo;
import com.dj.net.HttpClient;
import com.dj.robot.GroupRobot;
import com.dj.util.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.dj.entity.pojo.response.OpenData.NEW_OPEN_DATA;
import static com.dj.net.VertxWebClient.RestFul.GET;
import static com.dj.robot.GroupRobot.sendMessage;
import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.RequstUri.PLAYER_BETS_CHECK;

public class GroupHandler {

    public static GroupHandler groupHandler = new GroupHandler();

    public static final Queue<OpenEvent> EVENTS = new LinkedList<>();
    public static final Integer RESULT_LIMT = 20;

    //执行轮询器
    public void openGamePoller() {
        ExecutorPool.executorService.execute(this::eventExecute);
    }


    //发送历史图
    public void sendLiShiImage() {
        final List<OpenData> lishi = (ArrayList<OpenData>) getOpenData(false);
        File lishiFile = GroupRobot.sendImg(() -> {
            List<LishiVo> lishiVos = lishi.stream()
                    .map(obj -> LishiVo.builder()
                            .event(StringUtils.substring(obj.getExpect(), obj.getExpect().length() - 3))
                            .numbers(obj.getOpencode())
                            .result(obj.getResult2Lishi())
                            .sum(obj.getSum(obj) + "")
                            .time(DateUtil.toHm(obj.getOpentime()))
                            .build())
                    .collect(Collectors.toList());
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("lishis", lishiVos);
            return TemplateConverter.execute("lishi.ftl", parameter, ImageSize.LISHI);
        });
        lishiFile.delete();
    }


    //发送开奖图
    public void sendKaiJiangImage() {
        final OpenData kaijiang = (OpenData) getOpenData(true);
        File kaijiangFile = GroupRobot.sendImg(() -> {
            KaijiangVo kaijiangVo = KaijiangVo.builder()
                    .event(kaijiang.getExpect())
                    .numbers(kaijiang.getKaiJiangCode())
                    .result(kaijiang.getResult2Kaijiang())
                    .time(DateUtil.toHm(kaijiang.getOpentime()))
                    .build();
            return TemplateConverter.execute("open.ftl", JSONObject.parseObject(JSON.toJSONString(kaijiangVo)), ImageSize.KAIJIANG);
        });
        kaijiangFile.delete();
    }


    private void eventExecute() {
        Thread.currentThread().setDaemon(true);
        while (true) {
            if (EVENTS.size() != 0) {
                //则不往下执行
                //获取事件队列的中的元素
                OpenEvent event = GroupHandler.EVENTS.poll();
                if (event.getEventName().equals("SettingsStopBetsTime")) {
                    betVerify("游戏封盘");
                }
                if (event.getEventName().equals(AllKeys.DEFAULT_DOWN_30)) {
                    GroupRobot.sendImg(imageVo::getDefaultDown30);
                }
                //其他事件
            }
            try {
                Thread.sleep(500l);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.interrupted();
            }
        }
    }


    //封盘事件
    public void betVerify(String exMessage) {
        CAN_BET = false;
        if (!IS_GAME_BEGIN) return;
        GroupRobot.sendImg(() -> new File(IMAGE_PATH + AllKeys.STOP_BETS_TIME + ".png"));
        GroupRobot.robotRequest2Server(GET, PLAYER_BETS_CHECK.getUri() + "/" + GAME_INDEX + "/" + NEW_OPEN_DATA.getNextexpect(), null,exMessage);
    }

    //开盘事件
    public void betOpen(String exMessage) {
        GroupRobot.sendImg(() -> new File(IMAGE_PATH + AllKeys.START_BETS +".png"));
        sendMessage("当前期号为:" + NEW_OPEN_DATA.getNextexpect(), false);
        CAN_BET = true;
    }


    public Object getOpenData(boolean getOne) {
        Map<String, String> param = new HashMap<>();
        param.put("limit", RESULT_LIMT.toString());
        HttpResult httpResult = HttpClient.httpGet("http://192.168.1.107:8888/api/azxy5", null, param);
        JSONArray data = (JSONArray) httpResult.getData();
        List<OpenData> openDatas = data.toJavaList(OpenData.class);
        if (getOne) {
            return openDatas.get(0);
        } else {
            return openDatas;
        }
    }


    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String date = format.format(new Date());
        System.out.println(date);
    }

    public int getcountDownNumber(OpenData openData) {
        long nexttime = DateUtil.dateToStamp(openData.getNexttime());
        long time = (nexttime - System.currentTimeMillis()) / 1000l;
        //倒计时
        return (int) time;
    }


}
