package com.le.bigdata.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.le.bigdata.core.export.config.XlsxConfig;
import com.le.bigdata.core.export.config.XlsxHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class JSONUtil {

    private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：readValue(json,Student.class)
     * (2)转换为List:readValue(json,List.class).但是如果我们想把json转换为特定类型的List，比如List<Student>，就不能直接进行转换了。
     * 因为readValue(json,List.class)返回的其实是List<Map>类型，你不能指定readValue()的第二个参数是List<Student>.class，所以不能直接转换。
     * 我们可以把readValue()的第二个参数传递为Student[].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List。
     * (3)转换为Map：readValue(json,Map.class)
     * 我们使用泛型，得到的也是泛型
     *
     * @param content   要转换的JavaBean类型
     * @param valueType 原始json字符串数据
     * @return JavaBean对象
     */
    public static <T> T readValue(String content, Class<T> valueType) {
        return JSON.parseObject(content, valueType);
    }

    /**
     * 把JavaBean转换为json字符串
     * (1)普通对象转换：toJson(Student)
     * (2)List转换：toJson(List)
     * (3)Map转换:toJson(Map)
     * 我们发现不管什么类型，都可以直接传入这个方法
     *
     * @param object JavaBean对象
     * @return json字符串
     */
    public static String toJSON(Object object) {
        return JSON.toJSONString(object);
    }

    private static String readStringFromFile(String path) throws IOException {
        InputStream inputStream = JSONUtil.class.getResourceAsStream(path);
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            stringBuilder.append(str);
        }
        String jsonString = stringBuilder.toString();
        return jsonString;
    }

    public static <T> T readFromFile(String path, Class<T> tClass) throws IOException {
        String jsonString = readStringFromFile(path);
        return JSON.parseObject(jsonString, tClass);
    }

    public static JSONObject readJSONObjectFromFile(String path) throws IOException {
        String jsonString = readStringFromFile(path);
        return JSON.parseObject(jsonString);
    }

    public static JSONObject findJSONByValue(JSONArray array, String value, String key) {
        if (array == null || array.size() == 0) {
            return null;
        }
        for (int i = 0, length = array.size(); i < length; i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String value_ = jsonObject.getString(key);
            if (value.equals(value_)) {
                return jsonObject;
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
//        String json = "{\"driverClass\":\"org.apache.hive.jdbc.HiveDriver\",\"jdbcUrl\":\"jdbc:hive2://10.154.29.150:10000/default\",\"user\":\"cloud_big_data\",\"password\":\"\",\"acquireIncrement\":\"3\",\"acquireRetryAttempts\":\"10\",\"acquireRetryDelay\":\"1000\",\"autoCommitOnClose\":true,\"checkoutTimeout\":\"30000\",\"initialPoolSize\":\"3\",\"maxIdleTime\":\"0\",\"maxPoolSize\":\"15\",\"minPoolSize\":\"1\",\"namenodeIP\":\"\",\"namenodePort\":\"\"}";
//        ConnectionProperties properties = /*new ObjectMapper().readValue(json, HiveC3P0ConnectionProperties.class);*/JSONUtil.readValue(json, HiveC3P0ConnectionProperties.class);
//
//        System.out.println();
//
//        //json = "{service:\"xxxx\"}";
//        //JSONUtil.readValue(json, TaskSchdulerConfig.class);
        XlsxConfig config = readFromFile("/export/userPortrait.json", XlsxConfig.class);
        XlsxHeader xlsxHeader = config.getHeaders().get(0);

        System.out.println(xlsxHeader.getName());
//        System.out.println(JSON.toJSONString(config, true));
    }

    /**
     * 将 a:b;c:d;这种方法转换为JSONObject
     *
     * @param original
     * @return
     */
    public static JSONObject parseJSONObject(String original) {
        String str = "{" + original + "}";
        String jsonStr = str.replaceAll(";", ",");
        try {
            JSONObject result = JSON.parseObject(jsonStr);
            return result;
        } catch (Exception e) {
            //parse error 手动
            String[] outer = original.split(";");
            JSONObject jsonObject = new JSONObject();
            for (String s : outer) {
                String[] inner = s.split(":");
                if (inner.length == 2) {
                    try {
                        Long num = Long.parseLong(inner[1]);
                        jsonObject.put(inner[0], num);
                    } catch (NumberFormatException e1) {
                        logger.error("value {} can't covert to number", inner[1]);
                        jsonObject.put(inner[0], inner[1]);
                    }
                }
            }
            return jsonObject;
        }
    }

    public static JSONArray notNullArray(JSONArray... arrays) {
        for (JSONArray jsonArray : arrays) {
            if (jsonArray != null && jsonArray.size() > 0) {
                return jsonArray;
            }
        }
        return null;
    }
}

