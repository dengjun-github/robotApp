package com.dj.util;

import lombok.Data;

@Data
public class HttpResult {
    private  boolean isok;
    private  int code;
    private  String message;
    private  Object data;
}
