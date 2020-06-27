package com.dj.controller;

import com.dj.util.SettingUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import serialize.pojo.Keys;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.util.SimpleTools.simpleTools;

public class OddSettingTabPanePageController implements Initializable {

    @FXML
    private CheckBox use22OfTieCheckBox;

    @FXML
    private CheckBox use22OfTieAndSmallCheckBox;

    @FXML
    private CheckBox use23OfTieCheckBox;

    @FXML
    private CheckBox use23OfTieAndBigCheckBox;

    @FXML
    private CheckBox tIgerAndDragonReturnPrincipleCheckBox;

    @FXML
    private TextField tIgerAndDragonReturnPrincipleTextField;

    @FXML
    private CheckBox combinationReturnCheckBox;

    @FXML
    private TextField combinationReturnTextField;

    @FXML
    private CheckBox singlePumpingWaterCheckBox;

    @FXML
    private TextField singleWinTextField;

    @FXML
    private TextField singlePumpingWaterTextField;

    @FXML
    private CheckBox sumReturnCheckBox;

    @FXML
    private TextField sumReturnTextField;

    @FXML
    private TextField singleMaxBetEachPeriodTextField;

    @FXML
    private CheckBox allowCancelBetCheckBox;

    @FXML
    private TextField twoContinuousTextField;

    @FXML
    private TextField threeContinuousTextField;

    @FXML
    private TextField fourContinuousTextField;

    @FXML
    private TextField fiveContinuousTextField;

    @FXML
    private TextField singleMaxTextField;

    @FXML
    private TextField tieMaxTextField;

    @FXML
    private TextField loctionMaxTextField;

    @FXML
    private TextField parseMaxTextField;

    @FXML
    private CheckBox balanceStudTextField;

    @FXML
    private CheckBox use22ReturnCheckBox;

    @FXML
    private CheckBox use23ReturnCheckBox;

    @FXML
    private CheckBox use22SweepCheckBox;

    @FXML
    private CheckBox use23SweepCheckBox;

    @FXML
    private TextField minCodeTextField;

    @FXML
    private TextField maxCodeTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);

        setCheckbox();

        setRatio();

        setMoney();

        setNumber();

        setLength();




//        sumReturnTextField.setText(SettingUtils.getJsonByKey(Keys.SETTINGS_SUM_RETURN).get(SettingUtils.RATIO).toString());
//        singleMaxBetEachPeriodTextField.setText(SettingUtils.getJsonByKey(Keys.SETTINGS_SINGLE_MAX_BET_MONEY).get(SettingUtils.MONEY).toString());
    }



    @FXML
    void allowCancelBetCheckBoxActionEvent(ActionEvent event) {
        if (allowCancelBetCheckBox.isSelected()) {

        }
    }

    @FXML
    void balanceStudTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void combinationReturnCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void combinationReturnTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void fiveContinuousTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void fourContinuousTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void loctionMaxTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void loctionMaxTextFieldActiopmEvent(ActionEvent event) {

    }

    @FXML
    void maxCodeTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void minCodeTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void parseMaxTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void singleMaxBetEachPeriodTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void singleMaxTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void singlePumpingWaterCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void singlePumpingWaterTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void singleWinTextFielActionEvent(ActionEvent event) {

    }

    @FXML
    void sumReturnCheckBoxActionEvnet(ActionEvent event) {

    }

    @FXML
    void sumReturnTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void tIgerAndDragonReturnPrincipleCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void tIgerAndDragonReturnPrincipleTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void threeContinuousTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void twoContinuousTextFieldActionEvent(ActionEvent event) {

    }

    @FXML
    void use22OfTieAndSmallCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void use22OfTieCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void use22ReturnCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void use22SweepCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void use23OfTieAndSmallCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void use23OfTieCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void use23ReturnCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void use23SweepCheckBoxActionEvent(ActionEvent event) {

    }


    private void setRatio(){
        TextField[] ratio = new TextField[]{
                tIgerAndDragonReturnPrincipleTextField,combinationReturnTextField,singlePumpingWaterTextField,sumReturnTextField
        };
        String[] ratioKeys = new String[]{
                Keys.SETTINGS_DRAGON_AND_TIGER_RETURN,Keys.SETTINGS_COMBINATION_RETURN,Keys.SETTINGS_PUMPING_WATER_SINGLE,Keys.SETTINGS_SUM_RETURN
        };
        simpleTools.initTextField(ratio,ratioKeys, SettingUtils.RATIO);
    }

    private void setMoney() {
        TextField[] money = new TextField[]{
                singleWinTextField,/*singleMaxBetEachPeriodTextField,*/twoContinuousTextField,threeContinuousTextField,fourContinuousTextField,fiveContinuousTextField,singleMaxTextField,tieMaxTextField,
                loctionMaxTextField
        };
        String[] moneyKeys = new String[]{
                Keys.SETTINGS_PUMPING_WATER_SINGLE,Keys.SETTINGS_QUOTA_FOR_ER_LIAN,Keys.SETTINGS_QUOTA_FOR_SAN_LIAN,Keys.SETTINGS_QUOTA_FOR_SILIAN,Keys.SETTINGS_QUOTA_FOR_WU_LIAN,Keys.SETTINGS_SINGLE_MAX_BET_MONEY,
                Keys.SETTINGS_HE_ZHI_MAX_BET_MONEY,Keys.SETTINGS_DING_WEI_MAX_BET_MONEY
        };
        simpleTools.initTextField(money,moneyKeys,SettingUtils.MONEY);
    }

    private void setNumber() {
        TextField[] number = new TextField[]{
                minCodeTextField,maxCodeTextField
        };
        String[] numberKeys = new String[]{
                Keys.SETTINGS_MIN_NUMBER,Keys.SETTINGS_MAX_NUMBER
        };
        simpleTools.initTextField(number,numberKeys,SettingUtils.NUMBER);
    }

    private void setLength() {
        TextField[] length = new TextField[]{
                parseMaxTextField
        };
        String[] lengthKeys = new String[]{
                Keys.SETTINGS_BET_MSG_MAX_PARSE
        };
        simpleTools.initTextField(length,lengthKeys,SettingUtils.LENGTH);
    }


    private void setCheckbox() {
        CheckBox[] checkBoxes = new CheckBox[]{use22OfTieCheckBox,use23OfTieCheckBox,use22OfTieAndSmallCheckBox,use23OfTieAndBigCheckBox,tIgerAndDragonReturnPrincipleCheckBox,
                combinationReturnCheckBox,singlePumpingWaterCheckBox,sumReturnCheckBox,allowCancelBetCheckBox,balanceStudTextField,use22ReturnCheckBox,use23ReturnCheckBox,
                use22SweepCheckBox,use23SweepCheckBox
        };

        String[] checkBoxKeys = new String[]{
                Keys.SETTINGS22_OF_TIE,Keys.SETTINGS23_OF_TIE,Keys.SETTINGS22_OF_SMALL_AND_TIE,Keys.SETTINGS23_OF_BIG_AND_TIE,Keys.SETTINGS_DRAGON_AND_TIGER_RETURN,
                Keys.SETTINGS_COMBINATION_RETURN,Keys.SETTINGS_PUMPING_WATER_SINGLE,Keys.SETTINGS_SUM_RETURN,Keys.SETTINGS_ALLOW_CANCEL_BET,Keys.SETTINGS_BALANCE_STUD,
                Keys.SETTINGS22_SMALL_AND_DOUBLE_RETURN,Keys.SETTINGS23_BIG_AND_SINGLE_RETURN,Keys.SETTINGS22_SWEEP,Keys.SETTINGS23_SWEEP
        };

        simpleTools.initCheckBox(checkBoxes,checkBoxKeys);

    }
}
