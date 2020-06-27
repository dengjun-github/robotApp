package com.dj.module;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

import static com.dj.util.SimpleTools.simpleTools;

/**
 * 图片查看器
 */
public class ImageShow {

    public static ImageShow IMAGE_SHOW = new ImageShow();




    private double sceneWidth = 400;
    private double sceneHeight = 400;
    private Image image;

    //制作一个图片查看器
    public void create(String title,File file,Stage stage) throws MalformedURLException {
        BorderPane root = new BorderPane();

        image = new Image(file.toURI().toURL().toString());

        ImageView imageView = new ImageView();
        imageView.setImage(image);
        root.setCenter(imageView);

        Scene scene = new Scene(root,sceneWidth,sceneHeight);
        stage.setScene(scene);
        simpleTools.adjustStage2Center(stage,sceneWidth,sceneHeight);
        stage.show();
    }

}
