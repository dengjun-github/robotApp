package com.dj.controller;

import com.dj.util.ActionUtil;
import com.dj.util.GlobalConstant;
import com.dj.util.SettingUtils;
import common.Client;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import serialize.pojo.Odds;
import utils.App;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.util.GlobalConstant.Action.*;
import static com.dj.util.InstructionRegexUtil.INSTRUCT_FORMAT_MAP;

public class OddTabPageController implements Initializable {

    @FXML
    private TableView<Odds> oddTable;

    @FXML
    private TableColumn<Odds, String> gameNameColumn;

    @FXML
    private TableColumn<Odds, String> formatColumn;

    @FXML
    private TableColumn<Odds, String> oddValueColumn;

    @FXML
    private TableColumn<Odds, String> odd22ValueColumn;

    @FXML
    private TableColumn<Odds, String> odd23ValueColumn;

    @FXML
    private TableColumn<Odds, Integer> betsMinMoneyColumn;

    @FXML
    private TableColumn<Odds, Integer> betsMaxMoneyColumn;

    @FXML
    private TableColumn<Odds, Integer> betsSingleMaxMoneyColumn;

    @FXML
    private TextField upperFormatTextField;

    @FXML
    private TextField cacleBetFormatTextField;

    @FXML
    private TextField queryWaterFormatTextField;

    @FXML
    private TextField lowerFormatTextField;

    @FXML
    private TextField queryHistoryFormatTextField;

    @FXML
    private TextField queryScoreFormatTextField;

    private List<String> regexes = new ArrayList<>();

    private List<String> formats = new ArrayList<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(), this);
        oddTable.setEditable(true);
        //初始化表格
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("keyword"));
        oddValueColumn.setCellValueFactory(new PropertyValueFactory<>("odds"));
        odd22ValueColumn.setCellValueFactory(new PropertyValueFactory<>("odds22"));
        odd23ValueColumn.setCellValueFactory(new PropertyValueFactory<>("odds23"));
        betsMinMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("betsMaxMoney"));
        betsMaxMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("betsMinMoney"));
        betsSingleMaxMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("betsSingleMaxMoney"));

        formatColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        oddValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        odd22ValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        odd23ValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        betsMinMoneyColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        betsMaxMoneyColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        betsSingleMaxMoneyColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        oddTable.getItems().addAll(FXCollections.observableList(App.getOdds(Client.account)));

        oddTable.getColumns().forEach(col -> {
            col.setOnEditCommit(t -> {
                if (!t.getNewValue().toString().trim().equals(t.getOldValue().toString().trim())) {
                    Odds betsType = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    if (col.getText().equals("识别关键字")) {
                        formats.clear();//先清除列表
                        Arrays.asList(betsType.getKeyword().split("\\|")).forEach(format -> formats.add(ActionUtil.getRegex4Format(betsType.getKey(), format)));
                        SettingUtils.REGEX_MAP.put(betsType.getKey(),formats);

                        betsType.setKeyword(t.getNewValue().toString());
                    }
                    if (col.getText().equals("赔率设置")) {
                        betsType.setOdds(t.getNewValue().toString());
                    }
                    if (col.getText().equals("22赔率")) {
                        betsType.setOdds22(t.getNewValue().toString());
                    }
                    if (col.getText().equals("23赔率")) {
                        betsType.setOdds23(t.getNewValue().toString());
                    }
                    if (col.getText().equals("最小限额")) {
                        betsType.setBetsMaxMoney(Integer.parseInt(t.getNewValue().toString()));
                    }
                    if (col.getText().equals("最大限额")) {
                        betsType.setBetsMinMoney(Integer.parseInt(t.getNewValue().toString()));
                    }
                    if (col.getText().equals("单局总限额")) {
                        betsType.setBetsSingleMaxMoney(Integer.parseInt(t.getNewValue().toString()));
                    }
                }
            });
        });

        setTextAndFocusedProperty();
    }

    private void focusedChange(Boolean oldValue, Boolean newValue, String key, TextField textField) {
        if (oldValue && !newValue) {
            if (!INSTRUCT_FORMAT_MAP.get(key).equals(textField.getText())) {
                INSTRUCT_FORMAT_MAP.put(key, textField.getText());
                regexes.clear();
                Arrays.asList(textField.getText().split("\\|")).forEach(value -> {
                    regexes.add(ActionUtil.getRegex4Format(key, value));
                });
                SettingUtils.REGEX_MAP.put(key, regexes);
                System.out.println(textField.getText());
            }
        }
    }

    private void setTextAndFocusedProperty(){

        TextField[] textFields = new TextField[]{
                upperFormatTextField,lowerFormatTextField,queryScoreFormatTextField,
                cacleBetFormatTextField,queryWaterFormatTextField,queryHistoryFormatTextField
        };

        GlobalConstant.Action[] actions = new GlobalConstant.Action[]{
                UPPER_SCORE,LOWER_SCORE,QUERY_SCORE,CANCEL_BET,QUERY_BILL,QUERY_HiSTORY
        };
        setTextField(textFields,actions);

    }


    private void setTextField(TextField[] textFields,GlobalConstant.Action[] actions) {
        for (int i = 0; i < textFields.length; i++) {
            textFields[i].setText(SettingUtils.getInstructionVoByKey(actions[i].getKey()).getValue());
            SettingUtils.setInstruct(textFields[i],actions[i]);
        }
    }
}
