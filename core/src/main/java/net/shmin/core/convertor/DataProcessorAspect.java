package net.shmin.core.convertor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.shmin.core.bean.BeanCreateFactory;
import net.shmin.core.convertor.filter.DataFilter;
import net.shmin.core.convertor.filter.impl.CountLimitFilter;
import net.shmin.core.convertor.filter.impl.ExcludeKeyFilter;
import net.shmin.core.convertor.filter.impl.OrderFilter;
import net.shmin.core.convertor.filter.impl.PortraitDataFilter;
import net.shmin.core.convertor.provider.DataProvider;
import net.shmin.core.dto.CommonResponseDTO;
import net.shmin.core.util.JSONUtil;
import net.shmin.core.util.PortraitJson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @see
 */
@Aspect
@Order(2)
public class DataProcessorAspect {

    private static Logger logger = LoggerFactory.getLogger(DataProcessorAspect.class);

    private static final String PORTRAIT_GLOBAL = "_portrait_global_";

    private JSONObject initRules(String path) throws IOException {
        JSONObject jsonObject = JSONUtil.readJSONObjectFromFile(path);
        return jsonObject;
    }

    /**
     * 拦截controller层所有带有 DataProcessor 的方法
     */
    @Pointcut(value = "(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController))" +
            "&& @annotation(net.shmin.core.convertor.DataProcessor)")
    public void pointCut() {
    }

//    @Pointcut(value = "@annotation(com.le.bigdata.core.convertor.DataFilter)")
//    public void pointCut(){}

    @Around(value = "pointCut()")
    public Object keySorted(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Object result = joinPoint.proceed();
        if (methodSignature.getMethod().isAnnotationPresent(DataProcessor.class)) {
            DataProcessor dataProcessor = methodSignature.getMethod().getAnnotation(DataProcessor.class);
            String path = dataProcessor.value();
            //String rule = keySortedRule.rule();
            try {
                JSONObject object = initRules(path);
                Class<?> type = methodSignature.getMethod().getReturnType();
                Object a = sortedUseKeyList(result, object, type);

                return a;
            } catch (IOException e) {
                logger.error("Key sorted Error");
                logger.error("reason ", e);
            }
        }
        return result;
    }

    private Object sortedUseKeyList(Object result, JSONObject keyMap, Class<?> resultType) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (result == null) {
            return null;
        }

        if (keyMap == null || keyMap.size() == 0) {
            return result;
        }

        if (resultType.getName().equals(CommonResponseDTO.class.getName())) {
            JSONObject result1 = (JSONObject) ((CommonResponseDTO) result).getData();
            return dealWithJSONObject(result1, keyMap);
        } else if (resultType.getName().equals(JSONObject.class.getName())) {
            return dealWithJSONObject((JSONObject) result, keyMap);
        }
        return null;
    }

    private Object dealWithJSONObject(JSONObject jsonObject, JSONObject keyMap) {

        Set<String> keySet_ = keyMap.keySet();

        /**
         * 1.解决所有的provider 原始数据中未存在的字段(再原始数据被处理之前)
         */
        for (String str : keySet_) {
            JSONObject propertyConfig = keyMap.getJSONObject(str);
            if (propertyConfig.containsKey("provider")) {
                String clazzName = propertyConfig.getString("provider");
                try {
                    DataProvider dataProvider = (DataProvider) BeanCreateFactory.getBean(clazzName, true);
                    Object object = dataProvider.provideData(jsonObject);
                    jsonObject.put(str, object);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            } else {
                continue;
            }
        }

        /**
         * 2.转换为jsonarray的格式
         */
        Set<String> keySet1 = jsonObject.keySet();
        // 先把所有的字段都变成jsonArray的格式
        for (String key : keySet1) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                JSONArray array = null;
                try {
                    array = PortraitJson.flatJson((JSONObject) value);
                    jsonObject.put(key, array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 3.执行配置文件里的配置
         */
        for (String property : keySet_) {
//            String str = iterator.next();
            JSONObject propertyConfig = keyMap.getJSONObject(property);
            JSONArray originalArray = null;
            try {
                originalArray = jsonObject.getJSONArray(property);
            } catch (Exception e) {
                // 如果不是jsonArray的格式，我们不处理 直接跳过此次循环
                continue;
            }
            JSONArray jsonArray = originalArray;
            if (originalArray == null || originalArray.size() == 0) {
                continue;
            }
            // 使用自定义过滤器过滤原始数据(前置过滤器 原始数据)
            JSONArray dataFiltersClazz = propertyConfig.getJSONArray("pre-filters");
            jsonArray = filterData(dataFiltersClazz, jsonArray);

            // 首先使用脏数据过滤器 过滤脏数据
            JSONArray excludeKeys = getMergedExcludeKeys(keyMap, property);
            if (excludeKeys != null && excludeKeys.size() > 0) {
                // 先排序 例如: count asc 按照count字段升序 a desc 按照a字段降序排序
                //
                Set<String> excludeKeySet = new HashSet<>();
                for (int i = 0, length = excludeKeys.size(); i < length; i++) {
                    excludeKeySet.add(excludeKeys.getString(i));
                }
                jsonArray = new ExcludeKeyFilter(excludeKeySet).filter(jsonArray);
            }

            // 使用排序filter
            if (propertyConfig.getString("orderBy") != null && !propertyConfig.getString("orderBy").isEmpty()) {
                // 先排序 例如: count asc 按照count字段升序 a desc 按照a字段降序排序
                String[] orderString = propertyConfig.getString("orderBy").split(" ");
                if (orderString.length == 2 && (orderString[1].equals("asc") || orderString[1].equals("desc"))) {
                    jsonArray = new OrderFilter(propertyConfig.getString("orderBy")).filter(jsonArray);
                }
            }

            // 使用countFilter
            if (propertyConfig.getInteger("count") != null && propertyConfig.getInteger("count") > 0) {
                jsonArray = new CountLimitFilter(propertyConfig.getInteger("count")).filter(jsonArray);
            }

            // 再使用KeyRule 和 display  进行重排序
            JSONArray keyRule = propertyConfig.getJSONArray("keyRule");
            JSONArray displayNames = propertyConfig.getJSONArray("display");
            jsonArray = sortJsonByRule(jsonArray, keyRule, displayNames);

            // 使用自定义过滤器过滤原始数据 后置过滤器
            JSONArray postDataFiltersClazz = propertyConfig.getJSONArray("post-filters");
            jsonArray = filterData(postDataFiltersClazz, jsonArray);

            jsonObject.put(property, jsonArray);
        }

        /**
         * 执行global 过滤
         */
        if (keyMap != null && keyMap.containsKey(PORTRAIT_GLOBAL)) {
            JSONObject global_config = keyMap.getJSONObject(PORTRAIT_GLOBAL);

            // 数据加权 防止真实数据被获取(目前采用算百分比的方式 )
            if (global_config.getBoolean("showPercent")) {
                // 算百分比
                int scale = global_config.getInteger("scale");
                Set<String> originalKeySet = jsonObject.keySet();
                Iterator<String> originalKeySetIterator = originalKeySet.iterator();
                PortraitDataFilter dataFilter = new PortraitDataFilter();
                if (scale >= 0) {
                    dataFilter = new PortraitDataFilter(scale);
                }
                while (originalKeySetIterator.hasNext()) {
                    String key = originalKeySetIterator.next();

                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray(key);
//                        jsonArray = excludeKeyFilter.filter(jsonArray);
                        if (global_config.containsKey("skipProperties")) {
                            JSONArray skipProperties = global_config.getJSONArray("skipProperties");
                            if (skipProperties.contains(key)) {
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    if (object.containsKey("percent")) {
                                        continue;
                                    }
                                    object.put("percent", object.get("count"));
                                    object.remove("count");
                                }
                            } else {
                                // percentFilter 计算百分比
                                jsonArray = dataFilter.filter(jsonArray);
                            }
                        } else {
                            // percentFilter 计算百分比
                            jsonArray = dataFilter.filter(jsonArray);
                        }
//                        jsonArray = dataFilter.filter(jsonArray);
                        jsonObject.put(key, jsonArray);
                    } catch (Exception e) {
                        logger.error("key[ " + key + " ]不是一个JSONArray, 转型失败不做处理, 错误:" + e.getLocalizedMessage());
                        continue;
                    }
                }
            }
        }

        return jsonObject;
    }

    private JSONArray getMergedExcludeKeys(JSONObject keyMap, String property) {
        if (property == null || property.isEmpty() || !keyMap.containsKey(property)) {
            return null;
        }

        JSONArray excludeKeys = keyMap.getJSONObject(property).getJSONArray("excludeKeys");
        Set<String> set = new HashSet<>();
        if (excludeKeys != null && excludeKeys.size() >= 0) {
            for (int i = 0; i < excludeKeys.size(); i++) {
                String str = excludeKeys.getString(i);
                set.add(str);
            }
        }
        if (keyMap.containsKey(PORTRAIT_GLOBAL)) {
            JSONArray excludeKeys_global = keyMap.getJSONObject(PORTRAIT_GLOBAL).getJSONArray("excludeKeys");
            if (excludeKeys_global != null && excludeKeys_global.size() >= 0) {
                for (int i = 0; i < excludeKeys_global.size(); i++) {
                    String str = excludeKeys_global.getString(i);
                    set.add(str);
                }
            }
        }
        JSONArray result = new JSONArray();
        for (String str : set) {
            result.add(str);
        }
        return result;
    }

    private JSONArray filterData(JSONArray dataFiltersClazz, JSONArray jsonArray) {
        if (dataFiltersClazz != null && dataFiltersClazz.size() > 0) {
            for (int h = 0, length5 = dataFiltersClazz.size(); h < length5; h++) {
                String filterName = dataFiltersClazz.getString(h);
                DataFilter dataFilter = null;
                try {
                    dataFilter = (DataFilter) BeanCreateFactory.getBean(filterName, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                if (dataFilter != null) {
                    return dataFilter.filter(jsonArray);
                }
            }
        }
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
    private JSONArray sortJsonByRule(JSONArray data, JSONArray jsonKeys, JSONArray displayNames) {
//        JSONArray jsonArray = jsonObject.getJSONArray(key);
//        if(jsonArray == null || jsonArray.size() == 0){
//            return null;
//        }
        if (jsonKeys == null || jsonKeys.size() == 0) {
            return data;
        }
        JSONArray jsonArrayCopy = new JSONArray();
        for (int i = 0, length = jsonKeys.size(); i < length; i++) {
            String ruleKeyItem = jsonKeys.getString(i);
            String displayItem = null;
            if (displayNames != null && displayNames.size() == jsonKeys.size()) {
                displayItem = displayNames.getString(i);
            }
            JSONObject jsonObject1 = findAndReplaceJsonKey(ruleKeyItem, data, displayItem);
            if (jsonObject1 != null)
                jsonArrayCopy.add(jsonObject1);
        }


        return jsonArrayCopy;
    }

    private JSONObject findAndReplaceJsonKey(String jsonKey, JSONArray array, String displayKey) {
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
