package com.zhangpeng.avfun.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.zhangpeng.avfun.data.AvfunDataSourceFactory;
import com.zhangpeng.avfun.javabean.VideoItem;
import com.zhangpeng.avfun.utils.ConstantUtils;
import com.zhangpeng.avfun.utils.NetWorkStatus;
import com.zhangpeng.avfun.utils.UrlUtils;
public class UncensoredViewModel extends AndroidViewModel {
    private AvfunDataSourceFactory dataSourceFactory;
    private LiveData<PagedList<VideoItem>> liveData;
    private UrlUtils.RequireType requireType;
    private ConstantUtils constantUtils;
    private LiveData<NetWorkStatus> networkstaus;
    public UrlUtils.RequireType getRequireType() {
        return requireType;
    }
    public void setRequireType(UrlUtils.RequireType requireType) {
        this.requireType = requireType;
    }

    public ConstantUtils getConstantUtils() {
        return constantUtils;
    }

    public void setConstantUtils(ConstantUtils constantUtils) {
        this.constantUtils = constantUtils;
    }
    public UncensoredViewModel(@NonNull Application application) {
        super(application);

    }
    /**
     * 数据初始化
     */
    public void initData(){
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(1)//每页显示的词条数
                .setPageSize(10)    //每页显示的词条数
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10) //首次加载的数据量
                .setPrefetchDistance(5)     //距离底部还有多少条数据时开始预加载
                .build();
        dataSourceFactory=new AvfunDataSourceFactory(getApplication(),constantUtils,requireType);
        //通过datasource获取netwstatus
        networkstaus= Transformations.switchMap(dataSourceFactory.getLiveDataSource(),(dataSource)->dataSource.liveData);
        this.liveData=new LivePagedListBuilder(dataSourceFactory, config).build();
    }

    public LiveData<PagedList<VideoItem>> getLiveData() {
        return liveData;
    }

    public LiveData<NetWorkStatus> getNetworkstaus() {
        return networkstaus;
    }
    public void retryGetData(){
        //通过datasourc重新获取数据
        if (dataSourceFactory!=null){
            dataSourceFactory.getLiveDataSource().getValue().retryGetData();
        }
    }
}