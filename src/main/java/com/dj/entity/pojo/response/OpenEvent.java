package com.dj.entity.pojo.response;

import lombok.Data;

@Data
public class OpenEvent {
    private String eventName;
    private int countDown;

    public OpenEvent(String eventName, int countDown) {
        this.eventName = eventName;
        this.countDown = countDown;
    }
}
