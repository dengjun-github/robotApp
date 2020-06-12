package com.dj.controller;

import com.dj.Exception.ClientErrorException;
import com.dj.entity.pojo.request.robot.RobotInfoByGroup;
import com.dj.entity.pojo.response.OpenData;
import com.dj.robot.GroupRobot;
import com.dj.task.CountDownTask;
import com.dj.util.ExecutorPool;
import com.dj.util.HttpResult;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.dj.App.controllers;
import static com.dj.entity.pojo.response.OpenData.NEW_OPEN_DATA;
import static com.dj.handler.GroupHandler.groupHandler;
import static com.dj.net.VertxWebClient.RestFul.GET;
import static com.dj.net.VertxWebClient.httpRequest;
import static com.dj.robot.GroupRobot.*;
import static com.dj.util.AllFxmlPath.*;
import static com.dj.util.GlobalConstant.*;
import static com.dj.util.GlobalConstant.RequstUri.BILL;
import static com.dj.util.SimpleTools.simpleTools;

public class BillTabPageController implements Initializable {


    @FXML
    private Label openCodeLabel;

    @FXML
    private Label openTimeLabel;

    @FXML
    private Label openResultLabel;

    @FXML
    private CheckBox scoreCheckBox;

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

    private int countNumber;

    private String text = "";

    private Stage mainStage;

    //上下分管理窗口
    private Stage scoreManageStage;

    //上下分历史窗口
    private Stage scoreHistoryStage;

    //手动开奖窗口
    private Stage manualOpenStage;

    //机器人登录窗口
    private Stage robotLoginStage;

    //添加或查找玩家窗口
    private Stage findOrAddPlayerStage;

    private RobotLoginController robotLoginController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);
        //获取开奖数据
        OpenData openData = (OpenData) groupHandler.getOpenData(true);
        OpenData.NEW_OPEN_DATA = openData;
        System.out.println("执行了");
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
            }
        });
    }

    /**
     * 监听"登录机器人"按钮事件
     *
     * @param event
     */
    @FXML
    void loginRobotButtonEvent(ActionEvent event) {
        try {
            if (null != robotLoginController) {
                robotLoginController.getQqPasswordTextField().setText(null);
            }
            if (null == robotLoginStage) {
                robotLoginStage = simpleTools.getScoreStage(ROBOT_LOGIN_PATH, "机器人登录", 600, 400, (mainStage.getWidth()-600)/2+mainStage.getX(), (mainStage.getHeight()-400)/2+mainStage.getY());
                robotLoginStage.initModality(Modality.APPLICATION_MODAL);
            }

            robotLoginStage.show();

            if (null == robotLoginController) {
                robotLoginController = (RobotLoginController) controllers.get(RobotLoginController.class.getName());
            }
            robotLoginController.setRobotLoginStage(robotLoginStage);
            Platform.runLater(robotLoginController::setQqAccountComboBoxItem);
        } catch (IOException e) {
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
                //选择彩种
                GAME_INDEX.append("AoZhou5");
                GAME_TYPE.append("ShiShiCai");
                GroupRobot.listen();
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
    void RobotGroupComboBoxOnClicked(ActionEvent event) {
        System.out.println("点击了");
//        refreshRobotGroup();
    }

    /**
     * 监听"上下分窗口显示"复选框事件
     *
     * @param event
     */
    @FXML
    void scoreCheckBoxActionEvent(ActionEvent event) {
        try {
            if (scoreCheckBox.isSelected()) {
                if (null == scoreManageStage) {
                    scoreManageStage = simpleTools.getScoreStage(SCORE_MANAGE_PATH, "上下分管理", 300, 580, (mainStage.getWidth()-300)/2+mainStage.getX(), (mainStage.getHeight()-580)/2+mainStage.getY());
                }
                //加载
                scoreManageStage.show();
            } else {
                scoreManageStage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听"发送账单按钮"
     *
     * @param event
     */
    @FXML
    void sendBillMenultemActionEvent(ActionEvent event) {
        ExecutorPool.executorService.execute(() -> {
            try {
                if (!IS_GAME_BEGIN) {
                    throw new ClientErrorException("游戏未启动,请先开始游戏");
                }
                if (null == ROBOT) {
                    throw new ClientErrorException("您还没有登录机器人,请登录机器人");
                }
                //获取账单
                httpRequest(PORT, HOST, BILL.getUri() + "/" + GAME_INDEX + "/" + NEW_OPEN_DATA.getExpect(), null, USER_TOKEN, GET,
                        res -> {
                            if (res.succeeded()) {
                                HttpResult result = res.result().body();
                                if (result.isIsok()) {
                                    sendMessage(result.getData().toString(), false);
                                } else {
                                    throw new ClientErrorException(result.getMessage());
                                }
                            } else {
                                throw new ClientErrorException("服务器异常,请稍后再试");
                            }
                        });
            } catch (Exception e) {
                Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR, "发送账单", "失败", e.getMessage()));
            }

        });
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

                groupHandler.betVerify(title);
                manualOperation.setText("手动开盘");
                manualOperation.setStyle("-fx-background-color: #54FF9F");
                inCloseShow();
                closeStatus = true;
            } else {
                //开盘
                groupHandler.betOpen("手动封盘");
                manualOperation.setText("手动封盘");
                manualOperation.setStyle("-fx-background-color: #FF3030");
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
                scoreHistoryStage = simpleTools.getScoreStage(SCORE_HISTORY_PATH, "上下分详细历史", 719, 674, (mainStage.getWidth()-719)/2+mainStage.getX(), (mainStage.getHeight()-674)/2+mainStage.getY());
            }
            scoreHistoryStage.show();
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
                manualOpenStage = simpleTools.getScoreStage(MANUAL_OPEN_PATH, "手动开奖", 320, 456, (mainStage.getWidth()-320)/2+mainStage.getX(), (mainStage.getHeight()-456)/2+mainStage.getY());
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
                findOrAddPlayerStage = simpleTools.getScoreStage(FIND_AND_PLAYER_PATH, "查找或添加玩家", 295, 375, (mainStage.getWidth()-295)/2+mainStage.getX(), (mainStage.getHeight()-375)/2+mainStage.getY());
            }
            findOrAddPlayerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        CountDownTask task = new CountDownTask();
        ExecutorPool.executorService.execute(task);
        task.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (closeStatus) {
                return;
            }
            if (!NEW_OPEN_DATA.getExpect().equals(newValue.getExpect())) {
                OpenData.NEW_OPEN_DATA = newValue;
                refreshOpenData(newValue);

            }
        });

        task.messageProperty().addListener((observable, oldValue, newValue) -> {
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

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }
}
