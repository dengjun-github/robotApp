package com.dj.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dj.entity.pojo.request.order.ResultFinanceBo;
import com.dj.entity.pojo.request.order.ResultOrderBo;
import com.dj.entity.pojo.response.OpenData;
import com.dj.entity.pojo.response.OpenEvent;
import com.dj.entity.pojo.response.SettleResponse;
import com.dj.handler.OrderHandler;
import com.dj.net.HttpClient;
import com.dj.util.DateUtil;
import com.dj.util.HttpResult;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.dj.entity.pojo.response.OpenData.NEW_OPEN_DATA;
import static com.dj.handler.GroupHandler.*;
import static com.dj.robot.GroupRobot.sendMessage;
import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.RequstUri.PLAYER_RESULT;

public class CountDownTask extends Task<OpenData> {



    @Override
    protected void updateMessage(String message) {
        super.updateMessage(message);
    }

    @Override
    protected void updateValue(OpenData value) {
        super.updateValue(value);
    }

    @Override
    protected OpenData call() throws Exception {
        int countDown = 0;
        while (true) {
            //接口请求
            OpenData openData = (OpenData) getOpenData(true);
            if (!NEW_OPEN_DATA.getExpect().equals(openData.getExpect())) {
                //开奖
                updateValue(openData);
                Open();

            }

            //倒计时
            countDown = getcountDownNumber(openData);
            while (countDown > 0) {
//                System.out.println("距离下次开奖时间还有:" + countDown + "s");
                int finalCountDown = countDown;
                if (IS_GAME_BEGIN){

                    USER_ROOT.getSettings().stream()

                            .filter(settings -> {
                                Pattern pattern = Pattern.compile("[0-9]*");
                                return pattern.matcher(settings.getValue()).matches();
                            })
                            .forEach(settings -> {
                                if (settings.getStatus() && finalCountDown == Integer.parseInt(settings.getValue())) {
                                    EVENTS.offer(new OpenEvent(settings.getKey(), Integer.parseInt(settings.getValue())));
                                }
                            });

                    USER_ROOT.getImageSettings()
                            .forEach(settings -> {
                                if (settings.getStatus() && finalCountDown == settings.getValue()) {
                                    EVENTS.offer(new OpenEvent(settings.getKey(), settings.getValue()));
                                }
                            });
                }
                countDown --;
                updateMessage(countDown+"");
                Thread.sleep(1000l);
            }
            Thread.sleep(1000l);
        }
    }




    private void Open() {
        CAN_BET = false;
        if(!IS_GAME_BEGIN) {
            CAN_BET = true;
            return;
        }
        try {
            List<ResultFinanceBo> resultFinanceBos = new OrderHandler().execute();
            ResultOrderBo build = ResultOrderBo.builder()
                    .finances(resultFinanceBos)
                    .code(NEW_OPEN_DATA.getOpencode())
                    .results(OpenData.getCodeResultList(NEW_OPEN_DATA))
                    .prePeriod(NEW_OPEN_DATA.getExpect())
                    .build();

            HttpResult result = HttpClient.httpPost(PLAYER_RESULT.getUri() + "/" + GAME_INDEX + "/" + NEW_OPEN_DATA.getExpect(), (JSONObject) JSONObject.toJSON(build), USER_TOKEN);

            if (null != result && result.isIsok()) {
                JSONObject jsonObject = (JSONObject) result.getData();
                SettleResponse settleResponse = jsonObject.toJavaObject(SettleResponse.class);
                String msg = settleResponse.getMsg();
                sendMessage(msg,true);




                //发送开奖图
                groupHandler.sendLiShiImage();
                //发送历史图
                groupHandler.sendKaiJiangImage();

                //开盘
                groupHandler.betOpen("开盘");
            } else {
                sendMessage("连接服务器异常,请重试或联系管理员", false);
                CAN_BET = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public int getcountDownNumber(OpenData openData){
        long nexttime = DateUtil.dateToStamp(openData.getNexttime());
        long time = (nexttime - System.currentTimeMillis()) / 1000l;
        //倒计时
        return (int) time;
    }



}
