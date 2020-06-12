package com.dj.controller;

import com.alibaba.fastjson.JSONObject;
import com.dj.App;
import com.dj.entity.pojo.request.user.UserRegister;
import com.dj.util.HttpResult;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.dj.net.VertxWebClient.RestFul.POST;
import static com.dj.net.VertxWebClient.httpRequest;
import static com.dj.util.GlobalConstant.HOST;
import static com.dj.util.GlobalConstant.PORT;
import static com.dj.util.GlobalConstant.RequstUri.REGISTER;
import static com.dj.util.SimpleTools.simpleTools;

public class RegisterController implements Initializable {
    @FXML
    private Button returnButton;

    @FXML
    private Label systemLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private TextField account;

    @FXML
    private Label passwordLabel;

    @FXML
    private PasswordField password;

    @FXML
    private Label confirmLabel;

    @FXML
    private PasswordField confirm;

    @FXML
    private Button resetButton;

    @FXML
    private Button clearButton;

    private App application;


    public void setApp(App application) {
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * 监听"注册"按钮事件
     * @param actionEvent
     */
    public void registerEvent(ActionEvent actionEvent) {
        httpRequest(PORT, HOST,REGISTER.getUri(),(JSONObject) JSONObject.toJSON(new UserRegister(account.getText(),password.getText(),confirm.getText())),null, POST,
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
                            Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"注册","错误",result.getMessage()));
                        }
                    } else {
                        Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"注册","错误","服务器异常,请检查ip再试"));
                    }
                });
    }

    /**
     * 监听"重置"按钮事件
     * @param actionEvent
     */
    public void clearEvent(ActionEvent actionEvent) {
        account.setText(null);
        password.setText(null);
        confirm.setText(null);
    }

    /**
     * 监听"返回"按钮事件
     * @param actionEvent
     */
    public void returnEvent(ActionEvent actionEvent) {
        application.gotologin();
    }
}
