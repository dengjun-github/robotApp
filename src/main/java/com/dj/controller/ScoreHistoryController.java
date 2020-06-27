package com.dj.controller;

import com.alibaba.fastjson.JSONObject;
import com.browniebytes.javafx.control.DateTimePicker;
import com.dj.Exception.ClientErrorException;
import com.dj.entity.vo.ScoreVo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import static com.dj.Application.controllers;
import static com.dj.util.SimpleTools.simpleTools;

public class ScoreHistoryController implements Initializable {

    private final String pattern = "yyyy-MM-dd HH:mm:ss";


    @FXML
    private DateTimePicker endDateTimePicker;

    @FXML
    private DateTimePicker startDateTimePicker;


    @FXML
    private TableView<ScoreVo> upperTable;

    @FXML
    private TableColumn<ScoreVo, String> upperDateColumn;

    @FXML
    private TableColumn<ScoreVo, String> upperAccountColumn;

    @FXML
    private TableColumn<ScoreVo, String> upperNicknameColumn;

    @FXML
    private TableColumn<ScoreVo, String> upperMoneyColumn;

    @FXML
    private TableColumn<ScoreVo, String> upperBalanceColumn;

    @FXML
    private TextField accountUpperTextField;

    @FXML
    private TextField accountLoweTextField;

    @FXML
    private TableView<ScoreVo> lowerTable;

    @FXML
    private TableColumn<ScoreVo, String> lowerDateColumn;

    @FXML
    private TableColumn<ScoreVo, String> lowerAccountColumn;

    @FXML
    private TableColumn<ScoreVo, String> lowerNicknameColumn;

    @FXML
    private TableColumn<ScoreVo, String> lowerMoneyColumn;

    @FXML
    private TableColumn<ScoreVo, String> lowerBalanceColumn;

    @FXML
    private Label upperTotalLabel;

    @FXML
    private Label lowerTotalLabel;

    private FileChooser fileChooser;

    private String before;
    private String after;

    private WritableWorkbook wwb;

    private ContextMenu contextMenu = new ContextMenu();

    private Stage fileChooserStage = new Stage();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers.put(this.getClass().getName(),this);
        //初始化所有字段
        upperDateColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        upperAccountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        upperNicknameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        upperMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("money"));
        upperBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        lowerDateColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        lowerAccountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        lowerNicknameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        lowerMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("money"));
        lowerBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        fileChooser = new FileChooser();
        fileChooser.setTitle("保存上下分数据");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("EXCEL", "*.xls"),
                new FileChooser.ExtensionFilter("EXCEL", "*.xlsx")
        );




        upperTable.setContextMenu(contextMenu);



        contextMenu.getItems().addAll(new MenuItem("删除记录并扣款"));





    }

    /**
     * 监听"导出数据"按钮事件
     * @param event
     */
    @FXML
    void exportButtonActionEvent(ActionEvent event){
        try{

            File file = fileChooser.showSaveDialog(fileChooserStage);
            if(file != null){
                if (!file.exists()) {
                    file.createNewFile();
                }
                //将要写入的内容写到file文件中
                String path = file.getPath();
                System.out.println(path);
                export(file);
                simpleTools.informationDialog(Alert.AlertType.INFORMATION,"导入数据","成功","点击确定关闭");
          }
        }catch (IOException e) {
            simpleTools.informationDialog(Alert.AlertType.ERROR,"导入数据","错误","出现未知错误");
        }

    }

    /**
     * 监听"查找全部"按钮事件
     * @param event
     */
    @FXML
    void findAllButtonActionEvent(ActionEvent event) {
        after = startDateTimePicker.dateTimeProperty().get().format(DateTimeFormatter.ofPattern(pattern));
        before = endDateTimePicker.dateTimeProperty().get().format(DateTimeFormatter.ofPattern(pattern));
        JSONObject param = new JSONObject();
        param.put("before",before);
        param.put("after",after);
//        getUpperAndLowerData(param);
        System.out.println("after  = " + after);
        System.out.println("before  =  "+ before);
    }

    /**
     * 监听"查询下分"按钮事件
     * @param event
     */
    @FXML
    void findLowerButtonActionEvent(ActionEvent event) {

        setsingleTabel(false,accountLoweTextField.getText());
    }

    /**
     * 监听"查询上分"按钮事件
     * @param event
     */
    @FXML
    void findUpperButtonActionEvent(ActionEvent event) {

        setsingleTabel(true,accountUpperTextField.getText());
    }

//    public void getUpperAndLowerData(JSONObject param){
//        httpRequest(PORT,HOST,FINANCE.getUri(),param,USER_TOKEN, VertxWebClient.RestFul.GET,
//                res ->{
//                    HttpResult result = res.result().body();
//                    if (result.isIsok()){
//                        LinkedHashMap data = (LinkedHashMap) result.getData();
//
//                        List<ScoreVo> uppers = JSONArray.parseArray(data.get("uppers").toString(),ScoreVo.class);
//                        List<ScoreVo> lowers = JSONArray.parseArray(data.get("lowers").toString(),ScoreVo.class);
//                        upperTable.setItems(FXCollections.observableList(uppers));
//                        lowerTable.setItems(FXCollections.observableList(lowers));
//                        Platform.runLater(() ->setLowerTotalLabel(lowers));
//                        Platform.runLater(() ->setUpperTotalLabel(uppers));
//                    }
//                });
//    }

    private void setLowerTotalLabel(List<ScoreVo> lowers) {
        Double lowerTotal = lowers.stream()
                .map(scoreVo -> Double.parseDouble(scoreVo.getMoney()))
                .reduce(0.0, Double::sum);
        lowerTotalLabel.setText(lowerTotal.toString());

    }

    private void setUpperTotalLabel(List<ScoreVo> uppers) {
        Double upperTotal = uppers.stream()
                .map(scoreVo -> Double.parseDouble(scoreVo.getMoney()))
                .reduce(0.0, Double::sum);
        upperTotalLabel.setText(upperTotal.toString());
    }

    private void setsingleTabel(boolean isUpper,String account){
        JSONObject param = new JSONObject();
        if (null != account) {
            param.put("account",account);
        }
//        param.put()
        System.out.println(after);
        System.out.println(before);

//        httpRequest(PORT,HOST,FINANCE.getUri(),param,USER_TOKEN, VertxWebClient.RestFul.GET,
//                res ->{
//                    HttpResult result = res.result().body();
//                    if (result.isIsok()){
//                        LinkedHashMap data = (LinkedHashMap) result.getData();
//
//                        List<ScoreVo> uppers = JSONArray.parseArray(data.get("uppers").toString(),ScoreVo.class);
//                        List<ScoreVo> lowers = JSONArray.parseArray(data.get("lowers").toString(),ScoreVo.class);
//                        if (isUpper) {
//                            upperTable.setItems(FXCollections.observableList(uppers));
//                            Platform.runLater(() ->setLowerTotalLabel(lowers));
//                        }
//                        else {
//                            lowerTable.setItems(FXCollections.observableList(lowers));
//                            Platform.runLater(() ->setUpperTotalLabel(uppers));
//                        }
//
//                    }
//                });
    }


    /**
     * 制作Excel文件
     * @param file
     */
    private void export (File file){
        try {
            //以fileName为文件名来创建一个Workbook
            wwb = Workbook.createWorkbook(file);

            // 创建工作表
            WritableSheet ws = wwb.createSheet("scoreData", 0);

            ws.addCell(new jxl.write.Label(0, 0, "上分"));
            ws.addCell(new jxl.write.Label(0, 1, "时间"));
            ws.addCell(new jxl.write.Label(1, 1, "账号"));
            ws.addCell(new jxl.write.Label(2, 1, "昵称"));
            ws.addCell(new jxl.write.Label(3, 1, "金额"));
            ws.addCell(new jxl.write.Label(4, 1, "余额"));
            for (int i = 0; i < upperTable.getItems().size(); i++) {
                ws.addCell(new jxl.write.Label(0, i+2, upperTable.getItems().get(i).getCreateTime()));
                ws.addCell(new jxl.write.Label(1, i+2, upperTable.getItems().get(i).getAccount()));
                ws.addCell(new jxl.write.Label(2, i+2, upperTable.getItems().get(i).getNickname()));
                ws.addCell(new jxl.write.Label(3, i+2, upperTable.getItems().get(i).getMoney()));
                ws.addCell(new jxl.write.Label(4, i+2, upperTable.getItems().get(i).getBalance()));
            }
            ws.addCell(new jxl.write.Label(0, upperTable.getItems().size()+2, "下分"));
            ws.addCell(new jxl.write.Label(0, upperTable.getItems().size()+3, "时间"));
            ws.addCell(new jxl.write.Label(1, upperTable.getItems().size()+3, "账号"));
            ws.addCell(new jxl.write.Label(2, upperTable.getItems().size()+3, "昵称"));
            ws.addCell(new jxl.write.Label(3, upperTable.getItems().size()+3, "金额"));
            ws.addCell(new jxl.write.Label(4, upperTable.getItems().size()+3, "余额"));
            for (int i = 0; i < lowerTable.getItems().size(); i++) {
                ws.addCell(new jxl.write.Label(0, i+upperTable.getItems().size()+4, lowerTable.getItems().get(i).getCreateTime()));
                ws.addCell(new jxl.write.Label(1, i+upperTable.getItems().size()+4, lowerTable.getItems().get(i).getAccount()));
                ws.addCell(new jxl.write.Label(2, i+upperTable.getItems().size()+4, lowerTable.getItems().get(i).getNickname()));
                ws.addCell(new jxl.write.Label(3, i+upperTable.getItems().size()+4, lowerTable.getItems().get(i).getMoney()));
                ws.addCell(new jxl.write.Label(4, i+upperTable.getItems().size()+4, lowerTable.getItems().get(i).getBalance()));
            }

            //写进文档
            wwb.write();
            wwb.close();
        } catch (Exception e) {
            throw new ClientErrorException("导出excel错误"+e.getMessage());
        }finally {

        }
    }
}
