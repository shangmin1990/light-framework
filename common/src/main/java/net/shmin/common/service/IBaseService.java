package net.shmin.common.service;

import com.github.pagehelper.Page;
import net.shmin.core.dto.CommonPageDTO;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
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
    MODEL selectById(ID id);

    /**
     * 获取所有的数据
     *
     * @return 所有数据库中的数据
     */
    List<MODEL> selectAll();

    /**
     * 分页获取
     *
     * @param page 页码
     * @param size 每页条数
     * @return
     */
    Page<MODEL> selectAllPage(int page, int size);

    /**
     * 插入一条数据
     * null数据会保存
     *
     * @param model
     * @return true 插入成功, 插入失败
     */
    boolean insert(MODEL model);

    /**
     * 插入一条数据
     * null 数据不保存
     *
     * @param model
     * @return true 插入成功, 插入失败
     */
    boolean insertSelective(MODEL model);

    /**
     * 更新一条数据
     *
     * @param model 如果字段为null 则不更新
     * @return 更新成功或失败
     * @see #updateByPrimaryKeySelective
     */
    @Deprecated
    boolean updateSelective(MODEL model);

    /**
     * 更新一条数据(通过主键)
     *
     * @param model 如果字段为null 则不更新
     * @return 更新成功或失败
     */
    boolean updateByPrimaryKeySelective(MODEL model);

    /**
     * 更新一条数据
     *
     * @param model 如果字段为null也会更新
     * @return 更新成功或失败
     * @see #updateByPrimaryKey
     */
    @Deprecated
    boolean update(MODEL model);

    /**
     * 更新一条数据(通过主键)
     *
     * @param model 如果字段为null 则不更新
     * @return 更新成功或失败
     *
     */
    boolean updateByPrimaryKey(MODEL model);


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
     * 通过通用条件查询
     * @param example
     * @return
     */
    MODEL selectOneByCondition(Example example) throws Exception;

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

    /**
     * 查询条数
     * @param model
     * @return
     */
    long selectCount(MODEL model);

    /**
     * 根据条件查询条数
     * @param example
     * @return
     */
    long selectCountByCondition(Example example);

    /**
     * 根据条件查询并分页
     * @param example
     * @param page
     * @param size
     * @return
     */
    Page<MODEL> selectByConditionPage(Example example, int page, int size);

    /**
     * 批量插入
     *
     * @param models
     * @return 成功的行数
     */
    int insertBatch(List<MODEL> models);

    /**
     * 批量插入
     *
     * @param models
     * @return 成功的行数
     */
    int updateBatchByPrimaryKey(List<MODEL> models);

    /**
     * 批量插入
     *
     * @param models
     * @return 成功的行数
     */
    int updateBatchByPrimaryKeySelective(List<MODEL> models);

    /**
     * 批量插入
     *
     * @param ids
     * @return 成功的行数
     */
    int deleteBatchByPrimaryKey(List<ID> ids);
}
