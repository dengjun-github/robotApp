package com.dj.controller;

import api.account.AddRobot;
import api.account.AllRobot;
import api.finance.AllRobotFutureListener;
import api.future.AddRobotFutureListener;
import api.future.Future;
import com.dj.Application;
import com.dj.Exception.ClientErrorException;
import com.dj.robot.MiraiBot;
import com.dj.util.ExecutorPool;
import com.dj.util.GlobalConstant;
import com.dj.util.StringUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.network.LoginFailedException;
import serialize.pojo.Robot;
import test.ClientInfo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.dj.Application.controllers;
import static com.dj.robot.GroupRobot.ROBOT;
import static com.dj.util.SimpleTools.simpleTools;

public class RobotLoginController implements Initializable {

    @FXML
    private AnchorPane rootLayout;

    @FXML
    private VBox loginVBox;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private ComboBox<Robot> qqAccountComboBox;

    @FXML
    @Getter
    private PasswordField qqPasswordTextField;

    @FXML
    private AnchorPane waitAnchorPane;


    private Long qqAccount = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);
        qqAccountComboBox.setConverter(new StringConverter<Robot>() {
            @Override
            public String toString(Robot object) {
                if (object == null) return "";
                return object.getAccount();
            }

            @Override
            public Robot fromString(String string) {

                return Robot.builder().account(string).build();
            }
        });
        //设置连接选择框被选中
        qqAccountComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                qqAccount = Long.parseLong(newValue.getAccount());
                if (newValue.getPassword() != null) {
                    qqPasswordTextField.setText(newValue.getPassword());
                }
            }
        });

    }

    @FXML
    void closeButtonActionEvent(ActionEvent event) {

    }

    @FXML
    void minimizeButtonActionEvent(ActionEvent event) {

    }

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

        loginVBox.setOpacity(0.33);

        waitAnchorPane.setVisible(true);
        ExecutorPool.executorService.execute(() -> {
            try {
                Bot bot = MiraiBot.getInstance(qqAccount, qqPasswordTextField.getText());
                //检测是否已经登录
                if (ROBOT != null && ROBOT.getId() ==  bot.getId()) throw new ClientErrorException(ROBOT.getId()+"->"+ROBOT.getNick()+"：已经登录，请勿重复登录");
                bot.login();
                addRobotInfo(qqAccount.toString(),qqPasswordTextField.getText(),bot.getNick());
                if (ROBOT != null) ROBOT.close(new ClientErrorException("关闭失败"));
                ROBOT = bot;
                Platform.runLater(() ->{
                    loginVBox.setOpacity(1);
                    waitAnchorPane.setVisible(false);

                    boolean flag = simpleTools.informationDialog(Alert.AlertType.INFORMATION, "登录机器人", "成功", "点击确定返回主页");
                    if (flag) {
                        Application.ALL_STAGE.get(GlobalConstant.StageInfo.ROBOT_LOGIN).close();
                        BillTabPageController billTabPageController = (BillTabPageController) controllers.get(BillTabPageController.class.getName());
                        billTabPageController.setLoginRobot();
                    }
                });

            } catch (LoginFailedException e){
                Platform.runLater(() ->{
                    simpleTools.informationDialog(Alert.AlertType.ERROR,"登录机器人","失败",StringUtil.subString(e.getMessage(),"message=","。"));
                    loginVBox.setOpacity(1);
                    waitAnchorPane.setVisible(false);
                });

            } catch (Exception e) {
                Platform.runLater(() ->{
                    simpleTools.informationDialog(Alert.AlertType.ERROR,"登录机器人","失败",e.getMessage());
                    loginVBox.setOpacity(1);
                    waitAnchorPane.setVisible(false);
                });

            }
        });

    }


    private void addRobotInfo(String account,String password,String nickname){
        //检测账号是否在列表中
        List<Robot> robotInfoList = qqAccountComboBox.getItems().stream()
                .filter(robot -> robot.getAccount().equals(account))
                .collect(Collectors.toList());
        //如果存在
        if (!robotInfoList.isEmpty()) {
            //判断密码是否修改了
            if (!robotInfoList.get(0).getPassword().equals(password)){
                addOrUpdate2Server(account,password,nickname);
            }
        } else {
            //如果不存在
            addOrUpdate2Server(account,password,nickname);
        }


    }

    public void setQqAccountComboBoxItem(){
        //先清空,再添加
        if (!qqAccountComboBox.getItems().isEmpty())  {
            qqAccountComboBox.getItems().clear();
        }

        Future future = AllRobot.deal();
        future.addListener(new AllRobotFutureListener() {
            public void onSuccess(List<Robot> robots) {
                qqAccountComboBox.getItems().addAll(robots);
            }

            public void onFailure(int code) {
                System.out.println("失败:" + code);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void addOrUpdate2Server(String account,String password,String nickname) {
        Future future = AddRobot.deal(account, password, nickname);
        future.addListener(new AddRobotFutureListener() {
            public void onSuccess() {
                System.out.println("机器人添加成功");
                System.out.print(ClientInfo.username + " > ");
            }

            public void onFailure(int code) {
                System.out.println("失败：" + code);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }
}
