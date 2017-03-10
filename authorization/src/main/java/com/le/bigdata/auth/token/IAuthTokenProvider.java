package com.le.bigdata.auth.token;

/**
 * Created by benjamin on 9/3/14.
 */
public interface IAuthTokenProvider {
//  /**
//   * AuthTokenProvider初始化 调用的方法
//   */
//  void initializer();

    /**
     * 检查token的合法性
     *
     * @param key
     * @param tokenValue
     * @return
     */
    boolean checkToken(String key, Token tokenValue);

    /**
     * 保存一个Token(可能是 authorization_code 与 refreshToken)
     *
     * @param key
     * @param token
     */
    void saveToken(String key, Token token);

    /**
     * 获取access_token
     *
     * @param key
     * @return
     */
    String getAccessToken(String key);

    /**
     * 获取authorization_code
     *
     * @param key
     * @return
     */
    String getAuthorizationCode(String key);

    /**
     * 通过 refreshToken获取 新的Token
     *
     * @param token
     * @return
     */
    String refreshToken(Token token);

    /**
     * 删除一个Token
     *
     * @param key
     */
    void deleteToken(String key);

    /**
     * 销毁方法
     */
    void destroy();


}
