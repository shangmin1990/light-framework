package net.shmin.auth.token.impl;

import net.shmin.auth.TokenError;
import net.shmin.auth.authentication.exception.TokenException;
import net.shmin.auth.token.Token;
import net.shmin.auth.token.TokenType;
import net.shmin.core.util.JedisPoolUtil;
import net.shmin.core.util.SerializationUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;


/**
 * Created by benjamin on 2016/12/13.
 */
@Component("redisTokenProviderImpl")
@Lazy
public class RedisTokenProvider extends AbstractTokenProvider {

    @Autowired
    private JedisPoolUtil jedisPoolUtil;

    @Override
    public boolean checkToken(String token, TokenType tokenType) {
        Jedis conn = jedisPoolUtil.getJedis();
        try {
            selectDataBase(conn, tokenType);
            return conn.exists(token);
        } finally {
            conn.close();
        }

    }

    @Override
    public void saveToken(Token token) {
        long expires = token.getExpires();
        Jedis conn = jedisPoolUtil.getJedis();
        selectDataBase(conn, token.getTokenType());
        // 如果已经存在则不更新value 重新更新过期时间
        conn.hset(token.getValue(), token.getValue(), token.getValue());
        conn.pexpire(token.getValue(), expires);
        conn.close();
        if(token.getRefreshToken() != null){
            saveToken(token.getRefreshToken());
        }
    }

    @Override
    public  <T> T getAttribute(String token, String attr, Class<T> tClass) {
        if (checkToken(token, TokenType.accessToken)){
            Jedis conn = jedisPoolUtil.getJedis();
            selectDataBase(conn, TokenType.accessToken);
            String value = conn.hget(token, attr);
            if (value == null || value.isEmpty()){
                return null;
            }
            byte[] bytes = Base64.decodeBase64(value);
            conn.close();
            return SerializationUtil.deserializeByKryo(bytes, tClass);
        }
        throw new TokenException(TokenError.TOKEN_NOT_EXIST, "值为:["+ token +"]的token不存在");
    }

    @Override
    public void setAttribute(String token, String attr, Object result) {
        if(result == null)
            return;
        if (checkToken(token, TokenType.accessToken)){
            Jedis conn = jedisPoolUtil.getJedis();
            selectDataBase(conn, TokenType.accessToken);
            byte[] bytes = SerializationUtil.serializeByKryo(result);
            String encoded = Base64.encodeBase64String(bytes);
            conn.hset(token, attr, encoded);
            conn.close();
            return;
        }
        throw new TokenException(TokenError.TOKEN_NOT_EXIST, "值为:["+ token +"]的token不存在");
    }


    @Override
    public void removeAttribute(String token, String attr) {
        if (checkToken(token, TokenType.accessToken)){
            Jedis conn = jedisPoolUtil.getJedis();
            selectDataBase(conn, TokenType.accessToken);
            conn.hdel(token, attr);
            conn.close();
            return;
        }
        throw new TokenException(TokenError.TOKEN_NOT_EXIST, "值为:["+ token +"]的token不存在");
    }


    private void selectDataBase(Jedis conn, TokenType tokenType) {

        switch (tokenType){
            case authorizationCode:
                conn.select(authContext.getAuthorizationCodeRedisDatabase());
                break;
            case accessToken:
                conn.select(authContext.getAccessTokenRedisDatabase());
                break;
            case refreshToken:
                conn.select(authContext.getRefreshTokenRedisDatabase());
                break;
        }
    }


    @Override
    public void removeToken(String token, TokenType tokenType) {
        Jedis conn = jedisPoolUtil.getJedis();
        selectDataBase(conn, tokenType);
        conn.del(token);
        conn.close();
    }

    @Override
    public void destroy() {
        Jedis jedis = jedisPoolUtil.getJedis();
        jedis.select(authContext.getAuthorizationCodeRedisDatabase());
        jedis.flushDB();
        jedis.select(authContext.getAccessTokenRedisDatabase());
        jedis.flushDB();
        jedis.select(authContext.getRefreshTokenRedisDatabase());
        jedis.flushDB();
        jedis.close();
    }
}
