package com.dj.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ReturnWaterSettingController implements Initializable {

    @FXML
    private TableView<?> manualTableView;

    @FXML
    private TableColumn<?, ?> manualStartColumn;

    @FXML
    private TableColumn<?, ?> manualEndColumn;

    @FXML
    private TableColumn<?, ?> manualRatioColumn;

    @FXML
    private TextField manualStartTextField;

    @FXML
    private TextField manualEndTextField;

    @FXML
    private TextField manualRatioTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void confirmButtonActionEvent(ActionEvent event) {

    }

    @FXML
    void manualAddOneButtonActionEvent(ActionEvent event) {

    }


}
