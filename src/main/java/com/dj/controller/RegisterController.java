package com.dj.controller;

import api.account.Register;
import api.future.Future;
import api.future.RegisterFutureListener;
import com.dj.Application;
import com.dj.Exception.ClientErrorException;
import com.dj.util.ErrorCodeUtil;
import com.dj.util.GlobalConstant;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.util.SimpleTools.simpleTools;

public class RegisterController implements Initializable {

    @FXML
    private Button returnButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField accountTextField;

    @FXML
    private PasswordField confirmSuperPasswordTextFeild;

    @FXML
    private PasswordField superPasswordTextFeild;

    @FXML
    private PasswordField confirmPasswordTextFeild;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button registerButton;

    @FXML
    private Label accountErrorLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Label confirmPasswordErrorLabel;

    @FXML
    private Label superPasswordErrorLabel;

    @FXML
    private Label confirmSuperPasswordErrorLabel;

    @Setter
    private Application application;

    private String username;

    private String password;

    private String superCode;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);

        simpleTools.setBackgrondColor(minimizeButton);
        simpleTools.setBackgrondColor(closeButton);
        simpleTools.setBackgrondColor(returnButton);
        checkNull(accountTextField,accountErrorLabel,"账号不能为空","username");
        checkNull(passwordTextField,passwordErrorLabel,"密码不能为空","");
        checkNull(superPasswordTextFeild,superPasswordErrorLabel,"超级密码不能为空","");
        chekeAccordance(confirmPasswordTextFeild,passwordTextField,confirmPasswordErrorLabel,"两次密码不一致","password");
        chekeAccordance(confirmSuperPasswordTextFeild,superPasswordTextFeild,confirmSuperPasswordErrorLabel,"两次超级密码不一致","superCode");
    }

    /**
     * 监听"关闭"按钮事件
     * @param event
     */
    @FXML
    void closeButtonActionEvent(ActionEvent event) throws Exception {
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.REGISTER).close();
    }

    /**
     * 监听"最小化"按钮事件
     * @param event
     */
    @FXML
    void minimizeButtonActionEvent(ActionEvent event) {
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.REGISTER).setIconified(true);
    }

    /**
     * 监听"返回"按钮事件
     * @param event
     */
    @FXML
    void returnButtonActionEvent(ActionEvent event) {
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.LOGIN).show();
        Application.ALL_STAGE.get(GlobalConstant.StageInfo.REGISTER).close();
    }

    /**
     * 监听"注册"按钮事件
     * @param event
     */
    @FXML
    void registerButtonActionEvent(ActionEvent event) {
        try {
            if (simpleTools.isEmpty(username)) {
               throw new ClientErrorException("账号不能为空");
            }
            if (simpleTools.isEmpty(password)) {
               throw new ClientErrorException("密码不能为空");
            }
            if (simpleTools.isEmpty(superCode)) {
               throw new ClientErrorException("超级密码不能为空");
            }


            Future register = Register.register(username, password, superCode);
            register.addListener(new RegisterFutureListener() {
                @Override
                public void onSuccess(String s) {
                    Platform.runLater(() -> {

                        boolean dialog = simpleTools.informationDialog(Alert.AlertType.INFORMATION, "账号注册", "成功", "注册成功,是否进行充值?\n点击确定进行绑卡充值,点击取消返回登录");

                        if (dialog) {
                            Application.ALL_STAGE.get(GlobalConstant.StageInfo.BIND).show();
                        }else {
                            Application.ALL_STAGE.get(GlobalConstant.StageInfo.LOGIN).show();
                        }
                        Application.ALL_STAGE.get(GlobalConstant.StageInfo.REGISTER).close();
                    });
                }
                @Override
                public void onFailure(int errorCode) {
                    Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"账号注册","失败","错误码:"+errorCode +"\n错误信息:"+ ErrorCodeUtil.getMsgByErroCode(errorCode)));
                }
            });
        } catch (Exception e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR,"账号注册","失败",e.getMessage());
        }
    }


    /**
     * 非空检查
     * @param text
     * @param errorLabel
     * @param msg
     * @param param
     */
    private void checkNull(TextInputControl text,Label errorLabel, String msg, String param) {
        text.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                if (null != text.getText() && !text.getText().equals("")) {
                    if (param.equals("username")) username = text.getText();
                    errorLabel.setText(null);
                } else {
                    errorLabel.setText(msg);
                }
            }
        });
    }

    /**
     * 密码是否一致检查
     * @param field
     * @param compare
     * @param errorLabel
     * @param msg
     * @param param
     */
    public void chekeAccordance(PasswordField field,PasswordField compare,Label errorLabel, String msg, String param){
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                if (null != field.getText() && !field.getText().equals("") && null != compare.getText() && !compare.getText().equals("") && compare.getText().equals(field.getText())) {
                    if (param.equals("password")) password = field.getText();
                    if (param.equals("superCode")) superCode = field.getText();
                    errorLabel.setText(null);
                } else {
                    errorLabel.setText(msg);
                }
            }
        });
    }

}
