package com.dj.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;

public class DummyScoreSettingController implements Initializable {

    @FXML
    private TextField upperExceedTimeTextField;

    @FXML
    private TextArea upperWhiteListTextArea;

    @FXML
    private TextField lowerExceedTimeTextField;

    @FXML
    private TextArea lowerWhiteListTextArea;

    @FXML
    private CheckBox autoUpperCheckBox;

    @FXML
    private CheckBox autoLowerCheckBox;

    @FXML
    private TextField upperTimeStartTextField;

    @FXML
    private TextField upperTimeEndTextField;

    @FXML
    private TextField lowerTimeStartTextField;

    @FXML
    private TextField lowerTimeEndTextField;

    @FXML
    private CheckBox EummyAutoUpperCheckBox;

    @FXML
    private CheckBox EummyAutoLowerCheckBox;

    @FXML
    private CheckBox aotuHideCheckBox;

    @FXML
    private CheckBox clearBillCheckBox;

    @FXML
    private CheckBox useChineseWindowCheckBox;

    @FXML
    private CheckBox stageTopCheckBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);
    }


    @FXML
    void EummyAutoLowerActionEvent(ActionEvent event) {

    }

    @FXML
    void EummyAutoUpperActionEvent(ActionEvent event) {

    }

    @FXML
    void aotuHideActionEvent(ActionEvent event) {

    }

    @FXML
    void autoUpperCheckBoxActionEvent(ActionEvent event) {

    }

    @FXML
    void clearBillActionEvent(ActionEvent event) {

    }

    @FXML
    void stageTopActionEvent(ActionEvent event) {

    }

    @FXML
    void useChineseWindowActionEvent(ActionEvent event) {

    }

}
