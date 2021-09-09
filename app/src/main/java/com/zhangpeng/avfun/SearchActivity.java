package com.zhangpeng.avfun;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.zhangpeng.avfun.adapter.SearchHistoryAdapter;
import com.zhangpeng.avfun.dao.SearchHistoryDao;
import com.zhangpeng.avfun.entity.SearchHistory;
import com.zhangpeng.avfun.fragment.UncensoredFragment;
import com.zhangpeng.avfun.repository.SqlRepository;
import com.zhangpeng.avfun.utils.ConstantUtils;
import com.zhangpeng.avfun.utils.CustomDialog;
import com.zhangpeng.avfun.utils.FullScreenUtils;
import com.zhangpeng.avfun.utils.UrlUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {
    private SearchHistoryDao searchHistoryDao;
    private RecyclerView searchHistoryRecycleView;
    private SearchHistoryAdapter searchHistoryAdapter;
    private Toolbar toolbar;
    private LinearLayout searchLinearLayout;
    private LinearLayout searchHistoryLinerlayout;
    private ImageView deleteImageView;
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
        setContentView(R.layout.activity_search);
        searchHistoryLinerlayout = findViewById(R.id.search_history_linerlayout);
        toolbar = findViewById(R.id.search_toolbar);
        //设置沉浸式状态栏
        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        int height = layoutParams.height;
        FullScreenUtils.fullScreen(this);
        int statusBarHeight = FullScreenUtils.getStatusBarHeight(this);
        if (statusBarHeight != -1) {
            layoutParams.height = height + statusBarHeight;
            toolbar.setLayoutParams(layoutParams);
            toolbar.setPadding(20, statusBarHeight + 10, 20, 10);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        TextInputEditText editText = toolbar.findViewById(R.id.search_view);
        View backview = toolbar.findViewById(R.id.back_icon);
        backview.setOnClickListener((view) -> {
            if (view.getId() == R.id.back_icon) {
                finish();
            }
        });
        TextView searchText = toolbar.findViewById(R.id.search_text);
        Intent intent = getIntent();
        String keyword = intent.getStringExtra("keyword");
        if (keyword != null && keyword.length() > 0) {
            editText.setText(keyword);
        } else {
            editText.setHint("搜索");
        }
        editText.setFocusable(true);
        editText.requestFocusFromTouch();
        searchText.setOnClickListener((view) -> {
            String content = editText.getText().toString();
            execSearch(content);
        });
        searchHistoryDao = SqlRepository.getSearchHistoryDao(this);
        searchLinearLayout = findViewById(R.id.searchfragment);
        deleteImageView = findViewById(R.id.delete_imageView);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomDialog(SearchActivity.this).setMessage("亲,确定清空搜索历史吗?要三思啊!").setTittle("提示")
                        .setCancel("取消", (dialog) -> {
                        }).setConfirm("确定", (dialog) -> {
                    //清空数据库
                    new SqlRepository.SearchHistoryDeleteAllSearchHistory(searchHistoryDao).execute();
                    //更新recycleview
                    if (searchHistoryRecycleView.getChildCount() > 0) {
                        searchHistoryRecycleView.removeAllViews();
                        searchHistoryAdapter.setList(null);
                        searchHistoryAdapter.notifyDataSetChanged();
                    }
                    searchHistoryLinerlayout.setVisibility(View.INVISIBLE);
                }).show();
            }
        });
        searchHistoryRecycleView = findViewById(R.id.searchhistory);
        searchHistoryRecycleView.setLayoutManager(new GridLayoutManager(this, 4));
        searchHistoryAdapter = new SearchHistoryAdapter(this, new SearchHistoryAdapter.CallBack() {
            @Override
            public void callback(String text) {
                //执行查询
                execSearch(text);
            }

            @Override
            public void longcallback(int position) {
                new CustomDialog(SearchActivity.this).setMessage("亲,确定删除吗?要三思啊!").setTittle("提示")
                        .setCancel("取消", (dialog) -> {
                        }).setConfirm("确定", (dialog) -> {
                    List<SearchHistory> list = searchHistoryAdapter.getList();
                    SearchHistory searchHistory = list.get(position);
                    String keyword = searchHistory.getKeyword();
                    list.remove(position);
                    if (list.size() == 0) {
                        searchHistoryLinerlayout.setVisibility(View.INVISIBLE);
                    }
                    searchHistoryAdapter.notifyItemRemoved(position);
                    //删除数据库数据
                    new SqlRepository.SearchHistoryDeleteSearchHistoryByKeyword(searchHistoryDao).execute(keyword);
                }).show();
            }
        });
        searchHistoryRecycleView.setAdapter(searchHistoryAdapter);
        SqlRepository.SearchHistoryQueryAllAsyncTask queryAllAsyncTask = new SqlRepository.SearchHistoryQueryAllAsyncTask(searchHistoryDao);
        AsyncTask<Void, Void, List<SearchHistory>> execute = queryAllAsyncTask.execute();
        try {
            //会等待
            List<SearchHistory> searchHistories = execute.get();
            if (searchHistories != null && searchHistories.size() > 0) {
                //设置adapter数据
                searchHistoryAdapter.setList(searchHistories);
                searchHistoryAdapter.notifyDataSetChanged();
                searchHistoryLinerlayout.setVisibility(View.VISIBLE);
            } else {
                searchHistoryLinerlayout.setVisibility(View.INVISIBLE);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void search(String keyword) {
        searchLinearLayout.removeAllViewsInLayout();
        //动态更换fragment
        ConstantUtils constantUtils = ConstantUtils.search;
        constantUtils.setCategory(keyword);
        UncensoredFragment uncensoredFragment = new UncensoredFragment(constantUtils, UrlUtils.RequireType.Search);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.searchfragment, uncensoredFragment);
        fragmentTransaction.commit();
    }

    private void execSearch(String content) {
        if (content != null && !content.equals("")) {
            //执行查询业务
            search(content);
            //执行搜索记录入库
            SqlRepository.SearchHistoryQueryAsyncTask queryAsyncTask = new SqlRepository.SearchHistoryQueryAsyncTask(searchHistoryDao);
            SqlRepository.SearchHistoryUpadteAsyncTask upadteAsyncTask = new SqlRepository.SearchHistoryUpadteAsyncTask(searchHistoryDao);
            SqlRepository.SearchHistoryInsertAsyncTask insertAsyncTask = new SqlRepository.SearchHistoryInsertAsyncTask(searchHistoryDao);
            AsyncTask<String, Void, List<SearchHistory>> execute = queryAsyncTask.execute(content);
            try {
                //会等待
                List<SearchHistory> searchHistories = execute.get();
                if (searchHistories != null && searchHistories.size() > 0) {
                    //执行更新
                    SearchHistory searchHistoryExist = searchHistories.get(0);
                    searchHistoryExist.setTime(new Date().getTime());
                    upadteAsyncTask.execute(searchHistoryExist);
                } else {
                    //执行插入
                    SearchHistory searchHistory = new SearchHistory(content, new Date().getTime());
                    insertAsyncTask.execute(searchHistory);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}