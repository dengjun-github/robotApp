package com.dj.controller;

import api.future.ConfirmScoreFutureListener;
import api.future.Future;
import api.group.ConfirmScore;
import com.dj.Exception.ClientErrorException;
import com.dj.entity.vo.ScoreMessageVo;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageUtils;
import serialize.pojo.Keys;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.robot.GroupRobot.sendMessage;
import static com.dj.util.GlobalConstant.gameIsBegan;
import static com.dj.util.SimpleTools.simpleTools;

public class ScoreManageController implements Initializable {

    //上分列表
    private static final SimpleListProperty<ScoreMessageVo> upperScoreList = new SimpleListProperty<>(FXCollections.observableArrayList());
    //下分列表
    private static final SimpleListProperty<ScoreMessageVo> lowerScoreList = new SimpleListProperty<>(FXCollections.observableArrayList());
    //所有的数据

    @FXML
    private HBox upperHBox;

    @FXML
    private TextField upperContentTextField;

    @FXML
    private TextField upperScoreTextField;

    @FXML
    private CheckBox upperVoiceCheckBox;

    @FXML
    private TableView<ScoreMessageVo> onUpperScoreTable;

    @FXML
    private TableColumn<ScoreMessageVo, String> upperPlayerColumn;

    @FXML
    private TableColumn<ScoreMessageVo, String> upperMessageColumn;

    @FXML
    private TableColumn<ScoreMessageVo, String> upperTypeColumn;

    @FXML
    private HBox lowerHBox;

    @FXML
    private TextField lowerContentTextField;

    @FXML
    private TextField lowerScoreTextField;

    @FXML
    private CheckBox lowerVoiceCheckBox;

    @FXML
    private TableView<ScoreMessageVo> onLowerScoreTable;

    @FXML
    private TableColumn<ScoreMessageVo, String> lowerPlayerColumn;

    @FXML
    private TableColumn<ScoreMessageVo, String> lowerMessageColumn;

    @FXML
    private TableColumn<ScoreMessageVo, String> lowerTypeColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(), this);


        //初始化表格
        upperPlayerColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        upperMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        upperTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        lowerPlayerColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        lowerMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        lowerTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        upperScoreList.addListener((ListChangeListener<? super ScoreMessageVo>) c -> onUpperScoreTable.setItems(upperScoreList));
        lowerScoreList.addListener((ListChangeListener<? super ScoreMessageVo>) c -> onLowerScoreTable.setItems(lowerScoreList));

        onLowerScoreTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lowerContentTextField.setText(newValue.getContent());
                lowerScoreTextField.setText(newValue.getFinance().getMoney() + "");
            }
        });
        onUpperScoreTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                upperContentTextField.setText(newValue.getContent());
                upperScoreTextField.setText(newValue.getFinance().getMoney() + "");
            }
        });
    }


    /**
     * 监听"下分到"按钮事件
     *
     * @param event
     */
    @FXML
    void lowerArriveActionEvent(ActionEvent event) {
        try {
            gameIsBegan();//检查游戏是否开始
            ScoreMessageVo scoreMessageVo = (ScoreMessageVo) simpleTools.isSelect(onLowerScoreTable);

            //发送请求
            confirmScore(scoreMessageVo);
        } catch (ClientErrorException e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR, "下分@喊查", "错误", e.getMessage());
        }
    }

    /**
     * 监听"下分忽略"按钮时间
     *
     * @param event
     */
    @FXML
    void lowerIgnoreActionEvent(ActionEvent event) {
        try {
            gameIsBegan();//检查游戏是否开始
            ScoreMessageVo scoreMessageVo = (ScoreMessageVo) simpleTools.isSelect(onLowerScoreTable);
            lowerScoreList.remove(scoreMessageVo);

        } catch (ClientErrorException e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR, "下分忽略", "错误", e.getMessage());
        }

    }

    /**
     * 监听"上分确认"按钮事件
     *
     * @param event
     */
    @FXML
    void upperArriveActionEvent(ActionEvent event) {
        try {
            gameIsBegan();//检查游戏是否开始
            ScoreMessageVo scoreMessageVo = (ScoreMessageVo) simpleTools.isSelect(onUpperScoreTable);

            //发送请求
            confirmScore(scoreMessageVo);


        } catch (ClientErrorException e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR, "上分@喊到", "错误", e.getMessage());
        }
    }


    /**
     * 监听"上分忽略"按钮事件
     *
     * @param event
     */
    @FXML
    void upperIgnoreActionEvent(ActionEvent event) {
        try {
            gameIsBegan();//检查游戏是否开始
            ScoreMessageVo scoreMessageVo = (ScoreMessageVo) simpleTools.isSelect(onUpperScoreTable);
            upperScoreList.remove(scoreMessageVo);

        } catch (ClientErrorException e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR, "上分忽略", "错误", e.getMessage());
        }
    }

    /**
     * 监听"上分喊没到"按钮事件
     *
     * @param event
     */
    @FXML
    void upperNotArriveActionEvent(ActionEvent event) {
        try {
            gameIsBegan();//检查游戏是否开始
            ScoreMessageVo scoreMessageVo = (ScoreMessageVo) simpleTools.isSelect(onUpperScoreTable);
            upperScoreList.remove(scoreMessageVo);
            sendMessage(scoreMessageVo.getFinance().getMoney() + "没到", scoreMessageVo.getEvent().getSender());
        } catch (ClientErrorException e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR, "上分@喊没到", "错误", e.getMessage());
        }
    }

    /**
     * 上下分通知
     */
    public void scoreNotification(ScoreMessageVo scoreMessageVo) {
        String content = scoreMessageVo.getContent();
        if (scoreMessageVo.getType().equals(Keys.FINANCE_TYPE[0])) {
            upperContentTextField.setText(content);
            upperScoreTextField.setText(scoreMessageVo.getFinance().getMoney() + "");
            upperScoreList.add(scoreMessageVo);
        } else {
            lowerContentTextField.setText(content);
            lowerScoreTextField.setText(scoreMessageVo.getFinance().getMoney() + "");
            lowerScoreList.add(scoreMessageVo);
        }
    }

    private void confirmScore(ScoreMessageVo scoreMessageVo) {
        Future fut = ConfirmScore.deal(scoreMessageVo.getFinance().getId());
        fut.addListener(new ConfirmScoreFutureListener() {
            public void onSuccess(String msg) {
                if (scoreMessageVo.getType().equals(Keys.FINANCE_TYPE[0])) {
                    upperScoreList.remove(scoreMessageVo);
                    if (upperScoreList.isEmpty()) {
                        upperScoreTextField.setText(null);
                        upperContentTextField.setText(null);
                    }
                } else {
                    lowerScoreList.remove(scoreMessageVo);
                    if (lowerScoreList.isEmpty()) {
                        lowerScoreTextField.setText(null);
                        lowerContentTextField.setText(null);
                    }
                }
                scoreMessageVo.getEvent().getGroup().sendMessage(MessageUtils.newChain(new At(scoreMessageVo.getEvent().getSender()))
                        .plus("\r")
                        .plus(msg));
            }

            public void onFailure(int errorCode, String msg) {
                scoreMessageVo.getEvent().getGroup().sendMessage(MessageUtils.newChain(new At(scoreMessageVo.getEvent().getSender()))
                        .plus("\r")
                        .plus(msg));
            }
        });
    }

}
