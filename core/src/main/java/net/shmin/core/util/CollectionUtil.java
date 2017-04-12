package net.shmin.core.util;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Created by benjamin on 16/8/31.
 */
public class CollectionUtil {

    private static Logger logger = LoggerFactory.getLogger(CollectionUtil.class);

    private CollectionUtil() {

    }

    /**
     * 将list内的数据合并(一级 例如 T 中有a property: [{a:5,c:2}, {a:6,b:4}] = {a:11, b:4, c:2} )
     *
     * @param list
     * @param notMergeFields
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T mergeList(List<T> list, String[] notMergeFields) throws IllegalAccessException, InstantiationException {

        if (list == null || list.size() == 0) {
            return null;
        }
        T obj = list.get(0);
        if (obj == null) {
            return null;
        }
        Class clazz = list.get(0).getClass();
        // 要新建一个对象,不能改变之前的对象,我们可能要从原来的对象里取值
        T result = (T) clazz.newInstance();
        //T result = list.get(0);
        // 获取class中的所有定义的属性(包含父类的)
        Field[] fields = JAVAReflectionUtil.getAllDeclaredFields(clazz);
        for (T item : list) {
            for (Field field : fields) {
                ReflectionUtils.makeAccessible(field);
                // 此字段是否需要merge
                boolean allowMerge = checkMergeField(notMergeFields, field);
                if (allowMerge) {
                    // 将此字段定义的value合并
                    mergeObject(result, field, field.get(item));
                }
            }

        }
        setNotMergeFields(result, notMergeFields, list.get(0));
        return result;
    }

    private static <T> void setNotMergeFields(T result, String[] notMergeFields, T value) {
        if (notMergeFields == null || notMergeFields.length == 0) {
            return;
        }
        List<String> list = Arrays.asList(notMergeFields);
        Class clazz = result.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (list.indexOf(field.getName()) >= 0) {
                ReflectionUtils.makeAccessible(field);
                Object value1 = ReflectionUtils.getField(field, value);
                ReflectionUtils.setField(field, result, value1);
            }
        }
    }

    /**
     * 将value 合并到result上
     *
     * @param result
     * @param field
     * @param value
     * @param <T>
     * @throws IllegalAccessException
     */
    private static <T> void mergeObject(T result, Field field, Object value) throws IllegalAccessException {
        Object originalValue = field.get(result);
        if (value == null) {
            return;
        }
        Class type = field.getType();
        if (type == Long.class) {
            // Long类型的数据直接累加
            Long originalLong = 0L;
            if (originalValue != null) {
                originalLong = (Long) originalValue;
            }
            Long num = (Long) value;
            originalLong += num;
            field.set(result, originalLong);
        } else if (type == Integer.class) {
            // Integer类型的数据直接累加
            Integer originalInteger = 0;
            if (originalValue != null) {
                originalInteger = (Integer) originalValue;
            }
            Integer num = (Integer) value;
            originalInteger += num;
            field.set(result, originalInteger);
        } else if (type == JSONObject.class) {
            // JSONObject类型的数据
            JSONObject dest = (JSONObject) originalValue;
            JSONObject original = (JSONObject) value;
            // 合并两个JSONObject
            JSONObject merged = PortraitJson.sumPortraitJson(dest, original);
            field.set(result, merged);
        }
        // 其他类型的数据 不处理
    }

    public static <T> JSONObject simpleMerge(List<T> list, String keyFieldName, String mergeFieldName) throws NoSuchFieldException, IllegalAccessException {
        if (list == null || list.size() == 0) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        Class clazz = list.get(0).getClass();
        Field keyField = JAVAReflectionUtil.getField(clazz, keyFieldName);
        Field mergedField = JAVAReflectionUtil.getField(clazz, mergeFieldName);
        ReflectionUtils.makeAccessible(keyField);
        ReflectionUtils.makeAccessible(mergedField);
        for (T obj : list) {
            Object key = keyField.get(obj);
            // key 必须是string类型
            Assert.isTrue(key instanceof String);
            String keyStr = (String) key;
            Object originalValue = mergedField.get(obj);
            if (jsonObject.containsKey(key)) {
                Long value = jsonObject.getLong(keyStr);
                if (originalValue instanceof Long
                        || originalValue instanceof Integer
                        || originalValue.getClass() == int.class
                        || originalValue.getClass() == long.class) {
                    Long sum = (Long) originalValue + value;
                    jsonObject.put(keyStr, sum);
                } else if (originalValue instanceof String) {
                    Long sum = Long.parseLong((String) originalValue) + value;
                    jsonObject.put(keyStr, sum);
                }
            } else {
                jsonObject.put(keyStr, originalValue);
            }
        }
        return jsonObject;
    }

    private static boolean checkMergeField(String[] notMergeFields, Field field) {
        if (notMergeFields == null || notMergeFields.length == 0) {
            return true;
        }
        String fieldName = field.getName();
        List<String> list = Arrays.asList(notMergeFields);
        return list.indexOf(fieldName) < 0;
    }

    public static String join(List<String> list, String s) {
        if (list == null || list.size() == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : list) {
            stringBuilder.append(string);
            stringBuilder.append(s);
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
    }
}
