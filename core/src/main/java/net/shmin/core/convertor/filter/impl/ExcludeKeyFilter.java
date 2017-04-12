package net.shmin.core.convertor.filter.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.shmin.core.convertor.filter.DataFilter;

import java.util.Collection;

/**
 * Created by benjamin on 2016/11/2.
 */
public class ExcludeKeyFilter implements DataFilter {

    private final Collection<String> excludeKeys;

    public ExcludeKeyFilter() {
        excludeKeys = null;
    }

    public ExcludeKeyFilter(Collection<String> excludeKeys) {
        this.excludeKeys = excludeKeys;
    }

    @Override
    public JSONArray filter(JSONArray array) {
        if (array == null || array.size() == 0) {
            return array;
        }
        if (excludeKeys != null && excludeKeys.size() > 0) {
            JSONArray result = new JSONArray();
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String key = jsonObject.getString("key");
                if (key != null) {
                    // 存在
                    if (!excludeKeys.contains(key)) {
                        result.add(jsonObject);
                    }
                }
            }
            return result;
        }
        return array;
    }
}
