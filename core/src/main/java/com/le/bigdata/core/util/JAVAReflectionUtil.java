package com.le.bigdata.core.util;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by benjamin on 16/9/5.
 */
public class JAVAReflectionUtil {

    public static Field[] getAllDeclaredFields(Class clazz) {
        List<Field> fields = new LinkedList<>();
        getAllDeclaredFieldsInternal(clazz, fields);
        Field[] fieldArray = new Field[fields.size()];
        return fields.toArray(fieldArray);
    }

    private static void getAllDeclaredFieldsInternal(Class clazz, List<Field> fields) {
        if (clazz == null) {
            return;
        }
        Field[] fieldArray = clazz.getDeclaredFields();
        for (Field field : fieldArray) {
            fields.add(field);
        }
        getAllDeclaredFieldsInternal(clazz.getSuperclass(), fields);
    }

    public static Field getField(Class clazz, String mergeFieldName) throws NoSuchFieldException {
        if (clazz == null) {
            throw new NoSuchFieldException("not field name found:" + mergeFieldName);
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(mergeFieldName)) {
                return field;
            }
        }
        return getField(clazz.getSuperclass(), mergeFieldName);
    }
}
