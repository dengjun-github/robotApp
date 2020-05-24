package com.dj.controller;

import com.alibaba.fastjson.JSONObject;
import com.dj.App;
import com.dj.pojo.request.user.UserRegister;
import com.dj.net.WebClientVerticle;
import com.dj.util.HttpResult;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.RequstUri.REGISTER;

public class RegisterController implements Initializable {

    @FXML
    private Text error;

    @FXML
    private TextField account;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirm;

    private App application;


    public void setApp(App application) {
        this.application = application;
    }

    //用户注册
    public void sign(ActionEvent actionEvent) {
        WebClientVerticle.httpPost(PORT, HOST,REGISTER.getUri(),(JSONObject) JSONObject.toJSON(new UserRegister(account.getText(),password.getText(),confirm.getText())),null,
                res -> {
                    if (res.succeeded()) {
                        HttpResult result = res.result().body();
                        if (result.isIsok()) {
                            //跳转到main页面
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"注册成功", new ButtonType("返回登录", ButtonBar.ButtonData.YES));
                                alert.setHeaderText(null);
                                Optional<ButtonType> buttonType = alert.showAndWait(); //将在对话框消失以前不会执行之后的代码
                                if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                                    LoginController login = application.gotologin();
                                    login.setUser(account.getText(),password.getText());
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void back(ActionEvent actionEvent) {
        application.gotologin();
    }
}
