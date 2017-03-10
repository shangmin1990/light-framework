package com.le.bigdata.common.dao;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by benjamin on 16/3/17.
 * base接口 不能实例化
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
