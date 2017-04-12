package net.shmin.core.util;

import javax.servlet.http.Cookie;

/**
 * Cookie工具类
 * Created by tianbaoyou on 2016/8/10.
 */
public class CookieUtils {

    /*
    * 获得Cookie值
    * @param key 需要获取值的key
    * @param cookies cookie列表
    * */
    public static String getCookieValue(String key, Cookie[] cookies) {
        if (cookies != null && cookies.length != 0) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
