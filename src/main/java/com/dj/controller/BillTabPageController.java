package com.dj.controller;

import api.future.SendBillFutureListener;
import api.order.SendBill;
import com.dj.Application;
import com.dj.Exception.ClientErrorException;
import com.dj.entity.pojo.request.robot.RobotInfoByGroup;
import com.dj.entity.pojo.response.OpenData;
import com.dj.robot.GroupRobot;
import com.dj.task.CountDownTask;
import com.dj.util.ErrorCodeUtil;
import com.dj.util.ExecutorPool;
import com.dj.util.SettingUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import serialize.pojo.Keys;
import serialize.pojo.option.SendBillOption;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.dj.Application.controllers;
import static com.dj.entity.pojo.response.OpenData.NEW_OPEN_DATA;
import static com.dj.handler.GroupHandler.groupHandler;
import static com.dj.robot.GroupRobot.*;
import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.StageInfo.*;
import static com.dj.util.SimpleTools.simpleTools;

public class BillTabPageController implements Initializable {

    @FXML
    private AnchorPane firstAn;

    @FXML
    private Label openCodeLabel;

    @FXML
    private Label openTimeLabel;

    @FXML
    private Label openResultLabel;

    @FXML
    private CheckBox betVoiceCheckbox;

    @FXML
    private CheckBox closeVoiceCheckBox;

    @FXML
    private CheckBox scoreVoiceCheckBox;

    @FXML
    private CheckBox scoreCheckBox;

    @FXML
    private TableView<?> billTable;

    @FXML
    private HBox countDownHBox;

    @FXML
    private Label closeBetLabel;

    @FXML
    private Label countDownLabel;

    @FXML
    private Button loginRobotButton;

    @FXML
    private Label robotnicknameLabel;

    @FXML
    private Button startButton;

    @FXML
    private Label robotAccountLabel;

    @FXML
    private ComboBox<RobotInfoByGroup> robotGroupComboBox;

    @FXML
    private MenuButton sendImageMenuButton;

    @FXML
    private MenuItem sendBillMenultem;

    @FXML
    private MenuItem sendKaiJiangMenultem;

    @FXML
    private MenuItem sendLiShiMenultem;

    @FXML
    private Button manualOperation;

    @Setter
    @Getter
    private Stage mainStage;

    //上下分管理窗口
    @Getter
    private Stage scoreManageStage;

    //上下分历史窗口
    private Stage scoreHistoryStage;

    //手动开奖窗口
    private Stage manualOpenStage;

    //机器人登录窗口
    private Stage robotLoginStage;

    //添加或查找玩家窗口
    private Stage findOrAddPlayerStage;

    @Getter
    private Stage accountStage;


    private RobotLoginController robotLoginController;

    private CountDownTask countDownTask;

    private ScoreHistoryController scoreHistoryController;

    private ContextMenu contextMenu = new ContextMenu();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);

        //获取开奖数据
        OpenData openData = (OpenData) groupHandler.getOpenData(true);
        OpenData.NEW_OPEN_DATA = openData;
        OpenData.setNewCodeResultList(openData);
        //刷新数据
        refreshOpenData(openData);
        timelabel();

        groupHandler.openGamePoller();
        robotGroupComboBox.setConverter(new StringConverter<RobotInfoByGroup>() {
            @Override
            public String toString(RobotInfoByGroup object) {
                if (object == null) return "";
                return object.getGroupAccount() + "-" + object.getGroupName();
            }

            @Override
            public RobotInfoByGroup fromString(String string) {

                return RobotInfoByGroup.builder().groupAccount(string).build();
            }
        });

        //设置连接选择框被选中
        robotGroupComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                GROUPID = Long.parseLong(newValue.getGroupAccount());
            } else if (oldValue != null && newValue == null) {
                robotGroupComboBox.getSelectionModel().select(oldValue);
            }
        });

        //上下分窗口的监听
        scoreCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Stage scoreManage = Application.ALL_STAGE.get(SCORE_MANAGE);
            if (null == scoreManage) {
                scoreManage = simpleTools.createStage(SCORE_MANAGE,Application.ALL_STAGE.get(INDEX));
            }
            if (scoreCheckBox.isSelected()) {
                scoreManage.show();
                scoreManage.setOnCloseRequest(handler -> {
                    scoreCheckBox.setSelected(false);
                });
            } else {
                scoreManage.close();
            }
        });

        CheckBox[] checkBoxes = new CheckBox[]{scoreVoiceCheckBox,betVoiceCheckbox,closeVoiceCheckBox,scoreCheckBox};

        String [] keys = new String[]{Keys.SETTINGS_UPPER_OR_LOWER_VOICE_TIPS,Keys.SETTINGS_BETS_ORDER_VOICE_TOPS,Keys.SETTINGS_CLOSE_BET_VOICE_TIPS,Keys.SETTINGS_UPPER_OR_LOWER_WINDOW_SHOW};
        simpleTools.initCheckBox(checkBoxes, keys);


        contextMenu.getItems().addAll(
                new MenuItem("修改玩家余额"),
                new MenuItem("更新玩家昵称"),
                new MenuItem("删除选中玩家"),
                new MenuItem("修改玩家昵称"),
                new MenuItem("设置/取消假人"),
                new MenuItem("管理所有假人"),
                new MenuItem("修改玩家下注"),
                new MenuItem("复制玩家QQ"),
                new MenuItem("打开玩家资料夹"),
                new MenuItem("回滚账单"),
                new MenuItem("重新设置机器人QQ"));


    }

    /**
     * 监听"登录机器人"按钮事件
     *
     * @param event
     */
    @FXML
    void loginRobotButtonEvent(ActionEvent event) {
        try {
            //第一次打开
            Stage robotLogin = Application.ALL_STAGE.get(ROBOT_LOGIN);
            if (null == robotLogin) robotLogin = simpleTools.createStage(ROBOT_LOGIN, null);

////
//            if (null != robotLoginController) {
//                robotLoginController.getQqPasswordTextField().setText(null);
//            }
            robotLogin.initModality(Modality.APPLICATION_MODAL);
            robotLogin.show();

            if (null == robotLoginController) {
                robotLoginController = (RobotLoginController) controllers.get(RobotLoginController.class.getName());
            }
            Platform.runLater(robotLoginController::setQqAccountComboBoxItem);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 监听"开始游戏"按钮事件
     *
     * @param event
     */
    @FXML
    void startButtonEvent(ActionEvent event) {
        try {
            //开始游戏
            if (startButton.getText().equals("开始游戏")) {

                GroupRobot.listen();
                SettingUtils.initEventMap();
                IS_GAME_BEGIN = true;

                simpleTools.informationDialog(Alert.AlertType.NONE, "开始游戏", "成功", "点击确定关闭");
                startButton.setText("结束游戏");
            }
            //结束游戏
            else {
                startButton.setText("开始游戏");
                IS_GAME_BEGIN = false;
            }
        } catch (IllegalArgumentException e) {
            simpleTools.informationDialog(Alert.AlertType.NONE, "开始游戏", "失败", "未登录机器人,请先登录");
        } catch (Exception e) {
            simpleTools.informationDialog(Alert.AlertType.NONE, "开始游戏", "失败", e.getMessage());
        }

    }

    /**
     * 监听点击群下拉框事件
     *
     * @param event
     */
    @FXML
    void RobotGroupComboBoxOnClicked(MouseEvent event) {
//        System.out.println("点击了");
        if (null != ROBOT) {
            refreshRobotGroup();
        }
    }

    /**
     * 监听"上下分窗口显示"复选框事件
     *
     * @param event
     */
    @FXML
    void scoreCheckBoxActionEvent(ActionEvent event) {

    }

    /**
     * 监听"发送账单按钮"
     *
     * @param event
     */
    @FXML
    void sendBillMenultemActionEvent(ActionEvent event) {
        try {
            if (!IS_GAME_BEGIN) {
                throw new ClientErrorException("游戏未启动,请先开始游戏");
            }
            if (null == ROBOT) {
                throw new ClientErrorException("您还没有登录机器人,请登录机器人");
            }
            SendBill.deal(new SendBillOption(OpenData.NEW_OPEN_DATA.getExpect(),PLAYING_GAME.getIndex())).addListener(new SendBillFutureListener() {
                @Override
                public void onSuccess(String s) {
                    sendMessage(s, false);
                }

                @Override
                public void onFailure(int errorCode) {
                    Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR, "账号登录", "失败", "错误码:" + errorCode + "\n" + "错误信息:" + ErrorCodeUtil.getMsgByErroCode(errorCode)));
                }
            });

        } catch (Exception e) {
            Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR, "发送账单", "失败", e.getMessage()));
        }
    }

    /**
     * 监听"发送开奖"按钮事件
     *
     * @param event
     */
    @FXML
    void sendKaiJiangMenultemActionEvnet(ActionEvent event) {
        ExecutorPool.executorService.execute(() -> {
            try {
                if (!IS_GAME_BEGIN) {
                    throw new ClientErrorException("游戏未启动,请先开始游戏");
                }
                groupHandler.sendKaiJiangImage();
                Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.NONE, "发送开奖", "成功", "点击确定返回"));
            } catch (Exception e) {
                Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR, "发送开奖图", "失败", e.getMessage()));
            }
        });
    }

    /**
     * 监听"发送历史"按钮事件
     *
     * @param event
     */
    @FXML
    void sendLiShiMenultemActionEvnet(ActionEvent event) {
        ExecutorPool.executorService.execute(() -> {
            try {
                if (!IS_GAME_BEGIN) {
                    throw new ClientErrorException("游戏未启动,请先开始游戏");
                }
                groupHandler.sendLiShiImage();
                Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.NONE, "发送历史图", "成功", "点击确定返回"));
            } catch (Exception e) {
                Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR, "发送历史图", "失败", e.getMessage()));
            }
        });
    }

    /**
     * 监听"手动封盘"按钮事件
     *
     * @param event
     */
    @FXML
    void manualOperationActionEvent(ActionEvent event) {
        String title = manualOperation.getText();
        try {
            if (manualOperation.getText().equals("手动封盘")) {
                if (!IS_GAME_BEGIN) {
                    throw new ClientErrorException("游戏未启动,请开始游戏");
                }
                //封盘

                groupHandler.betVerify();
                manualOperation.setText("手动开盘");
                manualOperation.setStyle("-fx-background-color: #54FF9F");
                inCloseShow();
                closeStatus = true;
            } else {
                //开盘
                groupHandler.betOpen("手动封盘");
                manualOperation.setText("手动封盘");
                //刷新数据
                refreshOpenData(NEW_OPEN_DATA);
                closeStatus = false;
            }
            Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.INFORMATION, title, "成功", "点击确定返回"));
        } catch (Exception e) {
            Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR, title, "失败", e.getMessage()));
        }
    }

    /**
     * 监听"上下分详细历史"按钮事件
     *
     * @param event
     */
    @FXML
    void scoreHistoryButtonActionEvent(ActionEvent event) {
        try {
            if (null == scoreHistoryStage) {
//                scoreHistoryStage = simpleTools.createStage(SCORE_HISTORY_PATH, "上下分详细历史", 719, 674, (mainStage.getWidth()-719)/2+mainStage.getX(), (mainStage.getHeight()-674)/2+mainStage.getY());
            }
            scoreHistoryStage.show();
            if (null == scoreHistoryController) scoreHistoryController = (ScoreHistoryController) controllers.get(ScoreHistoryController.class.getName());
//            scoreHistoryController.getUpperAndLowerData(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听"手动开奖"按钮事件
     * @param event
     */
    @FXML
    void manualOpenButtonActionEvent(ActionEvent event) {
        try {
            if (null == manualOpenStage) {
//                manualOpenStage = simpleTools.createStage(MANUAL_OPEN_PATH, "手动开奖", 320, 456, (mainStage.getWidth()-320)/2+mainStage.getX(), (mainStage.getHeight()-456)/2+mainStage.getY());
            }
            manualOpenStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听"添加或查找玩家"按钮事件
     * @param event
     */
    @FXML
    void findOrAddPlayerButtonActionEvent(ActionEvent event) {
        try {
            if (null == findOrAddPlayerStage) {
//                findOrAddPlayerStage = simpleTools.createStage(FIND_AND_PLAYER_PATH, "查找或添加玩家", 295, 375, (mainStage.getWidth()-295)/2+mainStage.getX(), (mainStage.getHeight()-375)/2+mainStage.getY());
            }
            findOrAddPlayerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 监听"清空零分玩家"按钮事件
     * @param event
     */
    @FXML
    void clearZeroUserActionEvent(ActionEvent event) {

    }

    /**
     * 监听"续费/查看授权"按钮事件
     * @param event
     */
    @FXML
    void accountButtonActionEvent(ActionEvent event) {
        try {
            if (null == accountStage) {
//                accountStage = simpleTools.createStage(ACCOUNT_PATH, "续费/查看授权", 450, 358, (mainStage.getWidth()-450)/2+mainStage.getX(), (mainStage.getHeight()-358)/2+mainStage.getY());
                accountStage.initStyle(StageStyle.TRANSPARENT);
                accountStage.setResizable(true);
            }
            accountStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 设置登录的机器人
     */
    public void setLoginRobot() {
        if (null == ROBOT) return;
        robotAccountLabel.setText(String.valueOf(ROBOT.getId()));
        robotnicknameLabel.setText(ROBOT.getNick());
        refreshRobotGroup();
    }

    /**
     * 刷新群列表
     */
    private void refreshRobotGroup() {
        if (null == ROBOT) return;
        if (!robotGroupComboBox.getItems().isEmpty()) robotGroupComboBox.getItems().clear();
        robotGroupComboBox.getItems().addAll(
                ROBOT.getGroups()
                        .stream()
                        .map(e ->
                                RobotInfoByGroup
                                        .builder()
                                        .groupAccount(String.valueOf(e.getId()))
                                        .groupName(e.getName())
                                        .build())
                        .collect(Collectors.toList())
        );
    }

    /**
     * 刷新上期开奖数据
     */
    public void refreshOpenData(OpenData openData) {
        openCodeLabel.setText(openData.getOpencode());
        openTimeLabel.setText(openData.getNexttime());
        openResultLabel.setText(openData.getResult2Index());
    }

    /**
     * 开奖中显示
     */
    public void inOpenShow() {
        countDownHBox.setVisible(false);
        countDownHBox.setManaged(false);
        closeBetLabel.setText("开奖中...");
        closeBetLabel.setVisible(true);
        closeBetLabel.setManaged(true);
    }

    /**
     * 封盘中显示
     */
    public void inCloseShow() {
        countDownHBox.setVisible(false);
        countDownHBox.setManaged(false);
        closeBetLabel.setText("封盘中...");
        closeBetLabel.setVisible(true);
        closeBetLabel.setManaged(true);
    }

    /**
     * 倒计时显示
     */
    public void countDownShow(String coutdown) {
        countDownHBox.setVisible(true);
        countDownHBox.setManaged(true);
        closeBetLabel.setVisible(false);
        closeBetLabel.setManaged(false);
        countDownLabel.setText(coutdown);
    }

    /**
     * 开启倒计时
     */
    private void timelabel() {
        countDownTask = new CountDownTask();
        ExecutorPool.executorService.execute(countDownTask);
        countDownTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (closeStatus) {
                return;
            }
            if (!NEW_OPEN_DATA.getExpect().equals(newValue.getExpect())) {
                OpenData.NEW_OPEN_DATA = newValue;
                refreshOpenData(newValue);

            }
        });

        countDownTask.messageProperty().addListener((observable, oldValue, newValue) -> {
            if (closeStatus) {
                return;
            }
            if (Integer.parseInt(newValue) > 0) {
                countDownShow(newValue);
            } else {
                inOpenShow();
            }
        });
    }

}
