package net.shmin.auth.token.impl;

import net.shmin.auth.token.Token;
import net.shmin.auth.token.TokenType;
import net.shmin.core.util.JedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;


/**
 * Created by benjamin on 2016/12/13.
 */
@Component("redisTokenProvider")
public class RedisTokenProvider extends AbstractTokenProvider {

    @Autowired
    private JedisPoolUtil jedisPoolUtil;

    private static final String ACCESS_TOKEN_SUFFIX_REG = "${access-token.key.suffix}";

    private static final String TOKEN_PREFIX_REG = "${token.key.prefix}";

    private static final String REFRESH_TOKEN_SUFFIX_REG = "${refresh-token.key.suffix}";

    private static final String AUTHORIZATION_CODE_SUFFIX_REG = "${authorization-code.key.suffix}";

    private static final String ACCESS_TOKEN_REDIS_DATABASE_REG = "${access-token.redis.database}";

    private static final String REFRESH_TOKEN_REDIS_DATABASE_REG = "${refresh-token.redis.database}";

    private static final String AUTHORIZATION_CODE_REDIS_DATABASE_REG = "${authorization-code.redis.database}";

    @Value(TOKEN_PREFIX_REG)
    private String TOKEN_PREFIX;

    @Value(ACCESS_TOKEN_SUFFIX_REG)
    private String ACCESS_TOKEN_SUFFIX;

    @Value(REFRESH_TOKEN_SUFFIX_REG)
    private String REFRESH_TOKEN_SUFFIX;

    @Value(AUTHORIZATION_CODE_SUFFIX_REG)
    private String AUTHORIZATION_CODE_SUFFIX;

    @Value(ACCESS_TOKEN_REDIS_DATABASE_REG)
    private String access_token_storage;

    @Value(REFRESH_TOKEN_REDIS_DATABASE_REG)
    private String refresh_token_storage;

    @Value(AUTHORIZATION_CODE_REDIS_DATABASE_REG)
    private String authorization_code_storage;

    private int ACCESS_TOKEN_STORAGE = 0;

    private int AUTHORIZATION_CODE_STORAGE = 0;

    private int REFRESH_TOKEN_STORAGE = 0;

    public String getAccess_token_storage() {
        return access_token_storage;
    }

    public void setAccess_token_storage(String access_token_storage) {
        this.access_token_storage = access_token_storage;
    }

    public String getRefresh_token_storage() {
        return refresh_token_storage;
    }

    public void setRefresh_token_storage(String refresh_token_storage) {
        this.refresh_token_storage = refresh_token_storage;
    }

    public String getAuthorization_code_storage() {
        return authorization_code_storage;
    }

    public void setAuthorization_code_storage(String authorization_code_storage) {
        this.authorization_code_storage = authorization_code_storage;
    }

    public int getACCESS_TOKEN_STORAGE() {
        return ACCESS_TOKEN_STORAGE;
    }

    public int getAUTHORIZATION_CODE_STORAGE() {
        return AUTHORIZATION_CODE_STORAGE;
    }

    public int getREFRESH_TOKEN_STORAGE() {
        return REFRESH_TOKEN_STORAGE;
    }

    public String getTOKEN_PREFIX() {
        return TOKEN_PREFIX;
    }

    public void setTOKEN_PREFIX(String TOKEN_PREFIX) {
        this.TOKEN_PREFIX = TOKEN_PREFIX;
    }

    public String getACCESS_TOKEN_SUFFIX() {
        return ACCESS_TOKEN_SUFFIX;
    }

    public void setACCESS_TOKEN_SUFFIX(String ACCESS_TOKEN_SUFFIX) {
        this.ACCESS_TOKEN_SUFFIX = ACCESS_TOKEN_SUFFIX;
    }

    public String getREFRESH_TOKEN_SUFFIX() {
        return REFRESH_TOKEN_SUFFIX;
    }

    public void setREFRESH_TOKEN_SUFFIX(String REFRESH_TOKEN_SUFFIX) {
        this.REFRESH_TOKEN_SUFFIX = REFRESH_TOKEN_SUFFIX;
    }

    public String getAUTHORIZATION_CODE_SUFFIX() {
        return AUTHORIZATION_CODE_SUFFIX;
    }

    public void setAUTHORIZATION_CODE_SUFFIX(String AUTHORIZATION_CODE_SUFFIX) {
        this.AUTHORIZATION_CODE_SUFFIX = AUTHORIZATION_CODE_SUFFIX;
    }

    @PostConstruct
    public void init(){
        // 设置database
        if (access_token_storage != null
                && !access_token_storage.isEmpty()
                && !ACCESS_TOKEN_REDIS_DATABASE_REG.equals(access_token_storage)){
            ACCESS_TOKEN_STORAGE = Integer.parseInt(access_token_storage);
        }

        if (refresh_token_storage != null
                && !refresh_token_storage.isEmpty()
                && !REFRESH_TOKEN_REDIS_DATABASE_REG.equals(refresh_token_storage)){
            REFRESH_TOKEN_STORAGE = Integer.parseInt(refresh_token_storage);
        }

        if (authorization_code_storage != null
                && !authorization_code_storage.isEmpty()
                && !AUTHORIZATION_CODE_REDIS_DATABASE_REG.equals(authorization_code_storage)){
            AUTHORIZATION_CODE_STORAGE = Integer.parseInt(authorization_code_storage);
        }

        if (ACCESS_TOKEN_SUFFIX.isEmpty()
                || ACCESS_TOKEN_SUFFIX_REG.equals(ACCESS_TOKEN_SUFFIX)){
            ACCESS_TOKEN_SUFFIX = "_ACCESS_TOKEN";
        }

        if (REFRESH_TOKEN_SUFFIX.isEmpty() ||
                REFRESH_TOKEN_SUFFIX_REG.equals(REFRESH_TOKEN_SUFFIX)){
            REFRESH_TOKEN_SUFFIX = "_REFRESH_TOKEN";
        }

        if (AUTHORIZATION_CODE_SUFFIX.isEmpty()
                || AUTHORIZATION_CODE_SUFFIX_REG.equals(AUTHORIZATION_CODE_SUFFIX)){
            AUTHORIZATION_CODE_SUFFIX = "_AUTHORIZATION_CODE";
        }

        if(TOKEN_PREFIX_REG.equals(TOKEN_PREFIX)){
            TOKEN_PREFIX = "";
        }

    }

    @Override
    public boolean checkToken(String username, Token token) {
        Jedis conn = jedisPoolUtil.getJedis();
        selectDataBase(conn, token.getTokenType());
        String key = buildKey(username, token.getTokenType());
        String value = conn.get(key);
        conn.close();
        return value != null && value.equals(token.getValue());
    }

    private String buildKey(String username, TokenType tokenType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TOKEN_PREFIX);
        stringBuilder.append(username);
        switch (tokenType){
            case accessToken:
                stringBuilder.append(ACCESS_TOKEN_SUFFIX);
                break;
            case refreshToken:
                stringBuilder.append(REFRESH_TOKEN_SUFFIX);
                break;
            case authorizationCode:
                stringBuilder.append(AUTHORIZATION_CODE_SUFFIX);
        }
        return stringBuilder.toString();
    }

    @Override
    public void saveToken(String username, Token token) {
        long expires = token.getExpires();
        Jedis conn = jedisPoolUtil.getJedis();
        selectDataBase(conn, token.getTokenType());
        // 如果已经存在则不更新value 重新更新过期时间
        String key = buildKey(username, token.getTokenType());
        conn.setnx(key, token.getValue());
        conn.pexpire(key, expires);
        conn.close();
        if(token.getRefreshToken() != null){
            saveToken(username, token.getRefreshToken());
        }
    }

    @Override
    public String getToken(String username, TokenType tokenType) {
        Jedis conn = jedisPoolUtil.getJedis();
        selectDataBase(conn, tokenType);
        String key = buildKey(username, tokenType);
        String value = conn.get(key);
        conn.close();
        return value;
    }

    private void selectDataBase(Jedis conn, TokenType tokenType) {

        switch (tokenType){
            case authorizationCode:
                conn.select(AUTHORIZATION_CODE_STORAGE);
                break;
            case accessToken:
                conn.select(ACCESS_TOKEN_STORAGE);
                break;
            case refreshToken:
                conn.select(ACCESS_TOKEN_STORAGE);
                break;
        }
    }


    @Override
    public void deleteToken(String username, TokenType tokenType) {
        Jedis conn = jedisPoolUtil.getJedis();
        selectDataBase(conn, tokenType);
        String key = buildKey(username, tokenType);
        conn.del(key);
        conn.close();
    }

    @Override
    public void destroy() {
        Jedis jedis = jedisPoolUtil.getJedis();
        jedis.select(AUTHORIZATION_CODE_STORAGE);
        jedis.flushDB();
        jedis.select(ACCESS_TOKEN_STORAGE);
        jedis.flushDB();
        jedis.select(REFRESH_TOKEN_STORAGE);
        jedis.flushDB();
        jedis.close();
    }
}
