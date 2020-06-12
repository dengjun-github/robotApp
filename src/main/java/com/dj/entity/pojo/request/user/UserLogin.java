package com.dj.entity.pojo.request.user;

import lombok.Data;

@Data
public class UserLogin {

    private String account;
    private String password;

    public UserLogin(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
