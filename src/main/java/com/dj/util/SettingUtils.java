package com.dj.util;

import api.account.EditSettings;
import api.future.EditSettingsFutureListener;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dj.entity.vo.InstructionVo;
import com.google.common.collect.Lists;
import common.Client;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import org.apache.commons.lang3.StringUtils;
import serialize.Serializer;
import serialize.pojo.Odds;
import serialize.pojo.Settings;
import serialize.pojo.option.EditSettingsOption;
import utils.App;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dj.util.GlobalConstant.IMAGE_PATH;
import static com.dj.util.GlobalConstant.IMAGE_URL;
import static serialize.pojo.Keys.*;

public class SettingUtils {
    public static final Map<String, List<String>> REGEX_MAP = new HashMap<>();

    public static final Map<String, File> IMAGE_SETTINGS_MAP = new HashMap<>();

    public static final SimpleBooleanProperty IS_REMEMBER_PASSWORD = new SimpleBooleanProperty();

    public static final Map<Integer,String> EVENT_MAP = new HashMap<>();

    public static final String RATIO = "ratio";

    public static final String MONEY = "money";

    public static final String NUMBER = "num";

    public static final String LENGTH = "length";


    /**
     * 图片初始化
     */
    public static void initImg() {
        Client.account.getSettings().forEach(it -> {
            setImageFile(it, SETTINGS_STOP_BETS_IMAGE);
            setImageFile(it, SETTINGS_START_BETS_IMAGE);
            setImageFile(it, SETTINGS_START_BETS_IMAGE2);
            setImageFile(it, SETTINGS_COUNTDOWN_IMAGE);
        });
        System.out.println("图片初始化完成");

        InitRegexMap();
        System.out.println("指令初始化完成");
    }

    private static void setImageFile(Settings it, String key) {
        if (it.getKey().equals(key)) {
            JSONObject json = JSONObject.parseObject(it.getJson());
            String imageName = json.get("image").toString();
            if (!imageName.equals(""))
                IMAGE_SETTINGS_MAP.put(it.getKey(), FileUtil.saveUrlAs(IMAGE_URL + imageName, IMAGE_PATH, it.getKey(), "GET"));
        }
    }

    private static void InitRegexMap() {
        try {
            //玩法的指令
            List<Odds> odds = App.getOdds(Client.account);
            odds.forEach(odd -> {
                List<String> list = new ArrayList<>();
                Arrays.asList(odd.getKeyword().split("\\|")).forEach(format -> list.add(ActionUtil.getRegex4Format(odd.getKey(), format)));
                SettingUtils.REGEX_MAP.put(odd.getKey(), list);
            });

            //其他操作的指令,如上下分

            List<InstructionVo> instructionVos = getInstructionVos();
            instructionVos.forEach(ins -> {
                List<String> list = new ArrayList<>();
                Arrays.asList(ins.getValue().split("\\|")).forEach(format -> list.add(ActionUtil.getRegex4Format(ins.getKey(), format)));
                SettingUtils.REGEX_MAP.put(ins.getKey(), list);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Settings getSettingsByKey(String key) {
        Settings settings = (Settings) Client.account.getSettings().stream().filter((it) -> it.getKey().equals(key)).findAny().get();
        return settings;
    }

    public static InstructionVo getInstructionVoByKey(String key) {
        return getInstructionVos().stream().filter(it -> it.getKey().equals(key)).findAny().get();
    }


    private static List<InstructionVo> getInstructionVos() {
        Settings settings = Client.account.getSettings().stream().filter((it) -> it.getKey().equals(SETTINGS_INSTRUCT_FORMAT)).findAny().get();
        List list = Serializer.deserialize(settings.getJson(), List.class);
        List<InstructionVo> ins = Lists.newArrayList();
        for (int i = 0; i < list.size(); ++i) {
            String obj = Serializer.serialize(list.get(i));
            InstructionVo instructionVo = (InstructionVo) Serializer.deserialize(obj, InstructionVo.class);
            ins.add(instructionVo);
        }
        return ins;
    }


    public static JSONObject getJsonByKey(String key) {
        Settings settings = getSettingsByKey(key);
        String jsonStr = settings.getJson();
        return JSONObject.parseObject(jsonStr);
    }

    public static void initEventMap() {
        Map<Integer, String> secondMap = Stream.of(getSettingsByKey(SETTINGS_COUNTDOWN_IMAGE), getSettingsByKey(SETTINGS_STOP_BETS_TIME), getSettingsByKey(SETTINGS_SEND_MESSAGE1)
                , getSettingsByKey(SETTINGS_SEND_MESSAGE2), getSettingsByKey(SETTINGS_SEND_MESSAGE3), getSettingsByKey(SETTINGS_SEND_MESSAGE4))
                .filter(settings -> settings.getStatus() && StringUtils.isNotBlank(getJsonByKey(settings.getKey()).get("second").toString()))
                .collect(Collectors.toMap(settings -> (Integer) getJsonByKey(settings.getKey()).get("second"), Settings::getKey));

        EVENT_MAP.putAll(secondMap);
    }

    //修改复选框
    public static void setCheckBox(CheckBox checkBox,Settings settings) {
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) {
                settings.setStatus(newValue);
                editSettings(settings);
            }
        });
    }

    //修改普通的常规settings
    public static void setTextField(TextInputControl textinputControl , Settings settings,String jsonKey) {
        textinputControl.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                System.out.println();
                settings.getJson();
                String oldStr = JSONObject.parseObject(settings.getJson()).get(jsonKey).toString();
                if (!oldStr.equals(textinputControl.getText())) {
                    editSettings(settings);
                }
            }
        });
    }

    //修改指令
    public static void setInstruct(TextField textField,GlobalConstant.Action action){
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                Settings settings = getSettingsByKey(SETTINGS_INSTRUCT_FORMAT);
                List<InstructionVo> instructionVos = JSONArray.parseArray(settings.getJson(), InstructionVo.class);
                InstructionVo instructionVo = instructionVos.stream().filter(vo -> vo.getKey().equals(action.getKey())).findAny().get();
                if (!instructionVo.getValue().equals(textField.getText())) {
                    instructionVo.setValue(textField.getText());
                    settings.setJson(JSONArray.toJSONString(instructionVos));
                    EditSettingsOption option = EditSettingsOption.builder().key(settings.getKey()).json(settings.getJson()).status(settings.getStatus()).title(settings.getTitle()).build();
                    EditSettings.deal(option).addListener(new EditSettingsFutureListener() {
                        @Override
                        public void onSuccess() {
                            System.out.println("修改指令成功!");
                            List<String> regexes = new ArrayList<>();
                            Arrays.asList(textField.getText().split("\\|")).forEach(value -> {
                                regexes.add(ActionUtil.getRegex4Format(action.getKey(), value));
                            });
                            SettingUtils.REGEX_MAP.put(action.getKey(), regexes);
                        }

                        @Override
                        public void onFailure(int i) {
                            System.out.println("修改失败!,错误信息:" + ErrorCodeUtil.getMsgByErroCode(i));
                        }
                    });
                }
            }
        });

    }


    private static void editSettings(Settings settings) {
        EditSettingsOption option = EditSettingsOption.builder().key(settings.getKey()).json(settings.getJson()).status(settings.getStatus()).title(settings.getTitle()).build();
        EditSettings.deal(option).addListener(new EditSettingsFutureListener() {
            @Override
            public void onSuccess() {
                System.out.println("修改成功!");
            }

            @Override
            public void onFailure(int i) {
                System.out.println("修改失败!,错误信息:" + ErrorCodeUtil.getMsgByErroCode(i));
            }
        });
    }

}
