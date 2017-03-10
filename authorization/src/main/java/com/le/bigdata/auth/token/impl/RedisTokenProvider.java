package com.le.bigdata.auth.token.impl;

import com.le.bigdata.auth.token.IAuthTokenProvider;
import com.le.bigdata.auth.token.Token;
import com.le.bigdata.core.util.JedisPoolUtil;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;


/**
 * Created by benjamin on 2016/12/13.
 */
@Component("redisTokenProvider")
public class RedisTokenProvider implements IAuthTokenProvider {

    private int AUTHORIZATION_CODE_STORAGE = 13;

    private int ACCESS_TOKEN_STORAGE = 15;

    private int REFRESH_TOKEN_STORAGE = 14;

    @Override
    public boolean checkToken(String key, Token token) {
        Jedis conn = JedisPoolUtil.getJedis();
        selectDataBase(conn, token);
        String value = conn.get(key);
        conn.close();
        return value != null && value.equals(token.getValue());
    }

    @Override
    public void saveToken(String key, Token token) {
        long expires = token.getExpires();
        Jedis conn = JedisPoolUtil.getJedis();
        selectDataBase(conn, token);
        // 如果已经存在则不更新value 重新更新过期时间
        conn.setnx(key, token.getValue());
        conn.pexpire(key, expires);
        conn.close();
    }

    private void selectDataBase(Jedis conn, Token token) {
        if (token.isAuthorizationCode()) {
            conn.select(AUTHORIZATION_CODE_STORAGE);
        } else if (token.isRefresh()) {
            conn.select(REFRESH_TOKEN_STORAGE);
        } else {
            conn.select(ACCESS_TOKEN_STORAGE);
        }
    }

    @Override
    public String getAccessToken(String key) {
        return getValueByKey(ACCESS_TOKEN_STORAGE, key);
    }

    @Override
    public String getAuthorizationCode(String key) {
        return getValueByKey(AUTHORIZATION_CODE_STORAGE, key);
    }

    private String getValueByKey(int database, String key) {
        Jedis conn = JedisPoolUtil.getJedis();
        conn.select(database);
        String value = conn.get(key);
        conn.close();
        return value;
    }

    @Override
    public String refreshToken(Token token) {
        return null;
    }

    @Override
    public void deleteToken(String key) {
        Jedis conn = JedisPoolUtil.getJedis();
        conn.select(AUTHORIZATION_CODE_STORAGE);
        conn.del(key);
        conn.close();
    }

    @Override
    public void destroy() {
        Jedis jedis = JedisPoolUtil.getJedis();
        jedis.select(AUTHORIZATION_CODE_STORAGE);
        jedis.flushDB();
        jedis.select(ACCESS_TOKEN_STORAGE);
        jedis.flushDB();
        jedis.select(REFRESH_TOKEN_STORAGE);
        jedis.flushDB();
        jedis.close();
    }
}
