package net.shmin.core.cache;

import com.alibaba.fastjson.JSONObject;
import net.shmin.core.util.JedisPoolUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by benjamin on 2016/10/25.
 */
@Component
@Aspect
@Order(3)
public class JedisResultCacheAspect {

    @Autowired
    private JedisPoolUtil jedisPoolUtil;

    private static Logger logger = LoggerFactory.getLogger(JedisResultCacheAspect.class);

    @Pointcut(value = "(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController))" +
            "&& @annotation(net.shmin.core.cache.ResultCache)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object keySorted(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        if (methodSignature.getMethod().isAnnotationPresent(ResultCache.class)) {
            Method method = methodSignature.getMethod();
            ResultCache resultCache = method.getAnnotation(ResultCache.class);
//            StringBuilder keyBuilder = new StringBuilder(method.getName());
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            for (Object arg : args) {
                if (arg != null && arg instanceof HttpServletRequest) {
                    request = (HttpServletRequest) arg;
                }
            }
            org.springframework.util.Assert.notNull(request);
            String queryString = CacheUtil.getQueryParam(request);

            queryString = CacheUtil.sortCacheKey(queryString);
            String path = request.getServletPath();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("path", path);
            jsonObject.put("queryString", queryString);
            String key = jsonObject.toJSONString();
            Jedis jedis = jedisPoolUtil.getJedis();
            long begin = System.nanoTime();
            String str = jedis.get(key);
            long end = System.nanoTime();
            logger.info("从redis中获取缓存花费时间{}ms", TimeUnit.MILLISECONDS.convert(end - begin, TimeUnit.NANOSECONDS));
            if (str != null && !str.isEmpty()) {
                jedis.close();
                long begin1 = System.nanoTime();
                Object o = JSONObject.parse(str);
                long end1 = System.nanoTime();
                logger.info("parse JSON花费时间{}ms", TimeUnit.MILLISECONDS.convert(end1 - begin1, TimeUnit.NANOSECONDS));
                return o;
            } else {
                Object result = joinPoint.proceed();
                jedis.psetex(key, resultCache.expire(), JSONObject.toJSONString(result));
                jedis.close();
                return result;
            }
        } else {
            Object result = joinPoint.proceed();
            return result;
        }
    }
}
