package net.shmin.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class JSONUtil {

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

    /**
     * @see this#readObjectFromFile
     * @param path
     * @param tClass
     * @param <T>
     * @return
     * @throws IOException
     */
    @Deprecated
    public static <T> T readFromFile(String path, Class<T> tClass) throws IOException {
        String jsonString = readStringFromFile(path);
        return JSON.parseObject(jsonString, tClass);
    }

    @Deprecated
    public static <T> T readObjectFromFile(String path, Class<T> tClass) throws IOException {
        String jsonString = readStringFromFile(path);
        return JSON.parseObject(jsonString, tClass);
    }

    public static <T> List<T> readArrayFromFile(String path, Class<T> tClass) throws IOException {
        String jsonString = readStringFromFile(path);
        JSONArray jsonArray = JSON.parseArray(jsonString);
        if(jsonArray != null && jsonArray.size() > 0){
            List<T> result = new ArrayList<>();
            for (int i =0,length = jsonArray.size(); i < length; i++){
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    T item = JSON.parseObject(jsonObject.toJSONString(), tClass);
                    result.add(item);
                } catch (Exception e){
                    return null;
                }
            }
            return result;
        }
        return null;
    }


    public static JSONObject readJSONObjectFromFile(String path) throws IOException {
        String jsonString = readStringFromFile(path);
        return JSON.parseObject(jsonString);
    }

    public static JSONArray readJSONArrayFromFile(String path) throws IOException {
        String jsonString = readStringFromFile(path);
        return JSON.parseArray(jsonString);
    }

}

