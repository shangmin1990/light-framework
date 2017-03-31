package com.le.bigdata.auth.interceptor;

import com.le.bigdata.auth.permission.IPermissionValidator;
import com.le.bigdata.auth.permission.Privilege;
import com.le.bigdata.auth.permission.model.ACLEnum;
import com.le.bigdata.auth.util.WebUtil;
import com.le.bigdata.core.bean.BeanCreateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import static com.le.bigdata.core.Constant.USERNAME;

/**
 * Created by benjamin on 2017/1/4.
 */
public class PrivilegeInterceptor extends HandlerInterceptorAdapter {

    @Value("${cookie.username.name}")
    private String usernameCookieKey;

    @PostConstruct
    public void init(){
        if(usernameCookieKey == null
                || usernameCookieKey.isEmpty()
                || usernameCookieKey.equals("${cookie.username.name}")){
            usernameCookieKey = USERNAME;
        }
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = WebUtil.getCookieValue(request, usernameCookieKey);
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
