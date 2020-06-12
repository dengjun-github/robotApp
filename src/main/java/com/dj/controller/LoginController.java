package com.dj.controller;

import com.alibaba.fastjson.JSONObject;
import com.dj.App;
import com.dj.entity.pojo.request.user.UserLogin;
import com.dj.entity.pojo.response.Root;
import com.dj.util.*;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import static com.dj.net.VertxWebClient.RestFul.GET;
import static com.dj.net.VertxWebClient.RestFul.POST;
import static com.dj.net.VertxWebClient.httpRequest;
import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.RequstUri.CONFIG;
import static com.dj.util.GlobalConstant.RequstUri.LOGIN;
import static com.dj.util.SimpleTools.simpleTools;


public class LoginController implements Initializable {

    @FXML
    private Label systemLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private TextField userNameTextField;

    @FXML
    private Label passwordLabel;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button logupButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button bindCardButton;


    private App application;

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public void initialize(URL location, ResourceBundle resources) {
        Labeled[] labeleds = new Labeled[]{logupButton, resetButton,bindCardButton};
        Arrays.asList(labeleds).forEach(labeled -> {
            labeled.setCursor(Cursor.HAND);
        });

        // 给组件添加图标
/*
        Labeled[] labeleds = new Labeled[]{systemLabel, userNameLabel, passwordLabel, logupButton, resetButton};
        String[] imagePaths = new String[]{"src/BookManageSystem/images/logo.png", "src/BookManageSystem/images/userName.png",
                "src/BookManageSystem/images/password.png",
                "src/BookManageSystem/images/login.png", "src/BookManageSystem/images/reset.png"};
        simpleTools.setLabeledImage(labeleds, imagePaths);
*/
    }




    public void setApp(App application) {
        this.application = application;
    }



    /**
     * 监听"登录"按钮事件
     * @param actionEvent
     */
    @FXML
    public void logupButtonEvent(ActionEvent actionEvent) {
        httpRequest(PORT, HOST,LOGIN.getUri(),(JSONObject) JSONObject.toJSON(new UserLogin(userNameTextField.getText(),passwordTextField.getText())),null, POST,
                res -> {
                    if (res.succeeded()) {
                        HttpResult result = res.result().body();
                        if (result.isIsok()) {
                            JSONObject data = (JSONObject)JSONObject.toJSON(result.getData());
                            //保存token
                            USER_TOKEN = (String) data.get("token");
                            //保存配置
                            setUserRoot(USER_TOKEN);
                            //跳转到main页面
                            Platform.runLater(() -> application.gotoIndex());
                        } else {
                            Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"机器人登录","错误",result.getMessage()));
                        }
                    } else {
                        Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"机器人登录","错误","服务器异常,请检查ip地址"));
                    }
                });
    }

    /**
     * 监听""注册"按钮事件
     * @param actionEvent
     */
    @FXML
    public void resetButtonEvent(ActionEvent actionEvent) {
        application.gotoRegister();
    }

    /**
     * 监听"绑卡"按钮事件
     * @param actionEvent
     */
    @FXML
    public void bindCardEvent(ActionEvent actionEvent) {
        application.gotoBind();
    }

    public void setUser(String account,String password) {
        this.userNameTextField.setText(account);
        this.passwordTextField.setText(password);
    }

    public void setUser(String account) {
        this.userNameTextField.setText(account);
    }

    //读取并保存配置项
    private void setUserRoot(String token){
        httpRequest(PORT,HOST,CONFIG.getUri(),null,token,GET,
                res -> {
                    if (res.succeeded()) {
                        HttpResult result = res.result().body();
                        if (result.isIsok()) {
                            LinkedHashMap data = (LinkedHashMap) result.getData();
                            Root root = DozerUtils.mapper.map(data, Root.class);
                            InstructionRegexUtil.initRegexMap(root);
                            //初始化图片
                            initImg(root);
                            USER_ROOT = root;
                        } else {
                            simpleTools.informationDialog(Alert.AlertType.ERROR,"获取用户配置项","错误",result.getMessage());
                        }
                    } else {
                        simpleTools.informationDialog(Alert.AlertType.ERROR,"获取用户配置项","错误","服务器异常,请检查ip地址");
                    }
                });
    }


    private void initImg(Root root){
        root.getImageSettings().forEach(imageSettings -> {
            if (AllKeys.START_BETS.equals(imageSettings.getKey()))
                imageVo.setStartBets(FileUtil.saveUrlAs(imageSettings.getImageUrl(), IMAGE_PATH , AllKeys.START_BETS,"GET"));
            if (AllKeys.STOP_BETS_TIME.equals(imageSettings.getKey()))
                imageVo.setStopBetsTime(FileUtil.saveUrlAs(imageSettings.getImageUrl(), IMAGE_PATH , AllKeys.STOP_BETS_TIME,"GET"));
            if (AllKeys.DEFAULT_DOWN_30.equals(imageSettings.getKey()))
                imageVo.setDefaultDown30(FileUtil.saveUrlAs(imageSettings.getImageUrl(), IMAGE_PATH , AllKeys.DEFAULT_DOWN_30,"GET"));
        });

        System.out.println("图片初始化完成");
    }
}
