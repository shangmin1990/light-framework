package net.shmin.auth.authentication.impl;

import net.shmin.auth.event.LoginFailureEvent;
import net.shmin.auth.event.LoginSuccessEvent;
import net.shmin.auth.token.Token;
import net.shmin.auth.util.WebUtil;
import net.shmin.core.dto.CommonResponseDTO;
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
            // 每一次登录都要换一个新的token
            // 用户名密码模式不需要refresh_token
            Token token = getAuthTokenGenerator().generateAccessToken(false);

            getTokenProvider().saveToken(token);

            LoginSuccessEvent loginSuccessEvent = new LoginSuccessEvent(this, authContext, request, response, commonResponseDTO.getData(), token);
            loginListenerManager.fireEvent(loginSuccessEvent);

            setLoginSuccessCookies(request, response, token, username);

            WebUtil.response(request, response, token, access_token_cookie_name, username_cookie_name, commonResponseDTO.getData());
        } else {
            LoginFailureEvent loginFailureEvent = new LoginFailureEvent(this, authContext, request, response, commonResponseDTO.getData());
            loginListenerManager.fireEvent(loginFailureEvent);
            try {
                WebUtil.replyNoAccess(request, response, commonResponseDTO.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.handlePasswordGrantType(request, response);
    }


}
