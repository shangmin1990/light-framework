package net.shmin.core.convertor.filter.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by benjamin on 2016/12/26.
 */
public class PortraitDataFilter extends PercentDataFilter {

    private static Logger logger = LoggerFactory.getLogger(PortraitDataFilter.class);

    public PortraitDataFilter() {
        super();
    }

    public PortraitDataFilter(int scale) {
        super(scale);
    }

    @Override
    public JSONArray filter(JSONArray array) {
        if (array == null || array.size() == 0) {
            return null;
        }
        long sum = getSum(array, "count");
        if (sum <= 0L) {
            logger.error("计算百分比时出错，原因：除数为0");
            return null;
        }
        BigDecimal sumDecimal = new BigDecimal(sum);
        for (int i = 0, length = array.size(); i < length; i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            BigDecimal count = new BigDecimal(jsonObject.getLong("count"));
            BigDecimal percent = count.divide(sumDecimal, scale, BigDecimal.ROUND_HALF_UP);
            jsonObject.put("percent", percent.multiply(b100));
            jsonObject.remove("count");
        }
        return array;
    }
}
