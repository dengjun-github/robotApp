package com.dj.controller;

import com.alibaba.fastjson.JSONObject;
import com.dj.App;
import com.dj.pojo.request.user.UserBind;
import com.dj.net.WebClientVerticle;
import com.dj.util.HttpResult;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.RequstUri.*;

public class BindController implements Initializable {

    @FXML
    private TextField account;

    @FXML
    private TextField security_key;

    private App application;

    @FXML
    private Text error;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void bind(ActionEvent actionEvent) {
        WebClientVerticle.httpPost(PORT, HOST,BIND.getUri(),(JSONObject) JSONObject.toJSON(new UserBind(account.getText(),security_key.getText())),null,
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
                            error.setText(result.getMessage());
                        }
                    } else {
                        error.setText("服务器异常");
                    }
                });
    }

    public void clear(ActionEvent actionEvent) {

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
