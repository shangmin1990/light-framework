package net.shmin.common.service;

import com.github.pagehelper.PageHelper;
import net.shmin.common.dao.BaseMapper;
import org.apache.ibatis.session.RowBounds;
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
    public MODEL findById(ID id) {
        return MAPPER.selectByPrimaryKey(id);
    }

    @Override
    public List<MODEL> selectAll() {
        return MAPPER.selectAll();
    }

    @Override
    public List<MODEL> selectAllPage(RowBounds rowBounds) {
        PageHelper.startPage(rowBounds.getOffset(), rowBounds.getLimit());
        List<MODEL> models = MAPPER.selectAll();
        return models;
    }

    @Override
    public boolean insert(MODEL model) {
        int count = MAPPER.insertSelective(model);
        return count == 1;
    }

    @Override
    public boolean update(MODEL model) {
        return MAPPER.updateByPrimaryKeySelective(model) == 1;
    }

    @Override
    public int insertBatch(List<MODEL> models) {
        return MAPPER.insertList(models);
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
}
