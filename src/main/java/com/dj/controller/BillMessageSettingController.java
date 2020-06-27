package com.dj.controller;

import com.alibaba.fastjson.JSONObject;
import com.dj.util.SettingUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import serialize.pojo.Keys;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dj.util.SimpleTools.simpleTools;

public class BillMessageSettingController implements Initializable {

    @FXML
    private TextArea billFormatHeadTextArea;

    @FXML
    private TextArea billFormatBodyTextArea;

    @FXML
    private TextArea billFormatFootTextArea;

    @FXML
    private TextArea betVerifyFormatHeadTextArea;

    @FXML
    private TextArea betVerifyFormatBodyTextArea;

    @FXML
    private TextArea betVerifyFormatFootTextArea;

    @FXML
    private TextArea winnerFormatHeadTextArea;

    @FXML
    private TextArea winnerFormatBodyTextArea;

    @FXML
    private TextArea winnerFormatFootTextArea;

    @FXML
    private CheckBox numberFormatCheckBox;

    @FXML
    private CheckBox nickNameFormatCheckBox;

    @FXML
    private TextField minScoreShowTextField;

    @FXML
    private TextField minScoreReplenishTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTextArea(billFormatHeadTextArea,billFormatBodyTextArea,billFormatFootTextArea, Keys.SETTINGS_BILL_FORMAT);
//        setTextArea(betVerifyFormatHeadTextArea,betVerifyFormatBodyTextArea,betVerifyFormatFootTextArea, Keys.Ver);
        setTextArea(winnerFormatHeadTextArea,winnerFormatBodyTextArea,winnerFormatFootTextArea, Keys.SETTINGS_WINNERS_FORMAT);

        CheckBox[] checkBoxes = new CheckBox[]{numberFormatCheckBox,nickNameFormatCheckBox};
        String[] checkBoxKeys = new String[]{Keys.SETTINGS_MIN_SCORE_SHOW,Keys.SETTINGS_MIN_SCORE_REPLENISH};
        simpleTools.initCheckBox(checkBoxes,checkBoxKeys);

        TextField[] textFields = new TextField[]{minScoreShowTextField,minScoreReplenishTextField};
        String[] textFieldKeys = new String[]{Keys.SETTINGS_MIN_SCORE_SHOW,Keys.SETTINGS_MIN_SCORE_REPLENISH};

    }



    private void setTextArea(TextArea headTextAreas,TextArea bodyTextAreas,TextArea footTextAreas, String key) {
        JSONObject jsonByKey = SettingUtils.getJsonByKey(key);
        String head = jsonByKey.get("head").toString();
        String body = jsonByKey.get("body").toString();
        String foot = jsonByKey.get("foot").toString();
        if (StringUtils.isNotBlank(head) && StringUtils.isNotBlank(body) && StringUtils.isNotBlank(foot)) {
            headTextAreas.setText(head);
            bodyTextAreas.setText(body);
            footTextAreas.setText(foot);
        }
    }

    private void setScore(TextField[] textFields, String[] keys) {

    }
}
