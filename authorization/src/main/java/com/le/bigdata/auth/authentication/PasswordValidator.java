package com.le.bigdata.auth.authentication;


import javax.servlet.http.HttpServletRequest;

/**
 * Created by benjamin on 9/10/14.
 */
public interface PasswordValidator {
    /**
     * 用户名密码登录 需要用户自己实现
     *
     * @param request
     * @return
     */
    boolean login(HttpServletRequest request) throws Exception;
}
