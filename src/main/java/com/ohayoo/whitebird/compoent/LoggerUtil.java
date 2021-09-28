package com.ohayoo.whitebird.compoent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private static Logger log = LoggerFactory.getLogger(LoggerUtil.class);

    public static void info(String info){
        log.info(info);
    }
    public static boolean isInfoEnabled(){return log.isInfoEnabled();}
    public static void debug(String debug){
        log.debug(debug);
    }
    public static boolean isDebugEnabled(){return log.isDebugEnabled();}
    public static void warn(String warn){
        log.warn(warn);
    }
    public static boolean isWarnEnabled(){return log.isWarnEnabled();}
    public static void error(String error){
        log.error(error);
    }
    public static boolean isErrorEnabled(){return log.isErrorEnabled();}
}
