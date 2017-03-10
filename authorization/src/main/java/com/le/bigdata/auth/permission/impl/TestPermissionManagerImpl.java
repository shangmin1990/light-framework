package com.le.bigdata.auth.permission.impl;

import com.le.bigdata.auth.permission.IPermissionValidator;
import com.le.bigdata.auth.permission.model.ACLEnum;

/**
 * Created by benjamin on 2017/1/4.
 */
public class TestPermissionManagerImpl implements IPermissionValidator<String, String> {
    @Override
    public boolean hasPermission(String userId, String[] resourceIds, ACLEnum[] needed) {
        if (userId.equals("liuhongliang1")) {
            return true;
        }
        return false;
    }
}
