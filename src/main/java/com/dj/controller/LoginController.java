package com.dj.controller;

import com.alibaba.fastjson.JSONObject;
import com.dj.App;
import com.dj.pojo.response.Root;
import com.dj.pojo.request.user.UserLogin;
import com.dj.net.WebClientVerticle;
import com.dj.util.DozerUtils;
import com.dj.util.GlobalConstant;
import com.dj.util.HttpResult;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.RequstUri.*;


public class LoginController implements Initializable {

    @FXML
    private Text error;

    @FXML
    private TextField account;

    @FXML
    private PasswordField password;

    private App application;

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();


    public LoginController() {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setApp(App application) {
        this.application = application;
    }



    //登录
    @FXML
    public void loginInTo(ActionEvent actionEvent) {
        WebClientVerticle.httpPost(PORT, HOST,LOGIN.getUri(),(JSONObject) JSONObject.toJSON(new UserLogin(account.getText(),password.getText())),null,
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
                            Platform.runLater(() -> application.gotoMain());

                        } else {
                            error.setText(result.getMessage());
                        }
                    } else {
                        error.setText("服务器异常");
                    }
                });
    }

    private boolean checkLogin(String username, String password) {

        return false;
    }

    public void setUser(String account,String password) {
        this.account.setText(account);
        this.password.setText(password);
    }

    public void setUser(String account) {
        this.account.setText(account);
    }

    public void sign(ActionEvent actionEvent) {
        application.gotoRegister();
    }

    public void bind(ActionEvent actionEvent) {
        application.gotoBind();
    }

    public void enter(DragEvent dragEvent) {
        System.out.println("键盘事件enter");
    }

    //读取并保存配置项
    private void setUserRoot(String token){
        WebClientVerticle.httpGet(PORT,HOST,CONFIG.getUri(),null,token,
                res -> {
                    if (res.succeeded()) {
                        HttpResult result = res.result().body();
                        if (result.isIsok()) {
                            //JSONObject.toJavaObject((JSONObject),Root.class)
                            LinkedHashMap data = (LinkedHashMap) result.getData();
                            Root root = DozerUtils.mapper.map(data, Root.class);
                            USER_ROOT.setRoot(root);
                            System.out.println("root为: " + USER_ROOT);
                            GlobalConstant.Action.setUp(USER_ROOT);
                        } else {
                            error.setText(result.getMessage());
                        }
                    } else {
                        error.setText("服务器异常");
                    }
                });

    }
}
