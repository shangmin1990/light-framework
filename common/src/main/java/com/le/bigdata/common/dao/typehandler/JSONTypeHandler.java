package com.le.bigdata.common.dao.typehandler;

import com.alibaba.fastjson.JSONObject;
import com.le.bigdata.core.util.JSONUtil;
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
     * 因为我们从库中都是读操作,库中的数据由hive生成,所以此方法可以不用重写
     *
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public JSONObject getResult(ResultSet rs, String columnName) throws SQLException {
//        long begin = System.nanoTime();
        String original = rs.getString(columnName);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONUtil.parseJSONObject(original);
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
            return JSONUtil.parseJSONObject(original);
        } catch (Exception e) {
            logger.error("JSON ParseError, Column:{}", columnIndex);
            return null;
        }
    }

    @Override
    public JSONObject getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String original = cs.getString(columnIndex);
        try {
            return JSONUtil.parseJSONObject(original);
        } catch (Exception e) {
            logger.error("JSON ParseError, Column:{}", columnIndex);
            return null;
        }
    }


    public static void main(String[] args) throws Exception {
        String original = "公司/企业职员:48523;私营个体户:19773;自由职业者:19225;其他:18631;政府/事业单位工作人员:13041;在校学生:8797;中高级管理人员:6885;职员:5257;专业/技术人员:4822;学生:1757;工人:1598;临时/兼职工:1049;无工作/退休:921;无:439";
        JSONUtil.parseJSONObject(original);
    }
}
