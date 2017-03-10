package com.le.bigdata.auth.interceptor;

import com.le.bigdata.auth.permission.IPermissionValidator;
import com.le.bigdata.auth.permission.Privilege;
import com.le.bigdata.auth.permission.model.ACLEnum;
import com.le.bigdata.auth.util.WebUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import static com.le.bigdata.core.Constant.USERNAME;

/**
 * Created by benjamin on 2017/1/4.
 */
public class PrivilegeInterceptor extends HandlerInterceptorAdapter {

//    private String accessTokenCookieKey = ACCESS_TOKEN;

    private String usernameCookieKey = USERNAME;

    private ConcurrentHashMap<String, IPermissionValidator> cache = new ConcurrentHashMap<>();

//    public String getAccessTokenCookieKey() {
//        return accessTokenCookieKey;
//    }
//
//    public void setAccessTokenCookieKey(String accessTokenCookieKey) {
//        this.accessTokenCookieKey = accessTokenCookieKey;
//    }

    public String getUsernameCookieKey() {
        return usernameCookieKey;
    }

    public void setUsernameCookieKey(String usernameCookieKey) {
        this.usernameCookieKey = usernameCookieKey;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String cookieTokenValue = WebUtil.getCookieValue(request, accessTokenCookieKey);
        String username = WebUtil.getCookieValue(request, usernameCookieKey);
//        Token token = new Token();
//        token.setValue(cookieTokenValue);
//        String username = token.getKey();
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Privilege privilege = getAnnotation(handlerMethod);
            if (privilege == null) {
                return true;
            }
            String[] resourceIds = privilege.resourceId();
            ACLEnum[] needed = privilege.needed();

            Class<? extends IPermissionValidator> clazz = privilege.permissionValidator();
            String name = clazz.getName();

            if (!cache.containsKey(name)) {
                IPermissionValidator permissionManager = clazz.newInstance();
                cache.putIfAbsent(name, permissionManager);
            }
            IPermissionValidator permissionManager = cache.get(name);
            boolean result = permissionManager.hasPermission(username, resourceIds, needed);
            if (!result) {
                WebUtil.replyNoAccess(request, response, username + "没有访问" + Arrays.toString(resourceIds) + "资源的权限");
            }
            return result;
        }
        return true;
    }

    private Privilege getAnnotation(HandlerMethod handlerMethod) {
        Privilege privilege = handlerMethod.getMethodAnnotation(Privilege.class);
        if (privilege != null) {
            return privilege;
        }
        return getAnnotationInternal(handlerMethod.getBeanType());
    }

    private Privilege getAnnotationInternal(Class<?> beanType) {
        if (beanType == null)
            return null;
        Privilege privilege = beanType.getAnnotation(Privilege.class);
        if (privilege != null) {
            return privilege;
        } else {
            return getAnnotationInternal(beanType.getSuperclass());
        }
    }
}
