package net.shmin.auth.interceptor;

import net.shmin.auth.AuthContext;
import net.shmin.auth.handler.IRequestHandler;
import net.shmin.auth.token.IAuthTokenProvider;
import net.shmin.auth.token.Token;
import net.shmin.auth.token.TokenType;
import net.shmin.auth.util.WebUtil;
import net.shmin.core.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by benjamin on 2016/12/14.
 * 1.登录检查
 * 2.如果请求有参数,验证Verify-Code的值,保证请求不会被篡改.
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    @Autowired
    private AuthContext authContext;

    private IAuthTokenProvider tokenProvider;

    @Autowired
    private ServletContext servletContext;

    private String loginUrl;

    // 请求参数code验证 默认关闭
    private boolean verifyCodeEnable = false;

    @Autowired
    private IRequestHandler requestHandler;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public boolean isVerifyCode() {
        return verifyCodeEnable;
    }

    public void setVerifyCode(boolean verifyCode) {
        this.verifyCodeEnable = verifyCode;
    }

    @PostConstruct
    public void init() {
        if (loginUrl == null || loginUrl.isEmpty()) {
            loginUrl = servletContext.getContextPath() + "/login.html";
        }

        tokenProvider = authContext.getAuthTokenProvider();
        logger.info("tokenProvider:{}", tokenProvider);
        //servletContext.getContextPath();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String cookieTokenValue = WebUtil.getCookieValue(request, Constant.ACCESS_TOKEN);
//        String username = WebUtil.getCookieValue(request, Constant.USERNAME);
        Token token = new Token();
        token.setTokenType(TokenType.accessToken);
        token.setValue(cookieTokenValue);
        // 没有传递token 或者token已经过期
        // 不用refresh Token了 直接跳转到登录页
        if (cookieTokenValue == null
                || cookieTokenValue.isEmpty()
//                || username == null
//                || username.isEmpty()
                || !tokenProvider.checkToken(cookieTokenValue, TokenType.accessToken)) {
            if (WebUtil.isAjaxRequest(request)) {
                logger.info("not login and request use ajax, send response ");
                WebUtil.reply(request, response, 401, "请先登录");
            } else {
                logger.info("not login, redirect to url:{}", loginUrl);
                response.sendRedirect(loginUrl);
            }
        } else {

            if (verifyCodeEnable) {
                // token已经验证通过 现在检查请求是否被篡改
                try {
                    boolean result = requestHandler.handleRequest(request, response);
                    if (result) {
                        return true;
                    } else {
                        // request string 签名值不对,
                        WebUtil.reply(request, response, 601, "请求的签名值不对");
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return true;
            }
        }
        return false;
    }
}
