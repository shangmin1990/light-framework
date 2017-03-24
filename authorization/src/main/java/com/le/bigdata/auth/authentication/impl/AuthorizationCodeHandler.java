package com.le.bigdata.auth.authentication.impl;

import com.le.bigdata.auth.client.IClientManager;
import com.le.bigdata.auth.client.impl.ClientManager;
import com.le.bigdata.auth.token.Token;
import com.le.bigdata.auth.util.WebUtil;
import com.le.bigdata.core.dto.CommonResponseDTO;
import com.le.bigdata.core.util.PropertiesUtil;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by benjamin on 9/12/14.
 * 授权码模式实现。
 */
@Component("authorizationCodeHandler")
public class AuthorizationCodeHandler extends GrantTypeAuthorizationHandlerAdapter {

    private IClientManager<String> clientManager;

    public AuthorizationCodeHandler() {
        super();
        clientManager = new ClientManager();
    }

    @Override
    public void handleAuthCodeGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 用户名cookie
        String userCookieName = PropertiesUtil.getString(USER_COOKIE_NAME, DEFAULT_USER_COOKIE_NAME);
        //尝试从cookie中读取user
        String username = WebUtil.getCookieValue(request, userCookieName);
        //如果cookie中没有user 第一,没有登录,第二,正在登录
        if (username == null) {
            username = request.getParameter("username");
        }
        String responseType = request.getParameter(RESPONSE_TYPE);
        String clientId = request.getParameter(CLIENT_ID);
        String redirect_uri = request.getParameter(REDIRECT_URI);
        if (responseType != null
                && !responseType.isEmpty()
                && responseType.equals("code")
                && request.getPathInfo().contains("authorize")) {
            //未登录过 必须登录
            if (username == null) {
                try {
                    request.getRequestDispatcher("/loginPage.jsp").forward(request, response);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServletException e) {
                    e.printStackTrace();
                }
            }
            // 如果cookie是空 而且用户名非空,那就是正在登录验证
            if (WebUtil.getCookieValue(request, DEFAULT_USER_COOKIE_NAME) == null && username != null) {
                CommonResponseDTO result = login(request);
                if (!result.isSuccess()) {
                    try {
                        WebUtil.replyNoAccess(request, response, result.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            String state = request.getParameter(STATE);
            if (clientManager.checkClientId(clientId)) {
                Token token = getAuthTokenGenerator().generateAccessToken(username, false);
                getTokenProvider().saveToken(username, token);
//        req.getRequestDispatcher("access_token?code="+token.getValue()+"&redirect_uri="+redirect_uri+"&state="+state+"&grant_type=authorization_code").forward(req, resp);
                if (redirect_uri == null || redirect_uri.isEmpty()) {
                    redirect_uri = PropertiesUtil.getString(REDIRECT_URI);
                }
                StringBuilder stringBuilder = new StringBuilder(redirect_uri);
                stringBuilder.append("?code=");
                stringBuilder.append(token.getValue());
                stringBuilder.append("&client_id=");
                stringBuilder.append(clientId);
                //access_token的回调uri
                String redirect_uri_access_token = PropertiesUtil.getString(REDIRECT_URI, redirect_uri);
                stringBuilder.append("&redirect_uri=");
                stringBuilder.append(redirect_uri_access_token);
                if (state != null && !state.isEmpty()) {
                    stringBuilder.append("&state=");
                    stringBuilder.append(state);
                }
//        if(WebUtil.getCookieValue(request, DEFAULT_USER_COOKIE_NAME) == null){
//          Cookie cookie = new Cookie(, username);
//          cookie.setPath(request.getContextPath());
//          response.addCookie(cookie);
//          stringBuilder.append("&username=");
//          stringBuilder.append(username);
//        }
                try {
                    response.sendRedirect(stringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //直接获取token
        } else if (request.getPathInfo().contains("access_token")) {
            if (clientManager.checkClientId(clientId)) {
                String code = request.getParameter("code");
                if (code == null || code.isEmpty()) {
                    try {
                        WebUtil.replyNoAccess(request, response, CommonResponseDTO.error(10009, "No Request code found").toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (username == null) {
                        username = (String) request.getAttribute("user");
                    }
                    String value = getTokenProvider().getAuthorizationCode(username);
                    if (value.equals(code)) {
                        Token token = getAuthTokenGenerator().generateAccessToken(username, false);
                        getTokenProvider().saveToken(username, token);
                        try {
                            response.sendRedirect(redirect_uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        super.handleAuthCodeGrantType(request, response);
    }
}
