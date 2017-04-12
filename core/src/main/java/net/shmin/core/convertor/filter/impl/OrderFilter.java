package net.shmin.core.convertor.filter.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.shmin.core.convertor.filter.DataFilter;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by benjamin on 16/10/11.
 * 非线程安全
 */
public class OrderFilter implements DataFilter {

    private final String orderString;

    public OrderFilter(String orderString) {
        this.orderString = orderString;
    }

    @Override
    public JSONArray filter(JSONArray array) {
        if (array == null || array.size() == 0 || orderString == null || orderString.isEmpty()) {
            return array;
        }
        final String[] orderArray = orderString.split(" ");
        if (orderArray.length == 2 && (orderArray[1].equals("asc") || orderArray[1].equals("desc"))) {
            final String filed = orderArray[0];
            Collections.sort(array, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    JSONObject jsonObject1 = (JSONObject) o1;
                    JSONObject jsonObject2 = (JSONObject) o2;
                    if (orderArray[1].equals("desc")) {
                        return (int) (jsonObject2.getLong(filed) - jsonObject1.getLong(filed));
                    } else if (orderArray[1].equals("asc")) {
                        return (int) (jsonObject1.getLong(filed) - jsonObject2.getLong(filed));
                    }
                    return 0;
                }
            });
        }
        return array;
    }
}
