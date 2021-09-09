package com.zhangpeng.avfun.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
/**
 * 时间处理工具类
 */
public class TimeUtils {
    /**
     * 毫秒转HH；mm；ss
     * @param millTime
     * @return
     */
    public static String  parseMillTime(Long millTime){
        return formartTimes(millTime);
    }
    private static String formartTimes(Long millTime){
        Date date=new Date(millTime);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String format = simpleDateFormat.format(date);
        return format;
    }
    public static String formartTimes(Date date,String type){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(type);
        String format = simpleDateFormat.format(date);
        return format;
    }
    public static String formartTimesToDate(Long millTime){
        Date date=new Date(millTime);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String format = simpleDateFormat.format(date);
        return format;
    }
}
