package com.le.bigdata.auth.token;

/**
 * Created by benjamin on 9/3/14.
 */
public interface IAuthTokenProvider {

    /**
     * 检查token的合法性
     *
     * @param key
     * @param token
     * @return
     */
    boolean checkToken(String key, Token token);

    /**
     * 保存一个Token
     *
     * @param key
     * @param token
     */
    void saveToken(String key, Token token);

    /**
     * 通过key获取一个token的值
     *
     * @param key
     * @return
     */
    String getToken(String key, TokenType tokenType);

    /**
     * 通过refreshToken获取 新的Token
     * @param key
     * @param refreshTokenValue
     * @return
     */
    Token newTokenFromRefreshToken(String key, String refreshTokenValue);

    /**
     * 删除一个Token
     *
     * @param key
     */
    void deleteToken(String key, TokenType tokenType);

    /**
     * 销毁方法
     */
    void destroy();


}
