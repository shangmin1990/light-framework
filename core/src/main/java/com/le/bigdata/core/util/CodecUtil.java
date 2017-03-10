package com.le.bigdata.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by benjamin on 9/4/14.
 * 编码工具类
 */
public class CodecUtil {

    private CodecUtil() {

    }

    private static MessageDigest messageDigest = null;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (Exception e) {

        }
    }

    public static final String md5hex(String str) throws UnsupportedEncodingException {
        messageDigest.reset();
        messageDigest.update(str.getBytes("UTF-8"));
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String a = md5hex("admin");
        System.out.println(a);
        System.out.println(a.length());
    }
}
