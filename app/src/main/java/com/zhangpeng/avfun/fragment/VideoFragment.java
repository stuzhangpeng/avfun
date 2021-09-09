package com.zhangpeng.avfun.fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zhangpeng.avfun.R;
import com.zhangpeng.avfun.broadcastreceiver.NetWorkStatusBroadCastReceiver;
import com.zhangpeng.avfun.utils.ConstantUtils;
import com.zhangpeng.avfun.utils.UrlUtils;

import java.util.Arrays;
import java.util.List;

public class VideoFragment extends Fragment {
    private VideoViewModel mViewModel;
    private View view;
    private  NetWorkStatusBroadCastReceiver netWorkStatusBroadCastReceiver;
    private  UncensoredFragment uncensored;
    private  UncensoredFragment censored;
    private  UncensoredFragment selfphoto;
    private  UncensoredFragment chinesefont;
    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.video_fragment, container, false);
        //实现滑动分页
        ViewPager2 viewPager = view.findViewById(R.id.viewpager2);
        //去除两侧光晕
        View childAt = viewPager.getChildAt(0);
        if(childAt instanceof RecyclerView){
            childAt.setOverScrollMode(ViewPager2.OVER_SCROLL_NEVER);
        }
        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        uncensored=new UncensoredFragment(ConstantUtils.uncensored, UrlUtils.RequireType.category);
        censored=new UncensoredFragment(ConstantUtils.censored, UrlUtils.RequireType.category);
        selfphoto=new UncensoredFragment(ConstantUtils.selfphoto, UrlUtils.RequireType.category);
        chinesefont=new UncensoredFragment(ConstantUtils.chinesefont, UrlUtils.RequireType.category);
        UncensoredFragment[] uncensoredFragments=new UncensoredFragment[]{uncensored,censored,selfphoto,chinesefont};
        List<String> tabTittles = Arrays.asList("无码", "有码", "自拍", "中文字幕");
        //设置adapter
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
               return uncensoredFragments[position];
            }

            @Override
            public int getItemCount(){
                return uncensoredFragments.length;
            }


        });
        //设置viewpager不需要懒加载，否则tablayout点击会导致，fragment重复创建
        new TabLayoutMediator(tabLayout, viewPager,true,false, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
               tab.setText(tabTittles.get(position));

            }
        }).attach();
         return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        //注册广播接收者
        initBroadCastReceiver();
        // TODO: Use the ViewModel

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(netWorkStatusBroadCastReceiver);
    }
    /**
     * 注册广播接收者
     */
    private void initBroadCastReceiver(){
        NetWorkStatusBroadCastReceiver.NetWorkSuccessCallBack [] callBacks=new  NetWorkStatusBroadCastReceiver
        .NetWorkSuccessCallBack[]{uncensored.new CallBackImpl(),censored.new CallBackImpl(),chinesefont.new CallBackImpl(),selfphoto.new CallBackImpl()};
        netWorkStatusBroadCastReceiver=new NetWorkStatusBroadCastReceiver(callBacks);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        getContext().registerReceiver(netWorkStatusBroadCastReceiver,intentFilter) ;


    }
}