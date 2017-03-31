package com.le.bigdata.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

/**
 * Created by benjamin on 2016/10/25.
 */
public class JedisPoolUtil extends JedisPoolConfig {

    private static Logger logger = LoggerFactory.getLogger(JedisPoolUtil.class);

    private JedisPool pool;

    private String host;

    private int port;

    private int timeout;

    private String password;

    private int database = 0;

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        JedisPoolUtil.logger = logger;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    @PostConstruct
    public void init(){
        pool = new JedisPool(this, host, port, timeout, password, database);
    }

    /**
     * 获取一个jedis 对象
     *
     * @return
     */
    public Jedis getJedis() {
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
