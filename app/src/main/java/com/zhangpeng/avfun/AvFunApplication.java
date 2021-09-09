package com.zhangpeng.avfun;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class AvFunApplication extends Application {
    private boolean isLocked=false;
    @Override
    public void onCreate(){
        SharedPreferences sharedPreferences = getSharedPreferences("system_setting", Context.MODE_PRIVATE);
        String app_password = sharedPreferences.getString("app_password", "");
        if(!TextUtils.isEmpty(app_password)){
            isLocked=true;
        }
        super.onCreate();
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
