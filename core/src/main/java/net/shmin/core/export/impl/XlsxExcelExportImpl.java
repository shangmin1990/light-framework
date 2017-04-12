package net.shmin.core.export.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.shmin.core.bean.BeanCreateFactory;
import net.shmin.core.convertor.filter.DataFilter;
import net.shmin.core.convertor.filter.impl.CountLimitFilter;
import net.shmin.core.convertor.filter.impl.ExcludeKeyFilter;
import net.shmin.core.convertor.filter.impl.KeySortRuleFilter;
import net.shmin.core.convertor.filter.impl.OrderFilter;
import net.shmin.core.export.ExcelExport;
import net.shmin.core.export.config.SheetConfig;
import net.shmin.core.export.config.TableConfig;
import net.shmin.core.export.config.XlsxConfig;
import net.shmin.core.export.config.XlsxHeader;
import net.shmin.core.export.provider.TableDataProvider;
import net.shmin.core.util.JSONUtil;
import net.shmin.core.util.PortraitJson;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by benjamin on 16/8/23.
 *
 * @Scope: prototype
 */
@Component
public class XlsxExcelExportImpl implements ExcelExport {

    /**
     * 创建通用头
     *
     * @param xlsxwb
     * @param sheetTitle
     * @param headerTitle
     * @param headers
     * @return
     */
    private XSSFSheet buildHeader(XSSFWorkbook xlsxwb, String sheetTitle, String headerTitle, List<XlsxHeader> headers) {
        Assert.notNull(xlsxwb);
        Assert.notNull(headers);
        XSSFSheet sheet = xlsxwb.createSheet(sheetTitle);
        // -----title-----
        XSSFRow row = sheet.createRow(0);
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 3);
        XSSFCell cell = row.createCell(0);
        XSSFCellStyle xssfCellStyle = xlsxwb.createCellStyle();
        xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = xlsxwb.createFont();
        font.setFontName("微软雅黑");
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        xssfCellStyle.setFont(font);
        cell.setCellStyle(xssfCellStyle);
        cell.setCellValue(headerTitle);
        sheet.addMergedRegion(cellRangeAddress);
        // -----header-----
        for (int i = 0, length = headers.size(); i < length; i++) {
            int rowNumber = i + 1;
            XlsxHeader header = headers.get(i);
            buildHeaderRow(rowNumber, 0, xlsxwb, sheet, header.getName(), header.getValue());
        }
        return sheet;
    }

    /**
     * 创建title以下 实际数据以上的header部分
     *
     * @param offsetY
     * @param offsetX
     * @param xlsxwb
     * @param sheet
     * @param values
     */
    private void buildHeaderRow(int offsetY, int offsetX, XSSFWorkbook xlsxwb, XSSFSheet sheet, String... values) {
        XSSFRow row = sheet.createRow(offsetY);
//        font.setItalic(true);
        for (int i = 0, length = values.length; i < length; i++) {
            XSSFCellStyle xssfCellStyle = xlsxwb.createCellStyle();
            xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
            xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            XSSFFont font = xlsxwb.createFont();
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short) 11);
            if (i == 0) {
                font.setBold(true);
            } else {
                font.setBold(false);
            }
            xssfCellStyle.setFont(font);
            offsetX += i;
            XSSFCell keyCell = row.createCell(offsetX);
            // 设置header 单元格的样式
            keyCell.setCellStyle(xssfCellStyle);
            keyCell.setCellValue(values[i]);

        }
    }

    /**
     * 创建一行数据行
     *
     * @param offsetX     创建此行的行数(基于左上角y轴偏移量)
     * @param offsetY     创建此行的行数(基于左上角x轴偏移量)
     * @param tableHeader 是否是table的表头 (表头的样式不一样, 字体加粗)
     * @param xlsxwb
     * @param sheet
     * @param values
     */
    private void buildRow(int offsetY, int offsetX, boolean tableHeader, XSSFWorkbook xlsxwb, XSSFSheet sheet, Object... values) {
        CellStyle cellStyle = xlsxwb.createCellStyle();
        // 设置所有的cell边框为细线
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);

        Font font = xlsxwb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("微软雅黑");
        if (tableHeader) {
            // tableHeader 加粗
            font.setBold(true);
        } else {
            font.setBold(false);
        }
        cellStyle.setFont(font);
        XSSFRow row = sheet.getRow(offsetY);
        if (row == null) {
            row = sheet.createRow(offsetY);
        }

        for (int i = 0, length = values.length; i < length; i++) {
            XSSFCell keyCell = row.createCell(offsetX);
            keyCell.setCellStyle(cellStyle);
            if (values[i] instanceof Long) {
                long value = (long) values[i];
                keyCell.setCellValue(value);
            } else if (values[i] instanceof String) {
                String value = (String) values[i];
                keyCell.setCellValue(value);
            } else if (values[i] instanceof Integer) {
                Integer value = (Integer) values[i];
                keyCell.setCellValue(value);
            } else if (values[i] instanceof Double) {
                Double value = (Double) values[i];
                keyCell.setCellValue(value);
            } else if (values[i] instanceof Float) {
                Float value = (Float) values[i];
                keyCell.setCellValue(value);
            }
            sheet.setColumnWidth(offsetX, 20 * 256);
            offsetX++;

        }

        // sheet.setColumnWidth(m, “列名”.getBytes().length*2*256);
        // 这个方法是计算字符串的长度，以便设置列宽，该方法在解决中文的问题上比较好，前面两种方法对中文不好好用。。。。
//        for(int i = offsetX; i < offsetX + values.length; i++){
//            // sheet.autoSizeColumn(1);
//            // sheet.autoSizeColumn(1, true);
//            // 这两种方式都是自适应列宽度，但是注意这个方法在后边的版本才提供，poi的版本不要太老。 注意：第一个方法在合并单元格的的单元格并不好使，必须用第二个方法。
//            sheet.autoSizeColumn((short) i, true); //调整宽度
//
//        }
    }

    @Override
    public void buildXlsx(XSSFWorkbook xssfWorkbook, XlsxConfig config, JSONObject result) {
        // flat json
        if (result != null || result.size() > 0) {
            Set<String> keySet = result.keySet();
            for (String key : keySet) {
                Object value = result.get(key);
                if (value instanceof JSONObject) {
                    JSONArray array = null;
                    try {
                        array = PortraitJson.flatJson((JSONObject) value);
                        result.put(key, array);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        SheetConfig[] sheetConfigs = config.getSheets();
        if (sheetConfigs == null || sheetConfigs.length == 0) {
            return;
        }
        // 多个sheet
        for (SheetConfig sheetConfig : sheetConfigs) {
            if (!sheetConfig.isCreate()) {
                continue;
            }
            List<XlsxHeader> sheet_headers = sheetConfig.getHeaders();
            List<XlsxHeader> common_headers = config.getHeaders();
            mergeHeaders(common_headers, sheet_headers);
            // 创建sheet 并设置通用头部
            XSSFSheet sheet = buildHeader(xssfWorkbook, sheetConfig.getTitle(), config.getTitle(), common_headers);
            // 构造table
            List<List<TableConfig>> tables_ = sheetConfig.getTables();

            for (int i = 0, length = tables_.size(); i < length; i++) {
                List<TableConfig> tables = tables_.get(i);

                int startRow = sheet.getLastRowNum() + 1 + 1;
                if (i > 0) {
                    startRow = sheet.getLastRowNum() + 1;
                }

                for (int j = 0, length1 = tables.size(); j < length1; j++) {
                    int offsetX = 0;
                    // 第一行的table 距离最上边多少行
                    Row row = sheet.getRow(startRow);
                    TableConfig table = tables.get(j);
                    String[] tableHeaders = table.getHeaders();

                    if (j == 0) {
                        // 新行 从最左边cell开始
                        offsetX = 0;
                    } else {
                        // 中间有个间隔
                        offsetX = row.getLastCellNum() + 1;
                    }

//                    if(table.getName() != null && !table.getName().isEmpty()){
//                        int tableNameRowNum = startRow;
//                        buildTableNameRow(tableNameRowNum, offsetX, table, xssfWorkbook, sheet);
//                    }

                    buildRow(startRow, offsetX, true, xssfWorkbook, sheet, tableHeaders);

                    JSONArray array;
                    try {
                        if (table.getDataReference() != null && !table.getDataReference().isEmpty()) {
                            // 使用dataReference构造数据
                            array = result.getJSONArray(table.getDataReference());
                        } else {
                            TableDataProvider provider = (TableDataProvider) BeanCreateFactory.getBean(table.getDataProvider(), true);
//                            Assert.notNull(provider);
                            array = provider.provideData(result, table);
                        }

                        // 首先使用脏数据过滤器 过滤脏数据
                        if (table.getExcludeKeys() != null && table.getExcludeKeys().length > 0) {
                            List<String> excludeKeyList = new ArrayList<>();
                            for (String key : table.getExcludeKeys()) {
                                excludeKeyList.add(key);
                            }
                            array = new ExcludeKeyFilter(excludeKeyList).filter(array);
                        }

                        // 使用排序filter
                        if (table.getOrder() != null && !table.getOrder().isEmpty()) {
                            // 先排序 例如: count asc 按照count字段升序 a desc 按照a字段降序排序
                            String[] orderString = table.getOrder().split(" ");
                            if (orderString.length == 2 && (orderString[1].equals("asc") || orderString[1].equals("desc"))) {
                                array = new OrderFilter(table.getOrder()).filter(array);
                            }
                        }

                        // 使用countFilter
                        if (table.getCount() > 0) {
                            array = new CountLimitFilter(table.getCount()).filter(array);
                        }

                        // 使用自定义过滤器过滤数据
                        String[] filters = table.getFilters();
                        if (filters != null && filters.length > 0) {
                            for (int h = 0, length5 = filters.length; h < length5; h++) {
                                DataFilter dataFilter = (DataFilter) BeanCreateFactory.getBean(filters[h], false);
                                if (dataFilter != null) {
                                    array = dataFilter.filter(array);
                                }
                            }
                        }

                        // keyRules=[1,2,3] 最终排序key的值 例如[{"key":3,value:2},{key:2, value: 5}, {key: 1, value: 10}]排序为[{key:1},{key:2},{key:3}]
                        if (table.getKeyRules() != null && table.getKeyRules().length > 0) {
                            array = new KeySortRuleFilter(table.getKeyRules()).filter(array);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }

                    // 真正的build数据
                    if (array != null && array.size() > 0) {
                        // 如果我们直接再最后一行的基础上累加,则通一行的第二列计算就会错误.
                        int startRow_ = startRow;
                        for (int k = 0, length3 = array.size(); k < length3; k++) {
                            ++startRow_;
                            JSONObject jsonObject = array.getJSONObject(k);
                            String[] keyMap = table.getKeyMap();
                            Object[] params = new Object[keyMap.length];
                            Map<String, String> displayKeyMap = table.getDisplayKeyMap();
                            for (int m = 0, length4 = keyMap.length; m < length4; m++) {
                                Object value = jsonObject.get(keyMap[m]);
                                if (displayKeyMap != null && displayKeyMap.size() > 0) {
                                    if (displayKeyMap.containsKey(value)) {
                                        value = displayKeyMap.get(value);
                                    }
                                }
                                params[m] = value;
                            }
                            buildRow(startRow_, offsetX, false, xssfWorkbook, sheet, params);
                        }
                    }
                }
            }
            // 当前sheet已经导出完毕，设置最外层边框的样式
            int size = config.getHeaders().size();
            // header的行数 + title + 一个行的间隔
            int beginRow = size + 2;
            int endRow = sheet.getLastRowNum();
            List<List<TableConfig>> tableConfigLists = sheetConfig.getTables();
            if (tableConfigLists == null || tableConfigLists.size() == 0) {
                return;
            }
            List<TableConfig> tableConfigList = tableConfigLists.get(0);
            int beginCol = 0;
            int endCol = 0;
            for (int i = 0, length = tableConfigList.size(); i < length; i++) {
                TableConfig tableConfig = tableConfigList.get(i);
                String[] headers = tableConfig.getHeaders();
                if (i == 0) {
                    endCol += headers.length;
                } else {
                    endCol += headers.length + 1;
                }
                drawBorder(beginRow, endRow, beginCol, endCol, xssfWorkbook, sheet);
                // 间隔1
                beginCol += headers.length + 1;
            }
        }
    }

    private void mergeHeaders(List<XlsxHeader> src_headers, List<XlsxHeader> sheet_headers) {
        if (src_headers == null || src_headers.size() == 0) {
            return;
        }
        if (sheet_headers == null || sheet_headers.size() == 0) {
            return;
        }

        for (XlsxHeader header : sheet_headers) {
            String name = header.getName();
            XlsxHeader xlsxHeader = new XlsxHeader();
            xlsxHeader.setName(name);
            int index = src_headers.indexOf(xlsxHeader);
            if (index >= 0) {
                XlsxHeader find = src_headers.get(index);
                find.setValue(header.getValue());
            } else {
                src_headers.add(xlsxHeader);
            }
        }
    }

    /**
     * 绘制外层的border
     *
     * @param beginRow
     * @param endRow
     * @param beginCol
     * @param endCol
     * @param xssfWorkbook
     * @param sheet
     */
    private void drawBorder(int beginRow, int endRow, int beginCol, int endCol, XSSFWorkbook xssfWorkbook, XSSFSheet sheet) {
        if (beginRow >= 0
                && endRow > 0
                && endRow > beginRow
                && beginCol >= 0
                && endCol > 0
                && endCol > beginCol) {


            for (int k = beginRow; k <= endRow; k++) {
                Row bRow = sheet.getRow(k);


                for (int i = beginCol; i < endCol; i++) {
                    Cell cell = bRow.getCell(i);
                    if (cell != null) {
//                        CellStyle cellStyle = cell.getCellStyle();
                        CellStyle cellStyle = xssfWorkbook.createCellStyle();
                        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                        CellStyle originalStyle = cell.getCellStyle();
                        // 如果新建cellStyle 之前的font样式失效了 重新设置一下。
                        if (originalStyle != null) {
                            short index = originalStyle.getFontIndex();
                            Font font = xssfWorkbook.getFontAt(index);
                            cellStyle.setFont(font);
                        }
                        if (k == beginRow) {
                            cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
                        }
                        if (k == endRow) {
                            cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
                        }
                        if (i == beginCol) {
                            cellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
                        }
                        if (i == endCol - 1) {
                            cellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
                        }
                        cell.setCellStyle(cellStyle);
                    }
                }
            }

        }
    }


    @Override
    public XlsxConfig getXlsxConfig(String path) throws IOException {
        return JSONUtil.readFromFile(path, XlsxConfig.class);
    }
}
