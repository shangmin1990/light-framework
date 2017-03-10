package com.le.bigdata.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by benjamin on 9/3/14.
 */
public class PropertiesUtil {

    private static Properties properties = new Properties();

    private PropertiesUtil() {

    }

    private static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = PropertiesUtil.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public static void initDefault() {
        InputStream in = PropertiesUtil.getDefaultClassLoader().getResourceAsStream("oauth.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void init(String path) {
        InputStream in = null;
//    try{
//      File file = new File(path);
//      in = new FileInputStream(file);
//      properties.load(in);
//    }catch (IOException e){
//      e.printStackTrace();
//      initDefault();
//    }finally {
//      try {
//        in.close();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//    }
        try {
            in = getDefaultClassLoader().getResourceAsStream(path);
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
            initDefault();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

}
