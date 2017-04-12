package net.shmin.auth.token.impl;

import net.shmin.auth.token.IAuthTokenGenerator;
import net.shmin.auth.token.Token;
import net.shmin.auth.token.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * Token生成器
 * Created by benjamin on 9/3/14.
 */
@Component
public class SimpAuthTokenGenerator implements IAuthTokenGenerator {

    private Logger logger = LoggerFactory.getLogger(SimpAuthTokenGenerator.class);

    //默认配置，可以在authorization.properties中配置此项
    //Token有效期 (单位分钟)
    @Value("${access-token.expires}")
    private String accessTokenExpires;
    //默认配置，可以在oauth.properties中配置此项
    //AccessToken有效期 10分钟 (默认配置)
    @Value("${authorization-code.expires}")
    private String authorizationCodeExpires;
    //默认配置，可以在oauth.properties中配置此项
    //永不过期
    @Value("${refresh-token.expires}")
    private String refreshTokenExpires;

    public long getAccessTokenExpires() {
        try{
            // 如果没有设置 则取默认值
            long result = Long.parseLong(accessTokenExpires) * 60 * 1000L;
            return result;
        } catch (NumberFormatException e){
            // 默认值: 7天
            return 7 * 24 * 3600 * 1000L;
        }
    }

    public void setAccessTokenExpires(String accessTokenExpires) {
        this.accessTokenExpires = accessTokenExpires;
    }

    public long getAuthorizationCodeExpires() {
        try {
            // 如果没有设置 则取默认值
            return Long.parseLong(authorizationCodeExpires) * 60 * 1000L;
        }catch (NumberFormatException e){
            // 默认值: 10分钟
            return 10 * 60 * 1000L;
        }

    }

    public void setAuthorizationCodeExpires(String authorizationCodeExpires) {
        this.authorizationCodeExpires = authorizationCodeExpires;
    }

    public long getRefreshTokenExpires() {
        try {
            // 如果没有设置 则取默认值
            return Long.parseLong(refreshTokenExpires) * 60 * 1000L;
        }catch (NumberFormatException e){
            // 默认值: 30天
            return  30 * 24 * 3600 * 1000L;
        }
    }

    public void setRefreshTokenExpires(String refreshTokenExpires) {
        this.refreshTokenExpires = refreshTokenExpires;
    }

    @PostConstruct
    public void init() {
        logger.info("access_token有效期为{}分钟", getAccessTokenExpires() / 60000);
        logger.info("refresh_token有效期为{}分钟", getRefreshTokenExpires() / 60000);
        logger.info("authorization_code有效期为{}分钟", getAuthorizationCodeExpires() / 60000);
    }

    @Override
    public Token generateAuthorizationCode() {
        Token token = generateToken();
        if (token != null) {
            token.setExpires(getAuthorizationCodeExpires());
            token.setTokenType(TokenType.authorizationCode);
        }
        return token;
    }

    @Override
    public Token generateAccessToken(boolean useRefreshToken) {
        Token token = generateToken();
        token.setTokenType(TokenType.accessToken);
        token.setExpires(getAccessTokenExpires());
        if (useRefreshToken) {
            Token refreshToken = generateRefreshToken();
            token.setRefreshToken(refreshToken);
        }
        return token;
    }

    private Token generateRefreshToken() {
        Token token = generateToken();
        if (token != null) {
            token.setExpires(getRefreshTokenExpires());
            token.setTokenType(TokenType.refreshToken);
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

}
