package com.dj.controller;

import api.account.ChangePassword;
import api.future.ChangePasswordFutureListener;
import api.future.Future;
import com.dj.Application;
import com.dj.util.GlobalConstant;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.util.SimpleTools.simpleTools;

public class FindPasswordController implements Initializable {
    @FXML
    private Button returnButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField accountTextField;

    @FXML
    private PasswordField superPasswordTextFeild;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField confirmPasswordTextFeild;

    @FXML
    private Label confirmPasswordErrorLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(), this);
        simpleTools.setBackgrondColor(minimizeButton);
        simpleTools.setBackgrondColor(closeButton);
        simpleTools.setBackgrondColor(returnButton);

        confirmPasswordTextFeild.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                if (null != confirmPasswordTextFeild.getText() && !confirmPasswordTextFeild.getText().equals("") && confirmPasswordTextFeild.getText().equals(passwordTextField.getText())) {
                    confirmPasswordErrorLabel.setText(null);
                } else {
                    confirmPasswordErrorLabel.setText("两次密码不一致");
                }
            }
        });
    }

    /**
     * 监听"重置密码"按钮事件
     *
     * @param event
     */
    @FXML
    void resetEvent(ActionEvent event) {
        String account = accountTextField.getText();
        String superPassword = superPasswordTextFeild.getText();
        String password = passwordTextField.getText();
        String error = confirmPasswordErrorLabel.getText();
        if (error != null) {
            simpleTools.informationDialog(Alert.AlertType.ERROR, "找回密码", "失败", error);
            return;
        }
        if (account == null || superPassword == null || password == null) {
            simpleTools.informationDialog(Alert.AlertType.ERROR, "找回密码", "失败", "用户名或密码或超级密码不能为空");
        }
        Future future = ChangePassword.deal(account, superPassword, password);
        future.addListener(new ChangePasswordFutureListener() {
            @Override
            public void onSuccess() {
                Platform.runLater(() -> {
                    boolean dialog = simpleTools.informationDialog(Alert.AlertType.ERROR, "找回密码", "成功", "点击确定返回登录");
                    if (dialog) {
                        LoginController login = (LoginController) controllers.get(LoginController.class.getName());
                        login.setUser(account, password);
                    }
                });
            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    /**
     * 监听"关闭"按钮事件
     *
     * @param event
     */
    @FXML
    void closeButtonActionEvent(ActionEvent event) throws Exception {
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.FIND_PASSWORD).close();
    }

    /**
     * 监听"最小化"按钮事件
     *
     * @param event
     */
    @FXML
    void minimizeButtonActionEvent(ActionEvent event) {
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.FIND_PASSWORD).setIconified(true);
    }

    /**
     * 监听"返回"按钮事件
     *
     * @param event
     */
    @FXML
    void returnButtonActionEvent(ActionEvent event) {
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.LOGIN).show();
    }


}
