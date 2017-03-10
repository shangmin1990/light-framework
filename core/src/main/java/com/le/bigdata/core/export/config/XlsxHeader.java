package com.le.bigdata.core.export.config;

/**
 * Created by benjamin on 16/9/29.
 */
public class XlsxHeader {

    private String name;
    private String value;

    public XlsxHeader() {

    }

    public XlsxHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
