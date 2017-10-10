package net.shmin.auth.event;

import net.shmin.auth.AuthContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: benjamin
 * @Date: Create in  2017/9/29 下午3:41
 * @Description:
 */
public class LoginFailureEvent extends LoginEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public LoginFailureEvent(Object source,
                             AuthContext authContext,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             Object data) {
        super(source, authContext, request, response, data);
    }
}
