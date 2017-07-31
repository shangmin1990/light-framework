package net.shmin.core.util;

import java.text.ParseException;
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
 * 使用threadLocal可以变成线程安全.
 */
public class DateUtils {

    private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

    public final static String DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss";

    public final static String PATTERN_YMDHMS_ONE = "yyyy-MM-dd HH:mm:ss";

    public final static String PATTERN_MD = "MM/dd";

    public final static String PATTERN_HM = "HH:mm";

    public final static String PATTERN_YYYYMMDD = "yyyy-MM-dd";

    public static final String PATTERN_NO_SPEATOR_DATE = "yyyyMMdd";

    public static final String PATTERN_NO_SPEATOR_MONTH = "yyyyMM";

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

    public static String format(Date date) {
        return format(date, DEFAULT_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date == null) return "";
        SimpleDateFormat simpleDateFormat = threadLocal.get();
        if(simpleDateFormat == null){
            simpleDateFormat = new SimpleDateFormat(pattern);
            threadLocal.set(simpleDateFormat);
        }
        return simpleDateFormat.format(date);
    }

    public static Date parse(String date) throws ParseException {
        return parse(date, DEFAULT_PATTERN);
    }

    public static Date parse(String date, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = threadLocal.get();
        if(simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(pattern);
            threadLocal.set(simpleDateFormat);
        }
        return simpleDateFormat.parse(date);
    }
}
