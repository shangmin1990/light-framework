package net.shmin.auth.token.impl;

import net.shmin.auth.AuthContext;
import net.shmin.auth.token.IAuthTokenGenerator;
import net.shmin.auth.token.Token;
import net.shmin.auth.token.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AuthContext authContext;

    @PostConstruct
    public void init() {
//        logger.info("access_token有效期为{}分钟", authContext.getAccessTokenExpire() / 60000);
//        logger.info("refresh_token有效期为{}分钟", authContext.getRefreshTokenExpires() / 60000);
//        logger.info("authorization_code有效期为{}分钟", authContext.getAuthorizationCodeExpires() / 60000);
    }

    @Override
    public Token generateAuthorizationCode() {
        Token token = generateToken();
        if (token != null) {
            token.setExpires( authContext.getAuthorizationCodeExpires());
            token.setTokenType(TokenType.authorizationCode);
        }
        logger.info("生成鉴权码:{}", token);
        return token;
    }

    @Override
    public Token generateAccessToken(boolean useRefreshToken) {
        Token token = generateToken();
        token.setTokenType(TokenType.accessToken);
        token.setExpires(authContext.getAccessTokenExpire());
        if (useRefreshToken) {
            Token refreshToken = generateRefreshToken();
            token.setRefreshToken(refreshToken);
        }
        logger.info("生成访问令牌:{}", token);
        return token;
    }

    private Token generateRefreshToken() {
        Token token = generateToken();
        if (token != null) {
            token.setExpires(authContext.getRefreshTokenExpires());
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
