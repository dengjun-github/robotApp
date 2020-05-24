package com.dj.pojo.request.user;

import lombok.Data;

@Data
public class UserBind {

    private String account;

    private String security_key;

    public UserBind(String account, String security_key) {
        this.account = account;
        this.security_key = security_key;
    }
}
