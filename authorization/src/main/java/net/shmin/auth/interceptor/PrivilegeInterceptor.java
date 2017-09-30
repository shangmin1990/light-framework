package net.shmin.auth.interceptor;

import net.shmin.auth.AuthContext;
import net.shmin.auth.permission.IPermissionValidator;
import net.shmin.auth.permission.Privilege;
import net.shmin.auth.permission.model.ACLEnum;
import net.shmin.auth.util.WebUtil;
import net.shmin.core.bean.BeanCreateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Created by benjamin on 2017/1/4.
 */
public class PrivilegeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AuthContext authContext;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = WebUtil.getCookieValue(request, authContext.getUsernameCookieName());
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Privilege privilege = getAnnotation(handlerMethod);
            if (privilege == null) {
                return true;
            }
            String[] resourceIds = privilege.resourceId();
            ACLEnum[] needed = privilege.needed();

            Class<? extends IPermissionValidator> clazz = privilege.permissionValidator();

            IPermissionValidator permissionManager = BeanCreateFactory.getBean(clazz, true);

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
