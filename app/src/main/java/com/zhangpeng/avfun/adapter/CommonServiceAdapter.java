package com.zhangpeng.avfun.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhangpeng.avfun.R;
import java.util.List;
public class CommonServiceAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mTextList;
    private List<Integer> mIconList;
    private LayoutInflater mLayoutInflater;
    public CommonServiceAdapter(Context mContext, List<String> mTextList, List<Integer> mIconList) {
        this.mContext = mContext;
        this.mTextList = mTextList;
        this.mIconList = mIconList;
        this.mLayoutInflater=LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mTextList.size()-1;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       CustomViewHolder customViewHolder=null;
        if (convertView==null){
            customViewHolder=new CustomViewHolder();
            convertView= mLayoutInflater.inflate(R.layout.common_service_item,null);
            customViewHolder.textView = convertView.findViewById(R.id.common_service_textview);
            convertView.setTag(customViewHolder);
        }else{
            customViewHolder= (CustomViewHolder) convertView.getTag();
        }
        if(position==7){
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("system_setting", Context.MODE_PRIVATE);
            String app_password = sharedPreferences.getString("app_password", "");
            if(!TextUtils.isEmpty(app_password)){
                position++;
            }
        }
        Drawable drawable = mContext.getResources().getDrawable(mIconList.get(position), null);
        customViewHolder.textView.setText(mTextList.get(position));
        customViewHolder.textView.setCompoundDrawablePadding(15);
        customViewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
        return convertView;
    }
    static class CustomViewHolder{
        public TextView textView;
    }
}
