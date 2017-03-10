package com.le.bigdata.core.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by benjamin on 2016/10/25.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultCache {
    // default 1 day
    long expire() default 24 * 60 * 60 * 1000;
}
