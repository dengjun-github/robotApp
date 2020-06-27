package com.dj.controller;

import com.dj.util.SettingUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import serialize.pojo.Keys;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.module.ImageShow.IMAGE_SHOW;
import static com.dj.util.SimpleTools.simpleTools;
import static serialize.pojo.Keys.*;

public class MessageTabPageController implements Initializable {

    @FXML
    private CheckBox closeImgCheckbox;

    @FXML
    private CheckBox openBetCheckBox;

    @FXML
    private CheckBox openBet2CheckBox;

    @FXML
    private CheckBox countdownImgCheckBox;

    @FXML
    private TextField countdownTextField;

    @FXML
    private CheckBox closeCheckBox;

    @FXML
    private TextField closeSecTextField;

    @FXML
    private CheckBox gameExplainCheckBox;

    @FXML
    private TextArea gameExplainTextArea;

    @FXML
    private CheckBox openBetTextCheckBox;

    @FXML
    private TextArea openBetTextArea;

    @FXML
    private CheckBox openMessageCheckBox;

    @FXML
    private TextArea openLotteryTextArea;

    @FXML
    private CheckBox openHistoryCheckBox;

    @FXML
    private CheckBox reversePatternCheckBox;

    @FXML
    private TextArea openHistoryTextArea;

    @FXML
    private CheckBox betSuccessCheckBox;

    @FXML
    private TextArea betSuccessTextArea;

    @FXML
    private CheckBox closeMessageCheckBox;

    @FXML
    private TextArea closeMessageTextArea;

    @FXML
    private CheckBox overrunBetCheckBox;

    @FXML
    private TextArea overrunBetTextArea;

    @FXML
    private CheckBox querySuccessMessageCheckBox;

    @FXML
    private TextArea querySuccessTextArea;

    @FXML
    private CheckBox closeCancelBetCheckBox;

    @FXML
    private TextArea closeCancelTextArea;

    @FXML
    private CheckBox closeGameCheckBox;

    @FXML
    private TextArea closeGameTextArea;

    @FXML
    private CheckBox betCancelCheckBox;

    @FXML
    private TextArea betCancelTextArea;

    @FXML
    private CheckBox balanceNotEnoughCheckBox;

    @FXML
    private TextArea balanceNotEnoughTextArea;

    @FXML
    private CheckBox formatErrorCheckBox;

    @FXML
    private TextArea formatErrorTextArea;

    @FXML
    private CheckBox queryWaterCheckBox;

    @FXML
    private TextArea queryWaterTextArea;

    @FXML
    private CheckBox startGameCheckBox;

    @FXML
    private TextArea startGameTextArea;

    @FXML
    private CheckBox overrunLowerCheckBox;

    @FXML
    private TextArea overrunLowerTextArea;

    @FXML
    private ComboBox<?> resultImgComboBox;

    @FXML
    private CheckBox resultImgCheckBox;

    @FXML
    private CheckBox historyImgCheckBox;

    @FXML
    private ComboBox<?> historyImgComboBox;

    @FXML
    private ComboBox<?> historyCountComboBox;

    @FXML
    private AnchorPane firstAn;

    @FXML
    private TextField sendMsg1TextField;

    @FXML
    private CheckBox sendMsg1CheckBox;

    @FXML
    private TextArea sendMsg1TextArea;

    @FXML
    private TextField sendMsg2TextField;

    @FXML
    private CheckBox sendMsg2CheckBox;

    @FXML
    private TextArea sendMsg2TextArea;

    @FXML
    private AnchorPane secondAn;

    @FXML
    private TextField sendMsg3TextField;

    @FXML
    private CheckBox sendMsg3CheckBox;

    @FXML
    private TextArea sendMsg3TextArea;

    @FXML
    private TextField sendMsg4TextField;

    @FXML
    private CheckBox sendMsg4CheckBox;

    @FXML
    private TextArea sendMsg4TextArea;

    @FXML
    private CheckBox privateBetCheckBox;

    @FXML
    private Button nextPageButton;

    private Stage openBetImgStage = new Stage();;

    private Stage countdownImgStage = new Stage();

    private Stage closeBetImgStage= new Stage();


    final FileChooser fileChooser = new FileChooser();

    final Stage fileChooserStage = new Stage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);

        CheckBox[] checkBoxes = new CheckBox[]{
                closeImgCheckbox,openBetCheckBox,openBet2CheckBox,countdownImgCheckBox,closeCheckBox,openBetTextCheckBox,gameExplainCheckBox,openMessageCheckBox,openHistoryCheckBox,
                /*reversePatternCheckBox,betSuccessCheckBox,betCancelCheckBox,closeMessageCheckBox,balanceNotEnoughCheckBox,overrunBetCheckBox,formatErrorCheckBox,querySuccessMessageCheckBox,
                queryWaterCheckBox,closeCancelBetCheckBox,startGameCheckBox,closeGameCheckBox,overrunLowerCheckBox,privateBetCheckBox,sendMsg1CheckBox,sendMsg2CheckBox,sendMsg3CheckBox,sendMsg4CheckBox,
                resultImgCheckBox,historyImgCheckBox*/
        };
        String[] keys = new String[]{
                SETTINGS_STOP_BETS_IMAGE,SETTINGS_START_BETS_IMAGE,SETTINGS_START_BETS_IMAGE2,SETTINGS_COUNTDOWN_IMAGE,SETTINGS_STOP_BETS_TIME,SETTINGS_START_BET_MESSAGE,SETTINGS_GAME_EXPLAIN_MESSAGE,
                SETTINGS_LOTTERY_OPEN_MESSAGE,SETTINGS_LOTTERY_HISTORY_MESSAGE
        };






    }

    @FXML
    void closeCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void closeImgCheckboxActionEvent(ActionEvent event) {

    }

    /**
     *  监听"封盘图片查看"按钮点击事件
     * @param event
     */
    @FXML
    void closeImgShowButtonActionEvent(ActionEvent event) {
        try {
            IMAGE_SHOW.create("封盘图片查看", SettingUtils.IMAGE_SETTINGS_MAP.get(SETTINGS_STOP_BETS_IMAGE),closeBetImgStage);
        } catch (Exception e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR,"封盘图片查看","错误",e.getMessage());
        }
    }

    /**
     * 监听"封盘图片更换"按钮点击事件
     * @param event
     */
    @FXML
    void closeImgbuttonActionEvent(ActionEvent event) {
        configureFileChooser(fileChooser,"封盘图片更换");
        File file = fileChooser.showOpenDialog(fileChooserStage);
        if (null != file && file.exists()) {
            //TODO 向服务器上传图片

            SettingUtils.IMAGE_SETTINGS_MAP.put(SETTINGS_STOP_BETS_IMAGE,file);
        }

    }

    /**
     * 监听"倒计时图片更换"按钮点击事件
     * @param event
     */
    @FXML
    void countdownButtonActionEvent(ActionEvent event) {
        configureFileChooser(fileChooser,"倒计时图片更换");
        File file = fileChooser.showOpenDialog(fileChooserStage);
        if (null != file && file.exists()) {
            //TODO 向服务器上传图片
            SettingUtils.IMAGE_SETTINGS_MAP.put(Keys.SETTINGS_COUNTDOWN_IMAGE,file);
        }
    }

    @FXML
    void countdownImgCheckBoxActionEvent(ActionEvent event) {

    }

    /**
     * 监听"倒计时图片查看"按钮点击事件
     * @param event
     */
    @FXML
    void countdownImgShowButtonActionEvent(ActionEvent event) {
        try {
            IMAGE_SHOW.create("倒计时图片查看", SettingUtils.IMAGE_SETTINGS_MAP.get(Keys.SETTINGS_COUNTDOWN_IMAGE),countdownImgStage);
        } catch (Exception e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR,"倒计时图片查看","错误",e.getMessage());
        }
    }

    @FXML
    void gameExplainCheckBoxActionEvent(ActionEvent event) {

    }


    /**
     * 监听"下一页"按钮点击事件
     * @param event
     */
    @FXML
    void nextPageButtonActionEvent(ActionEvent event) {
        if (nextPageButton.getText().equals("下一页")) {
            firstAn.setVisible(false);
            secondAn.setVisible(true);
            nextPageButton.setText("上一页");
        } else {
            firstAn.setVisible(true);
            secondAn.setVisible(false);
            nextPageButton.setText("下一页");
        }
    }

    @FXML
    void openBet2CheckBoxActionEvent(ActionEvent event) {

    }

    /**
     * 监听"开始攻击图片2查看"按钮点击事件
     * @param event
     */
    @FXML
    void openBet2ImgShowButtonActionEvent(ActionEvent event) {
        try {
            IMAGE_SHOW.create("开始图片2查看", SettingUtils.IMAGE_SETTINGS_MAP.get(SETTINGS_START_BETS_IMAGE2),openBetImgStage);
        } catch (Exception e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR,"封盘图片查看","错误",e.getMessage());
        }
    }

    /**
     * 监听"开始攻击图片2更换"按钮点击事件
     * @param event
     */
    @FXML
    void openBet2ImgbuttonActionEvent(ActionEvent event) {
        configureFileChooser(fileChooser,"开始攻击图片2更换");
        File file = fileChooser.showOpenDialog(fileChooserStage);
        if (null != file && file.exists()) {
            SettingUtils.IMAGE_SETTINGS_MAP.put(SETTINGS_START_BETS_IMAGE2,file);
        }
    }

    @FXML
    void openBetCheckBoxActionEvent(ActionEvent event) {

    }

    /**
     * 监听"开始攻击图片查看"按钮点击事件
     * @param event
     */
    @FXML
    void openBetImgShowButtonActionEvent(ActionEvent event) {
        try {
            IMAGE_SHOW.create("开始图片查看", SettingUtils.IMAGE_SETTINGS_MAP.get(SETTINGS_START_BETS_IMAGE),openBetImgStage);
        } catch (Exception e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR,"开始图片查看","错误",e.getMessage());
        }
    }

    /**
     * 监听"开始攻击图片更换"按钮点击事件
     * @param event
     */
    @FXML
    void openBetImgbuttonActionEvent(ActionEvent event) {
        configureFileChooser(fileChooser,"开始攻击图片更换");
        File file = fileChooser.showOpenDialog(fileChooserStage);
        if (null != file && file.exists()) {
            SettingUtils.IMAGE_SETTINGS_MAP.put(SETTINGS_START_BETS_IMAGE,file);
        }
    }

    @FXML
    void openBetTextCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void openHistoryCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void openMessageCheckBoxActionEvent(ActionEvent event) {

    }

    private static void configureFileChooser(final FileChooser fileChooser,String title) {
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

}
