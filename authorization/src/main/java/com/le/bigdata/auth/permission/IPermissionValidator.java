package com.le.bigdata.auth.permission;

import com.le.bigdata.auth.permission.model.ACLEnum;

/**
 * Created by benjamin on 2017/1/4.
 */
public interface IPermissionValidator<U, R> {

    /**
     * @param userId      用户id
     * @param resourceIds 资源id
     * @return
     */
    boolean hasPermission(U userId, R[] resourceIds, ACLEnum[] needed) throws Exception;

}
