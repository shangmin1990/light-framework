package net.shmin.common.service;

import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Base Service类 提供公共方法
 * Created by benjamin on 2017/3/6.
 */
public interface IBaseService<ID extends Comparable<ID>, MODEL> {

    /**
     * 根据主键获取MODEL
     *
     * @param id
     * @return MODEL
     */
    MODEL findById(ID id);

    /**
     * 获取所有的数据
     *
     * @return 所有数据库中的数据
     */
    List<MODEL> selectAll();

    /**
     * 分页获取
     *
     * @param rowBounds
     * @return
     */
    List<MODEL> selectAllPage(RowBounds rowBounds);

    /**
     * 插入一条数据
     *
     * @param model
     * @return true 插入成功, 插入失败
     */
    boolean insert(MODEL model);

    /**
     * 更新一条数据
     *
     * @param model 如果字段为null 则不更新
     * @return 更新成功或失败
     */
    boolean update(MODEL model);

    /**
     * 批量插入
     *
     * @param models
     * @return 成功的行数
     */
    int insertBatch(List<MODEL> models);

    /**
     * 删除一条数据
     *
     * @param id 主键
     * @return true 删除成功, false删除失败
     */
    boolean deleteByPrimaryKey(ID id);

    /**
     * 通过通用条件查询
     * @param example
     * @return
     */
    List<MODEL> selectByCondition(Example example);

    /**
     * 通过通用条件删除
     * @param example
     * @return
     */
    boolean deleteByCondition(Example example);

    /**
     * 通过通用条件更新
     * @param record
     * @param example
     * @return
     */
    boolean updateByCondition(MODEL record, Example example);
}
