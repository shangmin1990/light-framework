package com.le.bigdata.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by benjamin on 16/3/24.
 */
@Table(name = "menu")
public class MenuModel {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private String name;
    private Integer priority;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;
    private String userAdd;
    private String icon;
    @Column(name = "isLeaf")
    private Boolean leaf;
    private Byte height;
    private Integer parentId;

    private Boolean display = true;

    /**
     * 当前用户有没有权限看这个菜单 false 没有权限 true 有权限
     */
    @Transient
    private Boolean userPermission = false;
    /**
     * 显示M
     */
    @Transient
    private List<MenuModel> children;

    // 如果不加jsonIgnore 与 children属性形成循环引用
    @JsonIgnore
    @Transient
    private MenuModel parent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getUserAdd() {
        return userAdd;
    }

    public void setUserAdd(String userAdd) {
        this.userAdd = userAdd;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Byte getHeight() {
        return height;
    }

    public void setHeight(Byte height) {
        this.height = height;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<MenuModel> getChildren() {
        return children;
    }

    public void setChildren(List<MenuModel> children) {
        this.children = children;
    }


    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Boolean getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(Boolean userPermission) {
        this.userPermission = userPermission;
    }

    @JsonIgnore
    public MenuModel getParent() {
        return parent;
    }

    @JsonIgnore
    public void setParent(MenuModel parent) {
        this.parent = parent;
    }

}
