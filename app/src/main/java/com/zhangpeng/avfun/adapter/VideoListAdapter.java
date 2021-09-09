package com.zhangpeng.avfun.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhangpeng.avfun.R;
import com.zhangpeng.avfun.VideoPlayActivity;
import com.zhangpeng.avfun.javabean.VideoItem;
import io.supercharge.shimmerlayout.ShimmerLayout;
/**
 * 视频列表适配器
 */
public class VideoListAdapter extends PagedListAdapter<VideoItem, VideoListAdapter.VideoListViewHodler> {
    private Context context;
    private static DiffUtil.ItemCallback<VideoItem> diffCallback = new VideoListAdapter.VideoListItemCallback();
    public VideoListAdapter(Context context) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public VideoListViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //返回view holder
        return new VideoListViewHodler(LayoutInflater.from(context).inflate(R.layout.videolistitemlayout, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull VideoListViewHodler holder, int position) {
        if (getItem(position)!=null){
            holder.textView.setText(getItem(position).getVideoTittle());
            //image闪动
            holder.shimmerLayout.setShimmerColor(0x55FFFFFF);
            holder.shimmerLayout.setShimmerAngle(0);
            holder.shimmerLayout.startShimmerAnimation();
            //加载图片到imageview
            Glide.with(holder.itemView).load(getItem(position).getImageurl()).placeholder(R.drawable.placeholderimage)
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
                    Intent intent=new Intent(context, VideoPlayActivity.class);
                    intent.putExtra("videodetail",getItem(position));
                    context.startActivity(intent);
                }
            });
        }
    }
    /**
     * 视频列表 viewholder
     */
    class VideoListViewHodler extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private ShimmerLayout shimmerLayout;
        public VideoListViewHodler(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView4);
            shimmerLayout = itemView.findViewById(R.id.shimerlayout);
        }
    }

    /**
     * 回调方法
     */
    private static class VideoListItemCallback extends DiffUtil.ItemCallback<VideoItem> {
        @Override
        public boolean areItemsTheSame(@NonNull VideoItem oldItem, @NonNull VideoItem newItem) {
            return oldItem.getVideoTittle() == newItem.getVideoTittle();
        }
        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull VideoItem oldItem, @NonNull VideoItem newItem) {
            return (oldItem == newItem);
        }
    }
}
