package net.shmin.core.datasource;

import net.shmin.core.datasource.annotation.DataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据源切面,如果采用统一的方式来管理必须能够做到自动切换数据源.
 * 数据库路由切换AOP,切面横切带有@Transaction 和 Datasource包下的方法执行,动态的切换数据源
 * 需要指定order在@Transactional注解代理前生成代理 否则spring事务可能失效!!!
 *
 * @see Order
 * @see DataSource
 * @see org.springframework.transaction.annotation.Transactional
 */
@Component
@Aspect
@Order(0)
public class DataSourceServiceAspect {

    // Service 带有
//    @Pointcut("(@within(org.springframework.transaction.annotation.Transactional) " +
//            "|| @annotation(org.springframework.transaction.annotation.Transactional) ) " +
//            "&& (@annotation(DataSource) " +
//            "|| @within(DataSource))")
//    @Pointcut("@annotation(DataSource) || @within(DataSource)")
    // 如果使用以上两种拦截表达式则无法拦截继承与BaseService的方法执行
    @Pointcut("@within(org.springframework.transaction.annotation.Transactional) " +
            "|| @annotation(org.springframework.transaction.annotation.Transactional)")
    public void dataSourcePointcut() {
    }

    @Before("dataSourcePointcut()")
    public void before(JoinPoint jp) {
        Class clazz = jp.getTarget().getClass();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        DataSource dataSource = null;
        DataSource clazzAnnotation = (DataSource) clazz.getAnnotation(DataSource.class);
        DataSource methodAnnotation = method.getAnnotation(DataSource.class);

        if (clazzAnnotation != null) {
            dataSource = clazzAnnotation;
        }

        if (methodAnnotation != null) {
            dataSource = methodAnnotation;
        }

        if (dataSource == null) {
            DataSourceHolder.set(DataSources.SLOT0);
            return;
        }
        DataSources slot = dataSource.value();
        DataSourceHolder.set(slot);
    }

}
