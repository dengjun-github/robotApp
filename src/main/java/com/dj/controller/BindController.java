package com.dj.controller;

import api.card.BindCard;
import api.future.BindCardFutureListener;
import api.future.Future;
import com.dj.Application;
import com.dj.util.ErrorCodeUtil;
import com.dj.util.GlobalConstant;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import serialize.pojo.Account;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.util.SimpleTools.simpleTools;

public class BindController implements Initializable {

    @FXML
    private Button returnButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField accountTextField;

    @FXML
    private TextField cardTextField;



    private Application application;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);
        simpleTools.setBackgrondColor(minimizeButton);
        simpleTools.setBackgrondColor(closeButton);
        simpleTools.setBackgrondColor(returnButton);
    }

    /**
     * 监听"绑定"按钮事件
     * @param event
     */
    @FXML
    void bindEvent(ActionEvent event) {
        Future future = BindCard.get(cardTextField.getText(), accountTextField.getText());
        future.addListener(new BindCardFutureListener() {
            @Override
            public void onSuccess(Account account) {
                //跳转到login页面
                Platform.runLater(() -> {
                    boolean dialog = simpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "绑定卡密", "成功", "点击确定返回登录");
                    if (dialog) {
                        Application.ALL_STAGE.get(GlobalConstant.StageInfo.RENEW).show();
                        LoginController loginController = (LoginController) controllers.get(LoginController.class.getName());
                        loginController.setUser(account.getUsername(),account.getPassword());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"绑定卡密","失败","错误码:"+errorCode +"\n"+"错误信息:"+ ErrorCodeUtil.getMsgByErroCode(errorCode)));
            }
        });
    }

    /**
     * 监听"关闭"按钮事件
     * @param event
     */
    @FXML
    void closeButtonActionEvent(ActionEvent event){
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.BIND).close();
    }

    /**
     * 监听"最小化"按钮事件
     * @param event
     */
    @FXML
    void minimizeButtonActionEvent(ActionEvent event) {
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.RENEW).setIconified(true);
    }

    /**
     * 监听"返回"按钮事件
     * @param event
     */
    @FXML
    void returnButtonActionEvent(ActionEvent event) {
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.BIND).close();
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.LOGIN).show();
    }


    public void setApp(Application application) {
        this.application = application;
    }

}
