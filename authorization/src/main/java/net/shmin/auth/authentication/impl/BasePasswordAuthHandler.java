package net.shmin.auth.authentication.impl;

import net.shmin.auth.event.LoginFailureEvent;
import net.shmin.auth.event.LoginSuccessEvent;
import net.shmin.auth.token.Token;
import net.shmin.auth.util.WebUtil;
import net.shmin.core.dto.CommonResponseDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: benjamin
 * @Date: Create in  2018/5/30 下午9:43
 * @Description:
 */
public class BasePasswordAuthHandler extends GrantTypeAuthorizationHandlerAdapter {

    protected void handlePasswordAuth(HttpServletRequest request, HttpServletResponse response) throws Exception{

        CommonResponseDTO commonResponseDTO = login(request);

        boolean result = commonResponseDTO.isSuccess();

        // 登录成功
        if (result) {

            // access_token
            // 每一次登录都要换一个新的token
            // 用户名密码模式不需要refresh_token
            Token token = getAuthTokenGenerator().generateAccessToken(false);

            getTokenProvider().saveToken(token);

            LoginSuccessEvent loginSuccessEvent = new LoginSuccessEvent(this, authContext, request, response, commonResponseDTO.getData(), token);

            loginListenerManager.fireEvent(loginSuccessEvent);

            setLoginSuccessCookies(request, response, token);

            WebUtil.response(request, response, token, access_token_cookie_name, commonResponseDTO.getData());
        } else {

            LoginFailureEvent loginFailureEvent = new LoginFailureEvent(this, authContext, request, response, commonResponseDTO.getData());

            loginListenerManager.fireEvent(loginFailureEvent);

            try {
                WebUtil.replyNoAccess(request, response, commonResponseDTO.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
