package net.shmin.auth.authentication;

import net.shmin.auth.token.GrantType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static net.shmin.core.Constant.GRANT_TYPE;

/**
 * @Author: benjamin
 * @Date: Create in  2018/5/31 上午10:30
 * @Description:
 */
@Component
public class AuthorizationHandlerFactory {

    @Resource(name = "passwordAuthHandler")
    private AuthorizationHandler password;

    @Resource(name = "authorizationCodeHandler")
    private AuthorizationHandler authorizationCode;

    @Resource(name = "noneGrantTypeAuthHandler")
    private AuthorizationHandler noneGrantTypeAuthHandler;

    public AuthorizationHandler getAuthorizationHandler(HttpServletRequest httpServletRequest) {
        String grant_type = httpServletRequest.getParameter(GRANT_TYPE);
        if (grant_type == null || grant_type.trim().isEmpty()){
            return noneGrantTypeAuthHandler;
        }
        if (grant_type.equals(GrantType.AUTHORIZATION_CODE.getName())){
            return authorizationCode;
        }

        if (grant_type.equals(GrantType.PASSWORD.getName())){
            return password;
        }
        return null;
    }
}
