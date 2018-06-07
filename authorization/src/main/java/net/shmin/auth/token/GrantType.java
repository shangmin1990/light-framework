package net.shmin.auth.token;

/**
 * OAuth2.0 四种认证模式 目前支持 用户名密码授权模式
 */
public enum GrantType {
    // 用户名密码授权模式
    PASSWORD("password"),
    // 授权码授权模式
    AUTHORIZATION_CODE("authorization_code"),
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
