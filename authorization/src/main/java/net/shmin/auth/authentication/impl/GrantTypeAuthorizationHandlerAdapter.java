package net.shmin.auth.authentication.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by benjamin on 9/4/14.
 * 模板方法
 */

public class GrantTypeAuthorizationHandlerAdapter extends AbstractAuthorizationHandler {

    @Override
    protected void handleNoneGrantType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

    }

    @Override
    public void handlePasswordGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    @Override
    public void handleAuthCodeGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    @Override
    public void handleImplicitGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    @Override
    public void handleClientGrantType(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

}