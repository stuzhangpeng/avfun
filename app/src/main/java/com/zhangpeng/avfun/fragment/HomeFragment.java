package com.zhangpeng.avfun.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.zhangpeng.avfun.CatchListActivity;
import com.zhangpeng.avfun.FavouriteListActivity;
import com.zhangpeng.avfun.PasswordSettingActivity;
import com.zhangpeng.avfun.PlayHistoryActivity;
import com.zhangpeng.avfun.R;
import com.zhangpeng.avfun.SecrectActivity;
import com.zhangpeng.avfun.WebViewActivity;
import com.zhangpeng.avfun.adapter.CommonServiceAdapter;
import java.util.Arrays;
import java.util.List;
public class HomeFragment extends Fragment {
    private HomeViewModel mViewModel;
    private View view;
    private GridView gridView;
    private List<String> mTextList;
    private List<Integer> mIconList;
    private TextView favouriteListTextView;
    private TextView downloadMangerTextView;
    private TextView playHistoryTextView;
    private TextView messageNotificationTextView;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.home_fragment, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        favouriteListTextView= view.findViewById(R.id.favourite_list);
        downloadMangerTextView=view.findViewById(R.id.download_manager);
        playHistoryTextView=view.findViewById(R.id.play_history);
        messageNotificationTextView= view.findViewById(R.id.message_notification);
        MyClickListener myClickListener=new MyClickListener();
        favouriteListTextView.setOnClickListener(myClickListener);
        downloadMangerTextView.setOnClickListener(myClickListener);
        playHistoryTextView.setOnClickListener(myClickListener);
        messageNotificationTextView.setOnClickListener(myClickListener);
        mTextList= Arrays.asList("评论","点赞","夜间模式","版本更新","用户反馈","求片反馈","广告推广","隐私模式","重设密码");
        mIconList=Arrays.asList(R.drawable.pinlun,R.drawable.zan,R.drawable.yejianmoshi,R.drawable.versionrefresh,R.drawable.comment,R.drawable.qiupianfankui,R.drawable.ad,R.drawable.yinsimoshi,R.drawable.kaiqiyinsimoshi);
        gridView = view.findViewById(R.id.common_service);
        gridView.setAdapter(new CommonServiceAdapter(getContext(),mTextList,mIconList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text=null;
                if (view instanceof TextView){
                    TextView textView= (TextView) view;
                    text=textView.getText().toString();
                }else{
                    text = mTextList.get(position);
                }
                Intent  intent=null;
                switch (text){
                    case "评论":
                        break;
                    case "点赞":
                        break;
                    case "夜间模式":
                        break;
                    case "版本更新":
                        break;
                    case "用户反馈":
                        break;
                    case "求片反馈":
                        break;
                    case "广告推广":
                        intent=new Intent(getContext(),  WebViewActivity.class);
                        startActivity(intent);
                        break;
                    case "隐私模式":
                        intent=new Intent(getContext(), PasswordSettingActivity.class);
                        startActivity(intent);
                        break;
                    case "重设密码":
                        intent=new Intent(getContext(), PasswordSettingActivity.class);
                        intent.putExtra("action","resetting");
                        startActivity(intent);
                        break;
                }
            }
        });
    }
   class MyClickListener implements View.OnClickListener{
       @Override
       public void onClick(View v) {
           int id = v.getId();
           Intent  intent=null;
           switch (id){
               case R.id.favourite_list:
                   intent=new Intent(getContext(), FavouriteListActivity.class);
                   getContext().startActivity(intent);
                   break;
               case R.id.download_manager:
                   intent=new Intent(getContext(), CatchListActivity.class);
                   getContext().startActivity(intent);
                   break;
               case R.id.play_history:
                   intent=new Intent(getContext(), PlayHistoryActivity.class);
                   getContext().startActivity(intent);
                   break;
               case R.id.message_notification:
                   break;
           }
       }
   }
}