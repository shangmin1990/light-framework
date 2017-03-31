package com.le.bigdata.common.dao.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mybatis 数据格式转换器 VACHAR 转 JSONObject
 *
 * @Author Benjamin
 * @Desc 将数据库中的画像数据格式(一级) a:b;c:d转换为JSONObject
 * @Date 2016-08-31
 */
@MappedTypes(JSONObject.class)
public class JSONTypeHandler implements TypeHandler<JSONObject> {

    private static final Logger logger = LoggerFactory.getLogger(JSONTypeHandler.class);

    /**
     *
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toJSONString());
    }

    @Override
    public JSONObject getResult(ResultSet rs, String columnName) throws SQLException {
//        long begin = System.nanoTime();
        String original = rs.getString(columnName);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(original);
        } catch (Exception e) {
            logger.error("JSON ParseError, Column:{}", columnName);
            return null;
        }
//        long end = System.nanoTime();
//        logger.info("转换json列{},花费时间:{}ms", columnName, TimeUnit.MICROSECONDS.convert(end - begin, TimeUnit.NANOSECONDS));
        return jsonObject;
    }

    @Override
    public JSONObject getResult(ResultSet rs, int columnIndex) throws SQLException {
        String original = rs.getString(columnIndex);
        try {
            return JSON.parseObject(original);
        } catch (Exception e) {
            logger.error("JSON ParseError, Column:{}", columnIndex);
            return null;
        }
    }

    @Override
    public JSONObject getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String original = cs.getString(columnIndex);
        try {
            return JSON.parseObject(original);
        } catch (Exception e) {
            logger.error("JSON ParseError, Column:{}", columnIndex);
            return null;
        }
    }

}
