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

import com.zhangpeng.avfun.adapter.PlayHistoryListAdapter;
import com.zhangpeng.avfun.dao.VideoPlayHistoryDao;
import com.zhangpeng.avfun.entity.PlayHistory;
import com.zhangpeng.avfun.javabean.VideoItem;
import com.zhangpeng.avfun.repository.SqlRepository;
import com.zhangpeng.avfun.utils.CustomDialog;
import com.zhangpeng.avfun.utils.CustomEditDialog;
import com.zhangpeng.avfun.utils.FullScreenUtils;
import java.util.List;
import java.util.concurrent.ExecutionException;
public class PlayHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideoPlayHistoryDao playHistoryDao;
    private PlayHistoryListAdapter playHistoryListAdapter;
    private Toolbar toolbar;
    private TextView editTextView;
    private List<PlayHistory> playHistories;
    boolean isSelected = false;
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
        setContentView(R.layout.activity_play_history);
        //设置actionbar
        toolbar = findViewById(R.id.history_toolbar);
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
        View backView = toolbar.findViewById(R.id.play_history_back_icon);
        //处理返回
        backView.setOnClickListener((view) -> {
            finish();
        });
        editTextView = toolbar.findViewById(R.id.play_history_edit);
        editTextView.setOnClickListener((view) -> {
            if (view.getId() == R.id.play_history_edit) {
                //清空播放历史
                DisplayMetrics dm = new DisplayMetrics();
                //获取activit的宽度
                WindowManager windowManager = getWindowManager();
                windowManager.getDefaultDisplay().getRealMetrics(dm);
                new CustomEditDialog(PlayHistoryActivity.this).setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .setWidth(dm.widthPixels)
                        .setCancel("删除", (dialog) -> {
                            //清空播放历史
                            if (isSelected) {
                                SqlRepository.DeleteAllSearchHistory deleteAllSearchHistory = new SqlRepository.DeleteAllSearchHistory(playHistoryDao);
                                deleteAllSearchHistory.execute();
                                playHistories.clear();
                                playHistoryListAdapter.submitList(playHistories);
                                playHistoryListAdapter.notifyDataSetChanged();
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
                        dialog.setCancelText("删除" + "(" + playHistories.size() + ")");
                        dialog.setConfirmText("取消全选");
                    }

                }).show();
            }

        });
        recyclerView = findViewById(R.id.playhistoryrecycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        playHistoryListAdapter = new PlayHistoryListAdapter(this, new PlayHistoryListAdapter.CallBack() {
            @Override
            public void callback(VideoItem videoItem) {
                Intent intent = new Intent(PlayHistoryActivity.this, VideoPlayActivity.class);
                intent.putExtra("videodetail", videoItem);
                startActivity(intent);
            }

            @Override
            public void longcallback(int position) {
                //删除播放历史
                new CustomDialog(PlayHistoryActivity.this).setMessage("亲，确定删除吗？要三思啊!").setTittle("提示")
                        .setCancel("取消", (dialog) -> {
                        }).setConfirm("确定", (dialog) -> {
                    List<PlayHistory> currentList = playHistoryListAdapter.getCurrentList();
                    String videoTittle = currentList.get(position).getVideoTittle();
                    //删除数据库数据
                    new SqlRepository.DeleteSearchHistoryByVideoTittle(playHistoryDao).execute(videoTittle);
                    playHistories.remove(position);
                    playHistoryListAdapter.submitList(playHistories);
                    playHistoryListAdapter.notifyItemRemoved(position);
                    if (playHistories.size() == 0) {
                        editTextView.setVisibility(View.INVISIBLE);
                    }
                }).show();
            }
        });
        recyclerView.setAdapter(playHistoryListAdapter);
        //给listadapter设置数据
        playHistoryDao = SqlRepository.getPlayHistoryDao(this);
        SqlRepository.QueryAllAsyncTask queryAllAsyncTask = new SqlRepository.QueryAllAsyncTask(playHistoryDao);
        AsyncTask<Void, Void, List<PlayHistory>> execute = queryAllAsyncTask.execute();
        try {
            playHistories = execute.get();
            if (playHistories != null && playHistories.size() > 0) {
                //更新数据到adapter
                playHistoryListAdapter.submitList(playHistories);
            } else {
                editTextView.setVisibility(View.INVISIBLE);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}