package com.zhangpeng.avfun.fragment;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.zhangpeng.avfun.javabean.VideoCategoryEntity;
import com.zhangpeng.avfun.javabean.VideoItem;
import com.zhangpeng.avfun.network.HttpUtils;
import com.zhangpeng.avfun.utils.JsonUtils;

import java.util.List;

public class IntroductoryViewModel extends ViewModel {
    private MutableLiveData<List<VideoItem>> listLiveData=new MutableLiveData<List<VideoItem>>();
    public LiveData<List<VideoItem>> getListLiveData() {
        return listLiveData;
    }

   public void requestData(Context context, String url, HttpUtils.RequestType requestType){
        HttpUtils.getSingleton(context, url, requestType, new HttpUtils.CallBack() {
            @Override
            public void success(String response) {
                List<VideoItem> mapList = JsonUtils.parseJson(response, VideoCategoryEntity.class).getData().getMapList();
                listLiveData.setValue(mapList);
            }
            @Override
            public void error(VolleyError error) {
                Log.e("error","请求出错");
            }
        }).httpRequest();
    }
}
