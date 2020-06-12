package com.dj.controller;

import com.dj.App;
import com.dj.Exception.ClientErrorException;
import com.dj.robot.GroupRobot;
import com.dj.robot.MiraiBot;
import com.dj.util.AllKeys;
import com.dj.util.ExecutorPool;
import com.dj.util.StringUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import net.mamoe.mirai.Bot;

import java.io.File;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.dj.util.GlobalConstant.IMAGE_PATH;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class MainController1 implements Initializable {

    @FXML
    private Text result;

    @FXML
    private TextField qq_acount;

    @FXML
    private TextField qq_password;

    @FXML
    private TextField groupId;

    @FXML
    private Text error;

    private App application;

    public void setApp(App application) {
        this.application = application;
    }



    public void OUT_M(ActionEvent actionEvent) {

    }

    public void qq_login(ActionEvent actionEvent) {
        try {
            ExecutorPool.executorService.execute(()->{
                try{

                    Bot bot = MiraiBot.getInstance(Integer.parseInt(qq_acount.getText()), qq_password.getText());
                    bot.login();
                    showAlter("GroupRobot","登录QQ","成功", INFORMATION);
                    GroupRobot.set(bot,Long.parseLong(groupId.getText()));
                    bot.join();
                }catch (Exception e){
                    e.printStackTrace();
                    showAlter("GroupRobot","登录QQ","失败：" + StringUtil.subString(e.getMessage(),"message=","。"), ERROR);
                }
            });
        } catch (Exception e) {
            error.setText(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //qq登出
    public void qq_close(ActionEvent actionEvent) {
        try {
            GroupRobot.ROBOT.close(new ClientErrorException(""));
            showAlter("GroupRobot","退出QQ","成功", INFORMATION);
        } catch (NoSuchElementException e) {
            showAlter("GroupRobot","退出QQ","失败：" + e.getMessage(), ERROR);
        }
    }

    //开始游戏
    public void openGame(ActionEvent actionEvent) {
//        //TODO 检测qq是否登录
//        //检测是否有群
//        try {
//            try {
//                GroupRobot.ROBOT.getGroup(GroupRobot.GROUPID);
//            } catch (NoSuchElementException e) {
//                throw new ClientErrorException("当前群不存在");
//            }
//            //选择彩种
//            GAME_INDEX = "AoZhou5";
//            GAME_TYPE = "ShiShiCai";
//            GroupRobot.listen();
////            OpenData.setNewOpenData((OpenData) new GroupHandler().getOpenData(true));
//            new GroupHandler().openGamePoller();
//            System.out.println("当前的开奖数据为:"+NEW_OPEN_DATA.toString());
//        }catch (Exception e) {
//            showAlter("GroupRobot","开始游戏","失败：" + e.getMessage(), ERROR);
//
//        }
//
//

    }



    /**
     * 异步的弹框
     * @param contentText
     */
    private void showAlter(String title,String header,String contentText,Alert.AlertType type) {
        Platform.runLater(() ->{
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            if (null != contentText) alert.setContentText(contentText);
            Optional<ButtonType> buttonType = alert.showAndWait();
        });

    }


    public void betsCheack(ActionEvent actionEvent) {
//        System.err.println("攻击核对请求中");
//        System.out.println("攻击核对的期号为:"+NEW_OPEN_DATA.getNextexpect());
//        GroupRobot.robotRequest2Server(VertxWebClient.RestFul.GET,PLAYER_BETS_CHECK.getUri()+"/"+GAME_INDEX+"/"+NEW_OPEN_DATA.getNextexpect(),null);
//        List<BetsOrderVo> orderVos = new OrderHandler().getOrderVos();
    }

//    public void open(ActionEvent actionEvent) {
//        new GroupHandler().Open();
//    }

    public void sendLishiImg(ActionEvent actionEvent) {
        GroupRobot.sendImg(() -> new File(IMAGE_PATH + AllKeys.DEFAULT_DOWN_30 +".png"));
    }
}
