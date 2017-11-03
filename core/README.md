# core
核心功能模块(缓存处理,多数据源切换(sql), 文件上传)
## maven
```xml
<dependency>
    <groupId>net.shmin</groupId>
    <artifactId>core</artifactId>
    <version>2.2.3-RELEASE</version>
</dependency>
```
## 缓存模块
基于redis的缓存, 缓存controller层方法的返回结果. 对于幂等请求有效.
注意:请不要对非幂等的请求进行结果缓存.
此模块主要作用于瓶颈在service层的计算,而非数据库读取数据.如果瓶颈是再数据库读取数据(无论是query阶段还是fetch阶段)请使用其他数据库缓存.
代码示例
```java
@RestController
@RequestMapping
public class DemoCtrl {
   
    @RequestMapping("/test")
    // 单位 毫秒(默认一天)
    @ResultCache(expire = 60000)
    public CommonResponseDTO exampleServiceMethod(){
        //TODO 调用service层业务逻辑
        Object data = service.service();
        return CommonResponseDTO.success(data);
    }
}
```

## 多数据源切换
利用Spring的AbstractRoutingDataSource来实现此功能, 目的在于通过注解动态切换数据源.
用法示例:
```xml
   <!-- 配置多个数据源 -->
   <bean id="dataSource1" class="com.alibaba.druid.pool.DruidDataSource"   destroy-method="close">
        <property name="driverClassName" value="${db1.jdbc.mysql.driver.class}"/>
        <property name="url" value="${db1.jdbc.mysql.url}"/>
        <property name="username" value="${db1.jdbc.mysql.user}"/>
        <property name="password" value="${db1.jdbc.mysql.password}"/>

        <property name="maxActive" value="30" />
        <property name="initialSize" value="5" />
        <property name="maxWait" value="60000" />
        <property name="minIdle" value="5" />

        <property name="validationQuery" value="SELECT 'x' FROM DUAL" />
        <property name="testWhileIdle" value="true" />
        <property name="testOnBorrow" value="false" />
        <property name="testOnReturn" value="false" />
    </bean>
    
   <bean id="dataSource2" class="com.alibaba.druid.pool.DruidDataSource"   destroy-method="close">
        <property name="driverClassName" value="${db1.jdbc.mysql.driver.class}"/>
        <property name="url" value="${db1.jdbc.mysql.url}"/>
        <property name="username" value="${db1.jdbc.mysql.user}"/>
        <property name="password" value="${db1.jdbc.mysql.password}"/>
    
        <property name="maxActive" value="30" />
        <property name="initialSize" value="5" />
        <property name="maxWait" value="60000" />
        <property name="minIdle" value="5" />
    
        <property name="validationQuery" value="SELECT 'x' FROM DUAL" />
        <property name="testWhileIdle" value="true" />
        <property name="testOnBorrow" value="false" />
        <property name="testOnReturn" value="false" />
   </bean>
   
   <!-- 配置MultiDataSource -->
    <bean id="dataSource" class="net.shmin.core.datasource.MultiDataSource">
           <property name="targetDataSources">
               <!-- DataSources 是数据源槽 默认定义了SLOT0-SLOT15 16个数据源槽 -->
               <map key-type="net.shmin.core.datasource.DataSources">
                   <entry key="SLOT0" value-ref="dataSource1"/>
                   <entry key="SLOT1" value-ref="dataSource2"/>
               </map>
           </property>
     </bean>
```
配置完毕之后再service层使用@DataSource注解即可自动切换数据源(但同一方法内不能同时使用不同数据源, 如果是同一方法内需要使用不同数据源,
请将两个方法单独拆开编写,再调用,注意spring的事务传播)
注:不支持JTA事务.
```java
/**
 * 默认使用的是DataSources.SLOT0 数据源槽0 所指向的数据源
 */
@Service
@Transactional
@net.shmin.core.datasource.annotation.DataSource(net.shmin.core.datasource.DataSources.SLOT1)
public class DemoServiceImpl extends BaseService<Integer, DemoModel> implements IDemoService{
    
    // 事务的传播属性为什么是嵌套事务呢 原因事务只再同一个数据源内有效 如果切换了数据源则需要新建事务,并不能加入到当前事务中.
    @net.shmin.core.datasource.annotation.DataSource(net.shmin.core.datasource.DataSources.SLOT2)
    @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.NESTED)
    public void demoMethod1(){
        
    }
    // 事务的传播属性为什么是嵌套事务呢 原因事务只再同一个数据源内有效 如果切换了数据源则需要新建事务,并不能加入到当前事务中.
    @net.shmin.core.datasource.annotation.DataSource(net.shmin.core.datasource.DataSources.SLOT3)
    @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.NESTED)
    public void demoMethod2(){
            
    };
    
    public void demoMethod3(){
        demoMethod1();
        demoMethod2();
    }
}
```
## 文件上传
支持自定义文件上传实现.

## 通用工具类
提供了许多通用的工具类

## [Demo案例(light-framework-project)](https://github.com/shangmin1990/light-framework-project)