package com.dj.fxModule;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Clock extends Pane {

    private Timeline animation;


    public Clock(EventHandler<ActionEvent> onFinished) {
        animation = new Timeline(new KeyFrame(Duration.millis(1000), onFinished));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    public static void open(EventHandler<ActionEvent> onFinished){
        new Clock(onFinished);
    }

//    public void timelabel() {
//        tmp--;
//        S = tmp + "";
//        label.setText(S);
//    }

}