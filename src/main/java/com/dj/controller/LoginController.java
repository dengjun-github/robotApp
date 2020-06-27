package com.dj.controller;

import api.account.Login;
import api.future.Future;
import api.future.LoginFutureListener;
import com.dj.Application;
import com.dj.Exception.ClientErrorException;
import com.dj.util.ErrorCodeUtil;
import com.dj.util.GlobalConstant;
import com.dj.util.SettingUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import serialize.pojo.Account;
import serialize.pojo.Keys;
import test.MyReceiver;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.util.GlobalConstant.StageInfo.*;
import static com.dj.util.SettingUtils.IS_REMEMBER_PASSWORD;
import static com.dj.util.SimpleTools.simpleTools;


public class LoginController implements Initializable {

    public static final String remember_Img_1 = "src/main/resources/images/rememberPassword1.png";

    public static final String remember_Img_2 = "src/main/resources/images/rememberPassword2.png";

    public static final String IS_REMEMBER = "isRemember";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";


    @FXML
    private AnchorPane rootLayout;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField userNameTextField;

    @FXML
    private ImageView rememberImageView;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private ComboBox<Keys.Game> gameTypeComboBox;

    private File file;

    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(), this);
        simpleTools.setBackgrondColor(minimizeButton);
        simpleTools.setBackgrondColor(closeButton);

        IS_REMEMBER_PASSWORD.addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                file = new File(remember_Img_1);
                try {
                    if (file.exists()) {
                        rememberImageView.setImage(new Image(file.toURI().toURL().toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                file = new File(remember_Img_2);
                try {
                    if (file.exists()) {
                        rememberImageView.setImage(new Image(file.toURI().toURL().toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        gameTypeComboBox.setConverter(new StringConverter<Keys.Game>() {
            @Override
            public String toString(Keys.Game object) {
                if (object == null) return "";
                return object.getDesc();
            }

            @Override
            public Keys.Game fromString(String string) {

                return null;
            }
        });


        gameTypeComboBox.getItems().addAll(Keys.Game.AO_ZHOU_5,Keys.Game.HE_NEI);

        //选择彩种

        gameTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                GlobalConstant.PLAYING_GAME = newValue;
            } else if (oldValue != null && newValue == null) {
                gameTypeComboBox.getSelectionModel().select(oldValue);
            }
        });


        //读取
        Map<String, Object> map = loadAccount();
        if (map != null) {
            Boolean isRemember = (Boolean) map.get(IS_REMEMBER);
            if (isRemember) {
                userNameTextField.setText(map.get(USERNAME).toString());
                passwordTextField.setText(map.get(PASSWORD).toString());
                IS_REMEMBER_PASSWORD.set(isRemember);
            }
        }


    }


    /**
     * 监听"登录"按钮事件
     *
     * @param event
     */
    @FXML
    void loginButtonActionEvent(ActionEvent event) {
        try {
            if (userNameTextField.getText() == null) {
                throw new ClientErrorException("账号不能为空");
            }
            if (passwordTextField.getText() == null) {
                throw new ClientErrorException("密码不能为空");
            }
            if (gameTypeComboBox.getSelectionModel().getSelectedItem() == null) {
                throw new ClientErrorException("请选择彩种");
            }
            Future future = Login.deal(new MyReceiver(), userNameTextField.getText(), passwordTextField.getText());

            future.addListener(new LoginFutureListener() {

                @Override
                public void onSuccess(Account account) {
                    SettingUtils.initImg();
                    writeAccount(account.getUsername(), account.getPassword());
                    Platform.runLater(() -> {
                        //关闭登录界面,打开首页
                        simpleTools.createStage(INDEX, null).show();
                        Application.ALL_STAGE.get(LOGIN).close();
                    });
                }

                @Override
                public void onFailure(int errorCode) {
                    Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR, "账号登录", "失败", "错误码:" + errorCode + "\n" + "错误信息:" + ErrorCodeUtil.getMsgByErroCode(errorCode)));
                }
            });
        } catch (Exception e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR, "账号登录", "失败", e.getMessage());
        }

    }

    /**
     * 监听""注册"按钮事件
     *
     * @param event
     */
    @FXML
    void registerButtonActionEvent(ActionEvent event) {
        //关闭登录界面,打开注册页面
        simpleTools.createStage(REGISTER, null).show();
        Application.ALL_STAGE.get(LOGIN).close();
    }

    /**
     * 监听"记住密码"按钮点击事件
     *
     * @param event
     */
    @FXML
    void rememberPasswordButtonActionEvent(ActionEvent event) {
        if (IS_REMEMBER_PASSWORD.getValue()) IS_REMEMBER_PASSWORD.set(false);
        else IS_REMEMBER_PASSWORD.set(true);

    }

    /**
     * 监听"找回密码"按钮点击事件
     *
     * @param event
     */
    @FXML
    void resetPasswordButtonActionEvent(ActionEvent event) {
        simpleTools.createStage(FIND_PASSWORD, null).show();
        Application.ALL_STAGE.get(LOGIN).close();
    }

    /**
     * 监听"充值"按钮事件
     *
     * @param event
     */
    @FXML
    void rechargeButtonActionEvent(ActionEvent event) {
        //关闭登录界面,打开充值页面
        simpleTools.createStage(BIND, null).show();
        Application.ALL_STAGE.get(LOGIN).close();
    }

    /**
     * 监听"关闭"按钮点击事件
     *
     * @param event
     * @throws Exception
     */
    @FXML
    void closeButtonActionEvent(ActionEvent event) throws Exception {
        Application.ALL_STAGE.get(LOGIN).close();
    }


    /**
     * "最小化"按钮点击事件
     *
     * @param event
     */
    @FXML
    void minimizeButtonActionEvent(ActionEvent event) {
        Application.ALL_STAGE.get(LOGIN).setIconified(true);
    }

    private Map<String, Object> loadAccount() {
        File file = new File("src/main/resources/user.properties");
        Reader reader = null;
        Properties prop = new Properties();
        try {
            reader = new FileReader(file);
            prop.load(reader);

            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            Boolean isRemember = Boolean.parseBoolean(prop.getProperty("isRemember"));

            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password) && isRemember != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);
                map.put("isRemember", isRemember);
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    private void writeAccount(String username, String password) {
        FileWriter writer = null;
        try {
            Properties prop = new Properties();

            File file = new File("src/main/resources/user.properties");
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new FileWriter(file);
            prop.setProperty("username", username);
            prop.setProperty("password", password);
            prop.setProperty("isRemember", String.valueOf(IS_REMEMBER_PASSWORD.get()));
            prop.store(writer, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setUser(String account, String password) {
        this.userNameTextField.setText(account);
        this.passwordTextField.setText(password);
    }


}
