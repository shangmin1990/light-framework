package net.shmin.auth.event;

import net.shmin.auth.AuthContext;
import net.shmin.auth.token.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: benjamin
 * @Date: Create in  2017/9/29 下午3:40
 * @Description:
 */
public class LoginSuccessEvent extends LoginEvent {

    private Token token;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public LoginSuccessEvent(Object source,
                             AuthContext authContext,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             Object data,
                             Token token) {
        super(source, authContext, request, response, data);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
