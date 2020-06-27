package com.dj.robot;

import api.finance.PQScore;
import api.future.BetsOrderFutureListener;
import api.future.Future;
import api.future.PQScoreFutureListener;
import api.future.UWScoreFutureListener;
import api.group.UWScore;
import api.order.BetsOrder;
import com.dj.Application;
import com.dj.Exception.ClientErrorException;
import com.dj.controller.ScoreManageController;
import com.dj.entity.vo.ScoreMessageVo;
import com.dj.util.*;
import common.Client;
import javafx.application.Platform;
import javafx.stage.Stage;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageUtils;
import serialize.pojo.BetsContentOption;
import serialize.pojo.Finance;
import serialize.pojo.Keys;
import serialize.pojo.option.BetsOrderOption;
import serialize.pojo.option.UWScoreOption;
import utils.App;

import java.math.BigDecimal;
import java.util.*;

import static com.dj.Application.controllers;
import static com.dj.entity.pojo.response.OpenData.NEW_OPEN_DATA;
import static com.dj.util.GlobalConstant.Action.*;
import static com.dj.util.GlobalConstant.PLAYING_GAME;
import static com.dj.util.GlobalConstant.StageInfo.INDEX;
import static com.dj.util.GlobalConstant.StageInfo.SCORE_MANAGE;
import static com.dj.util.SimpleTools.simpleTools;

public class GroupListener implements ListenerHost {

    /**
     * 群监听
     *
     * @param event
     */
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        //监听到的群消息不是当前设置的群,则不往下执行
        if (event.getGroup().getId() != GroupRobot.GROUPID) {
            return;
        }
        //玩家所发送的消息
        String userSendMsg = event.getMessage().contentToString();

        try {
            Map<String, String> keyByMsg = InstructionRegexUtil.findKeyByMsg(userSendMsg);
            final List<BetsContentOption> betsContentOptions = new ArrayList<>();
            keyByMsg.forEach((key,msg) -> {
                GlobalConstant.Action action = findActionByKey(key);
                String local = null;
                String code = null;
                int money = 0;
                String format = null;
                if (key.equals(BETS_DINGWEI_CODE.getKey()) || key.equals(BETS_LIANMA.getKey()) || key.equals(BETS_DANMA.getKey())) {
                    Map<String, String> map = setLocalAndCode(BETS_DINGWEI_CODE.getKey(), msg);
                    local = map.get("point");
                    code = map.get("code");
                    money = Integer.parseInt(map.get("betMoney"));
                    format = map.get("format");
                } else if (key.equals(BETS_DINGWEI_DAXIAO.getKey())) {
                    money = BetUtil.getMoney4DingWeiDaXiao(msg);
                } else {
                    money = BetUtil.getNumber(msg);
                }


                switch (action) {
                    case UPPER_SCORE:
                        UWScoreOption upperOpt = UWScoreOption.builder()
                                .account(String.valueOf(event.getSender().getId()))
                                .money(money)
                                .upper(true)
                                .nickname(event.getSender().getNick())
                                .build();
                        upperOrLower(upperOpt,event,msg);
                        break;
                    case LOWER_SCORE:
                        UWScoreOption lowerOpt = UWScoreOption.builder()
                                .account(String.valueOf(event.getSender().getId()))
                                .money(money)
                                .upper(false)
                                .nickname(event.getSender().getNick())
                                .build();
                        upperOrLower(lowerOpt,event,msg);
                        break;
                    case QUERY_SCORE:
                        Future future = PQScore.deal(String.valueOf(event.getSender().getId()), event.getSender().getNick(), NEW_OPEN_DATA.getNextexpect());
                        future.addListener(new PQScoreFutureListener() {
                            public void onSuccess(String msg) {
                                event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                        .plus("\r")
                                        .plus(msg));
                            }

                            public void onFailure(int errorCode) {
                                event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                        .plus("\r")
                                        .plus(ErrorCodeUtil.getMsgByErroCode(errorCode)));
                            }
                        });
                        break;

                    case BETS_DINGWEI_LONG:
                    case BETS_DINGWEI_HU:
                    case BETS_DINGWEI_HE:
                    case BETS_DA:
                    case BETS_XIAO:
                    case BETS_DAN:
                    case BETS_HU:
                    case BETS_LONG:
                    case BETS_JIXIAO:
                    case BETS_JIDA:
                    case BETS_DINGWEI_DAXIAO:
                    case BETS_DADAN:
                    case BETS_He_OF_DAXIAO:
                    case BETS_HE_OF_LONGHU:
                    case BETS_XIAOSHUANG:
                    case BETS_LIANG_ZUHE:
                    case BETS_SAN_ZUHE:
                    case BETS_DASHUANG:
                    case BETS_XIAODAN:
                    case BETS_SHUANG:
                        //大小单双
                        betsContentOptions.add(BetsContentOption.builder().content(msg).money(new BigDecimal(money)).betsOrderKey(key).build());
                        break;
                    case BETS_BAOZI:
                    case BETS_DUIZI:
                    case BETS_BANSHUN:
                    case BETS_SHUNZI:
                    case BETS_ZALIU:
                        BigDecimal sanquBetMoney = new BigDecimal(money);
                        if (msg.contains("包")) {
                            String type = BetUtil.getChinese(msg).replace("包", "");
                            betsContentOptions.add(BetsContentOption.builder().content("前" + type + sanquBetMoney).money(sanquBetMoney).betsOrderKey(key).build());
                            betsContentOptions.add(BetsContentOption.builder().content("中" + type + sanquBetMoney).money(sanquBetMoney).betsOrderKey(key).build());
                            betsContentOptions.add(BetsContentOption.builder().content("后" + type + sanquBetMoney).money(sanquBetMoney).betsOrderKey(key).build());
                        } else {
                            betsContentOptions.add(BetsContentOption.builder().content(msg).money(sanquBetMoney).betsOrderKey(key).build());
                        }
                        break;
                    case BETS_HEZHI:
                        if (msg.contains(".")) money = Integer.parseInt(StringUtil.substringAfter(msg, ","));
                        else if (msg.contains("/")) money = Integer.parseInt(StringUtil.substringAfter(msg, "/"));
                        else money = BetUtil.getMoney4DingWeiDaXiao(msg);
                        betsContentOptions.add(BetsContentOption.builder().content(msg).betsOrderKey(key).money(new BigDecimal(money)).build());
                        break;
                    case BETS_DINGWEI_CODE:
                        //拿到定位球的betstype
                        //判断lcoal和code有无重复
                        if (!StringUtil.isunique(local) || !StringUtil.isunique(code))
                            throw new ClientErrorException("攻击格式错误");
                        for (int i = 0; i < local.length(); i++) {
                            for (int j = 0; j < code.length(); j++) {
                                String content = local.split("")[i] + format + code.split("")[j] + format + money;
                                betsContentOptions.add(BetsContentOption.builder().content(content).betsOrderKey(key).money(new BigDecimal(money)).build());
                            }
                        }
                        break;
                    case BETS_LIANMA:
                        for (int j = 0; j < code.length(); j++) {
                            String content = code.split("")[j] + format + money;
                            betsContentOptions.add(BetsContentOption.builder().content(content).betsOrderKey(key).money(new BigDecimal(money)).build());
                        }
                        break;

                    case BETS_DANMA:
                        String content = code + format + money;
                        betsContentOptions.add(BetsContentOption.builder().content(content).betsOrderKey(key).money(new BigDecimal(money)).build());
                        break;

                    default:
                        throw new ClientErrorException("攻击格式错误");
                }
            });

            if (!betsContentOptions.isEmpty()) {
                BetsOrderOption orderOption = BetsOrderOption.builder()
                        .content(betsContentOptions)
                        .contentStr(userSendMsg)
                        .account(String.valueOf(event.getSender().getId()))
                        .gameType(PLAYING_GAME.getIndex())
                        .nickname(event.getSender().getNick())
                        .period(NEW_OPEN_DATA.getNextexpect())
                        .build();
                //发送请求
                Future future = BetsOrder.deal(orderOption);
                future.addListener(new BetsOrderFutureListener() {
                    public void onSuccess(String msg) {
                        event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                .plus("\r")
                                .plus(msg));
                    }

                    public void onFailure(int errorCode) {
                        event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                .plus("\r")
                                .plus(ErrorCodeUtil.getMsgByErroCode(errorCode)));
                    }
                });
            }
        } catch (ClientErrorException e) {
            event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                    .plus("\r")
                    .plus(e.getMessage()));
        }
    }

    //三区形态的解析
    private Map<String, String> setLocalAndCode(final String key, final String msg) {
        final Map<String, String> map = new HashMap<>();
        Arrays.asList(App.getOdds(Client.account,key).getKeyword().split("|")).forEach(obj -> {
            if (msg.contains(obj)) {
                map.put("format", obj);
                if (key.equals(BETS_DINGWEI_CODE.getKey())) {
                    map.put("point", msg.split(obj)[0]);
                    map.put("code", msg.split(obj)[1]);
                    map.put("betMoney", msg.split(obj)[2]);
                } else {
                    map.put("code", msg.split(obj)[0]);
                    map.put("betMoney", msg.split(obj)[1]);
                }

            }
        });
        return map.size() == 0 ? null : map;
    }

   //上下分
    private void upperOrLower(UWScoreOption option, GroupMessageEvent event,String sendMsg) {
        Future future = UWScore.deal(option);
        future.addListener(new UWScoreFutureListener() {
            @Override
            public void onSuccess(Finance finance) {

                ScoreMessageVo messageVo = ScoreMessageVo.builder()
                        .message(Keys.FINANCE_TYPE[finance.getType()] + finance.getMoney())
                        .content(sendMsg)
                        .finance(finance)
                        .nickname(event.getSender().getNick())
                        .event(event)
                        .type(Keys.FINANCE_TYPE[finance.getType()])
                        .build();


                Platform.runLater(() -> {
                    Stage scoreManage = Application.ALL_STAGE.get(SCORE_MANAGE);
                    if (null == scoreManage)
                        scoreManage = simpleTools.createStage(SCORE_MANAGE, Application.ALL_STAGE.get(INDEX));
                    if (!scoreManage.isShowing())
                        scoreManage.show();

                    ScoreManageController scoreManageController = (ScoreManageController) controllers.get(ScoreManageController.class.getName());
                    scoreManageController.scoreNotification(messageVo);
                });


            }

            @Override
            public void onFailure(String s, int i) {
                event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                        .plus("\r")
                        .plus(s));
            }
        });

    }

}
