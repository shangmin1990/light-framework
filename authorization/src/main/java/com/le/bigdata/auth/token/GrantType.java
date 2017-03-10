package com.le.bigdata.auth.token;

/**
 * Created by benjamin on 9/4/14.
 */
public enum GrantType {
    // 用户名密码授权模式
    PASSWORD("password"),
    // 授权码授权模式
    AUTHORIZATION_CODE("code"),
    // 隐式授权
    IMPLICIT("implicit"),
    // 客户端授权模式
    CLIENT("client_credentials");

    GrantType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
