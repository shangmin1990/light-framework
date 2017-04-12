package net.shmin.auth.authentication;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by benjamin on 9/3/14.
 */
public interface AuthorizationHandler {

    /**
     * OAuth2.0授权接口
     * 当用户将要授权的时候,调用此方法
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    void handleAuthorization(ServletRequest request, ServletResponse response) throws Exception;

}
