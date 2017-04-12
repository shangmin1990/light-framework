package net.shmin.core.export.config;

import java.util.List;

/**
 * Created by benjamin on 16/9/29.
 */
public class XlsxConfig {

    private String fileName;
    private String filePath;
    private String title;
    private List<XlsxHeader> headers;
    private SheetConfig[] sheets;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<XlsxHeader> getHeaders() {
        return headers;
    }

    public SheetConfig[] getSheets() {
        return sheets;
    }

    public void setSheets(SheetConfig[] sheets) {
        this.sheets = sheets;
    }

    public void setHeaders(List<XlsxHeader> headers) {
        this.headers = headers;
    }

    public void unCreate(List<String> sheetIds) {
        if (this.sheets != null && this.sheets.length > 0) {
            for (SheetConfig sheetConfig : sheets) {
                if (sheetIds.contains(sheetConfig.getId())) {
                    sheetConfig.setCreate(false);
                }
            }
        }
    }


}

