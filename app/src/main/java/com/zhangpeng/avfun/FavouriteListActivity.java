package com.zhangpeng.avfun;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangpeng.avfun.adapter.FavouriteListAdapter;
import com.zhangpeng.avfun.dao.FavouriteListDao;
import com.zhangpeng.avfun.entity.FavouriteList;
import com.zhangpeng.avfun.repository.SqlRepository;
import com.zhangpeng.avfun.utils.CustomDialog;
import com.zhangpeng.avfun.utils.CustomEditDialog;
import com.zhangpeng.avfun.utils.FullScreenUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavouriteListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavouriteListDao favouriteListDao;
    private FavouriteListAdapter favouriteListAdapter;
    private Toolbar toolbar;
    private TextView editTextView;
    private List<FavouriteList> favouriteLists;
    boolean isSelected = false;
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
        setContentView(R.layout.activity_favourite_list);
        toolbar = findViewById(R.id.favourite_list_toolbar);
        //设置沉浸式状态栏
        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        int height = layoutParams.height;
        FullScreenUtils.fullScreen(this);
        int statusBarHeight = FullScreenUtils.getStatusBarHeight(this);
        if (statusBarHeight != -1) {
            layoutParams.height = height + statusBarHeight;
            toolbar.setLayoutParams(layoutParams);
            toolbar.setPadding(20, statusBarHeight + 15, 20, 15);
        }
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        View backView = toolbar.findViewById(R.id.favourite_list_back_icon);
        //处理返回
        backView.setOnClickListener((view) -> {
            finish();
        });
        editTextView = toolbar.findViewById(R.id.favourite_list_edit);
        editTextView.setOnClickListener((view) -> {
            if (view.getId() == R.id.favourite_list_edit && editTextView.getText().equals("编辑")) {
                //清空播放历史
                DisplayMetrics dm = new DisplayMetrics();
                //获取activit的宽度
                WindowManager windowManager = getWindowManager();
                windowManager.getDefaultDisplay().getRealMetrics(dm);
                new CustomEditDialog(FavouriteListActivity.this).setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .setWidth(dm.widthPixels)
                        .setCancel("删除", (dialog) -> {
                            //清空播放历史
                            if (isSelected) {
                                SqlRepository.FavouriteListDeleteAllTask deleteAllSearchHistory = new SqlRepository.FavouriteListDeleteAllTask(favouriteListDao);
                                AsyncTask<Void, Void, Integer> execute = deleteAllSearchHistory.execute();
                                favouriteLists.clear();
                                favouriteListAdapter.submitList(favouriteLists);
                                favouriteListAdapter.notifyDataSetChanged();
                                editTextView.setVisibility(View.INVISIBLE);
                                isSelected = false;
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "亲，请选择删除项哦！", Toast.LENGTH_SHORT).show();
                            }
                        }).setConfirm("全选", (dialog) -> {
                    if (isSelected) {
                        dialog.setConfirmText("全选");
                        isSelected = false;
                        dialog.setCancelText("删除");
                    } else {
                        isSelected = true;
                        dialog.setCancelText("删除" + "(" + favouriteLists.size() + ")");
                        dialog.setConfirmText("取消全选");
                    }

                }).show();
            }
        });
        recyclerView = findViewById(R.id.favouritelistrecycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        favouriteListAdapter = new FavouriteListAdapter(this, new FavouriteListAdapter.CallBack() {
            @Override
            public void callback(FavouriteList favouriteList) {
                Intent intent = new Intent(FavouriteListActivity.this, VideoPlayActivity.class);
                intent.putExtra("videodetail", favouriteList);
                startActivity(intent);
            }

            @Override
            public void longCallBack(int position) {
                new CustomDialog(FavouriteListActivity.this).setMessage("亲，确定删除吗？要三思啊!").setTittle("提示")
                        .setCancel("取消", (dialog) -> {
                        }).setConfirm("确定", (dialog) -> {
                    List<FavouriteList> currentList = favouriteListAdapter.getCurrentList();
                    String videoTittle = currentList.get(position).getVideoTittle();
                    //删除数据库数据
                    new SqlRepository.FavouriteListDeleteAsyncTaskByTittle(favouriteListDao).execute(videoTittle);
                    favouriteLists.remove(position);
                    favouriteListAdapter.submitList(favouriteLists);
                    favouriteListAdapter.notifyItemRemoved(position);
                    if (currentList.size() == 0) {
                        editTextView.setVisibility(View.INVISIBLE);
                    }
                }).show();
            }
        });
        recyclerView.setAdapter(favouriteListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        favouriteListDao = SqlRepository.getFavouriteListDao(this);
        SqlRepository.FavouriteListQueryAllAsyncTask queryAllAsyncTask = new SqlRepository.FavouriteListQueryAllAsyncTask(favouriteListDao);
        AsyncTask<Void, Void, List<FavouriteList>> execute = queryAllAsyncTask.execute();
        try {
            favouriteLists = execute.get();
            if (favouriteLists != null && this.favouriteLists.size() > 0) {
                //更新数据到adapter
                favouriteListAdapter.submitList(this.favouriteLists);
            } else {
                //没有播放历史
                editTextView.setVisibility(View.INVISIBLE);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}