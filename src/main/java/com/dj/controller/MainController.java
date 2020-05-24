package com.dj.controller;

import com.dj.App;
import com.dj.core.Robot;
import com.dj.core.event.RobotEvent;
import com.dj.util.ExecutorPool;
import com.dj.util.StringUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.Events;

import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Text result;

    @FXML
    private TextField qq_acount;

    @FXML
    private TextField qq_password;

    @FXML
    private Text error;

    private App application;

    public void setApp(App application) {
        this.application = application;
    }



    public void OUT_M(ActionEvent actionEvent) {

    }

    public void qq_login(ActionEvent actionEvent) {
        try {
            ExecutorPool.executorService.execute(()->{
                try{
                    Robot robot = new Robot(Integer.parseInt(qq_acount.getText()),qq_password.getText());
                    Bot bot = robot.Login();
                    Events.registerEvents(bot, new RobotEvent());
                    showAlter("Robot","登录QQ","成功",Alert.AlertType.NONE);
                    bot.join();
                }catch (Exception e){
                    e.printStackTrace();
                    showAlter("Robot","登录QQ","失败：" + StringUtil.subString(e.getMessage(),"message=","。"),Alert.AlertType.ERROR);
                }
            });
        } catch (Exception e) {
            error.setText(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void qq_close(ActionEvent actionEvent) {
        try {
            Bot instance = Bot.getInstance(Long.parseLong(qq_acount.getText()));
            instance.close(new Throwable());
            showAlter("Robot","退出QQ","成功",Alert.AlertType.NONE);
        } catch (NoSuchElementException e) {
            showAlter("Robot","退出QQ","失败：" + e.getMessage(),Alert.AlertType.ERROR);
        }
    }

    /**
     * 异步的弹框
     * @param contentText
     */
    private void showAlter(String title,String header,String contentText,Alert.AlertType type) {
        Platform.runLater(() ->{
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            if (null != contentText) alert.setContentText(contentText);
            Optional<ButtonType> buttonType = alert.showAndWait();
        });

    }
}
