package net.shmin.core.util;

import org.slf4j.Logger;

/**
 * @Author: benjamin
 * @Date: Create in  2017/10/10 下午11:43
 * @Description:
 */
public class LoggerUtil {
    private LoggerUtil(){

    }

    public static void throwableLog(Logger logger, Throwable throwable){
        logger.error(throwable.toString());
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length > 0){
            for(StackTraceElement stackTraceElement: stackTraceElements){
                logger.error("at {}.{}({}:{})", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), stackTraceElement.getLineNumber());
            }
        }

    }
}
