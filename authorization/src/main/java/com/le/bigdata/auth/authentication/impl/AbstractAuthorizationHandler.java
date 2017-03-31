package com.le.bigdata.auth.authentication.impl;

import com.le.bigdata.auth.authentication.AuthorizationHandler;
import com.le.bigdata.auth.authentication.PasswordValidator;
import com.le.bigdata.auth.authentication.exception.NoGrantTypeFoundException;
import com.le.bigdata.auth.token.GrantType;
import com.le.bigdata.auth.token.IAuthTokenGenerator;
import com.le.bigdata.auth.token.IAuthTokenProvider;
import com.le.bigdata.auth.token.Token;
import com.le.bigdata.core.Constant;
import com.le.bigdata.core.dto.CommonResponseDTO;
import com.le.bigdata.core.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by benjamin on 9/4/14.
 * handle authentication request.
 * 对应oauth种的四种授权方式
 * 隐式授权和客户端模式没有实现，请使用authorization_code授权码模式和密码模式
 */
public abstract class AbstractAuthorizationHandler implements AuthorizationHandler, Constant {

    @Deprecated
    @Value("${password-validator.class}")
    private String className;

    @Value("${password-validator.beanName}")
    private String beanName;

    @Resource(name = "redisTokenProvider")
    private IAuthTokenProvider tokenProvider;

    @Autowired
    private IAuthTokenGenerator authTokenGenerator;

    private PasswordValidator passwordValidator;

    @Value("${request.authorize.param.username}")
    protected String requestParamUsername;

    public PasswordValidator getPasswordValidator() {
        return passwordValidator;
    }

    @Resource(name = "passwordValidator")
    public void setPasswordValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    @Value("${cookie.access_token.name}")
    protected String access_token_cookie_name;

    @Value("${cookie.username.name}")
    protected String username_cookie_name;

    public String getAccess_token_cookie_name() {
        return access_token_cookie_name;
    }

    public void setAccess_token_cookie_name(String access_token_cookie_name) {
        this.access_token_cookie_name = access_token_cookie_name;
    }

    public String getUsername_cookie_name() {
        return username_cookie_name;
    }

    public void setUsername_cookie_name(String username_cookie_name) {
        this.username_cookie_name = username_cookie_name;
    }

    public String getRequestParamUsername() {
        return requestParamUsername;
    }

    public void setRequestParamUsername(String requestParamUsername) {
        this.requestParamUsername = requestParamUsername;
    }

    public IAuthTokenProvider getTokenProvider() {
        return tokenProvider;
    }

    public void setTokenProvider(IAuthTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public IAuthTokenGenerator getAuthTokenGenerator() {
        return authTokenGenerator;
    }

    public void setAuthTokenGenerator(IAuthTokenGenerator authTokenGenerator) {
        this.authTokenGenerator = authTokenGenerator;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @PostConstruct
    private void init() {
        initPasswordValidatorByClassName();
        initPasswordValidatorByBeanName();
        initProperty();
    }

    private void initProperty() {
        if(access_token_cookie_name == null
                || access_token_cookie_name.isEmpty()
                || access_token_cookie_name.equals("${cookie.access_token.name}")){
            access_token_cookie_name = "access_token";
        }

        if(username_cookie_name == null
                || username_cookie_name.isEmpty()
                || username_cookie_name.equals("${cookie.username.name}")){
            username_cookie_name = "username";
        }

        if(requestParamUsername == null
                || requestParamUsername.isEmpty()
                || requestParamUsername.equals("${request.authorize.param.username}")){
            requestParamUsername = "username";
        }

    }


    /**
     * 通过查找注册在spring ioc context中的bean name获取PasswordValidator实例
     */
    private void initPasswordValidatorByBeanName() {
        if (beanName != null && !beanName.isEmpty() && !"${password-validator.beanName}".equals(beanName)) {
            ApplicationContext context = SpringContextUtils.getApplicationContext();
            PasswordValidator passwordValidator = context.getBean(beanName, PasswordValidator.class);
            // 如果设置了错误的beanName 则报错!
            Assert.notNull(passwordValidator);
            this.passwordValidator = passwordValidator;
        }
    }

    /**
     * 通过反射创建PasswordValidator实例
     */
    @Deprecated
    private void initPasswordValidatorByClassName() {
        if (className != null && !className.isEmpty() && !"${password-validator.class}".equals(className)) {
            try {
                Class clazz = Class.forName(className);
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class clazz1 : interfaces) {
                    if (clazz1.equals(PasswordValidator.class)) {
                        PasswordValidator passwordValidator = (PasswordValidator) clazz.newInstance();
                        this.passwordValidator = passwordValidator;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleAuthorization(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String grant_type = request.getParameter(GRANT_TYPE);

        assert grant_type != null;
        if (GrantType.PASSWORD.getName().equals(grant_type)) {
            handlePasswordGrantType(httpServletRequest, httpServletResponse);
        } else if (GrantType.AUTHORIZATION_CODE.getName().equals(grant_type)) {
            handleAuthCodeGrantType(httpServletRequest, httpServletResponse);
        }
        //TODO 隐式授权  客户端授权
//    else if(GrantType.IMPLICIT == grantType){
//      handleImplicitGrantType(httpServletRequest, httpServletResponse);
//    } else if (GrantType.CLIENT == grantType){
//      handleClientGrantType(httpServletRequest, httpServletResponse);
//    }
        else {
            throw new NoGrantTypeFoundException(10001, "Grant_type " + grant_type + " not support");
        }

    }

    CommonResponseDTO login(HttpServletRequest request) throws Exception {
        return passwordValidator.login(request);
    }

    /**
     * 用户名,密码模式验证.
     * 此模式要求客户端具有高度可信任性.
     * 例如知名公司开发的客户端应用与自己开发的客户端应用可使用此方法
     *
     * @param request
     * @param response
     */
    public abstract void handlePasswordGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 授权码验证模式.
     * 此模式是最严谨,流程最全面的授权模式
     *
     * @param request
     * @param response
     */
    public abstract void handleAuthCodeGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 隐式授权模式
     *
     * @param request
     * @param response
     */
    public abstract void handleImplicitGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 客户端模式
     *
     * @param request
     * @param response
     */
    public abstract void handleClientGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected void setLoginSuccessCookies(HttpServletRequest request, HttpServletResponse response, Token token, String username){
        String path = request.getServletContext().getContextPath();
        // access_token cookie
        Cookie access_token_cookie = new Cookie(access_token_cookie_name, token.getValue());
        access_token_cookie.setPath(path);
        access_token_cookie.setHttpOnly(true);
        access_token_cookie.setVersion(1);
        access_token_cookie.setMaxAge((int) (token.getExpires() / 1000));
        // username cookie
        Cookie username_cookie = new Cookie(username_cookie_name, username);
        username_cookie.setPath(path);
        username_cookie.setHttpOnly(true);
        username_cookie.setVersion(1);
        username_cookie.setMaxAge((int) (token.getExpires() / 1000));

        response.addCookie(access_token_cookie);
        response.addCookie(username_cookie);
    }
}
