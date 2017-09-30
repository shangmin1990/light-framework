package net.shmin.auth.event;

import net.shmin.auth.AuthContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.EventObject;

/**
 * @Author: benjamin
 * @Date: Create in  2017/9/29 下午3:44
 * @Description:
 */
public abstract class LoginEvent extends EventObject {

    protected AuthContext authContext;

    protected HttpServletRequest httpServletRequest;

    protected HttpServletResponse httpServletResponse;

    protected Object data;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */

    public LoginEvent(Object source,
                      AuthContext authContext,
                      HttpServletRequest request,
                      HttpServletResponse response,
                      Object data) {
        super(source);
        this.authContext = authContext;
        this.httpServletRequest = request;
        this.httpServletResponse = response;
        this.data = data;
    }

    public AuthContext getAuthContext() {
        return authContext;
    }

    public void setAuthContext(AuthContext authContext) {
        this.authContext = authContext;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
