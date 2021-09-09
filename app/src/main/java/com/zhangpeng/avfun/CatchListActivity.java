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

import com.zhangpeng.avfun.adapter.CatchListAdapter;
import com.zhangpeng.avfun.dao.DownloadRecoreListDao;
import com.zhangpeng.avfun.entity.DownLoadRecord;
import com.zhangpeng.avfun.repository.SqlRepository;
import com.zhangpeng.avfun.utils.CustomDialog;
import com.zhangpeng.avfun.utils.CustomEditDialog;
import com.zhangpeng.avfun.utils.FullScreenUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CatchListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DownloadRecoreListDao downloadRecoreListDao;
    private CatchListAdapter catchListAdapter;
    private Toolbar toolbar;
    private TextView editTextView;
    private List<DownLoadRecord> catchList;
    boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_list);
        //设置actionbar
        toolbar = findViewById(R.id.catch_list_toolbar);
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
        View backView = toolbar.findViewById(R.id.catch_list_back_icon);
        //处理返回
        backView.setOnClickListener((view) -> {
            finish();
        });
        editTextView = toolbar.findViewById(R.id.catch_list_edit);
        editTextView.setOnClickListener((view) -> {
            if (view.getId() == R.id.catch_list_edit) {
                //清空播放历史
                DisplayMetrics dm = new DisplayMetrics();
                //获取activit的宽度
                WindowManager windowManager = getWindowManager();
                windowManager.getDefaultDisplay().getRealMetrics(dm);
                new CustomEditDialog(CatchListActivity.this).setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .setWidth(dm.widthPixels)
                        .setCancel("删除", (dialog) -> {
                            //清空播放历史
                            if (isSelected) {
                                SqlRepository.DownloadRecoreListDeleteAllTask deleteAllTask = new SqlRepository.DownloadRecoreListDeleteAllTask(downloadRecoreListDao);
                                deleteAllTask.execute();
                                catchList.clear();
                                catchListAdapter.submitList(catchList);
                                catchListAdapter.notifyDataSetChanged();
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
                        dialog.setCancelText("删除" + "(" + catchList.size() + ")");
                        dialog.setConfirmText("取消全选");
                    }

                }).show();
            }
        });
        recyclerView = findViewById(R.id.catchlistrecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        catchListAdapter = new CatchListAdapter(this, new CatchListAdapter.CallBack() {
            @Override
            public void callback(DownLoadRecord downLoadRecord) {
                Intent intent = new Intent(CatchListActivity.this, VideoPlayActivity.class);
                intent.putExtra("videodetail", downLoadRecord);
                startActivity(intent);
            }

            @Override
            public void longcallback(int position) {
                new CustomDialog(CatchListActivity.this).setMessage("亲，确定删除吗？要三思啊!").setTittle("提示")
                        .setCancel("取消", (dialog) -> {
                        }).setConfirm("确定", (dialog) -> {
                    List<DownLoadRecord> currentList = catchListAdapter.getCurrentList();
                    String videoTittle = currentList.get(position).getVideoTittle();
                    //删除数据库数据
                    new SqlRepository.DownloadRecoreListDeleteTaskByTittle(downloadRecoreListDao).execute(videoTittle);
                    catchList.remove(position);
                    catchListAdapter.submitList(catchList);
                    catchListAdapter.notifyItemRemoved(position);
                    if (catchList.size() == 0) {
                        editTextView.setVisibility(View.INVISIBLE);
                    }
                }).show();
            }
        });
        recyclerView.setAdapter(catchListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadRecoreListDao = SqlRepository.getDownloadRecoreListDao(this);
        SqlRepository.DownloadRecoreListQueryAllAsyncTask queryAllAsyncTask = new SqlRepository.DownloadRecoreListQueryAllAsyncTask(downloadRecoreListDao);
        AsyncTask<Void, Void, List<DownLoadRecord>> execute = queryAllAsyncTask.execute();
        try {
            catchList = execute.get();
            if (catchList != null && catchList.size() > 0) {
                //更新数据到adapter
                catchListAdapter.submitList(catchList);
                System.out.println(catchList);
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
    @Override
    protected void onResume() {
        super.onResume();
        AvFunApplication application = (AvFunApplication) getApplication();
        if(application.isLocked()==true){
            Intent intent=new Intent(this,SecrectActivity.class);
            startActivity(intent);
        }
    }
}