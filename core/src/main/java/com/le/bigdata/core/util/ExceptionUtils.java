package com.le.bigdata.core.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtils {

    /**
     * 将异常信息的堆栈转换为字符串
     *
     * @param e 异常
     * @return
     */
    public static String exception2String(Throwable e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        return baos.toString();
    }
}
