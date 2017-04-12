package net.shmin.core.export.provider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.shmin.core.export.config.TableConfig;

/**
 * 为table提供数据
 * 为什么需要provider:
 * 例如：从原始数据 C = A + B
 */
public interface TableDataProvider {

    /**
     * 一个excel可能有多个表, object为所有表的数据源, tableConfig 为当前表的一些信息,从所有数据源中 根据tableConfig 取出当前
     * 表适合的数据.
     *
     * @param object
     * @param tableConfig
     * @return
     */
    JSONArray provideData(JSONObject object, TableConfig tableConfig) throws Exception;

}
