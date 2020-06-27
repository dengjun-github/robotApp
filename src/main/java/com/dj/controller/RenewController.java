package com.dj.controller;

import api.card.BindCard;
import api.future.BindCardFutureListener;
import api.future.Future;
import com.dj.Application;
import com.dj.util.DateUtil;
import com.dj.util.ErrorCodeUtil;
import com.dj.util.GlobalConstant;
import common.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import serialize.pojo.Account;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.util.SimpleTools.simpleTools;

public class RenewController implements Initializable {

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField cardTextField;

    @FXML
    private Label usernameLabel;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(), this);
        simpleTools.setBackgrondColor(minimizeButton);
        simpleTools.setBackgrondColor(closeButton);

        usernameLabel.setText(Client.account.getUsername());


    }

    /**
     * 监听"关闭"按钮点击事件
     *
     * @param event
     * @throws Exception
     */
    @FXML
    void closeButtonActionEvent(ActionEvent event) throws Exception {
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.RENEW).close();
    }


    /**
     * "最小化"按钮点击事件
     *
     * @param event
     */
    @FXML
    void minimizeButtonActionEvent(ActionEvent event) {
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.RENEW).setIconified(true);
    }

    @FXML
    void renewEvent(ActionEvent event) {
        Future future = BindCard.get(cardTextField.getText(),Client.account.getUsername());
        future.addListener(new BindCardFutureListener() {
            @Override
            public void onSuccess(Account account) {
                //跳转到login页面
                Platform.runLater(() -> {
                    boolean dialog = simpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "绑定卡密", "成功", "点击确定返回");
                    if (dialog) {
                        Application.ALL_STAGE.get(GlobalConstant.StageInfo.RENEW).close();
                        AccountController accountController = (AccountController) controllers.get(AccountController.class.getName());
                        accountController.getExpirationTimeLabel().setText(DateUtil.toYmdhms(account.getExpireTime()));
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"绑定卡密","失败","错误码:"+errorCode +"\n"+"错误信息:"+ ErrorCodeUtil.getMsgByErroCode(errorCode)));
            }
        });
    }


}
