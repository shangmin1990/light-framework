package net.shmin.auth.authentication.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.shmin.auth.AuthContext;
import net.shmin.auth.authentication.AuthorizationHandler;
import net.shmin.auth.token.IAuthTokenProvider;
import net.shmin.auth.token.Token;
import net.shmin.auth.token.TokenType;
import net.shmin.auth.util.WebUtil;
import net.shmin.core.dto.CommonResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录权限相关的ctrl
 * Created by benjamin on 2016/12/14.
 */
@Controller
public class AuthorizationCtrl {

    private List<AuthorizationHandler> authorizationHandlers = new ArrayList<AuthorizationHandler>();

    @Resource(name = "redisTokenProvider")
    private IAuthTokenProvider tokenProvider;

    @Resource(name = "passwordAuthHandler")
    private AuthorizationHandler password;

    @Resource(name = "authorizationCodeHandler")
    private AuthorizationHandler authorizationCode;

    @Autowired
    private AuthContext authContext;

    @PostConstruct
    public void init() {
        authorizationHandlers.add(password);
//        authorizationHandlers.add(authorizationCode);
    }

    public IAuthTokenProvider getTokenProvider() {
        return tokenProvider;
    }

    public void setTokenProvider(IAuthTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @RequestMapping(method = RequestMethod.POST, value = "authorize")
    @ApiOperation(value = "登录接口(OAuth2.0认证 实现了 grant_type=password 和 grant_type=code的认证方式)",
    notes = "username是必传参数, 理论上来讲可以灵活的传递其他参数, 但默认的只需要传递 username, password, grant_type即可")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", dataType = "string",value = "用户名", paramType = "form"),
            @ApiImplicitParam(name = "password", dataType = "string", value = "密码", paramType = "form"),
            @ApiImplicitParam(name = "grant_type", dataType = "string", value = "用户名", defaultValue = "password", allowableValues = "password", paramType = "form")
    })
    public void authorize(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            for (AuthorizationHandler authorizationHandler : authorizationHandlers) {
                authorizationHandler.handleAuthorization(request, response);
            }
        } catch (Exception e) {
            response.setStatus(500);
            response.setCharacterEncoding(request.getCharacterEncoding());
            PrintWriter out = response.getWriter();
            e.printStackTrace(out);
            out.flush();
            out.close();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "revoke_token")
    @ApiOperation("登出接口")
    @ResponseBody
    public CommonResponseDTO revokeToken(@ApiIgnore HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = WebUtil.getCookieValue(request, authContext.getAccessTokenCookieName());
        tokenProvider.removeToken(token, TokenType.accessToken);
        Cookie cookie = new Cookie(authContext.getAccessTokenCookieName(), "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Cookie cookie1 = new Cookie(authContext.getUsernameCookieName(), "");
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        return CommonResponseDTO.success();
    }

    @RequestMapping(method = RequestMethod.GET, value = "refresh_token")
    @ApiOperation("通过refresh_token 获取新的token")
    @ResponseBody
    public CommonResponseDTO refreshToken(@ApiIgnore HttpServletResponse response,
                                          @RequestParam("refresh_token") String refreshToken) throws IOException {
//        String username = WebUtil.getCookieValue(request, authContext.getUsernameCookieName());
        Token token = tokenProvider.newTokenFromRefreshToken(refreshToken);
        Cookie cookie = new Cookie(authContext.getAccessTokenCookieName(), token.getValue());
        cookie.setMaxAge(0);
        cookie.setMaxAge((int) token.getExpires() / 1000);
        response.addCookie(cookie);
        return CommonResponseDTO.success(token);
    }
}
