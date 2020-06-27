package com.dj.controller;

import com.dj.util.DateUtil;
import common.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.util.GlobalConstant.StageInfo.ACCOUNT;
import static com.dj.util.SimpleTools.simpleTools;

public class AccountController implements Initializable {

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private Label usernameLabel;

    @FXML
    @Getter
    private Label expirationTimeLabel;

    @FXML
    private Label accountTypeLabel;

    private BillTabPageController billTabPageController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);
        simpleTools.setBackgrondColor(minimizeButton);
        simpleTools.setBackgrondColor(closeButton);

        billTabPageController = (BillTabPageController) controllers.get(BillTabPageController.class.getName());

        usernameLabel.setText(Client.account.getUsername());
        expirationTimeLabel.setText(DateUtil.toYmdhms(Client.account.getExpireTime()));
        accountTypeLabel.setText(Client.account.getUsername());
    }

    @FXML
    void closeButtonActionEvent(ActionEvent event) {

        billTabPageController.getAccountStage().close();
    }

    @FXML
    void minimizeButtonActionEvent(ActionEvent event) {
        billTabPageController.getAccountStage().setIconified(true);
    }

    @FXML
    void rechargeEvent(ActionEvent event) {
        try {
            Stage renewStage = simpleTools.createStage(ACCOUNT, null);
            renewStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
