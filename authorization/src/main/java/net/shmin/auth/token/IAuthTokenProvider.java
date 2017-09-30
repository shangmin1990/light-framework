package net.shmin.auth.token;

/**
 * Created by benjamin on 9/3/14.
 */
public interface IAuthTokenProvider {

    /**
     * 检查token的合法性
     * @param token
     * @return
     */
    boolean checkToken(String token, TokenType tokenType);

    /**
     * 保存一个Token
     *
     * @param key
     */
    void saveToken(Token key);

    /**
     * 获取一个属性值
     *
     * @param token
     * @param attr
     * @return
     */
    public <T> T getAttribute(String token, String attr, Class<T> tClass);

    /**
     * 设置一个属性值
     *
     * @param token
     * @param attr
     * @return
     */
    <T> void setAttribute(String token, String attr, T value);

    /**
     * 删除一个属性值
     *
     * @param token
     * @param attr
     * @return
     */
    void removeAttribute(String token, String attr);

    /**
     * 通过refreshToken获取 新的Token
     * @param refreshToken
     * @return
     */
    Token newTokenFromRefreshToken(String refreshToken);

    /**
     * 删除一个Token
     *
     * @param key
     */
    void removeToken(String key, TokenType tokenType);

    /**
     * 销毁方法
     */
    void destroy();


}
