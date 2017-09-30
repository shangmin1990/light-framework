package net.shmin.common.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import net.shmin.common.dao.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * Created by benjamin on 16/3/17.
 * 父类方法必须使用Transaction注解
 * 不使用则不会生成代理
 */
@Transactional
@Service
public abstract class BaseService<ID extends Comparable<ID>, MODEL> implements IBaseService<ID, MODEL> {

    @Autowired
    protected BaseMapper<MODEL> MAPPER;

    @Override
    public MODEL selectById(ID id) {
        return MAPPER.selectByPrimaryKey(id);
    }

    @Override
    public List<MODEL> selectAll() {
        return MAPPER.selectAll();
    }

    @Override
    public Page<MODEL> selectAllPage(int page, int size) {
        PageHelper.startPage(page, size);
        List<MODEL> models = MAPPER.selectAll();
        Page<MODEL> pages = (Page<MODEL>) models;
        return pages;
    }

    @Override
    public boolean insertSelective(MODEL model) {
        return MAPPER.insertSelective(model) == 1;
    }

    @Override
    public boolean insert(MODEL model) {
        return MAPPER.insert(model) == 1;
    }


    /**
     * Use updateByPrimaryKey instead.
     * @see #updateByPrimaryKey(Object)
     */
    @Override
    @Deprecated
    public boolean update(MODEL model) {
        return MAPPER.updateByPrimaryKey(model) == 1;
    }

    /**
     * Use updateByPrimaryKeySelective instead.
     * @see #updateByPrimaryKeySelective(Object)
     */
    @Override
    @Deprecated
    public boolean updateSelective(MODEL model) {
        return MAPPER.updateByPrimaryKeySelective(model) == 1;
    }

    @Override
    public boolean deleteByPrimaryKey(ID id) {
        return MAPPER.deleteByPrimaryKey(id) == 1;
    }

    public List<MODEL> selectByCondition(Example example){
        return MAPPER.selectByExample(example);
    }

    public boolean deleteByCondition(Example example){
        return MAPPER.deleteByExample(example) > 0;
    }

    public boolean updateByCondition(MODEL record, Example example){
        return MAPPER.updateByExampleSelective(record, example) > 0;
    }

    @Override
    public MODEL selectOneByCondition(Example example) throws Exception {
        List<MODEL> models = MAPPER.selectByExample(example);
        if(models == null || models.size() == 0){
            return null;
        }
        if(models.size() > 1){
            throw new Exception("查询出了多条数据");
        }
        return models.get(0);
    }

    @Override
    public long selectCount(MODEL model) {
        return MAPPER.selectCount(model);
    }

    @Override
    public long selectCountByCondition(Example example) {
        return MAPPER.selectCountByExample(example);
    }

    @Override
    public Page<MODEL> selectByConditionPage(Example example, int page, int size) {
        PageHelper.startPage(page, size);
        List<MODEL> list = MAPPER.selectByExample(example);
        return (Page<MODEL>) list;
    }

    @Override
    public int insertBatch(List<MODEL> models) {
        return MAPPER.insertList(models);
    }

    @Override
    public int updateBatchByPrimaryKey(List<MODEL> models) {
        int count = 0;
        for (MODEL model: models){
            count += MAPPER.updateByPrimaryKey(model);
        }
        return count;
    }

    @Override
    public int deleteBatchByPrimaryKey(List<ID> ids) {
        int count = 0;
        for (ID id: ids){
            count += MAPPER.deleteByPrimaryKey(id);
        }
        return count;
    }

    @Override
    public boolean updateByPrimaryKeySelective(MODEL model) {
        return MAPPER.updateByPrimaryKeySelective(model)== 1;
    }

    @Override
    public boolean updateByPrimaryKey(MODEL model) {
        return MAPPER.updateByPrimaryKey(model) == 1;
    }

    @Override
    public int updateBatchByPrimaryKeySelective(List<MODEL> models) {
        int count = 0;
        for (MODEL model: models){
            count += MAPPER.updateByPrimaryKeySelective(model);
        }
        return count;
    }
}
