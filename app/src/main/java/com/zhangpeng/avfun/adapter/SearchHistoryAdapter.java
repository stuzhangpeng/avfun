package com.zhangpeng.avfun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangpeng.avfun.R;
import com.zhangpeng.avfun.entity.SearchHistory;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>  {
    private Context context;
    private List<SearchHistory> list;
    private  CallBack callBack;

    public List<SearchHistory> getList() {
        return list;
    }

    public void setList(List<SearchHistory> list) {
        this.list = list;
    }

    public SearchHistoryAdapter(Context context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }
    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.searchhistoryitem,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder holder, int position) {
              if(list!=null&&list.size()>0){
                  holder.button.setText(list.get(position).getKeyword());
                  holder.button.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          callBack.callback(list.get(position).getKeyword());
                      }
                  });
                  holder.button.setOnLongClickListener(new View.OnLongClickListener() {
                      @Override
                      public boolean onLongClick(View view) {
                          callBack.longcallback(position);
                          return true;
                      }
                  });
              }
    }

    @Override
    public int getItemCount() {
        if(list!=null&&list.size()>0){
            return list.size();
        }
        return 0;
    }
    class SearchHistoryViewHolder extends RecyclerView.ViewHolder {
        private Button button;
        public SearchHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
           button = itemView.findViewById(R.id.searchkeyword_button);
        }
    }
    public interface CallBack{
        void callback(String text);
        void longcallback(int position);
    }
}
