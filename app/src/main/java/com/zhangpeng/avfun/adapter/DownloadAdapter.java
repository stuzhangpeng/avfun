package com.zhangpeng.avfun.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhangpeng.avfun.R;
import com.zhangpeng.avfun.javabean.DownloadTask;
import com.zhangpeng.avfun.javabean.VideoItem;

import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;
import jaygoo.library.m3u8downloader.M3U8Downloader;
import jaygoo.library.m3u8downloader.bean.M3U8Task;
import jaygoo.library.m3u8downloader.bean.M3U8TaskState;

public class DownloadAdapter extends ListAdapter<DownloadTask,  DownloadAdapter.DownloadListViewHolder> {
    private Context context;
    private DownloadAdapter.CallBack callBack;
    private static DiffUtil.ItemCallback<DownloadTask> diffCallback = new  DownloadAdapter .DownloadListCallback ();
    public  DownloadAdapter ( Context context,DownloadAdapter.CallBack callBack) {
        super(diffCallback);
        this.context = context;
        this.callBack=callBack;
    }

    @NonNull
    @Override
    public DownloadAdapter.DownloadListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DownloadAdapter.DownloadListViewHolder (LayoutInflater.from(context).inflate(R.layout.downloadtaskitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.DownloadListViewHolder holder, int position) {
        DownloadTask tasks = getItem(position);
        M3U8Task item = tasks.getM3U8Task();
        VideoItem videoItem = tasks.getVideoItem();
        if (item!=null&&videoItem!=null){
            holder.videoTittle.setText(videoItem.getVideoTittle());
            setStateText(holder.tittle,item);
            holder.progressBar.setMax(100);
            setProgressText(holder.progressBar,holder.speedOrSizeTextView,item);
            //image闪动
            holder.shimmerLayout.setShimmerColor(0x55FFFFFF);
            holder.shimmerLayout.setShimmerAngle(0);
            holder.shimmerLayout.startShimmerAnimation();
            //加载图片到imageview
            Glide.with(holder.itemView).load(videoItem.getImageurl()).placeholder(R.drawable.placeholderimage)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //停止闪动
                            if (holder.shimmerLayout != null) {
                                holder.shimmerLayout.stopShimmerAnimation();
                            }
                            return false;
                        }
                    })
                    .into(holder.imageView);
            //视频点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (item.getState()) {
                        case M3U8TaskState.SUCCESS:
                            //下载成功，播放视频
                            callBack.callback(videoItem);
                            break;
                        case M3U8TaskState.DOWNLOADING:
                            //暂停下载
                            callBack.pause(videoItem);
                            break;
                        case M3U8TaskState.PAUSE:
                            //重新开始下载
                            callBack.restartDownload(videoItem);
                            break;
                        case M3U8TaskState.ERROR:
                            //重新开始下载
                            callBack.restartDownload(videoItem);
                            break;
                        case M3U8TaskState.ENOSPC:
                            //存储空间不足
                            Toast.makeText(context,"存储空间不足",Toast.LENGTH_SHORT).show();
                            break;
                        default :
                            //暂停下载
                            callBack.pause(videoItem);
                            break;
                    }
                }
            });
        }

    }

    public class DownloadListViewHolder extends RecyclerView.ViewHolder {
        private TextView videoTittle;
        private TextView tittle;
        private ProgressBar progressBar;
        private ImageView imageView;
        private ShimmerLayout shimmerLayout;
        private TextView speedOrSizeTextView;
        public DownloadListViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTittle = itemView.findViewById(R.id.downloadtittletextView);
            tittle = itemView.findViewById(R.id.downloadtextview);
            imageView = itemView.findViewById(R.id.downloadimageView);
            shimmerLayout = itemView.findViewById(R.id.downloadshimerlayout);
            progressBar=itemView.findViewById(R.id.downloadprogessbar);
            speedOrSizeTextView=itemView.findViewById(R.id.speedorsizetextview);
        }

    }
    private static class DownloadListCallback extends DiffUtil.ItemCallback<DownloadTask> {
        @Override
        public boolean areItemsTheSame(@NonNull DownloadTask oldItem, @NonNull DownloadTask newItem) {
            return (oldItem == newItem);
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull DownloadTask oldItem, @NonNull DownloadTask newItem) {
          return oldItem.getM3U8Task().getProgress()==newItem.getM3U8Task().getProgress();
        }
    }
    public  interface CallBack{
        void callback(VideoItem item);
        void pause(VideoItem item);
        void restartDownload(VideoItem item);
    }
    private void setProgressText(ProgressBar progressBar,TextView speedOrSize, M3U8Task task) {
        switch (task.getState()) {
            case M3U8TaskState.DOWNLOADING:
                speedOrSize.setText("进度：" + String.format("%.1f ",task.getProgress() * 100)+"% 速度:"+task.getFormatSpeed());
                progressBar.setProgress((int)(task.getProgress()*100));
                break;
            case M3U8TaskState.SUCCESS:
                progressBar.setProgress((int)(task.getProgress()*100));
                speedOrSize.setText(task.getFormatTotalSize());
                break;
            case M3U8TaskState.PAUSE:
                progressBar.setProgress((int)(task.getProgress()*100));
                speedOrSize.setText("进度：" + String.format("%.1f ",task.getProgress() * 100)+ "%" + task.getFormatTotalSize());
                break;
            default:
                progressBar.setProgress((int)(task.getProgress()*100));
                speedOrSize.setText("");
                break;
        }
    }

    private void setStateText(TextView tittleTextView, M3U8Task task){
        if (M3U8Downloader.getInstance().checkM3U8IsExist(task.getUrl())){
            tittleTextView.setText("已下载");
            return;
        }
        switch (task.getState()){
            case M3U8TaskState.PENDING:
                tittleTextView.setText("等待中");
                break;
            case M3U8TaskState.DOWNLOADING:
                tittleTextView.setText("正在下载");
                break;
            case M3U8TaskState.ERROR:
                tittleTextView.setText("下载异常，点击重试");
                break;
            //关于存储空间不足测试方案，参考 http://blog.csdn.net/google_acmer/article/details/78649720
            case M3U8TaskState.ENOSPC:
                tittleTextView.setText("存储空间不足");
                break;
            case M3U8TaskState.PREPARE:
                tittleTextView.setText("准备中");
                break;
            case M3U8TaskState.SUCCESS:
                tittleTextView.setText("下载完成");
                break;
            case M3U8TaskState.PAUSE:
                tittleTextView.setText("暂停下载");
                break;
            default:tittleTextView.setText("未下载");
                break;
        }
    }
    public void notifyDataChanged(M3U8Task m3U8Task){
        List<DownloadTask> currentList =getCurrentList();
        if (currentList!=null&&currentList.size()>0){
            for(DownloadTask downloadTask:currentList){
                if(downloadTask.getM3U8Task().equals(m3U8Task)){
                    downloadTask.setM3U8Task(m3U8Task);
                    notifyDataSetChanged();
                }
            }
        }

    }
}
