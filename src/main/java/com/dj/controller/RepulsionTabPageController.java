package com.dj.controller;

import com.dj.Exception.ClientErrorException;
import com.dj.entity.vo.WelfareGrantVo;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.util.SimpleTools.simpleTools;

public class RepulsionTabPageController implements Initializable {

    //手动发放的福利设置表格
    private static final SimpleListProperty<WelfareGrantVo> manualList = new SimpleListProperty<>(FXCollections.observableArrayList());
    //自动发放的福利设置表格
    private static final SimpleListProperty<WelfareGrantVo> autoList = new SimpleListProperty<>(FXCollections.observableArrayList());

    @FXML
    private TextField manualCountTextFeild;

    @FXML
    private RadioButton betMoneyPaidRadioButton;

    @FXML
    private ToggleGroup manualWelfare;

    @FXML
    private RadioButton totalLossPaidRadioButton;

    @FXML
    private TableView<WelfareGrantVo> manualTableView;

    @FXML
    private TableColumn<WelfareGrantVo, String> manualStartColumn;

    @FXML
    private TableColumn<WelfareGrantVo, String> manualEndColumn;

    @FXML
    private TableColumn<WelfareGrantVo, String> manualRatioColumn;

    @FXML
    private TextField manualStartTextField;

    @FXML
    private TextField manualEndTextField;

    @FXML
    private TextField manualRatioTextField;

    @FXML
    private CheckBox autoWelfareCheckBox;

    @FXML
    private RadioButton lossRadioButton;

    @FXML
    private ToggleGroup autoWelfare;

    @FXML
    private RadioButton accountRadioButton;

    @FXML
    private TextField dayNumberTextFeild;

    @FXML
    private TextField autoCountTextFeild;

    @FXML
    private TableView<WelfareGrantVo> autoTableView;

    @FXML
    private TableColumn<WelfareGrantVo, String> autoStartColumn;

    @FXML
    private TableColumn<WelfareGrantVo, String> autoEndColumn;

    @FXML
    private TableColumn<WelfareGrantVo, String> autoRatioColumn;

    @FXML
    private TextField autoStartTextField;

    @FXML
    private TextField autoEndTextField;

    @FXML
    private TextField autoRatioTextField;

    @FXML
    private Label repulsionNumberLabel;

    @FXML
    private Label allSubordinateNumberLabel;

    @FXML
    private Label benefitsAmountLabel;

    @FXML
    private DatePicker queryStartDatePicker;

    @FXML
    private DatePicker queryEndDatePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);
        //初始化表格
        manualStartColumn.setCellValueFactory(new PropertyValueFactory<>("startMoney"));
        manualEndColumn.setCellValueFactory(new PropertyValueFactory<>("endMoney"));
        manualRatioColumn.setCellValueFactory(new PropertyValueFactory<>("ratio"));
        autoStartColumn.setCellValueFactory(new PropertyValueFactory<>("startMoney"));
        autoEndColumn.setCellValueFactory(new PropertyValueFactory<>("endMoney"));
        autoRatioColumn.setCellValueFactory(new PropertyValueFactory<>("ratio"));


        manualStartColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        manualEndColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        manualRatioColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        autoStartColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        autoEndColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        autoRatioColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        autoTableView.setItems(autoList);
        manualTableView.setItems(manualList);

        autoTableView.setEditable(true);
        manualTableView.setEditable(true);

        tableSetOnEditConmit(autoTableView);
        tableSetOnEditConmit(manualTableView);

    }



    /**
     * 监听"自动发送的加一栏"按钮事件
     * @param event
     */
    @FXML
    void autoAddOneButtonActionEvent(ActionEvent event) {
        try {
            checkTextField(autoStartTextField);
            checkTextField(autoEndTextField);
            checkTextField(autoRatioTextField);

            autoList.add(WelfareGrantVo.builder()
                    .startMoney(autoStartTextField.getText())
                    .endMoney(autoEndTextField.getText())
                    .ratio(autoRatioTextField.getText()).build());
            simpleTools.clearTextField(autoStartTextField,autoEndTextField,autoRatioTextField);
        } catch (ClientErrorException e) {
            Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"添加一栏","错误",e.getMessage()));
        }
    }


    @FXML
    void autoWelfareCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void betMoneyPaidRadioButtonActionEvent(ActionEvent event) {

    }

    @FXML
    void confirmButtonActionEvent(ActionEvent event) {

    }

    @FXML
    void countTextFeildActionEvent(ActionEvent event) {

    }

    @FXML
    void dayNumberTextFeildActionEvent(ActionEvent event) {

    }

    /**
     * 监听"手动发送的加一栏"按钮事件
     * @param event
     */
    @FXML
    void manualAddOneButtonActionEvent(ActionEvent event) {
        try {
            checkTextField(manualStartTextField);
            checkTextField(manualEndTextField);
            checkTextField(manualRatioTextField);

            manualList.add(WelfareGrantVo.builder()
                    .startMoney(manualStartTextField.getText())
                    .endMoney(manualEndTextField.getText())
                    .ratio(manualRatioTextField.getText()).build());
            simpleTools.clearTextField(manualStartTextField,manualEndTextField,manualRatioTextField);
        } catch (ClientErrorException e) {
            Platform.runLater(() -> simpleTools.informationDialog(Alert.AlertType.ERROR,"添加一栏","错误",e.getMessage()));
        }

    }

    @FXML
    void queryButtonActionEvent(ActionEvent event) {

    }

    @FXML
    void totalLossPaidRadioButtonActionEvent(ActionEvent event) {

    }

    //table设置编辑
    private void tableSetOnEditConmit(TableView<WelfareGrantVo> manualTableView) {
        manualTableView.getColumns().forEach(col -> {
            col.setOnEditCommit(t -> {
                if (!t.getNewValue().toString().trim().equals(t.getOldValue().toString().trim())) {
                    WelfareGrantVo welfareGrantVo = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    if (col.getText().equals("起始金额")) {
                        welfareGrantVo.setStartMoney(t.getNewValue().toString());
                    }
                    if (col.getText().equals("结束金额")) {
                        welfareGrantVo.setEndMoney(t.getNewValue().toString());
                    }
                    if (col.getText().equals("发放比例(%)")) {
                        welfareGrantVo.setRatio(t.getNewValue().toString());
                    }

                }
            });
        });
    }

    //检查textField是否为空
    private void checkTextField(TextField manualStartTextField) {
        if (simpleTools.isEmpty(manualStartTextField.getText()))
            throw new ClientErrorException(manualStartTextField.getPromptText() + "不能为空");
    }


}
