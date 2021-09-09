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
import com.zhangpeng.avfun.entity.DownLoadRecord;
import com.zhangpeng.avfun.utils.TimeUtils;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class CatchListAdapter  extends ListAdapter<DownLoadRecord,CatchListAdapter.CatchListViewHolder> {
    private Context context;
    private CatchListAdapter.CallBack callBack;
    private static DiffUtil.ItemCallback<DownLoadRecord> diffCallback = new CatchListAdapter  .CatchListCallback ();
    public CatchListAdapter ( Context context, CatchListAdapter.CallBack callBack) {
        super(diffCallback);
        this.context = context;
        this.callBack=callBack;
    }
    public  interface CallBack{
        void callback(DownLoadRecord downLoadRecord);
        void longcallback(int position);
    }

    @NonNull
    @Override
    public CatchListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatchListAdapter.CatchListViewHolder (LayoutInflater.from(context).inflate(R.layout.catchlistitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CatchListViewHolder holder, int position) {
        DownLoadRecord item = getItem(position);
        if (item!=null){
            holder.tittle.setText(item.getVideoTittle());
            holder.downloadTime.setText("下载时间:"+ TimeUtils.formartTimesToDate(item.getDownloadTime()));
            //image闪动
            holder.shimmerLayout.setShimmerColor(0x55FFFFFF);
            holder.sizeText.setText("文件大小:"+item.getFilesize());
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
            holder.itemView.setOnLongClickListener((view)->{
                callBack.longcallback(holder.getLayoutPosition());
                return true;
            });
        }
    }

    public class CatchListViewHolder extends RecyclerView.ViewHolder {
        private TextView tittle;
        private TextView downloadTime;
        private TextView sizeText;
        private ImageView imageView;
        private ShimmerLayout shimmerLayout;
        public CatchListViewHolder(@NonNull View itemView) {
            super(itemView);
            tittle= itemView.findViewById(R.id.catchtittletextview);
             sizeText = itemView.findViewById(R.id.catchsizetextview);
             downloadTime=itemView.findViewById(R.id.catchtimetextView);
            imageView = itemView.findViewById(R.id.catchimageView);
            shimmerLayout = itemView.findViewById(R.id.catchshimerlayout);
        }

    }
    private static class CatchListCallback extends DiffUtil.ItemCallback<DownLoadRecord> {


        @Override
        public boolean areItemsTheSame(@NonNull DownLoadRecord oldItem, @NonNull DownLoadRecord newItem) {
            return (oldItem == newItem);
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull DownLoadRecord oldItem, @NonNull DownLoadRecord newItem) {
            if(oldItem.getVideoTittle().equals(newItem.getVideoTittle())) {
                return true;
            }
            return false;
        }
    }
}
