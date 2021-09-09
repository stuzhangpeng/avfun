package com.zhangpeng.avfun.utils;

import android.util.Log;

/**
 * url工具类封装请求url
 * http://192.168.0.109/videos/category/all?category=uncensored&page=1&pagesize=48
 * url: "http://192.168.0.109/videos/keyword",
 *       method: "get",
 *       params: {
 *         keyword: this.keyword,
 *         page: this.pagenumber,
 *         pagesize: 48
 */
public class UrlUtils {
    private static final String BASE_URL="http://192.168.0.109";
    private static String searchUrl="/videos/keyword";
    private static String categoryUrl="/videos/category/all";
    public static final String  SEARCH_Type="keyword";
    public static final String  CATEGORY_Type="category";
    public static String getUrlByType(RequireType requireType,String keyword,int pageSize,int pageNumber){
        String url=null;
        switch (requireType){
            case Search:
                url = parseUrl(SEARCH_Type,BASE_URL,searchUrl, keyword, pageSize, pageNumber);
                break;
            case category:
                url = parseUrl(CATEGORY_Type,BASE_URL,categoryUrl, keyword, pageSize, pageNumber);
                break;
            default:
                Log.e(UrlUtils.class.getName(),"输入请求url的requireType错误");
                break;
       }
        return url;
    }
    public static enum RequireType{
        Search, category
    }
    private  static String parseUrl(String type ,String baseUrl,String url,String keyword,int pageSize,int pageNumber){
        return  baseUrl+url+"?"+type+"="+keyword+"&page="+pageNumber+"&pagesize="+pageSize;
    }
}
