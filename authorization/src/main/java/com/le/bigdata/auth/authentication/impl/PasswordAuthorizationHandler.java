package com.le.bigdata.auth.authentication.impl;

import com.le.bigdata.auth.token.Token;
import com.le.bigdata.auth.util.WebUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * //TODO 控制请求次数,防止暴力破解
 * 用户名密码模式，如果client信任可以使用此模式，例如当前client是服务端的一部分
 * Created by benjamin on 9/9/14.
 */
@Component("passwordAuthHandler")
public class PasswordAuthorizationHandler extends GrantTypeAuthorizationHandlerAdapter {
    @Override
    public void handlePasswordGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception {

        boolean result = login(request);
        String username = request.getParameter(USERNAME);
        // 登录成功
        if (result) {
            // access_token
            String tokenValue = getTokenProvider().getAccessToken(username);
            Token token = getAuthTokenGenerator().generateAccessToken(username, false);
            if (tokenValue != null && !tokenValue.isEmpty()) {
                token.setValue(tokenValue);
            }
            getTokenProvider().saveToken(username, token);
            WebUtil.responseToken(request, response, token);
        } else {
            try {
                WebUtil.replyNoAccess(request, response, "username or password is wrong");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.handlePasswordGrantType(request, response);
    }
}
