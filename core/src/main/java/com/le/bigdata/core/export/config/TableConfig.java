package com.le.bigdata.core.export.config;


import java.util.Map;

/**
 * Created by benjamin on 16/9/30.
 */
public class TableConfig {

    private String id;
    private String name;
    private String[] headers;
    private String[] keyMap;
    private String[] keyRules;
    private String dataReference;
    private String[] filters;
    private String dataProvider;
    private String[] excludeKeys;
    private Map<String, String> displayKeyMap;
    private String order;
    private int count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public String[] getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(String[] keyMap) {
        this.keyMap = keyMap;
    }

    public String[] getKeyRules() {
        return keyRules;
    }

    public void setKeyRules(String[] keyRules) {
        this.keyRules = keyRules;
    }

    public String getDataReference() {
        return dataReference;
    }

    public void setDataReference(String dataReference) {
        this.dataReference = dataReference;
    }

    public String[] getFilters() {
        return filters;
    }

    public void setFilters(String[] dataProvider) {
        this.filters = dataProvider;
    }

    public String getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(String dataProvider) {
        this.dataProvider = dataProvider;
    }

    public String[] getExcludeKeys() {
        return excludeKeys;
    }

    public void setExcludeKeys(String[] excludeKeys) {
        this.excludeKeys = excludeKeys;
    }

    public Map<String, String> getDisplayKeyMap() {
        return displayKeyMap;
    }

    public void setDisplayKeyMap(Map<String, String> displayKeyMap) {
        this.displayKeyMap = displayKeyMap;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
