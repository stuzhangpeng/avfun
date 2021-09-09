package com.zhangpeng.avfun.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.zhangpeng.avfun.entity.PlayHistory;
import com.zhangpeng.avfun.javabean.VideoItem;
import com.zhangpeng.avfun.utils.TimeUtils;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class PlayHistoryListAdapter extends ListAdapter< PlayHistory, PlayHistoryListAdapter.PlayHistoryListViewHolder> {
    private Context context;
    private PlayHistoryListAdapter.CallBack callBack;
    private static DiffUtil.ItemCallback<PlayHistory> diffCallback = new PlayHistoryListAdapter .PlayVideoListCallback ();
    public PlayHistoryListAdapter( Context context, PlayHistoryListAdapter.CallBack callBack) {
        super(diffCallback);
        this.context = context;
        this.callBack=callBack;
    }
    @NonNull
    @Override
    public PlayHistoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayHistoryListAdapter.PlayHistoryListViewHolder(LayoutInflater.from(context).inflate(R.layout.playhistoryitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlayHistoryListViewHolder holder, int position) {
       PlayHistory item = getItem(position);
        if (item!=null){
            holder.videoTittle.setText(item.getVideoTittle());
            holder.historyProgess.setText("上次观看到"+TimeUtils.parseMillTime(item.getPlaytimeProgess()));
            //image闪动
            holder.shimmerLayout.setShimmerColor(0x55FFFFFF);
            holder.shimmerLayout.setShimmerAngle(0);
            holder.shimmerLayout.startShimmerAnimation();
            //加载图片到imageview
            Glide.with(holder.itemView).load(item.getImageurl()).placeholder(R.drawable.placeholderimage)
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
                    callBack.callback(item);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    callBack.longcallback(holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public class PlayHistoryListViewHolder extends RecyclerView.ViewHolder {
        private TextView videoTittle;
        private TextView historyProgess;
        private ImageView imageView;
        private ShimmerLayout shimmerLayout;
        public PlayHistoryListViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTittle = itemView.findViewById(R.id.playhistoryvideotittle);
            historyProgess = itemView.findViewById(R.id.playhistorytextView);
            imageView = itemView.findViewById(R.id.playhistoryimage);
            shimmerLayout = itemView.findViewById(R.id.shimerlayout);
        }
    }
    private static class PlayVideoListCallback extends DiffUtil.ItemCallback<PlayHistory> {


        @Override
        public boolean areItemsTheSame(@NonNull PlayHistory oldItem, @NonNull PlayHistory newItem) {
            return (oldItem == newItem);
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull PlayHistory oldItem, @NonNull PlayHistory newItem) {
            if(oldItem.getVideoTittle().equals(newItem.getVideoTittle())&&oldItem.getPlaytimeProgess().equals(newItem.getPlaytimeProgess())) {
                return true;
            }
            return false;
        }
    }
    public  interface CallBack{
        void callback(VideoItem videoItem);
        void longcallback(int position);
    }
}
