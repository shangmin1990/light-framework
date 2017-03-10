package com.le.bigdata.core.cache;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.le.bigdata.core.Constant;
import com.le.bigdata.core.util.JedisPoolUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存所有全国数据到redis中
 */
public class JedisCacheJob extends QuartzJobBean implements Constant {

    private Logger logger = LoggerFactory.getLogger(JedisCacheJob.class);

    private static String GET_PORTRAIT_DATE_URL = "http://localhost:8080/m/userPortrait/v2/dt";

    private static String PORTRAIT_API = "http://localhost:8080/m/userPortrait/v2?";

    //private List<String> rules = new ArrayList<>();

    private List<String> initRules() {
        List<String> rules = new ArrayList<>();
        InputStream inputStream = JedisCacheJob.class.getResourceAsStream("/cacheRule/userPortrait.cache");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            String rule;
            while ((rule = bufferedReader.readLine()) != null) {
                rules.add(rule);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rules;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        String dtString = requestGetData(GET_PORTRAIT_DATE_URL);

        JSONArray result = JSONArray.parseArray(dtString);

        for (int i = 0, length = result.size(); i < length; i++) {
            Jedis jedis = JedisPoolUtil.getJedis();
            JSONObject item = result.getJSONObject(i);
            String key = "dt=" + item.getString("dt");
            key = CacheUtil.sortCacheKey(key);
            JSONObject keyObject = new JSONObject();
            keyObject.put("path", "/userPortrait/v2/");
            keyObject.put("queryString", key);
            if (jedis.exists(keyObject.toJSONString())) {
                jedis.close();
                continue;
            }
            String portraitDataStr = requestGetData(PORTRAIT_API + key);
            if (portraitDataStr != null && !portraitDataStr.isEmpty()) {
                jedis.set(keyObject.toJSONString(), portraitDataStr);
            }
            logger.info("缓存所有日期的画像数据成功~");
            jedis.close();
        }

        List<String> rules = initRules();
        if (rules != null && rules.size() > 0) {
            for (String rule : rules) {
                for (int i = 0, length = result.size(); i < length; i++) {
                    Jedis jedis = JedisPoolUtil.getJedis();
                    JSONObject item = result.getJSONObject(i);
                    String key = "dt=" + item.getString("dt") + "&" + rule;
                    key = CacheUtil.sortCacheKey(key);
                    JSONObject keyObject = new JSONObject();
                    keyObject.put("path", "/userPortrait/v2/");
                    keyObject.put("queryString", key);
                    if (jedis.exists(keyObject.toJSONString())) {
                        jedis.close();
                        continue;
                    }
                    String portraitDataStr = requestGetData(PORTRAIT_API + key);
                    if (portraitDataStr != null && !portraitDataStr.isEmpty()) {
                        jedis.set(keyObject.toJSONString(), portraitDataStr);
                    }
                    jedis.close();
                }
            }
            logger.info("缓存所有自定义条件的画像数据成功~");
        }
    }

    private String requestGetData(String request) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(request);
        // 设置cookie
        httpGet.setHeader("Cookie", "78bdfe11ce353909cb210160a76c330b=shangmin; " +
                "c8f90164e98c8b505750b43905022a83=Chao+Hao+%28%E9%83%9D%E8%B6%85%29; " +
                "b6b2627ee16d540bfee25a140f83f598=haochao%40le.com;");
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) throws JobExecutionException {
        new JedisCacheJob().executeInternal(null);
    }
}
