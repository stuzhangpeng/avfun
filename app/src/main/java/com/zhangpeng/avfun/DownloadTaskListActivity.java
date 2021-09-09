package com.zhangpeng.avfun;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangpeng.avfun.adapter.DownloadAdapter;
import com.zhangpeng.avfun.dao.DownloadRecoreListDao;
import com.zhangpeng.avfun.entity.DownLoadRecord;
import com.zhangpeng.avfun.javabean.DownloadTask;
import com.zhangpeng.avfun.javabean.VideoItem;
import com.zhangpeng.avfun.repository.SqlRepository;
import com.zhangpeng.avfun.utils.FullScreenUtils;
import com.zhangpeng.avfun.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import jaygoo.library.m3u8downloader.M3U8Downloader;
import jaygoo.library.m3u8downloader.M3U8DownloaderConfig;
import jaygoo.library.m3u8downloader.OnM3U8DownloadListener;
import jaygoo.library.m3u8downloader.bean.M3U8Task;

/**
 * 下载任务列表
 */
public class DownloadTaskListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DownloadAdapter downloadAdapter;
    private List<DownloadTask> listTask;
    private VideoItem videoItem;
    private String dirPath;
    private DownloadRecoreListDao downloadRecoreListDao;
    private Toolbar toolbar;
    private TextView editTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_task_list);
        //设置actionbar
        toolbar = findViewById(R.id.downloadtask_list_toolbar);
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
        View backView = toolbar.findViewById(R.id.downloadtask_list_back_icon);
        //处理返回
        backView.setOnClickListener((view) -> {
            finish();
        });
        editTextView = toolbar.findViewById(R.id.downloadtask_list_edit);
        recyclerView = findViewById(R.id.downloadrecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        downloadAdapter = new DownloadAdapter(this, new DownloadAdapter.CallBack() {
            @Override
            public void callback(VideoItem item) {
                if (M3U8Downloader.getInstance().checkM3U8IsExist(item.getPlayUrl())) {
                    Toast.makeText(getApplicationContext(), "本地文件已下载，正在播放中！！！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DownloadTaskListActivity.this, VideoPlayActivity.class);
                    intent.putExtra("videodetail", item);
                    startActivity(intent);
                }
            }

            @Override
            public void pause(VideoItem item) {
                //暂停下载
                M3U8Downloader.getInstance().pause(item.getPlayUrl());
            }

            @Override
            public void restartDownload(VideoItem item) {
                M3U8Downloader.getInstance().download(item.getPlayUrl());
            }
        });
        recyclerView.setAdapter(downloadAdapter);
        videoItem = (VideoItem) getIntent().getSerializableExtra("videodetail");
        String url = videoItem.getPlayUrl();
        listTask = new ArrayList<DownloadTask>();
        M3U8Task m3U8Task = new M3U8Task(url);
        DownloadTask downloadTask = new DownloadTask(m3U8Task, videoItem);
        listTask.add(downloadTask);
        downloadAdapter.submitList(listTask);
        //开始下载
        downloadM3U8Video(url);
        downloadRecoreListDao = SqlRepository.getDownloadRecoreListDao(this);
    }

    /**
     * m3u8视频下载
     */
    public void downloadM3U8Video(String url) {
        //存储目录
        File dir = new File(Environment.getExternalStorageDirectory(), "avfun/videodownloads");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dirPath = dir.getAbsolutePath();
        //下载配置
        M3U8DownloaderConfig
                .build(getApplicationContext())
                .setSaveDir(dir.getAbsolutePath())
                .setConnTimeout(10000)
                .setReadTimeout(10000)
                .setThreadCount(3);
        //下载器
        M3U8Downloader downloader = M3U8Downloader.getInstance();
        //下载监听器
        OnM3U8DownloadListener downloadListener = new OnM3U8DownloadListener() {
            @Override
            public void onDownloadItem(M3U8Task task, long itemFileSize, int totalTs, int curTs) {
                super.onDownloadItem(task, itemFileSize, totalTs, curTs);
            }

            @Override
            public void onDownloadSuccess(M3U8Task task) {
                super.onDownloadSuccess(task);
                downloadAdapter.notifyDataChanged(task);
                String saveDir = dirPath + File.separator + StringUtils.getPath(videoItem.getPlayUrl());
                mergeTS(saveDir, task);
            }

            @Override
            public void onDownloadPending(M3U8Task task) {
                super.onDownloadPending(task);
                notifyChanged(task);
            }

            @Override
            public void onDownloadPause(M3U8Task task) {
                super.onDownloadPause(task);
                notifyChanged(task);
            }

            @Override
            public void onDownloadProgress(final M3U8Task task) {
                super.onDownloadProgress(task);
                notifyChanged(task);
            }

            @Override
            public void onDownloadPrepare(final M3U8Task task) {
                super.onDownloadPrepare(task);
                notifyChanged(task);
            }

            @Override
            public void onDownloadError(final M3U8Task task, Throwable errorMsg) {
                super.onDownloadError(task, errorMsg);
                notifyChanged(task);
            }

        };
        downloader.setOnM3U8DownloadListener(downloadListener);
        downloader.download(url);
    }

    //异步执行通知recycleview item发生改变
    private void notifyChanged(final M3U8Task task) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadAdapter.notifyDataChanged(task);
            }
        });

    }

    //异步执行ts文件合并
    private void mergeTS(String dir, M3U8Task m3U8Task) {
        new Thread(() -> {
            File file = new File(dir);
            File[] files = file.listFiles(new TSFileNameFilter());
            if (files != null && files.length > 0) {
                List<File> fileList = Arrays.asList(files);
                Collections.sort(fileList, new FileComprator());
                mergeFile(fileList, dir, videoItem.getVideoTittle() + ".ts");
                deleteFiles(fileList);
                String saveDir = dirPath + File.separator + videoItem.getVideoTittle();
                File dest = new File(saveDir);
                renameFile(file, dest);
                //保存到数据库
                DownloadTask downloadTaskByM3U8Task = findDownloadTaskByM3U8Task(m3U8Task);
                System.out.println(downloadTaskByM3U8Task.getVideoItem());
                if (downloadTaskByM3U8Task != null) {
                    DownLoadRecord downLoadRecord = initDownLoadRecord(downloadTaskByM3U8Task, m3U8Task);
                    //插入数据库
                    saveDownloadFileRecord(downLoadRecord);

                }
            }
        }).start();
    }

    //文件过滤器
    private class TSFileNameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith("ts");
        }
    }

    private class FileComprator implements Comparator<File> {
        @Override
        public int compare(File file1, File file2) {
            return file1.getName().compareTo(file2.getName());
        }
    }

    private void mergeFile(List<File> listFiles, String dir, String mergeFileName) {
        byte[] byteArray = new byte[1024];
        List<InputStream> streams = new ArrayList<>();
        File mergeFile = new File(dir, mergeFileName);
        try {
            if (!mergeFile.exists()) {
                mergeFile.createNewFile();
            }
            for (int i = 0; i < listFiles.size(); i++) {
                FileInputStream fileInputStream = new FileInputStream(listFiles.get(i));
                streams.add(fileInputStream);
            }
            Enumeration<InputStream> enumeration = Collections.enumeration(streams);
            SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
            FileOutputStream fileOutputStream = new FileOutputStream(mergeFile);
            int len;
            while ((len = sequenceInputStream.read(byteArray)) != -1) {
                fileOutputStream.write(byteArray, 0, len);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //删除分片文件
    private void deleteFiles(List<File> files) {
        for (int i = 0; i < files.size(); i++) {
            files.get(i).delete();
        }
    }

    //重命名目录
    private void renameFile(File srcFile, File desFile) {
        srcFile.renameTo(desFile);
    }

    //下载完成保存到数据库
    private void saveDownloadFileRecord(DownLoadRecord downLoadRecord) {
        SqlRepository.DownloadRecoreListInsertAsyncTask insertTask = new SqlRepository.DownloadRecoreListInsertAsyncTask(downloadRecoreListDao);
        insertTask.execute(downLoadRecord);
    }

    private DownloadTask findDownloadTaskByM3U8Task(M3U8Task m3U8Task) {
        for (DownloadTask downloadTask : listTask) {
            if (downloadTask.getM3U8Task().equals(m3U8Task)) {
                return downloadTask;
            }
        }
        return null;
    }

    private DownLoadRecord initDownLoadRecord(DownloadTask downloadTask, M3U8Task m3U8Task) {
        VideoItem videoItem = downloadTask.getVideoItem();
        DownLoadRecord downLoadRecord = new DownLoadRecord();
        downLoadRecord.initData(videoItem);
        downLoadRecord.setFilesize(m3U8Task.getFormatTotalSize());
        downLoadRecord.setFilepath(dirPath + File.separator + videoItem.getVideoTittle());
        downLoadRecord.setDownloadTime(new Date().getTime());
        return downLoadRecord;
    }
    protected void onResume() {
        super.onResume();
        AvFunApplication application = (AvFunApplication) getApplication();
        if(application.isLocked()==true){
            Intent intent=new Intent(this,SecrectActivity.class);
            startActivity(intent);
        }
    }
}
