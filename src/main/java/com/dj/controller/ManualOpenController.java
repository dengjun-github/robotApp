package com.dj.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ManualOpenController implements Initializable {

    @FXML
    private TableColumn<?, ?> periodColumn;

    @FXML
    private TableColumn<?, ?> playerNumberCloumn;

    @FXML
    private TableColumn<?, ?> totalMoneyColimn;

    @FXML
    private TextField openCodeTextField;

    @FXML
    private CheckBox isBillSendCheckBox;

    @FXML
    void SelectOpenButtonActionEvent(ActionEvent event) {

    }

    @FXML
    void SelectRefundButtonActionEvent(ActionEvent event) {

    }

    @FXML
    void ignoreOfficialActionEvent(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
