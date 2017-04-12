package net.shmin.core.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benjamin on 2017/1/18.
 */
public class BeanCreateFactory {

    private static Map<Class<?>, Object> beanCache = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(BeanCreateFactory.class);

    public static <T> T getBean(Class<T> clazz, boolean single) throws IllegalAccessException, InstantiationException {
        if (single) {
            synchronized (BeanCreateFactory.class) {
                if (beanCache.containsKey(clazz)) {
                    return (T) beanCache.get(clazz);
                } else {
                    T instance = clazz.newInstance();
                    beanCache.put(clazz, instance);
                    return instance;
                }
            }
        } else {
            T instance = clazz.newInstance();
            return instance;
        }
    }

    public static Object getBean(String path, boolean single) throws IllegalAccessException, InstantiationException {
        Class clazz = null;
        try {
            clazz = Class.forName(path);
        } catch (ClassNotFoundException e) {
            logger.error("类名 [" + path + "]" + " 加载失败,请检查类的全额限定名");
        }
        if (single) {
            synchronized (BeanCreateFactory.class) {
                if (beanCache.containsKey(clazz)) {
                    return beanCache.get(clazz);
                } else {
                    Object instance = clazz.newInstance();
                    beanCache.put(clazz, instance);
                    return instance;
                }
            }
        } else {
            Object instance = clazz.newInstance();
            return instance;
        }
    }

}
