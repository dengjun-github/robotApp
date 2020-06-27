package com.dj.handler.openHandler;

import com.dj.annotation.MyService;
import com.dj.annotation.RequestMessage;
import com.dj.entity.pojo.response.OpenData;
import com.dj.util.BetUtil;
import com.dj.util.GlobalConstant;
import common.Client;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import serialize.pojo.BetsContent;
import serialize.pojo.BetsContentOption;
import serialize.pojo.Odds;
import utils.App;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@MyService
public class ShiShiCai {

    private static final int HE = 5;


    @RequestMessage(path = "BetsDa")
    public BetsContentOption da(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsXiao")
    public BetsContentOption xiao(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsDan")
    public BetsContentOption dan(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsShuang")
    public BetsContentOption shuang(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsLong")
    public BetsContentOption betsLong(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsHu")
    public BetsContentOption betsHu(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsHeOfLongHu")
    public BetsContentOption betsHeOfLongHu(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsDanMa")
    public BetsContentOption betsDanMa(BetsContent betsContent) {
        return getOption4DanMaAndLianMa(betsContent);
    }

    @RequestMessage(path = "BetsLianMa")
    public BetsContentOption betsLianMa(BetsContent betsContent) {
        return getOption4DanMaAndLianMa(betsContent);
    }

    @RequestMessage(path = "BetsDingWeiOfCode")
    public BetsContentOption betsDingWeiOfCode(BetsContent betsContent) {
        String openCode = OpenData.NEW_OPEN_DATA.getOpencode();
        List<Integer> lotteryCode = Arrays.stream(openCode.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        //解析下注格式
        String[] stArr = betsContent.getContent().split("/");
        int betLocal = Integer.parseInt(stArr[0]);

        int betCode = Integer.parseInt(stArr[1]);

        BetsContentOption option = BetsContentOption.builder()
                .betsOrderKey(betsContent.getBetsOrderKey())
                .content(betsContent.getContent())
                .money(betsContent.getMoney())
                .id(betsContent.getId())
                .build();

        //对应的位置上的号码是否一致
        if (lotteryCode.get(betLocal - 1).equals(betCode)) {
            //是,则中奖
            option.setStatus(true);
            option.setResultMoney(betsContent.getMoney().multiply(new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds())));
        } else {
            //否则不中奖
            option.setStatus(false);
            option.setResultMoney(new BigDecimal(0));
        }
        return option;
    }

    @RequestMessage(path = "BetsDingweiOfDaXiao")
    public BetsContentOption betsDingweiOfDaXiao(BetsContent betsContent) {
        String openCode = OpenData.NEW_OPEN_DATA.getOpencode();
        List<String> daxiaoList = Arrays.stream(openCode.split(",")).map(Integer::parseInt).map(code -> {
            if (code > HE) return "大";
            else return "小";
        }).collect(Collectors.toList());
        List<String> danshuangList = Arrays.stream(openCode.split(",")).map(Integer::parseInt).map(code -> {
            if (code % 2 == 0) return "双";
            else return "单";
        }).collect(Collectors.toList());

        List<String> lotteryResult = new ArrayList<>();
        for (int i = 0; i < daxiaoList.size(); i++) {
            lotteryResult.add(daxiaoList.get(i) + "," + danshuangList.get(i));
        }

        //解析位置
        int location = Integer.parseInt(betsContent.getContent().substring(0, 1));
        //解析单个下注玩法
        String content = BetUtil.getChinese(betsContent.getContent());

        BetsContentOption option = BetsContentOption.builder()
                .betsOrderKey(betsContent.getBetsOrderKey())
                .content(betsContent.getContent())
                .money(betsContent.getMoney())
                .id(betsContent.getId())
                .build();
        //根据位置取开奖结果中的字符串是否包含下注内容
        if (lotteryResult.get(location - 1).contains(content)) {
            //是,中奖
            //是,则中奖
            option.setStatus(true);
            option.setResultMoney(betsContent.getMoney().multiply(new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds())));
        } else {
            //不是,则不中
            option.setStatus(false);
            option.setResultMoney(new BigDecimal(0));
        }

        return option;
    }

    @RequestMessage(path = "BetsDaDan")
    public BetsContentOption betsDaDan(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsDaShuang")
    public BetsContentOption betsDaShuang(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsXiaoDan")
    public BetsContentOption betsXiaoDan(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsXiaoShuang")
    public BetsContentOption betsXiaoShuang(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsSanZuHe")
    public BetsContentOption betsSanZuHe(BetsContent betsContent) {
        return setBetsContentOption(betsContent, getOdd(betsContent));
    }

    @RequestMessage(path = "BetsBaoZi")
    public BetsContentOption betsBaoZi(BetsContent betsContent) {
        return setBetsContentOption(betsContent, getOdd(betsContent));
    }

    @RequestMessage(path = "BetsShunZi")
    public BetsContentOption betsShunZi(BetsContent betsContent) {
        return setBetsContentOption(betsContent, getOdd(betsContent));
    }

    @RequestMessage(path = "BetsLiangZuHe")
    public BetsContentOption betsLiangZuHe(BetsContent betsContent) {
        return setBetsContentOption(betsContent, getOdd(betsContent));
    }

    @RequestMessage(path = "BetsBanShunZi")
    public BetsContentOption betsBanShunZi(BetsContent betsContent) {
        return setBetsContentOption(betsContent, getOdd(betsContent));
    }

    @RequestMessage(path = "BetsZaLiu")
    public BetsContentOption betsZaLiu(BetsContent betsContent) {
        return setBetsContentOption(betsContent, getOdd(betsContent));
    }

    @RequestMessage(path = "BetsDuiZi")
    public BetsContentOption betsDuiZi(BetsContent betsContent) {
        return setBetsContentOption(betsContent, getOdd(betsContent));
    }

    @RequestMessage(path = "BetsHeZhi")
    public BetsContentOption betsHeZhi(BetsContent betsContent) {
        int number = BetUtil.getNumber4Hezhi(betsContent.getContent());
        int sum = OpenData.getSum(OpenData.NEW_OPEN_DATA);
        BetsContentOption option = BetsContentOption.builder()
                .betsOrderKey(betsContent.getBetsOrderKey())
                .content(betsContent.getContent())
                .money(betsContent.getMoney())
                .id(betsContent.getId())
                .build();
        if (number == sum) {
            //中了
            option.setStatus(true);
            option.setResultMoney(betsContent.getMoney().multiply(new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds())));
        } else {
            //不中
            option.setStatus(false);
            option.setResultMoney(new BigDecimal(0));
        }

        return null;
    }

    @RequestMessage(path = "BetsJiDa")
    public BetsContentOption betsJiDa(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsJiXiao")
    public BetsContentOption betsJiXiao(BetsContent betsContent) {
        return setBetsContentOption(betsContent, new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds()));
    }

    @RequestMessage(path = "BetsDingWeiLong")
    public BetsContentOption betsDingWeiLong(BetsContent betsContent) {
        return getOption4DingWeiLongHuHe(betsContent);
    }
    @RequestMessage(path = "BetsDingWeiHu")
    public BetsContentOption betsDingWeiHu(BetsContent betsContent) {
        return getOption4DingWeiLongHuHe(betsContent);
    }

    @RequestMessage(path = "BetsDingWeiHe")
    public BetsContentOption betsDingWeiHe(BetsContent betsContent) {
        return getOption4DingWeiLongHuHe(betsContent);
    }


    //针对单码和连码的计算
    @NotNull
    private BetsContentOption getOption4DanMaAndLianMa(BetsContent betsContent) {
        String openCode = OpenData.NEW_OPEN_DATA.getOpencode();
        List<String> lotteryCode = Arrays.asList(openCode.split(","));
        //开奖号码中出现用户所选择号码的次数

        String[] codes = betsContent.getContent().split("/");

        int betCode = Integer.parseInt(codes[0]);
        int count = Collections.frequency(lotteryCode, betCode);
        BetsContentOption option = BetsContentOption.builder()
                .betsOrderKey(betsContent.getBetsOrderKey())
                .content(betsContent.getContent())
                .money(betsContent.getMoney())
                .id(betsContent.getId())
                .build();
        if (count == 0) {
            //0次代表没中
            //不中
            option.setStatus(false);
            option.setResultMoney(new BigDecimal(0));
        } else {
            String[] odds = App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds().split("\\|");
            BigDecimal odd = null;
            if (betsContent.getBetsOrderKey().equals(GlobalConstant.Action.BETS_LIANMA.getKey())) {
                //拿到玩家所下的号码是多少连
                String[] numbers = codes[0].split("");
                odd = new BigDecimal(odds[numbers.length - 2]);
            }
            if (betsContent.getBetsOrderKey().equals(GlobalConstant.Action.BETS_LIANMA.getKey())) {
                odd = new BigDecimal(odds[count - 1]);
            }


            option.setStatus(true);
            option.setResultMoney(betsContent.getMoney().multiply(odd));
        }


        return option;
    }

    //针对大小单双龙虎合的计算
    @NotNull
    private BetsContentOption setBetsContentOption(BetsContent betsContent, BigDecimal odd) {
        BetsContentOption option = BetsContentOption.builder()
                .betsOrderKey(betsContent.getBetsOrderKey())
                .content(betsContent.getContent())
                .money(betsContent.getMoney())
                .id(betsContent.getId())
                .build();
        String game = BetUtil.getChinese(betsContent.getContent());
        if (OpenData.newCodeResultList.contains(game)) {
            //中了
            option.setStatus(true);
            option.setResultMoney(betsContent.getMoney().multiply(new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds())));
        } else {
            //不中
            option.setStatus(false);
            option.setResultMoney(new BigDecimal(0));
        }
        return option;
    }

    //针对定位龙虎合的计算输赢
    @NotNull
    private BetsContentOption getOption4DingWeiLongHuHe(BetsContent betsContent) {
        String openCode = OpenData.NEW_OPEN_DATA.getOpencode();
        List<String> lotteryCode = Arrays.asList(openCode.split(","));

        List<Integer> betLocal = BetUtil.findLocal4DingWeiLongHuHe(betsContent.getContent());
        int firstCode = Integer.parseInt(lotteryCode.get(betLocal.get(0)));
        int secondCode = Integer.parseInt(lotteryCode.get(betLocal.get(1)));
        String result = null;
        if (firstCode > secondCode) result = "龙";
        else if (firstCode < secondCode) result = "虎";
        else result = "合";

        String game = StringUtils.substring(betsContent.getContent(), 2);
        BetsContentOption option = BetsContentOption.builder()
                .betsOrderKey(betsContent.getBetsOrderKey())
                .content(betsContent.getContent())
                .money(betsContent.getMoney())
                .id(betsContent.getId())
                .build();
        if (result.equals(game)) {
            //中了
            option.setStatus(true);
            option.setResultMoney(betsContent.getMoney().multiply(new BigDecimal(App.getOdds(Client.account, betsContent.getBetsOrderKey()).getOdds())));
        } else {
            option.setStatus(false);
            option.setResultMoney(new BigDecimal(0));
        }
        return option;
    }


    //三区形态的赔率
    private BigDecimal getOdd(BetsContent betsContent) {
        String game = BetUtil.getChinese(betsContent.getContent());
        //中了
        Odds obj = App.getOdds(Client.account, betsContent.getBetsOrderKey());
        List<String> keywords = Arrays.asList(obj.getKeyword().split("\\|"));
        String[] odds = obj.getOdds().split("\\|");
        int index = keywords.indexOf(game);
        return new BigDecimal(odds[index]);
    }
}
