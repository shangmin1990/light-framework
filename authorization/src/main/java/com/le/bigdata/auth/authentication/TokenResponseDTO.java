package com.le.bigdata.auth.authentication;

/**
 * Created by benjamin on 2016/12/13.
 */
public class TokenResponseDTO {

    // 用户授权的唯一票据
    private String access_token;

    // access_token的生命周期，单位是秒数。
    private long expires_in;

    // 授权用户的UID，本字段只是为了方便开发者，
    // 第三方应用不能用此字段作为用户登录状态的识别，
    // 只有access_token才是用户授权的唯一票据。
    private String username;

    // 重新获取token时使用的token
    private String refresh_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
