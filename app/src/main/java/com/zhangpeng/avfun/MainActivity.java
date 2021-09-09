package com.zhangpeng.avfun;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zhangpeng.avfun.broadcastreceiver.LockScreenBroadCastReceiver;
import com.zhangpeng.avfun.service.NotifiCationService;
import com.zhangpeng.avfun.utils.FullScreenUtils;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private FragmentManager supportFragmentManager;
    private NavHostFragment navhostfragment;
    int statusBarHeight;
    private Toolbar toolbar;
    private boolean flag=false;
    private  LockScreenBroadCastReceiver lockScreenBroadCastReceiver;
    private LinearLayoutCompat compat;
    @Override
    protected void onResume() {
        super.onResume();
        AvFunApplication application = (AvFunApplication) getApplication();
        if(application.isLocked()==true){
            Intent intent=new Intent(this,SecrectActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.mytoolbar);
        compat= toolbar.findViewById(R.id.linerlayoutcompat);
        //设置沉浸式状态栏
        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        int height = layoutParams.height;
        FullScreenUtils.fullScreen(this);
        statusBarHeight = FullScreenUtils.getStatusBarHeight(this);
        if (statusBarHeight != -1) {
            layoutParams.height = height + statusBarHeight;
            toolbar.setLayoutParams(layoutParams);
            toolbar.setPadding(20, statusBarHeight + 15, 20, 15);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        View searchview = toolbar.findViewById(R.id.searchitem);
        View loginImageview = toolbar.findViewById(R.id.loginimageViews);
        loginImageview.setOnClickListener((view) -> {
            //跳转登录
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        searchview.setOnClickListener((view) -> {
            //跳转到搜索
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                CharSequence text = textView.getText();
                if (text != null) {
                    intent.putExtra("keyword", text);
                }
            }
            startActivity(intent);
        });
        //底部tab栏导航
        supportFragmentManager = getSupportFragmentManager();
        navhostfragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.fragment3);
        navigationView = findViewById(R.id.bottomNavigationView2);
        NavController navController = navhostfragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                //监听导航，动态设置导航标题
                int id = destination.getId();
                switch (id) {
                    case R.id.homeitem:
                        if(!flag){
                            LayoutInflater layoutInflater = getLayoutInflater();
                            View inflate = layoutInflater.inflate(R.layout.custom_toolbar,null);
                            ViewGroup.LayoutParams layoutParams1 = compat.getLayoutParams();
                            toolbar.removeAllViewsInLayout();
                            toolbar.addView(inflate, layoutParams1);
                            flag=true;
                        }
                        break;
                    default:
                        if(flag){
                          toolbar.removeAllViewsInLayout();
                          toolbar.addView(compat);
                          flag=false;
                        }
                        break;
                }
            }
        });
        //去掉底部菜单长按吐司
        List<Integer> ids = Arrays.asList(R.id.finditem, R.id.videoitem, R.id.imageitem, R.id.homeitem);
        View childAt = navigationView.getChildAt(0);
        for (int position=0;position<ids.size();position++){
            childAt.findViewById(ids.get(position)).setOnLongClickListener((view)-> true);
        }
        //设置启动选择的fragment
        navigationView.setSelectedItemId(R.id.videoitem);
        Intent intent=new Intent(this, NotifiCationService.class);
        startService(intent);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
         lockScreenBroadCastReceiver=new LockScreenBroadCastReceiver();
         registerReceiver(lockScreenBroadCastReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(lockScreenBroadCastReceiver);
        super.onDestroy();
    }
}