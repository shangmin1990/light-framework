package net.shmin.auth.token;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import static net.shmin.core.Constant.ACCESS_TOKEN;

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

    private TokenType tokenType;

    // 此token的刷新token
    private Token refreshToken;

    public Token() {

    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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
        if (this.getTokenType() == TokenType.accessToken)
            return refreshToken;
        return null;
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
        String tokenType = null;
        switch (this.tokenType){
            case authorizationCode:
                tokenType = "code";
                break;
            case refreshToken:
                tokenType = "refresh_token";
                break;
            case accessToken:
                tokenType = ACCESS_TOKEN;
                break;
        }

        str += ", tokenType = " + tokenType;
        if (refreshToken != null) {
            str += ", refreshToken = " + refreshToken.toString();
        }
        str += " ]";
        return str;
    }
}

