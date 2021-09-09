package com.zhangpeng.avfun.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.zhangpeng.avfun.utils.ConstantUtils;
import com.zhangpeng.avfun.utils.UrlUtils;

public class AvfunDataSourceFactory extends DataSource.Factory {
    private Context context;
    private ConstantUtils constantUtils;
    private UrlUtils.RequireType requireType;
    //需要是live data
    private MutableLiveData<AvfunDataSource> liveDataSource = new MutableLiveData<AvfunDataSource>();

    @NonNull
    @Override
    public DataSource create() {
        return init();
    }

    public AvfunDataSourceFactory(Context context, ConstantUtils constantUtils, UrlUtils.RequireType requireType) {
        this.context = context;
        this.constantUtils = constantUtils;
        this.requireType = requireType;
    }

    private AvfunDataSource init() {
        AvfunDataSource avfunDataSource = new AvfunDataSource(context, constantUtils, requireType);
        liveDataSource.postValue(avfunDataSource);
        return avfunDataSource;
    }

    public MutableLiveData<AvfunDataSource> getLiveDataSource() {
        if (liveDataSource.getValue() == null) {
            AvfunDataSource avfunDataSource = new AvfunDataSource(context, constantUtils, requireType);
            liveDataSource.setValue(avfunDataSource);
        }
        return liveDataSource;
    }

}
