package com.zhangpeng.avfun.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zhangpeng.avfun.R;
import com.zhangpeng.avfun.adapter.VideoListAdapter;
import com.zhangpeng.avfun.broadcastreceiver.NetWorkStatusBroadCastReceiver;
import com.zhangpeng.avfun.javabean.VideoItem;
import com.zhangpeng.avfun.utils.ConstantUtils;
import com.zhangpeng.avfun.utils.UrlUtils;

public class UncensoredFragment extends Fragment {
    private TextView refreshTextView;
    private  int RETRY_COUNT=0;
    private final int MAX_RETRY_COUNT=3;
    private RecyclerView recyclerView;
    private UncensoredViewModel mViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private ConstantUtils constantUtils;
    private  UrlUtils.RequireType requireType;
    private  VideoListAdapter videoListAdapter;
    private boolean flag=true;
    public UncensoredFragment(ConstantUtils constantUtils, UrlUtils.RequireType requireType) {
        this.constantUtils = constantUtils;
        this.requireType = requireType;
    }

    public UncensoredFragment() {
    }

    public static UncensoredFragment newInstance() {
        return new UncensoredFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.uncensored_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UncensoredViewModel.class);
        mViewModel.setConstantUtils(constantUtils);
        mViewModel.setRequireType(requireType);
        //mViewModel.initData();
        recyclerView=view.findViewById(R.id.recyclerview);
        refreshTextView=view.findViewById(R.id.networktextview);
        swipeRefreshLayout=view.findViewById(R.id.swiperrefreshlayout);
        //设置不可见
        refreshTextView.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
         videoListAdapter =new VideoListAdapter(getContext());
        recyclerView.setAdapter(videoListAdapter);
        //为recyclerview设置动画
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(500);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
        translateAnimation.setDuration(500);
        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        LayoutAnimationController controller = new LayoutAnimationController(animationSet,0.5f);
        recyclerView.setLayoutAnimation(controller);
        //设置观察者
        loadData();
        swipeRefreshLayout.setOnRefreshListener(()->{
            mViewModel.retryGetData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }
    public void loadData(){
        mViewModel.initData();
        mViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<VideoItem>>() {
                @Override
                public void onChanged(PagedList<VideoItem> videoItems) {
                    //数据发生变化提交到adapter
                    videoListAdapter.submitList(videoItems);
                }
            });
        mViewModel.getNetworkstaus().observe(getViewLifecycleOwner(),(netWorkStatus)->{
            //网络状态
           switch (netWorkStatus){
               case FAILED:
                   if(RETRY_COUNT<MAX_RETRY_COUNT){
                       RETRY_COUNT++;
                       mViewModel.retryGetData();
                   }else{
                       refreshTextView.setText("当前网络不可用,请查看网络设置,刷新请下拉！");
                       refreshTextView.setTextSize(18f);
                       refreshTextView.setVisibility(View.VISIBLE);
                       Toast toast = Toast.makeText(getContext(), "当前网络不可用,请检查网络设置", Toast.LENGTH_SHORT);
                       toast.setGravity(Gravity.CENTER,0,0);
                       toast.show();
                   }
                   break;
           }
        });
        }
    public class CallBackImpl implements NetWorkStatusBroadCastReceiver.NetWorkSuccessCallBack{
        @Override
        public void callBack() {
            //自动拉取数据
            if (mViewModel!=null&&isVisible()){
                System.out.println("1111111111111111111");
                System.out.println(mViewModel);
                System.out.println("1111111111111111111");
                mViewModel.retryGetData();
            }
        }
    }

}
