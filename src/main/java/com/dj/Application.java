package com.dj;

import api.account.Logout;
import com.dj.util.ExecutorPool;
import com.dj.util.GlobalConstant;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import net.mamoe.mirai.Bot;

import java.util.HashMap;
import java.util.Map;

import static com.dj.util.SimpleTools.simpleTools;

public class Application extends javafx.application.Application {

    public static final Map<String, Initializable> controllers = new HashMap<>();

    public static final Map<GlobalConstant.StageInfo, Stage> ALL_STAGE = new HashMap<>();



    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage loginStage = simpleTools.createStage(GlobalConstant.StageInfo.LOGIN, null);
        loginStage.show();
    }

    @Override
    public void stop(){
        try {
            //关掉线程池
            ExecutorPool.executorService.shutdownNow();
            //退出QQ的登录
            Bot.getBotInstances().forEach(bot -> bot.close(new Exception()));

            Platform.exit();
            //退出登录
            Logout.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }


}
