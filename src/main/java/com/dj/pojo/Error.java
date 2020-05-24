package com.dj.pojo;

import lombok.Data;

@Data
public class Error {

    private String title;

    private String message;

    public Error(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
