package com.dj.handler;

import com.alibaba.fastjson.JSONArray;
import com.dj.entity.pojo.request.order.ResultFinanceBo;
import com.dj.entity.vo.BetsOrderVo;
import com.dj.net.HttpClient;
import com.dj.util.GateWay;
import com.dj.util.HttpResult;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dj.entity.pojo.response.OpenData.NEW_OPEN_DATA;
import static com.dj.net.HttpClient.FIRST_URL;
import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.RequstUri.PLAYER_ORDER;

/**
 * 在orderhandler里干的事
 * 1.从服务器拿取订单
 * 2.计算开奖
 */
public class OrderHandler {

    public static final GateWay way = new GateWay();

    //计算每个订单的输赢
    public List<ResultFinanceBo> execute() throws InvocationTargetException {

        List<ResultFinanceBo> openResultVos = new ArrayList<>();
        HttpResult result = HttpClient.httpGet(FIRST_URL+PLAYER_ORDER.getUri() + "/" + GAME_INDEX + "/" + NEW_OPEN_DATA.getExpect(), USER_TOKEN,null);
        if (result.isIsok()) {
            Object data = result.getData();
            if (data == null ) {
                return openResultVos;
            }
            System.out.println(data.toString());
            JSONArray jsonData = (JSONArray) data;
            List<BetsOrderVo> betsOrderVos = jsonData.toJavaList(BetsOrderVo.class);
            for (BetsOrderVo orderVo:
                    betsOrderVos) {
                Map<String,Object> parameter = parseBetContent(orderVo);
                ResultFinanceBo resultFinanceBo = null;
                try {
                    resultFinanceBo = (ResultFinanceBo) way.gateWay(GAME_TYPE.toString(), orderVo.getKey(), parameter);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                openResultVos.add(resultFinanceBo);
            }

        } else {
            System.err.println(result.getMessage());
        }

        return openResultVos;
    }

    //解析每个订单
    private Map<String , Object> parseBetContent(BetsOrderVo betsOrderVo) {
        Map<String,Object> resultMap = new HashMap<>();
        Action action = Action.findActionByKey(betsOrderVo.getKey());
        switch (action) {
            case BETS_SHUANG:
            case BETS_XIAO:
            case BETS_DAN:
            case BETS_DA:
                resultMap.put("money",betsOrderVo.getBetsMoney());
                resultMap.put("odd",betsOrderVo.getOdds());
                resultMap.put("account",betsOrderVo.getAccount());
                resultMap.put("orderId",betsOrderVo.getOrderId());
                resultMap.put("content",betsOrderVo.getContent());
                resultMap.put("gameType",GAME_INDEX);
                break;
            case BETS_DING_WEI:
                break;
        }


        return resultMap;
    }


}
