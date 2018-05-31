package net.shmin.auth.authentication.impl;

import net.shmin.auth.AuthContext;
import net.shmin.auth.authentication.AuthorizationHandler;
import net.shmin.auth.authentication.PasswordValidator;
import net.shmin.auth.event.LoginListenerManager;
import net.shmin.auth.token.GrantType;
import net.shmin.auth.token.IAuthTokenGenerator;
import net.shmin.auth.token.IAuthTokenProvider;
import net.shmin.auth.token.Token;
import net.shmin.core.Constant;
import net.shmin.core.dto.CommonResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
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
public abstract class AbstractAuthorizationHandler implements AuthorizationHandler, Constant{

    @Autowired
    protected AuthContext authContext;

    protected IAuthTokenProvider tokenProvider;

    @Autowired
    private IAuthTokenGenerator authTokenGenerator;

    private PasswordValidator passwordValidator;

    @Autowired
    protected LoginListenerManager loginListenerManager;

    protected String requestParamUsername;

    private static Logger logger = LoggerFactory.getLogger(AbstractAuthorizationHandler.class);

    public PasswordValidator getPasswordValidator() {
        return passwordValidator;
    }

    protected String access_token_cookie_name;

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

    @PostConstruct
    private void init(){
        tokenProvider = authContext.getAuthTokenProvider();
        passwordValidator = authContext.getPasswordValidator();
        requestParamUsername = authContext.getUsernameRequestParam();
        access_token_cookie_name = authContext.getAccessTokenCookieName();
        username_cookie_name  = authContext.getUsernameCookieName();

    }

    @Override
    public void handleAuthorization(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        /**
         * 防止请求被处理多次
         */
        if (REQUEST_AUTH_HANDLE.equals(request.getAttribute(REQUEST_AUTH_HANDLE))){
            logger.info("当前请求已经被处理过!!!");
            return;
        }

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String grant_type = request.getParameter(GRANT_TYPE);
        if (grant_type == null){
            handleNoneGrantType(httpServletRequest, httpServletResponse);
            return;
        }
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
            handleNoneGrantType(httpServletRequest, httpServletResponse);
        }

        request.setAttribute(REQUEST_AUTH_HANDLE, REQUEST_AUTH_HANDLE);
    }



    CommonResponseDTO login(HttpServletRequest request) throws Exception {
        return passwordValidator.login(request);
    }

    /**
     * 普通登录验证
     * @param httpServletRequest
     * @param httpServletResponse
     */
    protected abstract void handleNoneGrantType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception ;

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

    protected void setLoginSuccessCookies(HttpServletRequest request, HttpServletResponse response, Token token){
        String path = request.getServletContext().getContextPath();
        // access_token cookie
        Cookie access_token_cookie = new Cookie(access_token_cookie_name, token.getValue());
        access_token_cookie.setPath(path);
        access_token_cookie.setHttpOnly(true);
        access_token_cookie.setVersion(1);
        access_token_cookie.setMaxAge((int) (token.getExpires() / 1000));

        response.addCookie(access_token_cookie);
//        response.addCookie(username_cookie);
    }
}
