package com.le.bigdata.core.datasource;

import com.le.bigdata.core.datasource.annotation.DataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 纵横有3个数据源,如果采用统一的方式来管理必须能够做到自动切换数据源.
 * 数据库路由切换AOP,切面横切service包下所有包,动态的切换数据源
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
    // Service 包及其子包
    @Pointcut("execution(* com.le.bigdata.service..*.*(..))")
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
            DataSourceHolder.set(DataSources.MATRIX_DATA_STORE);
            return;
        }
        String value = dataSource.value();
        DataSources[] dataSources = DataSources.values();
        for (int i = 0; i < dataSources.length; i++) {
            DataSources type = dataSources[i];
            if (type.toString().equals(value)) {
                DataSourceHolder.set(type);
                return;
            }
        }
        DataSourceHolder.set(DataSources.MATRIX_DATA_STORE);
    }

    public static void main(String... args) {
        DataSources type = Enum.valueOf(DataSources.class, "matrix");
        System.out.print(type);
    }
}
