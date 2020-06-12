package com.dj.controller;

import com.alibaba.fastjson.JSONObject;
import com.dj.Exception.ClientErrorException;
import com.dj.entity.pojo.request.robot.RobotInfo;
import com.dj.entity.pojo.request.robot.RobotInfoByBot;
import com.dj.net.VertxWebClient;
import com.dj.robot.MiraiBot;
import com.dj.util.DozerUtils;
import com.dj.util.HttpResult;
import com.dj.util.StringUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.network.LoginFailedException;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.dj.App.controllers;
import static com.dj.net.VertxWebClient.RestFul.*;
import static com.dj.net.VertxWebClient.httpRequest;
import static com.dj.robot.GroupRobot.ROBOT;
import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.RequstUri.ROBOT_INFO;
import static com.dj.util.SimpleTools.simpleTools;

public class RobotLoginController implements Initializable {

    @FXML
    private Label systemLabel;

    @FXML
    private Label qqAccountLabel;

    @FXML
    private ComboBox<RobotInfo> qqAccountComboBox;

    @FXML
    private Label qqPasswordLabel;

    @FXML
    @Getter
    private PasswordField qqPasswordTextField;

    @FXML
    private Button loginQQButton;

    @Getter
    @Setter
    private Stage robotLoginStage;

    private Long qqAccount = null;

    private int i = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);
        qqAccountComboBox.setConverter(new StringConverter<RobotInfo>() {
            @Override
            public String toString(RobotInfo object) {
                if (object == null) return "";
                return object.getAccount();
            }

            @Override
            public RobotInfo fromString(String string) {

                return RobotInfo.builder().account(string).build();
            }
        });
        //设置连接选择框被选中
        qqAccountComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            i++;
            System.out.println("qqAccountComboBox被选中了" + i +"次");
            try {
                System.out.println("newValue = "+newValue.getAccount());
            }catch (Exception e ){
                System.out.println("newValue为null");
            }
            try {
                System.out.println("oldValue = "+oldValue.getAccount());
            }catch (Exception e ){
                System.out.println("oldValue为null");
            }


            if (newValue != null) {
                qqAccount = Long.parseLong(newValue.getAccount());
                if (newValue.getPassword() != null) {
                    qqPasswordTextField.setText(newValue.getPassword());
                }
            }
        });
    }




//    /**
//     * 监听点击下拉框事件
//     * @param event
//     */
//    @FXML
//    void qqAccountComboBoxActionEvent(ActionEvent event) {
//        System.out.println("qqAccountComboBox被点击了");
//
//
//    }

    /**
     * 监听"登录"按钮事件
     * @param event
     */
    @FXML
    void loginQQButtonEvent(ActionEvent event) {
        //账号非空检测
        if (qqAccount == null) {
            simpleTools.informationDialog(Alert.AlertType.NONE,"登录机器人","失败","请选择或输入QQ");
            return;
        }

        //密码非空检测
        if (qqPasswordTextField.getText() == null) {
            simpleTools.informationDialog(Alert.AlertType.NONE,"登录机器人","失败","请填写密码");
            return;
        }


        try {
            Bot bot = MiraiBot.getInstance(qqAccount, qqPasswordTextField.getText());
            //检测是否已经登录
            if (ROBOT != null && ROBOT.getId() ==  bot.getId()) throw new ClientErrorException(ROBOT.getId()+"->"+ROBOT.getNick()+"：已经登录，请勿重复登录");

            bot.login();

            //添加QQ信息到服务器
            RobotInfoByBot robot = RobotInfoByBot.builder()
                    .account(qqAccount.toString())
                    .nickname(bot.getNick())
                    .password(qqPasswordTextField.getText())
                    .build();
            addRobotInfo(robot);
            if (ROBOT != null) ROBOT.close(new ClientErrorException("关闭失败"));
            ROBOT = bot;
            boolean flag = simpleTools.informationDialog(Alert.AlertType.INFORMATION, "登录机器人", "成功", "点击确定返回主页");
            if (flag) {
                robotLoginStage.close();
                BillTabPageController billTabPageController = (BillTabPageController) controllers.get(BillTabPageController.class.getName());
                billTabPageController.setLoginRobot();
            }
        } catch (LoginFailedException e){
            simpleTools.informationDialog(Alert.AlertType.ERROR,"登录机器人","失败",StringUtil.subString(e.getMessage(),"message=","。"));
        } catch (Exception e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR,"登录机器人","失败",e.getMessage());
        }

    }


    private void addRobotInfo(final RobotInfoByBot robot){
        //检测账号是否在列表中
        List<RobotInfo> robotInfoList = qqAccountComboBox.getItems().stream()
                .filter(robotInfo -> robotInfo.getAccount().equals(robot.getAccount()))
                .collect(Collectors.toList());
        //如果存在
        if (!robotInfoList.isEmpty()) {
            //判断密码是否修改了
            if (!robotInfoList.get(0).getPassword().equals(robot.getPassword())){
                addOrUpdate2Server(ROBOT_INFO.getUri()+"/"+robot.getId(),robot,PUT);
            }
        } else {
            //如果不存在
            addOrUpdate2Server(ROBOT_INFO.getUri(),robot,POST);
        }


    }


    public void setQqAccountComboBoxItem(){
        httpRequest(PORT,HOST,ROBOT_INFO.getUri(),null,USER_TOKEN, GET,
                res -> {
                    if (res.succeeded()) {
                        HttpResult result = res.result().body();
                        if (result.isIsok()) {
                            List<LinkedHashMap> list = (ArrayList<LinkedHashMap>) result.getData();
                            if (list.isEmpty()) {
                                return;
                            }
                            List<RobotInfo> robotInfos = list.stream()
                                    .map(e -> DozerUtils.mapper.map(e, RobotInfo.class))
                                    .collect(Collectors.toList());
                            //先清空,再添加
                            if (!qqAccountComboBox.getItems().isEmpty())   qqAccountComboBox.getItems().clear();

                            qqAccountComboBox.getItems().addAll(robotInfos);
                        }
                    }
                });
    }

    private void addOrUpdate2Server(String uri, RobotInfoByBot robot, VertxWebClient.RestFul method) {
        httpRequest(PORT, HOST,uri,(JSONObject) JSONObject.toJSON(robot),USER_TOKEN, method,
                res -> {
                    HttpResult result = res.result().body();
                    if (!result.isIsok()) {
                        throw new ClientErrorException(result.getMessage());
                    }
                });
    }
}
