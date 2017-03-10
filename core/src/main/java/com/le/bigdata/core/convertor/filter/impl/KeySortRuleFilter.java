package com.le.bigdata.core.convertor.filter.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.le.bigdata.core.convertor.filter.DataFilter;

/**
 * Created by benjamin on 2016/11/10.
 */
public class KeySortRuleFilter implements DataFilter {

    private final String[] keyRule;

    public KeySortRuleFilter(String[] keyRule) {
        this.keyRule = keyRule;
    }

    @Override
    public JSONArray filter(JSONArray array) {
        JSONArray jsonArray = sortJsonByRule(array, keyRule, null);
        return jsonArray;
    }

    /**
     * 按照rule对某个property的json排序
     *
     * @param data
     * @param jsonKeys
     * @param displayNames
     * @return
     */
    private JSONArray sortJsonByRule(JSONArray data, String[] jsonKeys, JSONArray displayNames) {
//        JSONArray jsonArray = jsonObject.getJSONArray(key);
//        if(jsonArray == null || jsonArray.size() == 0){
//            return null;
//        }
        if (jsonKeys == null || jsonKeys.length == 0) {
            return data;
        }
        JSONArray jsonArrayCopy = new JSONArray();
        for (int i = 0, length = jsonKeys.length; i < length; i++) {
            String ruleKeyItem = jsonKeys[i];
            String displayItem = null;
            if (displayNames != null && displayNames.size() == jsonKeys.length) {
                displayItem = displayNames.getString(i);
            }
            JSONObject jsonObject1 = findAndReplaceJsonKey(ruleKeyItem, data, displayItem);
            if (jsonObject1 != null)
                jsonArrayCopy.add(jsonObject1);
        }
        return jsonArrayCopy;
    }

    private JSONObject findAndReplaceJsonKey(String jsonKey, JSONArray array, String displayKey) {
        if (array == null || array.size() == 0) {
            return null;
        }
        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;
            if (jsonObject.get("key").equals(jsonKey)) {
                if (displayKey != null) {
                    // 如果需要替换显示名称,把显示名称替换
                    jsonObject.put("key", displayKey);
                }
                return jsonObject;
            }
        }
        // 没找到当前数据 手动构造一个数据 count设置为0
        JSONObject jsonObject = new JSONObject();
        if (displayKey != null) {
            // 如果需要替换显示名称,把显示名称替换
            jsonObject.put("key", displayKey);
        } else {
            jsonObject.put("key", jsonKey);
        }
        jsonObject.put("count", 0);
        return jsonObject;
    }
}
