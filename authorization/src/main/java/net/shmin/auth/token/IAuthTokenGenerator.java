package net.shmin.auth.token;

/**
 * Created by benjamin on 9/3/14.
 */
public interface IAuthTokenGenerator {
    /**
     * 生成授权码
     *
     * @return
     */
    Token generateAuthorizationCode();

    /**
     * 生成访问令牌
     *
     * @param withRefreshToken 是否生成刷新令牌
     * @return
     */
    Token generateAccessToken(boolean withRefreshToken);

}
