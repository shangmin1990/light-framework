package com.le.bigdata.auth.permission.model;

import java.util.List;

/**
 * Created by benjamin on 2017/1/4.
 */
public class Role<T, D> {

    private T id;

    private List<D> users;

    public List<D> getUsers() {
        return users;
    }

    public void setUsers(List<D> uid) {
        this.users = uid;
    }
}
