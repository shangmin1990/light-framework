package com.le.bigdata.core.convertor.filter.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.le.bigdata.core.convertor.filter.DataFilter;

import java.math.BigDecimal;

/**
 * Created by benjamin on 16/10/2.
 */
public class PercentDataFilter implements DataFilter {

    protected final BigDecimal b100 = new BigDecimal(100);

    protected int scale = 3;

    public PercentDataFilter() {

    }

    public PercentDataFilter(int scale) {
        this.scale = scale;
    }

    @Override
    public JSONArray filter(JSONArray array) {
        if (array == null || array.size() == 0) {
            return null;
        }
        long sum = getSum(array, "count");
        BigDecimal sumDecimal = new BigDecimal(sum);
        for (int i = 0, length = array.size(); i < length; i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            BigDecimal count = new BigDecimal(jsonObject.getLong("count"));
            BigDecimal percent = count.divide(sumDecimal, scale, BigDecimal.ROUND_HALF_UP);
            jsonObject.put("percent", (percent.multiply(b100).setScale(0, BigDecimal.ROUND_HALF_UP)) + "%");
        }
        return array;
    }

    protected Long getSum(JSONArray array, String key) {
        Long sum = 0L;
        if (array != null && array.size() > 0) {
            for (int i = 0, length = array.size(); i < length; i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                sum += jsonObject.getLong(key);
            }
        }
        return sum;
    }
}
