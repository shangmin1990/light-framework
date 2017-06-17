# common
通用功能模块(通用异常处理, 通用service层和dao层(dao层基于mybatis和mybatis-common-dao))
## maven
```xml
<dependency>
    <groupId>net.shmin</groupId>
    <artifactId>common</artifactId>
    <version>2.0.0</version>
</dependency>
```
## 通用Service
```java
public interface IExampleService extends IBaseService<Integer, ExampleModel> {
    void exampleServiceMethod();
}
```
```java
/**
 * 默认使用的是DataSources.SLOT0 数据源槽0 所指向的数据源
 * 一般情况下我们会在service层建立接口(方便业务扩展), 
 * service接口必须要是继承IBaseService
 */
@Service
@Transactional
public class ExampleServiceImpl extends BaseService<Integer, ExampleModel> implements IExampleService{
    @Override
    public void exampleServiceMethod(){
        // 自定义业务方法
    }
}
```

## 通用Service里的方法
```java
/**
 * Base Service类 提供公共方法
 * 
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
     * @param page
     * @param size
     * @return 
     */
    List<MODEL> selectAllPage(int page, int size);

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
     * 通过通用条件查询 结果唯一
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
}
```
## 通用异常处理
ControllerAspect会拦截service中的异常, 并将异常封装返回给客户端.
自定义业务异常要继承于BusinessServiceException, BusinessServiceException支持返回异常错误码.

## [Demo案例(light-framework-project)](https://github.com/shangmin1990/light-framework-project)