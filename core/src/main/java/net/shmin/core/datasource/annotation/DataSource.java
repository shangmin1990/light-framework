package net.shmin.core.datasource.annotation;

import net.shmin.core.datasource.DataSources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by benjamin on 16/5/23.
 */
@Target({
        ElementType.METHOD, ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    DataSources value();
}
