package com.le.bigdata.auth.token;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import static com.le.bigdata.core.Constant.ACCESS_TOKEN;

/**
 * Created by benjamin on 9/3/14.
 */
public class Token implements Comparator<Token>, Serializable {

    // Token 值
    private String value;
    // Token生成时间
    private long generatorTime = new Date().getTime();
    // 有效期
    private long expires;
    // 是否是临时token
    private boolean authorizationCode;

    private boolean refresh;
    // 此token的刷新token
    private Token refreshToken;

    public Token() {

    }

    public boolean isAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(boolean authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

//  public String getKey(){
//    String value = this.getValue();
//    if(value == null){
//      return null;
//    }
//    String[] strings = value.split(TOKEN_SEPARATOR);
//    if(strings.length == 0){
//      return null;
//    }
//    return strings[0];
//  }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public long getGeneratorTime() {
        return generatorTime;
    }

    public void setGeneratorTime(long generatorTime) {
        this.generatorTime = generatorTime;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public Token getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(Token refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public int compare(Token token1, Token token2) {
        return (int) (token1.getGeneratorTime() - token2.getGeneratorTime());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Token && this.getValue().equals(((Token) obj).getValue())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public String toString() {
        String str = "[ value = " + this.getValue() + ", generateTime = " + generatorTime + ", expires = " + expires;
        String tokenType = ACCESS_TOKEN;
        if (authorizationCode) {
            tokenType = "code";
        } else if (refresh) {
            tokenType = "refresh_token";
        }
        str += ", tokenType = " + tokenType;
        if (refreshToken != null) {
            str += ", refreshToken = " + refreshToken.toString();
        }
        str += " ]";
        return str;
    }

    public static void main(String[] args) {
        String a = "a~~b";
        String[] strings = a.split("~~");
        System.out.println(strings[0]);
    }
}
