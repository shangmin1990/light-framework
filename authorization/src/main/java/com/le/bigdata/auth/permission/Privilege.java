package com.le.bigdata.auth.permission;

import com.le.bigdata.auth.permission.impl.LebiPermissionManagerImpl;
import com.le.bigdata.auth.permission.model.ACLEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by benjamin on 2017/1/4.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Privilege {

    /**
     * 要访问的资源id
     *
     * @return
     */
    String[] resourceId();

    /**
     * 执行检查权限检查的类名
     */
    Class<? extends IPermissionValidator> permissionValidator() default LebiPermissionManagerImpl.class;

    /**
     * 访问该资源应该具有什么权限
     */
    ACLEnum[] needed();

}
