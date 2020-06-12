package com.dj.entity.pojo.request.user;

import lombok.Data;

@Data
public class UserRegister {

    private String account;

    private String password;

    private String confirm;

    public UserRegister(String account, String password, String confirm) {
        this.account = account;
        this.password = password;
        this.confirm = confirm;
    }
}
