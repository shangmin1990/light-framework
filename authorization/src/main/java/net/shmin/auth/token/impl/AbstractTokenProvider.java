package net.shmin.auth.token.impl;

import net.shmin.auth.AuthContext;
import net.shmin.auth.TokenError;
import net.shmin.auth.token.IAuthTokenGenerator;
import net.shmin.auth.token.IAuthTokenProvider;
import net.shmin.auth.token.Token;
import net.shmin.auth.token.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by benjamin on 2017/3/29.
 */
@Component
public abstract class AbstractTokenProvider implements IAuthTokenProvider, TokenError {

    private static Logger logger = LoggerFactory.getLogger(AbstractTokenProvider.class);

    @Autowired
    protected AuthContext authContext;

    @Autowired
    protected IAuthTokenGenerator tokenGenerator;

//    public <T> T getAttribute(String token, String attr, Class<T> tClass) {
//        byte[] bytes = getSerializedAttribute(token, attr);
//        String encoded = Base64.encodeBase64String(bytes);
//        if(bytes == null || bytes.length == 0){
//            return null;
//        }
//        return SerializationUtil.deserializeByKryo(bytes, tClass);
//    }
//
//    protected abstract byte[] getSerializedAttribute(String token, String attr);
//
//    public <T> void setAttribute(String token, String attr, T value){
//        if(value == null){
//            logger.error("value 不能为空!!!");
//            return;
//        }
//        byte[] result = SerializationUtil.serializeByKryo(value);
//        setSerializedAttribute(token, attr, result);
//    }
//
//    protected abstract void setSerializedAttribute(String token, String attr, byte[] result);


    @Override
    public Token newTokenFromRefreshToken(String refreshTokenValue) {

        if(checkToken(refreshTokenValue, TokenType.refreshToken)){
            Token token = tokenGenerator.generateAccessToken(true);
            token.getRefreshToken().setValue(refreshTokenValue);
            saveToken(token);
            logger.info("通过RefreshToken {} 重新生成了新的token {}", refreshTokenValue, token.toString());
            return token;
        }
        return null;
    }

}
