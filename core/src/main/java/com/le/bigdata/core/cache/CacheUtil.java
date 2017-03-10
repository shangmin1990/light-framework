package com.le.bigdata.core.cache;


import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;

/**
 * Created by benjamin on 2016/11/7.
 */
public class CacheUtil {

    private CacheUtil() {

    }

    public static final String sortCacheKey(String originalKey) {
        if (originalKey == null || originalKey.isEmpty()) {
            return originalKey;
        }
        String[] keys = originalKey.split("&");
        Arrays.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : keys) {
            stringBuilder.append(key);
            stringBuilder.append("&");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static String getQueryParam(HttpServletRequest request) {
        if (request.getMethod().toUpperCase().equals("GET")) {
            return request.getQueryString();
        } else if (request.getMethod().toUpperCase().equals("POST")) {
            StringBuilder stringBuilder = new StringBuilder();
            Enumeration<String> enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement();
                stringBuilder.append(name);
                stringBuilder.append("=");
                stringBuilder.append(request.getParameter(name));
                stringBuilder.append("&");
            }
            return stringBuilder.length() > 0 ? stringBuilder.substring(0, stringBuilder.length() - 1) : null;
        }
        return null;
    }
}
