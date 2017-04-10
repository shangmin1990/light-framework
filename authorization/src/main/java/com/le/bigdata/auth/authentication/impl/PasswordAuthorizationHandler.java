package com.le.bigdata.auth.authentication.impl;

import com.le.bigdata.auth.token.Token;
import com.le.bigdata.auth.token.TokenType;
import com.le.bigdata.auth.util.WebUtil;
import com.le.bigdata.core.dto.CommonResponseDTO;
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

        CommonResponseDTO commonResponseDTO = login(request);
        boolean result = commonResponseDTO.isSuccess();
        String username = request.getParameter(requestParamUsername);
        // 登录成功
        if (result) {
            // access_token
            String tokenValue = getTokenProvider().getToken(username, TokenType.accessToken);
            // 每一次登录都要换一个新的token
            // 用户名密码模式不需要refresh_token
            Token token = getAuthTokenGenerator().generateAccessToken(false);
            if (tokenValue != null && !tokenValue.isEmpty()) {
                token.setValue(tokenValue);
            }
            getTokenProvider().saveToken(username, token);

            setLoginSuccessCookies(request, response, token, username);

            WebUtil.response(request, response, token, access_token_cookie_name, username_cookie_name, commonResponseDTO.getData());
        } else {
            try {
                WebUtil.replyNoAccess(request, response, commonResponseDTO.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.handlePasswordGrantType(request, response);
    }


}
