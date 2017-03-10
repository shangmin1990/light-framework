package com.le.bigdata.core.export;

import com.alibaba.fastjson.JSONObject;
import com.le.bigdata.core.export.config.XlsxConfig;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;


/**
 * Created by benjamin on 16/8/23.
 */
public interface ExcelExport {

    //void buildSheet(XSSFWorkbook xssfWorkbook, String sheetName, String headerTitle, Map<String, String> commonExcelData, List<Map<String, String>> data);

    /**
     * 构建xlsx
     *
     * @param xssfWorkbook
     * @param config
     * @param result       数据
     */
    void buildXlsx(XSSFWorkbook xssfWorkbook, XlsxConfig config, JSONObject result);

    XlsxConfig getXlsxConfig(String path) throws IOException;
}
