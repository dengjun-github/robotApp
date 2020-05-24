package com.dj;

import com.dj.controller.BindController;
import com.dj.controller.LoginController;
import com.dj.controller.MainController;
import com.dj.controller.RegisterController;
import com.dj.net.WebClientVerticle;
import com.dj.util.ExecutorPool;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.ThreadUtils;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("UserLogin");
        gotologin();
        primaryStage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        WebClientVerticle.vertx.close();
        ExecutorPool.executorService.shutdownNow().stream().forEach(runnable->{
            new Thread(runnable).stop();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    //登录
    public LoginController gotologin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("../../fxml/Login.fxml");
            login.setApp(this);
            return login;
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //注册
    public void gotoRegister() {
        try {
            RegisterController register = (RegisterController) replaceSceneContent("../../fxml/Register.fxml");
            register.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //绑卡
    public void gotoBind() {
        try {
            BindController bind = (BindController) replaceSceneContent("../../fxml/Bind.fxml");
            bind.setApp(this);
        } catch (Exception e) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //主窗口
    public void gotoMain() {
        try {
            MainController main = (MainController) replaceSceneContent("../../fxml/Main.fxml");

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
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

}
