package com.le.bigdata.auth.token.impl;

import com.le.bigdata.auth.token.IAuthTokenGenerator;
import com.le.bigdata.auth.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Token生成器
 * Created by benjamin on 9/3/14.
 */
@Component
public class SimpAuthTokenGenerator implements IAuthTokenGenerator {

    private Logger logger = LoggerFactory.getLogger(SimpAuthTokenGenerator.class);

    //默认配置，可以在oauth.properties中配置此项
    //Token有效期 (单位天)
    private long accessTokenExpires = 7 * 24 * 60 * 60 * 1000L;
    //默认配置，可以在oauth.properties中配置此项
    //AccessToken有效期 10分钟 (默认配置)
    private long authorizationCodeExpires = 10 * 60 * 1000L;
    //默认配置，可以在oauth.properties中配置此项
    //永不过期
    private long refreshTokenExpires = 30 * 24 * 60 * 60 * 1000L;

    public long getAccessTokenExpires() {
        return accessTokenExpires;
    }

    public void setAccessTokenExpires(long accessTokenExpires) {
        this.accessTokenExpires = accessTokenExpires;
    }

    public long getAuthorizationCodeExpires() {
        return authorizationCodeExpires;
    }

    public void setAuthorizationCodeExpires(long authorizationCodeExpires) {
        this.authorizationCodeExpires = authorizationCodeExpires;
    }

    public long getRefreshTokenExpires() {
        return refreshTokenExpires;
    }

    public void setRefreshTokenExpires(long refreshTokenExpires) {
        this.refreshTokenExpires = refreshTokenExpires;
    }

    public SimpAuthTokenGenerator() {
        logger.info("access_token有效期为{}ms", accessTokenExpires);
        logger.info("refresh_token有效期为{}ms", refreshTokenExpires);
        logger.info("authorization_code有效期为{}ms", authorizationCodeExpires);
    }

    @Override
    public Token generateAuthorizationCode(String name) {
        Token token = generateToken();
        if (token != null) {
            token.setExpires(authorizationCodeExpires);
            token.setAuthorizationCode(true);
        }
        return token;
    }

    @Override
    public Token generateAccessToken(String name, boolean useRefreshToken) {
        Token token = generateToken();
        if (token != null) {
            token.setExpires(accessTokenExpires);
            if (useRefreshToken) {
                Token refreshToken = generateRefreshToken(name);
                token.setRefreshToken(refreshToken);
            }
        }
        return token;
    }

    private Token generateRefreshToken(String name) {
        Token token = generateToken();
        if (token != null) {
            token.setExpires(refreshTokenExpires);
        }
        return token;
    }

    private Token generateToken() {
        String uuid = UUID.randomUUID().toString();
        String[] uuidShorts = uuid.split("-");
        StringBuilder stringBuilder = new StringBuilder();
        for (String uuidString : uuidShorts) {
            stringBuilder.append(uuidString);
        }
        Token token;
        token = new Token();
        String value = stringBuilder.toString();
//    value = name + TOKEN_SEPARATOR + value;
        token.setValue(value);
        token.setGeneratorTime(System.currentTimeMillis());
        return token;
    }

    public static void main(String[] args) {
        Token a = new SimpAuthTokenGenerator().generateToken();
        System.out.println(a);
    }
}
