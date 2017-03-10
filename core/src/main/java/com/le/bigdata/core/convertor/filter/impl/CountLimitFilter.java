package com.le.bigdata.core.convertor.filter.impl;

import com.alibaba.fastjson.JSONArray;
import com.le.bigdata.core.convertor.filter.DataFilter;

/**
 * Created by benjamin on 16/10/11.
 */
public class CountLimitFilter implements DataFilter {

    private final int count;

    public CountLimitFilter(int count) {
        this.count = count;
    }

    @Override
    public JSONArray filter(JSONArray array) {
        if (array == null) {
            return null;
        }
        if (count > 0 && array.size() >= count) {
            JSONArray result = new JSONArray(count);
            for (int i = 0; i < count; i++) {
                result.add(array.get(i));
            }
            return result;
        }
        return array;
    }
}
