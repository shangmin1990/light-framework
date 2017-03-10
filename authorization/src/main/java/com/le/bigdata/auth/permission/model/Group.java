package com.le.bigdata.auth.permission.model;

import java.util.Set;

/**
 * Created by benjamin on 2017/1/4.
 * 用户组
 */
public class Group<T, R> {

    private T id;

    private Set<R> roles;

    private T parentId;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public Set<R> getRoles() {
        return roles;
    }

    public void setRoles(Set<R> roles) {
        this.roles = roles;
    }

    public T getParentId() {
        return parentId;
    }

    public void setParentId(T parentId) {
        this.parentId = parentId;
    }
}
