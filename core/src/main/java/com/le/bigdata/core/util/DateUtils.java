package com.le.bigdata.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 多线程方法中使用了共享变量SimpleDateFormat，报如下错误：
 * <p>
 * java.lang.NumberFormatException: multiple points
 * at sun.misc.FloatingDecimal.readJavaFormatString(FloatingDecimal.java:1084
 * <p>
 * 原因：
 * <p>
 * SimpleDateFormat是非线程安全的，切忌切忌！
 */
public class DateUtils {
    public final static String DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss";

    public final static String PATTERN_YMDHMS_ONE = "yyyy-MM-dd HH:mm:ss";

    public final static String PATTERN_MD = "MM/dd";

    public final static String PATTERN_HM = "HH:mm";

    public final static String PATTERN_YYYYMMDD = "yyyy-MM-dd";

    public static final String PATTERN_NO_SPEATOR_DATE = "yyyyMMdd";

    public static final String PATTERN_NO_SPEATOR_MONTH = "yyyyMM";

    public final static SimpleDateFormat SF_PATTERN_MD = new SimpleDateFormat(PATTERN_MD);

    public final static SimpleDateFormat SF_PATTERN_HM = new SimpleDateFormat(PATTERN_HM);

    public final static SimpleDateFormat SF_PATTERN_YYYYMMDD = new SimpleDateFormat(PATTERN_YYYYMMDD);

    public final static SimpleDateFormat SF_PATTERN_NO_SPEATOR_DATE = new SimpleDateFormat(PATTERN_NO_SPEATOR_DATE);

    public final static SimpleDateFormat SF_PATTERN_NO_SPEATOR_MONTH = new SimpleDateFormat(PATTERN_NO_SPEATOR_MONTH);

    public static Date getDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);

        return calendar.getTime();
    }

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        return calendar.getTime();
    }

    @Deprecated
    public static String format(Date date) {
        return format(date, DEFAULT_PATTERN);
    }

    @Deprecated
    public static String format(Date date, String pattern) {
        if (date == null) return "";

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    @Deprecated
    public static Date parse(String date) {
        return parse(date, DEFAULT_PATTERN);
    }

    @Deprecated
    public static Date parse(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String format(String dt) {
        return dt.substring(0, 4) + "-" + dt.substring(4, 6) + "-" + dt.substring(6, 8);
    }
}
