package com.dj.task;

import api.future.AllOrderFutureListener;
import api.future.Future;
import api.future.OrderResultFutureListener;
import api.order.AllOrder;
import api.order.OrderResult;
import com.alibaba.fastjson.JSONArray;
import com.dj.entity.pojo.response.OpenData;
import com.dj.entity.pojo.response.OpenEvent;
import com.dj.handler.GroupHandler;
import com.dj.net.HttpClient;
import com.dj.util.*;
import javafx.concurrent.Task;
import org.apache.commons.lang3.StringUtils;
import serialize.pojo.BetsContentOption;
import serialize.pojo.Order;
import serialize.pojo.OrderResultOption;
import serialize.pojo.option.OrderResultItem;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dj.entity.pojo.response.OpenData.NEW_OPEN_DATA;
import static com.dj.handler.GroupHandler.RESULT_LIMT;
import static com.dj.handler.GroupHandler.groupHandler;
import static com.dj.robot.GroupRobot.sendMessage;
import static com.dj.util.GlobalConstant.*;

public class CountDownTask extends Task<OpenData> {

    public static final GateWay way = new GateWay();


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
                OpenData.setNewCodeResultList(openData);
                open();

            }

            //倒计时
            countDown = getcountDownNumber(openData);
            while (countDown > 0) {
                int finalCountDown = countDown;
                if (IS_GAME_BEGIN) {
                    SettingUtils.EVENT_MAP.forEach((sec, key) -> {
                        if (sec.equals(finalCountDown)) GroupHandler.EVENTS.offer(OpenEvent.builder().countDown(sec).eventName(key).build());
                    });
                }
                countDown--;
                updateMessage(countDown + "");
                Thread.sleep(1000l);
            }
            Thread.sleep(1000l);
        }
    }

    public void open() {
        CAN_BET = false;
        if (!IS_GAME_BEGIN) {
            CAN_BET = true;
            return;
        }
        Future future = AllOrder.deal(PLAYING_GAME.getIndex(), NEW_OPEN_DATA.getExpect());
        future.addListener(new AllOrderFutureListener() {
            @Override
            public void onSuccess(List<Order> list) {
                List<OrderResultItem> items = new ArrayList<>();
                list.forEach(order -> {
                    List<BetsContentOption> betsContentOptionList = new ArrayList<>();
                    order.getContent().forEach(betsContent -> {
                        try {
                            BetsContentOption betsContentOption = (BetsContentOption) way.gateWay(PLAYING_GAME.getIndex(), betsContent.getBetsOrderKey(), betsContent);
                            betsContentOptionList.add(betsContentOption);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
                    OrderResultItem item = new OrderResultItem();
                    item.setId(order.getId());
                    item.setContent(betsContentOptionList);
                    items.add(item);
                });

                List<String> codeResultList = OpenData.newCodeResultList.subList(0,3);


                OrderResultOption resultOption = OrderResultOption.builder()
                        .gameType(PLAYING_GAME.getIndex())
                        .items(items)
                        .nums(OpenData.NEW_OPEN_DATA.getKaiJiangCode())
                        .period(NEW_OPEN_DATA.getExpect())
                        .result(StringUtils.join(codeResultList,"-"))
                        .build();
                OrderResult.deal(resultOption).addListener(new OrderResultFutureListener() {
                    @Override
                    public void onSuccess(Map<String, String> map) {
                        map.forEach((k, v) -> sendMessage(v, true));
                        //发送开奖图
                        groupHandler.sendLiShiImage();
                        //发送历史图
                        groupHandler.sendKaiJiangImage();
                        //开盘
                        groupHandler.betOpen("开盘");
                    }

                    @Override
                    public void onFailure(int i) {
                        sendMessage("发送订单异常---------错误码:" + i + "-----错误信息" + ErrorCodeUtil.getMsgByErroCode(i), false);
                        CAN_BET = true;
                    }
                });
            }

            @Override
            public void onFailure(int i) {
                sendMessage("获取订单异常---------错误码:" + i + "-----错误信息" + ErrorCodeUtil.getMsgByErroCode(i), false);
                CAN_BET = true;
            }
        });
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

    public int getcountDownNumber(OpenData openData) {
        long nexttime = DateUtil.dateToStamp(openData.getNexttime());
        long time = (nexttime - System.currentTimeMillis()) / 1000l;
        //倒计时
        return (int) time;
    }

}
