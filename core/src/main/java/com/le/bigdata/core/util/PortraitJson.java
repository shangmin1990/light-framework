package com.le.bigdata.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.*;

/**
 * 处理画像json数据的工具类
 * Created by Ethan-Zhang on 16/6/15.
 *
 * @Author Ethan-Zhang Benjamin
 */
public class PortraitJson {

    /**
     * 将源字典的最底层的数目合并到目的字典
     *
     * @param dst_value 目的字典
     * @param src_value 源字典
     * @return 目的字典
     */
    public static final JSONObject sumPortraitJson1(JSONObject dst_value, JSONObject src_value) {

        if (src_value == null) {
            return dst_value;
        }

        if (dst_value == null) {
            return JSONObject.parseObject(src_value.toJSONString());
        }

        Stack<ArrayDeque<String>> stack = new Stack<>();

        for (Iterator<Map.Entry<String, Object>> it = src_value.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> node = it.next();
            String key = node.getKey();
            // Object类型就继续往下解析
            if (node.getValue() instanceof JSONObject) {
                ArrayDeque<String> tmpQueue = new ArrayDeque<>();
                tmpQueue.add(key);
                stack.push(tmpQueue);
                //integer类型做转换
            } else if (node.getValue() instanceof Integer) {
                if (dst_value.containsKey(key)) {
                    Integer tmp_num = dst_value.getInteger(key);
                    tmp_num = tmp_num + (Integer) node.getValue();
                    dst_value.put(key, tmp_num);
                } else {
                    dst_value.put(key, node.getValue());
                }
                //Long类型做转换
            } else if (node.getValue() instanceof Long) {
                if (dst_value.containsKey(key)) {
                    Long tmp_num = dst_value.getLong(key);
                    tmp_num = tmp_num + (Long) node.getValue();
                    dst_value.put(key, tmp_num);
                } else {
                    dst_value.put(key, node.getValue());
                }
            }
            //String类型做转换 转型为long 再累加
            else if (node.getValue() instanceof String) {
                if (dst_value.containsKey(key)) {
                    Long tmp_num = dst_value.getLong(key);
                    tmp_num = tmp_num + Long.parseLong((String) node.getValue());
                    dst_value.put(key, tmp_num);
                } else {
                    dst_value.put(key, Long.parseLong((String) node.getValue()));
                }

            } else {
                //TODO: 非json是否需要错误日志
            }

        }

        while (!stack.isEmpty()) {

            ArrayDeque<String> key_path = stack.pop();
            JSONObject srcTmpVal = (JSONObject) getDeepValue(src_value, key_path.clone());
            JSONObject dstTmpVal = (JSONObject) getDeepValue(dst_value, key_path.clone());

            if (dstTmpVal == null) {
                String key = key_path.poll();
                JSONObject parentDstTmpVal = (JSONObject) getDeepValue(dst_value, key_path);
                parentDstTmpVal.put(key, srcTmpVal);
                continue;
            }

            for (Iterator<Map.Entry<String, Object>> it = srcTmpVal.entrySet().iterator(); it.hasNext(); ) {

                Map.Entry<String, Object> node = it.next();
                String key = node.getKey();

                if (node.getValue() instanceof JSONObject) {
                    ArrayDeque<String> tmpQueue = new ArrayDeque<>(key_path);
                    tmpQueue.add(key);
                    stack.push(tmpQueue);
                } else if (node.getValue() instanceof Integer) {
                    if (dstTmpVal.containsKey(key)) {
                        Integer tmp_num = dstTmpVal.getInteger(key);
                        tmp_num = tmp_num + (Integer) node.getValue();
                        dstTmpVal.put(key, tmp_num);
                    } else {
                        dstTmpVal.put(key, node.getValue());
                    }

                } else if (node.getValue() instanceof String) {
                    if (dstTmpVal.containsKey(key)) {
                        Integer tmp_num = dstTmpVal.getInteger(key);
                        tmp_num = tmp_num + Integer.parseInt((String) node.getValue());
                        dstTmpVal.put(key, tmp_num);
                    } else {
                        dstTmpVal.put(key, Integer.parseInt((String) node.getValue()));
                    }

                } else if (node.getValue() instanceof Long) {
                    if (dst_value.containsKey(key)) {
                        Long tmp_num = dst_value.getLong(key);
                        tmp_num = tmp_num + (Long) node.getValue();
                        dst_value.put(key, tmp_num);
                    } else {
                        dst_value.put(key, node.getValue());
                    }
                } else {
                    //TODO:非json是否需要错误日志
                }

            }
        }
        return dst_value;
    }

    public static final Object getDeepValue(JSONObject json_obj, Queue<String> key_path) {

        while (!key_path.isEmpty()) {
            String key = key_path.poll();
            Object value = json_obj.get(key);
            if (!key_path.isEmpty() && !(value instanceof JSONObject)) {
                return null;
            }
            json_obj = (JSONObject) value;
        }

        return json_obj;
    }


    // 两个json合并
    public static JSONObject sumPortraitJson(JSONObject dest, JSONObject src) {
        if (dest == null) {
            return src;
        }
        if (src == null || src.keySet().size() == 0) {
            return dest;
        }

        Set<Map.Entry<String, Object>> entrySet = dest.entrySet();

        for (Map.Entry<String, Object> entry : entrySet) {
            //
            String key = entry.getKey();
            if (entry.getValue() instanceof JSONObject) {
                JSONObject src1 = src.getJSONObject(key);
                JSONObject dest1 = (JSONObject) entry.getValue();
                JSONObject result = sumPortraitJson(dest1, src1);
                entry.setValue(result);
            } else if (entry.getValue() instanceof Integer) {
                if (src.containsKey(key)) {
                    Integer tmp_num = src.getInteger(key);
                    tmp_num = tmp_num + (Integer) entry.getValue();
                    dest.put(key, tmp_num);
                } else {
                    dest.put(key, entry.getValue());
                }

            } else if (entry.getValue() instanceof String) {
                if (src.containsKey(key)) {
                    Long tmp_num = src.getLong(key);
                    tmp_num = tmp_num + Long.parseLong((String) entry.getValue());
                    dest.put(key, tmp_num);
                } else {
                    dest.put(key, Long.parseLong((String) entry.getValue()));
                }

            } else if (entry.getValue() instanceof Long) {
                if (src.containsKey(key)) {
                    Long tmp_num = src.getLong(key);
                    tmp_num = tmp_num + (Long) entry.getValue();
                    dest.put(key, tmp_num);
                } else {
                    dest.put(key, entry.getValue());
                }
            }
        }

        // 为什么要遍历 srcEntrySet?
        // src 中可能有 dest中没有的key 要把这一部分key加到dest里
        checkSrcKey(dest, src);
//        return dest;
        return dest;
    }

    private static void checkSrcKey(JSONObject dest, JSONObject src) {
        Set<Map.Entry<String, Object>> srcEntrySet = src.entrySet();
        for (Map.Entry<String, Object> entry : srcEntrySet) {
            //
            String key = entry.getKey();
            if (entry.getValue() instanceof JSONObject) {
                if (!dest.containsKey(key)) {
                    dest.put(key, entry.getValue());
                } else {
                    JSONObject dest1 = dest.getJSONObject(key);
                    JSONObject src1 = (JSONObject) entry.getValue();
                    checkSrcKey(dest1, src1);
                }
            } else {
                if (!dest.containsKey(key)) {
                    dest.put(key, entry.getValue());
                }
            }
        }
    }

    /**
     * 递归的将in_obj中细分项的值累加出一个汇总,并将dict打平为{"key":"xxx", "count": xxx}的形式
     *
     * @param out_array
     * @param in_obj
     * @return 累加和
     */
    public static final Long flatSumJson(JSONArray out_array, JSONObject in_obj) {

        Long total = 0L;

        for (Iterator<Map.Entry<String, Object>> it = in_obj.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> node = it.next();
            String key = node.getKey();
            Object val = node.getValue();
            JSONArray out_tmp_array = new JSONArray();

            if (val instanceof JSONObject) {
                Long sum = flatSumJson(out_tmp_array, (JSONObject) val);
                JSONObject flat_json = new JSONObject();
                flat_json.put("key", key);
                flat_json.put("value_json", out_tmp_array);
                flat_json.put("total", sum);
                out_array.add(flat_json);
                total += sum;
            } else if (val instanceof Integer) {
                JSONObject flat_json = new JSONObject();
                flat_json.put("key", key);
                flat_json.put("count", val);
                out_array.add(flat_json);
                total += (Integer) val;
            } else if (val instanceof Long) {
                JSONObject flat_json = new JSONObject();
                flat_json.put("key", key);
                flat_json.put("count", val);
                out_array.add(flat_json);
                total += (Long) val;
            } else if (val instanceof String) {
                JSONObject flat_json = new JSONObject();
                flat_json.put("key", key);
                flat_json.put("count", val);
                out_array.add(flat_json);
                try {
                    total += Integer.valueOf((String) val);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }


        return total;
    }

    public static JSONArray flatJson(JSONObject in_obj) throws Exception {
        JSONArray outArray = new JSONArray();
        for (Iterator<Map.Entry<String, Object>> it = in_obj.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> node = it.next();
            String key = node.getKey();
            Object val = node.getValue();
            JSONObject flat_json = new JSONObject();
            flat_json.put("key", key);
            flat_json.put("count", val);
            outArray.add(flat_json);
        }
        return outArray;
    }

    public static void main(String args[]) {
//        String tmp_1 = "{\"nanren\":{\"明星\":{\"杨幂\":100, \"黄晓明\":150}, \"大卡\":{\"谢娜\":20, \"何炅\":30}}, \"nuren\":{\"明星\":{\"杨幂\":100, \"黄晓明\":150}, \"大卡\":{\"谢娜\":20, \"何炅\":30}}}";
        JSONObject obj = new JSONObject();
        JSONObject obj2 = new JSONObject();
        obj.put("a", "1");
        obj.put("c", "3");
        obj2.put("age", obj);
//        JSONObject obj = JSON.parseObject(tmp_1);
        JSONArray result = new JSONArray();
        flatSumJson(result, obj2);
        System.out.println(result);
        return;
    }

    public static List<Map<String, BigDecimal>> productRatio(List<Map<String, BigDecimal>> list) {
        BigDecimal sum = new BigDecimal(0);
        for (Map<String, BigDecimal> item : list) {
            sum = sum.add(item.get("user_count"));
        }
        for (Map<String, BigDecimal> item : list) {
            BigDecimal bigDecimal1 = item.get("user_count");
            item.put("percent", bigDecimal1.divide(sum, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            item.remove("user_count");
        }
        return list;
    }

    public static long sum(JSONObject user_type) {
        if (user_type == null || user_type.size() == 0) {
            return 0L;
        }
        long sum = 0L;
        Set<String> keySet = user_type.keySet();
        for (String key : keySet) {
            long value = user_type.getLong(key);
            sum += value;
        }
        return sum;
    }

    public static long sum(JSONArray array) {
        if (array == null || array.size() == 0) {
            return 0L;
        }
        long sum = 0L;
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            sum += jsonObject.getLong("count");
        }
        return sum;
    }

    public static JSONObject parseObject(JSONArray array) {
        if (array == null || array.size() == 0) {
            return null;
        }
        JSONObject result = new JSONObject();
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            result.put(jsonObject.getString("key"), jsonObject.get("count"));
        }
        return result;
    }
}
