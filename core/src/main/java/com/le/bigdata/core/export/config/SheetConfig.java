package com.le.bigdata.core.export.config;


import java.util.List;

/**
 * Created by benjamin on 16/9/29.
 */
public class SheetConfig {

    private String id;

    private String title;

    private List<XlsxHeader> headers;
    // 二维列表 第一维代表的是每一列的数据, 二维代表的是每一列的表格
    private List<List<TableConfig>> tables;

    // 是否创建此sheet 如果为false则此sheet将不会创建到excel文件中
    private boolean create = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public List<XlsxHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<XlsxHeader> headers) {
        this.headers = headers;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<List<TableConfig>> getTables() {
        return tables;
    }

    public void setTables(List<List<TableConfig>> tables) {
        this.tables = tables;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }
}
