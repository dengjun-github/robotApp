package com.dj.robot;

import com.dj.Exception.ClientErrorException;
import com.dj.entity.pojo.request.order.BetsTypeBo;
import com.dj.entity.pojo.request.order.OrderBo;
import com.dj.entity.pojo.request.player.CheckScore;
import com.dj.entity.pojo.request.player.PlayerBo;
import com.dj.entity.pojo.request.player.Score;
import com.dj.util.BetUtil;
import com.dj.util.GlobalConstant;
import com.dj.util.InstructionRegexUtil;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dj.entity.pojo.response.OpenData.NEW_OPEN_DATA;
import static com.dj.net.VertxWebClient.RestFul.GET;
import static com.dj.net.VertxWebClient.RestFul.POST;
import static com.dj.robot.GroupRobot.player2Server;
import static com.dj.robot.GroupRobot.robotRequest2Server;
import static com.dj.util.GlobalConstant.Action.findActionByKey;
import static com.dj.util.GlobalConstant.GAME_INDEX;
import static com.dj.util.GlobalConstant.RequstUri.PLAYER_BET;
import static com.dj.util.GlobalConstant.RequstUri.PLAYER_SCORE;

public class GroupListener implements ListenerHost {

    /**
     * 群监听
     *
     * @param event
     */
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        long groupId = event.getGroup().getId();
        //监听到的群消息不是当前设置的群,则不往下执行
        if (groupId != GroupRobot.GROUPID) {
            return;
        }
        //玩家所发送的消息
        String userSendMsg = event.getMessage().contentToString();
        //玩家
        final PlayerBo playerBo = new PlayerBo(String.valueOf(event.getSender().getId()), event.getSender().getNick(), event.getGroup().getName());
        //获取消息中的汉字
        String chinese = BetUtil.getChinese(userSendMsg);
        try {
            Map<String, String> keyMap = InstructionRegexUtil.findKeyByMsg(userSendMsg);
            final List<BetsTypeBo> betsTypeBos = new ArrayList<>();
            keyMap.forEach((key, msg) -> {
                GlobalConstant.Action action = findActionByKey(key);
                switch (action) {
                    case UPPER_SCORE:
                        // TODO 告知前端有上分请求
                        Score upper = Score.builder()
                                .money(BetUtil.getNumber(userSendMsg))
                                .is_upper(true)
                                .player(playerBo)
                                .build();
                        //发送请求
                        robotRequest2Server(event, POST, PLAYER_SCORE, upper);
                        break;
                    case LOWER_SCORE:
                        // TODO 告知前端有下分请求
                        Score lower = Score.builder()
                                .money(BetUtil.getNumber(userSendMsg) * (-1))
                                .is_upper(false)
                                .player(playerBo)
                                .build();
                        //发送请求
                        robotRequest2Server(event, POST, PLAYER_SCORE, lower);
                        break;
                    case QUERY_SCORE:
                        CheckScore checkScore = CheckScore.builder()
                                .account(playerBo.getAccount())
                                .gameType(GAME_INDEX.toString())
                                .groupName(playerBo.getGroupName())
                                .nickname(playerBo.getNickname())
                                .period(NEW_OPEN_DATA.getNextexpect())
                                .build();

                        robotRequest2Server(event, GET, PLAYER_SCORE, checkScore);
                        break;

                    case BETS_DA:
                    case BETS_XIAO:
                    case BETS_DAN:
                    case BETS_SHUANG:
                        //大小单双
                        betsTypeBos.add(BetsTypeBo.builder().content(msg).key(key).money(BetUtil.getNumber(msg)).build());
                        break;
                    case BETS_DING_WEI:

                        break;
                    default:
                        throw new ClientErrorException("攻击格式错误");
                }
            });

            if (!betsTypeBos.isEmpty()) {
                OrderBo orderBo = OrderBo.builder()
                        .betsTypes(betsTypeBos)
                        .content(userSendMsg)
                        .period(NEW_OPEN_DATA.getNextexpect())
                        .player(playerBo)
                        .gameType(GAME_INDEX.toString())
                        .build();
                //发送请求
                player2Server(event, POST, PLAYER_BET, orderBo);
            }
        } catch (ClientErrorException e) {
            event.getGroup().sendMessage(MessageUtils.newChain(new At(event.getSender()))
                    .plus("\r")
                    .plus(e.getMessage()));
        }
    }


}
