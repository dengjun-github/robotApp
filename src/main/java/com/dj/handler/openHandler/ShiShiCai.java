package com.dj.handler.openHandler;

import com.dj.annotation.MyService;
import com.dj.annotation.RequestMessage;
import com.dj.entity.pojo.request.order.ResultFinanceBo;
import com.dj.entity.pojo.response.OpenData;
import com.dj.util.Arith;

import java.util.List;
import java.util.Map;


@MyService
public class ShiShiCai {



    private List<String> codeResultList = OpenData.getCodeResultList(OpenData.NEW_OPEN_DATA);

    @RequestMessage(path = "BetsDa")
    public ResultFinanceBo da (Map<String,Object> param){
        return setResultFinanceBo(param,"大");
    }

    @RequestMessage(path = "BetsXiao")
    public ResultFinanceBo xiao (Map<String,Object> param){
        return setResultFinanceBo(param,"小");
    }

    @RequestMessage(path = "BetsDan")
    public ResultFinanceBo dan (Map<String,Object> param){
        return setResultFinanceBo(param,"单");
    }

    @RequestMessage(path = "BetsShuang")
    public ResultFinanceBo shuang (Map<String,Object> param){
        return setResultFinanceBo(param,"双");
    }


    private ResultFinanceBo setResultFinanceBo(Map<String,Object> param,String game){
        ResultFinanceBo resultFinanceBo = ResultFinanceBo.builder()
                .account(param.get("account").toString())
                .content(param.get("content").toString())
                .gameType(param.get("gameType").toString())
                .orderId((Long) param.get("orderId"))
                .build();
        if (game != null){
            if (codeResultList.contains(game)) {
                //中了
                resultFinanceBo.setIsWin(true);
                resultFinanceBo.setMoney(Arith.mul((Double)param.get("money"),(Double)param.get("odd")));
            } else {
                //不中
                resultFinanceBo.setIsWin(false);
                resultFinanceBo.setMoney(0.00);
            }
        }
     return resultFinanceBo;
    }
}
