package com.le.bigdata.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by benjamin on 2016/10/25.
 */
public class JedisPoolUtil {

    private static Logger logger = LoggerFactory.getLogger(JedisPoolUtil.class);

    private static JedisPool pool;

    static {
        String profile = null;
        if (SpringContextUtils.getApplicationContext() != null) {
            String[] utils = SpringContextUtils.getApplicationContext().getEnvironment().getDefaultProfiles();
            if (utils != null && utils.length > 0) {
                profile = utils[0];
            }
        }

        String filePath = "redis.properties";

        if (profile != null && !profile.isEmpty()) {
            filePath = "redis." + profile + ".properties";
        }

        InputStream inputStream = JedisPoolUtil.class.getClassLoader().getResourceAsStream(filePath);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Can't load properties file from path: " + filePath);
        }
        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();

        // 设置最大连接数
        config.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));

        // 设置最大阻塞时间，记住是毫秒数milliseconds
        config.setMaxWaitMillis(Long.parseLong(properties.getProperty("maxWait")));

        // 设置空间连接
        config.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));

        // 创建连接池
        pool = new JedisPool(config, properties.getProperty("host"),
                Integer.parseInt(properties.getProperty("port")),
                Integer.parseInt(properties.getProperty("timeout")),
                properties.getProperty("password"),
                Integer.parseInt(properties.getProperty("database", "0")));
    }


    private JedisPoolUtil() {

    }

    /**
     * 获取一个jedis 对象
     *
     * @return
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 归还一个连接 已废弃  直接使用jedis.close方法会自动归还
     *
     * @param cache
     */
//    public static void returnRes(Jedis cache) {
//        pool.close(cache);
//    }

}
