package com.le.bigdata.core.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Calendar;

/**
 * Created by benjamin on 16/3/24.
 */
public class StringUtils {

    public static int stringToInt(String str) {
        String[] temp = str.split("\\*");
        int j = 1;
        for (int i = 0; i < temp.length; i++) {
            j = j * Integer.parseInt(temp[i]);
        }
        return j;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().equals("");
    }

    /**
     * 随即生成一个排序号
     *
     * @return
     */
    public static long randomOrder() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * 把中文转成Unicode码
     *
     * @param str
     * @return
     */
    public static String chinaToUnicode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = (char) str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
                result += "\\u" + Integer.toHexString(chr1);
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }

    /**
     * 判断是否为中文字符
     *
     * @param c
     * @return
     */
    public boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static String unicode2String(String unicodeStr) {
        StringBuffer sb = new StringBuffer();
        String str[] = unicodeStr.toUpperCase().split("U");
        for (int i = 0; i < str.length; i++) {
            if (str[i].equals(""))
                continue;
            char c = (char) Integer.parseInt(str[i].trim(), 16);
            sb.append(c);
        }
        return sb.toString();
    }

    public static double string2Num(String str) throws Throwable {
        if (str == null) str = "";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());

        byte b[] = md.digest();
        StringBuffer buf = new StringBuffer();
        int _int;
        for (int offset = 0; offset < b.length; offset++) {
            _int = b[offset];
            if (_int < 0)
                _int += 256;
            if (_int < 16)
                buf.append("0");
            buf.append(/*Integer.toHexString(*/_int/*)*/);
        }

        return Double.valueOf(buf.toString());
    }

    final static int BUFFER_SIZE = 4096;

    /**
     * 将InputStream转换成String
     *
     * @param in InputStream
     * @return String
     * @throws Exception
     */
    public static String inputStreamTOString(InputStream in, String encoding) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(), isEmpty(encoding) ? "UTF-8" : encoding);
    }

    public static void main(String[] args) {
        String str = "liuhongbin";
        try {
            System.out.println(string2Num(str));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
