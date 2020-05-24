package com.dj.core.event;

import com.alibaba.fastjson.JSONObject;
import com.dj.Exception.ClientErrorException;
import com.dj.net.WebClientVerticle;
import com.dj.pojo.request.player.Player;
import com.dj.pojo.request.player.Score;
import com.dj.util.GlobalConstant.Action;
import com.dj.util.HttpResult;
import com.dj.util.gameUtils.GameUtil;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageUtils;

import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.RequstUri.PLAYER_SCORE;

public class RobotEvent implements ListenerHost {

    /**
     * 群监听事件
     * @param event
     */
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        //qq用户所发送的消息
        String msgSting = event.getMessage().contentToString();
        //玩家
        Player player = new Player(String.valueOf(event.getSender().getId()),event.getSender().getNick(),event.getGroup().getName());
        //拆分后的消息
        String actionMsg = GameUtil.parseContent(msgSting);
        //上分请求参数,默认为上分
        Score score = new Score(GameUtil.getNumbers(msgSting),true,player);

        try {
            switch (Action.witchAction(actionMsg)) {
                case UP:
                    // TODO 告知前端有上分请求
                    score.set_upper(true);
                    upperOrLower2Server(event, score);
                    break;
                case DOWN:
                    // TODO 告知前端有下分请求
                    score.set_upper(false);
                    score.setMoney(score.getMoney() * (-1));
                    upperOrLower2Server(event, score);
                    break;
                case CHECK:
                    WebClientVerticle.httpGet(PORT,HOST,PLAYER_SCORE.getUri(),(JSONObject)JSONObject.toJSON(player),USER_TOKEN,
                            res -> {
                                if (res.succeeded()) {
                                    HttpResult result = res.result().body();
                                    if (result.isIsok()) {
                                        event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                                .plus("\r")
                                                .plus(result.getData().toString()));
                                    } else {
                                        event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                                .plus("\r")
                                                .plus(result.getMessage()));
                                    }
                                } else {
                                    event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                            .plus("\r")
                                            .plus("服务器异常,请稍后再试"));
                                }
                            });
                break;
                case BET:
                    event.getGroup().sendMessage(MessageUtils
                        .newChain(new At(event.getSender()))
                        .plus("\n")
                        .plus("下注["+msgSting+"]成功"));
                break;

            }

        } catch (ClientErrorException e) {
            event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                    .plus("\r")
                    .plus(e.getMessage()));
        }
    }

    private void upperOrLower2Server(GroupMessageEvent event, Score score) {
        WebClientVerticle.httpPost(PORT,HOST,PLAYER_SCORE.getUri(),(JSONObject)JSONObject.toJSON(score),USER_TOKEN,
                res -> {
                    if (res.succeeded()) {
                        HttpResult result = res.result().body();
                        if (result.isIsok()) {
                            event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                    .plus("\r")
                                    .plus(result.getData().toString()));
                        } else {
                            event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                    .plus("\r")
                                    .plus(result.getMessage()));
                        }

                    } else {
                        event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                                .plus("\r")
                                .plus("服务器异常,请稍后再试"));
                    }
                });
    }

    public static void main(String[] args) {
        System.out.println("你\r\n好");
    }


}
