package com.dj.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Initializable {


    @FXML
    private Tab billTab;


    @FXML
    @Getter
    private BillTabPageController billTabPageController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMainStage(Stage stage) {
        billTabPageController.setMainStage(stage);
    }
}
