package com.zhangpeng.avfun.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.android.volley.VolleyError;
import com.zhangpeng.avfun.javabean.VideoCategoryEntity;
import com.zhangpeng.avfun.javabean.VideoItem;
import com.zhangpeng.avfun.network.HttpUtils;
import com.zhangpeng.avfun.utils.Constant;
import com.zhangpeng.avfun.utils.ConstantUtils;
import com.zhangpeng.avfun.utils.JsonUtils;
import com.zhangpeng.avfun.utils.NetWorkStatus;
import com.zhangpeng.avfun.utils.UrlUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 分页数据源
 */
public class AvfunDataSource extends PageKeyedDataSource {
    //网络状态
    private MutableLiveData<NetWorkStatus> networkstaus = new MutableLiveData<NetWorkStatus>();
    public LiveData<NetWorkStatus> liveData = networkstaus;
    private Context context;
    private int pageSize = Constant.pagesize;
    private int pageNumber = Constant.pageNumber;
    //= ConstantUtils.uncensored.getCategory()
    private String keyword;
    // = UrlUtils.getUrlByType(UrlUtils.RequireType.category, keyword, pageSize, pageNumber)
    private String urlByType;
    private UrlUtils.RequireType requireType;
    //保存错误现场数据
    private HashMap<String, Object> hashMap = new HashMap<String, Object>();

    public AvfunDataSource(Context context, ConstantUtils constantUtils, UrlUtils.RequireType requireType) {
        this.context = context;
        initData(constantUtils, requireType);
    }

    private void initData(ConstantUtils constantUtils, UrlUtils.RequireType requireType) {
        keyword = constantUtils.getCategory();
        this.requireType = requireType;
        urlByType = UrlUtils.getUrlByType(requireType, keyword, pageSize, pageNumber);
    }

    ;

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback callback) {
        hashMap.clear();
        networkstaus.postValue(NetWorkStatus.LOADIND);
        HttpUtils.getSingleton(context, urlByType, HttpUtils.RequestType.GET, new HttpUtils.CallBack() {
            @Override
            public void success(String response) {
                //解析数据
                List<VideoItem> mapList = JsonUtils.parseJson(response, VideoCategoryEntity.class).getData().getMapList();
                callback.onResult(mapList, null, 2);
            }

            @Override
            public void error(VolleyError error) {
                //保存错误现场
                hashMap.put("params", params);
                hashMap.put("callback", callback);
                networkstaus.postValue(NetWorkStatus.FAILED);
                Log.e("error", "error");
            }
        }).httpRequest();
    }

    @Override
    public void loadBefore(@NonNull LoadParams params, @NonNull LoadCallback callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams params, @NonNull LoadCallback callback) {
        hashMap.clear();
        networkstaus.postValue(NetWorkStatus.LOADIND);
        int pageNumber = Integer.valueOf(params.key.toString());
        urlByType = UrlUtils.getUrlByType(requireType, keyword, pageSize, pageNumber);
        HttpUtils.getSingleton(context, urlByType, HttpUtils.RequestType.GET, new HttpUtils.CallBack() {
            @Override
            public void success(String response) {
                //解析数据
                List<VideoItem> mapList = JsonUtils.parseJson(response, VideoCategoryEntity.class).getData().getMapList();
                callback.onResult(mapList, pageNumber + 1);
            }

            @Override
            public void error(VolleyError error) {
                //保存错误现场
                if (error.toString().equals("com.android.volley.clientError")) {
                    networkstaus.postValue(NetWorkStatus.COMPLETED);
                } else {
                    hashMap.put("params", params);
                    hashMap.put("callback", callback);
                    networkstaus.postValue(NetWorkStatus.FAILED);
                }
                Log.e(error.getClass().getName(), error.getMessage());
            }
        }).httpRequest();
    }

    //重新尝试获取数据
    public void retryGetData() {
        if (hashMap != null && hashMap.size() > 0) {
            if (hashMap.get("callback") instanceof LoadCallback) {
                loadAfter((LoadParams) hashMap.get("params"), (LoadCallback) hashMap.get("callback"));
            }
            if (hashMap.get("callback") instanceof LoadInitialCallback) {
                loadInitial((LoadInitialParams) hashMap.get("params"), (LoadInitialCallback) hashMap.get("callback"));
            }
        }
    }
}
