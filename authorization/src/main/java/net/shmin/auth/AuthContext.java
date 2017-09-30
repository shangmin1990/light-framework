package net.shmin.auth;

import net.shmin.auth.authentication.PasswordValidator;
import net.shmin.auth.token.IAuthTokenProvider;
import net.shmin.auth.util.WebUtil;
import net.shmin.core.util.DateUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: benjamin
 * @Date: Create in  2017/9/27 下午5:30
 * @Description:
 */
@Component
public class AuthContext implements ApplicationContextAware, EmbeddedValueResolverAware {

    public static final String PASSWORD_VALIDATOR_BEAN_NAME_REG = "${password-validator.beanName}";

    public static final String TOKEN_PROVIDER_BEAN_NAME_REG = "${token.provider.beanName}";

    public static final String ACCESS_TOKEN_EXPIRES_REG = "${access-token.expires}";

    public static final String AUTH_CODE_EXPIRES_REG = "${authorization-code.expires}";

    public static final String REFRESH_TOKEN_EXPIRES_REG = "${refresh-token.expires}";

    public static final String ACCESS_TOKEN_SUFFIX_REG = "${access-token.key.suffix}";

    public static final String TOKEN_PREFIX_REG = "${token.key.prefix}";

    public static final String REFRESH_TOKEN_SUFFIX_REG = "${refresh-token.key.suffix}";

    public static final String AUTHORIZATION_CODE_SUFFIX_REG = "${authorization-code.key.suffix}";

    public static final String ACCESS_TOKEN_REDIS_DATABASE_REG = "${access-token.redis.database}";

    public static final String REFRESH_TOKEN_REDIS_DATABASE_REG = "${refresh-token.redis.database}";

    public static final String AUTHORIZATION_CODE_REDIS_DATABASE_REG = "${authorization-code.redis.database}";

    public static final String ACCESS_TOKEN_COOKIE_NAME_REG = "${cookie.access-token.name}";

    public static final String USER_COOKIE_NAME_REG= "${cookie.username.name}";

    public static final String USER_REQUEST_PARAM_NAME_REG = "${request.authorize.param.username}";

    public static final String DEFAULT_TOKEN_PROVIDER_BEAN_NAME = "redisTokenProvider";


    private ApplicationContext applicationContext;

    private StringValueResolver stringValueResolver;

    private PasswordValidator passwordValidator;

    private long accessTokenExpire = 0L;

    private long authorizationCodeExpires = 0L;

    private long refreshTokenExpires = 0L;

    @Value(TOKEN_PREFIX_REG)
    private String tokenKeyPrefix;

    @Value(ACCESS_TOKEN_SUFFIX_REG)
    private String accessTokenKeySuffix = "_ACCESS_TOKEN";

    @Value(AUTHORIZATION_CODE_SUFFIX_REG)
    private String authorizationCodeKeySuffix = "_AUTHORIZATION_CODE";

    @Value(REFRESH_TOKEN_SUFFIX_REG)
    private String refreshTokenKeySuffix = "_REFRESH_TOKEN";

    private int accessTokenRedisDatabase = 0;

    private int authorizationCodeRedisDatabase = 0;

    private int refreshTokenRedisDatabase = 0;

    @Value(ACCESS_TOKEN_COOKIE_NAME_REG)
    private String accessTokenCookieName = "access_token";

    @Value(USER_COOKIE_NAME_REG)
    private String usernameCookieName = "username";

    @Value(USER_REQUEST_PARAM_NAME_REG)
    private String usernameRequestParam = "username";

    private IAuthTokenProvider authTokenProvider;

    @PostConstruct
    private void init(){
        // PASSWORD VALIDATOR
        String passwordValidatorBeanName = stringValueResolver.resolveStringValue(PASSWORD_VALIDATOR_BEAN_NAME_REG);
        passwordValidator = applicationContext.getBean(passwordValidatorBeanName, PasswordValidator.class);

        // AUTH TOKEN PROVIDER
        String authTokenProviderBeanName = stringValueResolver.resolveStringValue(TOKEN_PROVIDER_BEAN_NAME_REG);

        if (authTokenProviderBeanName == null
                || authTokenProviderBeanName.isEmpty()
                || TOKEN_PROVIDER_BEAN_NAME_REG.equals(authTokenProviderBeanName)){
            authTokenProviderBeanName = DEFAULT_TOKEN_PROVIDER_BEAN_NAME;
        }

        authTokenProvider = applicationContext.getBean(authTokenProviderBeanName, IAuthTokenProvider.class);

        // 默认七天
        String access_token_expires = stringValueResolver.resolveStringValue(ACCESS_TOKEN_EXPIRES_REG);

        if (access_token_expires == null
                || access_token_expires.isEmpty()
                || ACCESS_TOKEN_EXPIRES_REG.equals(access_token_expires)){
            access_token_expires = "7d";
        }

        accessTokenExpire = DateUtils.resolveMillionSeconds(access_token_expires);

        String authorization_code_expires = stringValueResolver.resolveStringValue(AUTH_CODE_EXPIRES_REG);

        if (authorization_code_expires == null
                || authorization_code_expires.isEmpty()
                || AUTH_CODE_EXPIRES_REG.equals(authorization_code_expires)){
            authorization_code_expires = "10m";
        }

        authorizationCodeExpires = DateUtils.resolveMillionSeconds(authorization_code_expires);

        String refresh_token_expires = stringValueResolver.resolveStringValue(REFRESH_TOKEN_EXPIRES_REG);

        if (refresh_token_expires == null
                || refresh_token_expires.isEmpty()
                || REFRESH_TOKEN_EXPIRES_REG.equals(refresh_token_expires)){
            refresh_token_expires = "30d";
        }

        refreshTokenExpires = DateUtils.resolveMillionSeconds(refresh_token_expires);

        String accessTokenRedisDatabaseStr = stringValueResolver.resolveStringValue(ACCESS_TOKEN_REDIS_DATABASE_REG);

        if (accessTokenRedisDatabaseStr == null
                || accessTokenRedisDatabaseStr.isEmpty()
                || ACCESS_TOKEN_REDIS_DATABASE_REG.equals(accessTokenRedisDatabaseStr)){
            accessTokenRedisDatabaseStr = "0";
        }

        String authorizationCodeRedisDatabaseStr = stringValueResolver.resolveStringValue(AUTHORIZATION_CODE_REDIS_DATABASE_REG);

        if (authorizationCodeRedisDatabaseStr == null
                || authorizationCodeRedisDatabaseStr.isEmpty()
                || AUTHORIZATION_CODE_REDIS_DATABASE_REG.equals(authorizationCodeRedisDatabaseStr)){
            authorizationCodeRedisDatabaseStr = "0";
        }

        String refreshTokenRedisDatabaseStr = stringValueResolver.resolveStringValue(REFRESH_TOKEN_REDIS_DATABASE_REG);

        if (refreshTokenRedisDatabaseStr == null
                || refreshTokenRedisDatabaseStr.isEmpty()
                || REFRESH_TOKEN_REDIS_DATABASE_REG.equals(refreshTokenRedisDatabaseStr)){
            refreshTokenRedisDatabaseStr = "0";
        }

        authorizationCodeRedisDatabase = Integer.parseInt(authorizationCodeRedisDatabaseStr);

        accessTokenRedisDatabase = Integer.parseInt(accessTokenRedisDatabaseStr);

        refreshTokenRedisDatabase = Integer.parseInt(refreshTokenRedisDatabaseStr);

        if (tokenKeyPrefix == null || tokenKeyPrefix.isEmpty() || TOKEN_PREFIX_REG.equals(tokenKeyPrefix)){
            tokenKeyPrefix = "";
        }

        if (accessTokenKeySuffix == null || accessTokenKeySuffix.isEmpty() || ACCESS_TOKEN_SUFFIX_REG.equals(accessTokenKeySuffix)){
            accessTokenKeySuffix = "_ACCESS_TOKEN";
        }

        if (authorizationCodeKeySuffix == null || authorizationCodeKeySuffix.isEmpty() || AUTHORIZATION_CODE_SUFFIX_REG.equals(authorizationCodeKeySuffix)){
            authorizationCodeKeySuffix = "_AUTHORIZATION_CODE";
        }

        if (refreshTokenKeySuffix == null || refreshTokenKeySuffix.isEmpty() || REFRESH_TOKEN_SUFFIX_REG.equals(refreshTokenKeySuffix)){
            refreshTokenKeySuffix = "_REFRESH_TOKEN";
        }

        if (accessTokenCookieName == null || accessTokenCookieName.isEmpty() || ACCESS_TOKEN_COOKIE_NAME_REG.equals(accessTokenCookieName)){
            accessTokenCookieName = "access_token";
        }

        if (usernameCookieName == null || usernameCookieName.isEmpty() || USER_COOKIE_NAME_REG.equals(usernameCookieName)){
            usernameCookieName = "username";
        }

        if (usernameRequestParam == null || usernameRequestParam.isEmpty() || USER_REQUEST_PARAM_NAME_REG.equals(usernameRequestParam)){
            usernameRequestParam = "username";
        }

    }

    public PasswordValidator getPasswordValidator() {
        return passwordValidator;
    }

    public void setPasswordValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    public long getAccessTokenExpire() {
        return accessTokenExpire;
    }

    public void setAccessTokenExpire(long accessTokenExpire) {
        this.accessTokenExpire = accessTokenExpire;
    }

    public long getAuthorizationCodeExpires() {
        return authorizationCodeExpires;
    }

    public void setAuthorizationCodeExpires(long authorizationCodeExpires) {
        this.authorizationCodeExpires = authorizationCodeExpires;
    }

    public long getRefreshTokenExpires() {
        return refreshTokenExpires;
    }

    public void setRefreshTokenExpires(long refreshTokenExpires) {
        this.refreshTokenExpires = refreshTokenExpires;
    }

    public String getTokenKeyPrefix() {
        return tokenKeyPrefix;
    }

    public void setTokenKeyPrefix(String tokenKeyPrefix) {
        this.tokenKeyPrefix = tokenKeyPrefix;
    }

    public String getAccessTokenKeySuffix() {
        return accessTokenKeySuffix;
    }

    public void setAccessTokenKeySuffix(String accessTokenKeySuffix) {
        this.accessTokenKeySuffix = accessTokenKeySuffix;
    }

    public String getAuthorizationCodeKeySuffix() {
        return authorizationCodeKeySuffix;
    }

    public void setAuthorizationCodeKeySuffix(String authorizationCodeKeySuffix) {
        this.authorizationCodeKeySuffix = authorizationCodeKeySuffix;
    }

    public String getRefreshTokenKeySuffix() {
        return refreshTokenKeySuffix;
    }

    public void setRefreshTokenKeySuffix(String refreshTokenKeySuffix) {
        this.refreshTokenKeySuffix = refreshTokenKeySuffix;
    }

    public int getAccessTokenRedisDatabase() {
        return accessTokenRedisDatabase;
    }

    public void setAccessTokenRedisDatabase(int accessTokenRedisDatabase) {
        this.accessTokenRedisDatabase = accessTokenRedisDatabase;
    }

    public int getAuthorizationCodeRedisDatabase() {
        return authorizationCodeRedisDatabase;
    }

    public void setAuthorizationCodeRedisDatabase(int authorizationCodeRedisDatabase) {
        this.authorizationCodeRedisDatabase = authorizationCodeRedisDatabase;
    }

    public int getRefreshTokenRedisDatabase() {
        return refreshTokenRedisDatabase;
    }

    public void setRefreshTokenRedisDatabase(int refreshTokenRedisDatabase) {
        this.refreshTokenRedisDatabase = refreshTokenRedisDatabase;
    }

    public String getAccessTokenCookieName() {
        return accessTokenCookieName;
    }

    public void setAccessTokenCookieName(String accessTokenCookieName) {
        this.accessTokenCookieName = accessTokenCookieName;
    }

    public String getUsernameCookieName() {
        return usernameCookieName;
    }

    public void setUsernameCookieName(String usernameCookieName) {
        this.usernameCookieName = usernameCookieName;
    }

    public String getUsernameRequestParam() {
        return usernameRequestParam;
    }

    public void setUsernameRequestParam(String usernameRequestParam) {
        this.usernameRequestParam = usernameRequestParam;
    }

    public IAuthTokenProvider getAuthTokenProvider() {
        return authTokenProvider;
    }

    public void setAuthTokenProvider(IAuthTokenProvider authTokenProvider) {
        this.authTokenProvider = authTokenProvider;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.stringValueResolver = resolver;
    }

    public <T> T getAttribute(String token, String attr, Class<T> tClass){
        return this.authTokenProvider.getAttribute(token, attr, tClass);
    }

    public void setAttribute(String token, String attr, Object value){
        this.authTokenProvider.setAttribute(token, attr, value);
    }

    public void removeAttribute(String token, String attr){
        this.authTokenProvider.removeAttribute(token, attr);
    }


    public <T> T getAttribute(HttpServletRequest request, String attr, Class<T> tClass){
        String token = WebUtil.getCookieValue(request, accessTokenCookieName);
        return this.authTokenProvider.getAttribute(token, attr, tClass);
    }

    public void setAttribute(HttpServletRequest request, String attr, Object value){
        String token = WebUtil.getCookieValue(request, accessTokenCookieName);
        this.authTokenProvider.setAttribute(token, attr, value);
    }

    public void removeAttribute(HttpServletRequest request, String attr){
        String token = WebUtil.getCookieValue(request, accessTokenCookieName);
        this.authTokenProvider.removeAttribute(token, attr);
    }
}
