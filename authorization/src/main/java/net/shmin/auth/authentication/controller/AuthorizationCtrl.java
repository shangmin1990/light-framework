package net.shmin.auth.authentication.controller;

import com.alibaba.fastjson.JSONObject;
import net.shmin.auth.AuthContext;
import net.shmin.auth.AuthErrorCode;
import net.shmin.auth.authentication.AuthorizationHandler;
import net.shmin.auth.authentication.AuthorizationHandlerFactory;
import net.shmin.auth.token.IAuthTokenProvider;
import net.shmin.auth.token.Token;
import net.shmin.auth.token.TokenType;
import net.shmin.auth.util.WebUtil;
import net.shmin.core.dto.CommonResponseDTO;
import net.shmin.core.exception.BusinessServiceException;
import net.shmin.core.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录权限相关的ctrl
 * Created by benjamin on 2016/12/14.
 */
@Controller
public class AuthorizationCtrl implements AuthErrorCode{

    @Autowired
    private AuthorizationHandlerFactory factory;

    private Logger logger = LoggerFactory.getLogger(AuthorizationCtrl.class);

    @Resource(name = "redisTokenProvider")
    private IAuthTokenProvider tokenProvider;

    @Autowired
    private AuthContext authContext;

    @PostConstruct
    public void init() {

    }

    public IAuthTokenProvider getTokenProvider() {
        return tokenProvider;
    }

    public void setTokenProvider(IAuthTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @RequestMapping(method = RequestMethod.POST, value = AuthContext.REQUEST_AUTHORIZE_PATH_REG)
//    @ApiOperation(value = "登录接口(OAuth2.0认证 实现了 grant_type=password 和 grant_type=code的认证方式)",
//    notes = "username是必传参数, 理论上来讲可以灵活的传递其他参数, 但默认的只需要传递 username, password, grant_type即可")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", dataType = "string",value = "用户名", paramType = "form"),
//            @ApiImplicitParam(name = "password", dataType = "string", value = "密码", paramType = "form"),
//            @ApiImplicitParam(name = "grant_type", dataType = "string", value = "用户名", defaultValue = "password", allowableValues = "password", paramType = "form")
//    })
    public void authorize(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            AuthorizationHandler authorizationHandler = factory.getAuthorizationHandler(request);
            if (authorizationHandler != null){
                authorizationHandler.handleAuthorization(request, response);
            }
        } catch (Exception e) {
            LoggerUtil.throwableLog(logger, e);
            response.setStatus(500);
            CommonResponseDTO commonResponseDTO;
            if (e instanceof BusinessServiceException){
                BusinessServiceException serviceException = (BusinessServiceException) e;
                commonResponseDTO = CommonResponseDTO.error(serviceException.getCode(), serviceException.getMessage());
            } else {
                commonResponseDTO = CommonResponseDTO.error(SERVICE_EXCEPTION, e.getMessage());
            }
            response.setCharacterEncoding(request.getCharacterEncoding());
            PrintWriter out = response.getWriter();
            out.write(JSONObject.toJSONString(commonResponseDTO));
            out.flush();
            out.close();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = AuthContext.REQUEST_LOGOUT_PATH_REG)
////    @ApiOperation("登出接口")
    @ResponseBody
    public CommonResponseDTO revokeToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = WebUtil.getCookieValue(request, authContext.getAccessTokenCookieName());
        tokenProvider.removeToken(token, TokenType.accessToken);
        Cookie cookie = new Cookie(authContext.getAccessTokenCookieName(), "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return CommonResponseDTO.success();
    }

    @RequestMapping(method = RequestMethod.GET, value = AuthContext.REQUEST_REFRESH_TOKEN_PATH_REG)
////    @ApiOperation("通过refresh_token 获取新的token")
    @ResponseBody
    public CommonResponseDTO refreshToken(HttpServletResponse response,
                                          @RequestParam("refresh_token") String refreshToken) throws IOException {
        Token token = tokenProvider.newTokenFromRefreshToken(refreshToken);
        Cookie cookie = new Cookie(authContext.getAccessTokenCookieName(), token.getValue());
        cookie.setMaxAge(0);
        cookie.setMaxAge((int) token.getExpires() / 1000);
        response.addCookie(cookie);
        return CommonResponseDTO.success(token);
    }
}
