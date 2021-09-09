package com.zhangpeng.avfun.utils;

public class StringUtils {
    public static String getPath(String url){
        if(url !=null){
            int index = url.lastIndexOf("/");
            if(index !=-1){
                return url.substring(index+1);
            }
        }
        return url;
    }
}
