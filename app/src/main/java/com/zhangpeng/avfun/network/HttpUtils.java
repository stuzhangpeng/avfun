package com.zhangpeng.avfun.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
/**
 * 网络请求工具类
 */
public class HttpUtils {
    private Context context;
    private RequestQueue requestQueue;
    private String url;
    private RequestType requestType;
    private int type;
    private CallBack callBack;
    StringRequest stringRequest;
    public void httpRequest(){
          if(requestType==RequestType.GET){
              type=Request.Method.GET;
          }else if (requestType==RequestType.POST){
              type=Request.Method.POST;
          }else{
              Log.e("requestType","requestType error");
              return;
          }
          stringRequest=new StringRequest(type, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.error(error);
            }
        });
        requestQueue.add(stringRequest);
    }
    public interface CallBack{
        public void success(String response);
        public void error(VolleyError error);
    }
    public static enum RequestType{
        GET,POST
    }
    /**
     * 获取实例
     */
    private HttpUtils (){};
    public static HttpUtils getSingleton(Context context, String url, RequestType requestType, CallBack callBack){
        HttpUtils instance= new HttpUtils();
        instance.init(context,url,requestType,callBack);
        return instance;
    }
    @Deprecated
    private static class HttpUtilsInstance{
        private static final HttpUtils instance=new HttpUtils();
    }
    public void init(Context context, String url, RequestType requestType, CallBack callBack){
        this.requestQueue =Volley.newRequestQueue(context);
        this.context=context;
        this.url = url;
        this.requestType = requestType;
        this.callBack=callBack;
    }
}
