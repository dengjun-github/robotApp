package com.dj;

import com.dj.controller.BindController;
import com.dj.controller.IndexController;
import com.dj.controller.LoginController;
import com.dj.controller.RegisterController;
import com.dj.net.VertxWebClient;
import com.dj.util.ExecutorPool;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.mamoe.mirai.Bot;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dj.util.AllFxmlPath.*;

public class App extends Application {

    public static final Map<String,Initializable> controllers = new HashMap<>();


    private Stage stage;

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("QQRobot");
        stage.initStyle(StageStyle.UNIFIED);
        gotologin();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        VertxWebClient.vertx.close();
        ExecutorPool.executorService.shutdownNow();
        Bot.getBotInstances().forEach(bot ->{
            bot.close(new Exception());
        });
        Platform.exit();
    }


    public static void main(String[] args) {
        launch(args);
    }

    //登录窗口
    public LoginController gotologin() {
        try {
            stage.setResizable(true);
//            if (!stage.getStyle().equals(StageStyle.UNDECORATED)) stage.initStyle(StageStyle.UNDECORATED);
            LoginController login = (LoginController) replaceSceneContent(LOGIN_PATH);
            login.setApp(this);
            controllers.put("LoginController",login);
            return login;
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //注册窗口
    public void gotoRegister() {
        try {
            RegisterController register = (RegisterController) replaceSceneContent(REGISTER_PATH);
            register.setApp(this);
            controllers.put("RegisterController",register);
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //绑卡窗口
    public void gotoBind() {
        try {
            BindController bind = (BindController) replaceSceneContent(BIND_PATH);
            bind.setApp(this);
            controllers.put("BindController",bind);
        } catch (Exception e) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //主窗口
    public void gotoIndex() {
        try {
            IndexController index = (IndexController) replaceSceneContent(INDEX_PATH);
            index.setMainStage(this.stage);
            controllers.put("IndexController",index);
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = App.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(App.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        page.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        page.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        Scene scene = new Scene(page);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.centerOnScreen();
//        stage.setY(stage.getY() * 3f / 2f);
        return (Initializable) loader.getController();
    }


}
