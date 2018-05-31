package net.shmin.auth.authentication.impl;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * //TODO 控制请求次数,防止暴力破解
 * 用户名密码模式，如果client信任可以使用此模式，例如当前client是服务端的一部分
 * Created by benjamin on 9/9/14.
 */
@Component("passwordAuthHandler")
public class PasswordAuthorizationHandler extends BasePasswordAuthHandler {

    @Override
    public void handlePasswordGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.handlePasswordAuth(request, response);
        super.handlePasswordGrantType(request, response);
    }

}
