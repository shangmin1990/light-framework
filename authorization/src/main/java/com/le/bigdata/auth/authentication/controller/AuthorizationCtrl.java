package com.le.bigdata.auth.authentication.controller;

import com.le.bigdata.auth.authentication.AuthorizationHandler;
import com.le.bigdata.auth.dto.CommonResponseDTO;
import com.le.bigdata.auth.token.IAuthTokenProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static com.le.bigdata.core.Constant.ACCESS_TOKEN;

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

    @PostConstruct
    public void init() {
        authorizationHandlers.add(password);
        authorizationHandlers.add(authorizationCode);
    }

    @RequestMapping(method = RequestMethod.POST, value = "authorize")
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
    @ResponseBody
    public CommonResponseDTO authorize(@CookieValue(ACCESS_TOKEN) String token) throws IOException {
        tokenProvider.deleteToken(token);
        return new CommonResponseDTO(true);
    }
}
