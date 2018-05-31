package net.shmin.auth.authentication.impl;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: benjamin
 * @Date: Create in  2018/5/30 下午9:46
 * @Description:
 * 没有grantType 普通登录
 */
@Component("noneGrantTypeAuthHandler")
public class NoneGrantTypeAuthHandler extends BasePasswordAuthHandler{
    @Override
    public void handleNoneGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.handlePasswordAuth(request, response);
        super.handlePasswordGrantType(request, response);
    }
}
