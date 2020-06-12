package com.dj.controller;

import com.alibaba.fastjson.JSONObject;
import com.dj.App;
import com.dj.entity.pojo.request.user.UserBind;
import com.dj.util.HttpResult;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.dj.net.VertxWebClient.RestFul.POST;
import static com.dj.net.VertxWebClient.httpRequest;
import static com.dj.util.GlobalConstant.HOST;
import static com.dj.util.GlobalConstant.PORT;
import static com.dj.util.GlobalConstant.RequstUri.BIND;
import static com.dj.util.SimpleTools.simpleTools;

public class BindController implements Initializable {

    @FXML
    private Label systemLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private TextField account;

    @FXML
    private Label securitykeyLabel;

    @FXML
    private TextField security_key;

    @FXML
    private Button bindButton;

    @FXML
    private Button bindCardButton;

    @FXML
    private Button returnButton;


    private App application;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    /**
     * 监听"绑定"按钮事件
     * @param actionEvent
     */
    public void bindEvent(ActionEvent actionEvent) {
        httpRequest(PORT, HOST,BIND.getUri(),(JSONObject) JSONObject.toJSON(new UserBind(account.getText(),security_key.getText())),null, POST,
                res -> {
                    if (res.succeeded()) {
                        HttpResult result = res.result().body();
                        if (result.isIsok()) {
                            //跳转到login页面
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"绑卡成功", new ButtonType("返回登录", ButtonBar.ButtonData.YES));
                                alert.setHeaderText(null);
                                Optional<ButtonType> buttonType = alert.showAndWait(); //将在对话框消失以前不会执行之后的代码
                                if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                                    LoginController login = application.gotologin();
                                    login.setUser(account.getText());
                                }
                            });
                        } else {
                            Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"绑定卡密","错误",result.getMessage()));
                        }
                    } else {
                        Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"绑定卡密","错误","服务器出现未知异常,请联系管理员"));
                    }
                });
    }

    /**
     * 监听"重置"按钮事件
     * @param actionEvent
     */
    public void clear(ActionEvent actionEvent) {
        account.setText(null);
        security_key.setText(null);
    }

    /**
     * 监听"返回"按钮事件
     * @param event
     */
    @FXML
    void retrunEvent(ActionEvent event) {
        application.gotologin();
    }

    public void setApp(App app) {
        this.application = app;
    }

    public void enter(DragEvent dragEvent) {
        System.out.println("键盘事件enter");
    }

    public void back(ActionEvent actionEvent) {
        application.gotologin();
    }
}
