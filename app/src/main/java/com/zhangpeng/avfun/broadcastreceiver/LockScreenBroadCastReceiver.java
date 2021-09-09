package com.zhangpeng.avfun.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhangpeng.avfun.AvFunApplication;

public class LockScreenBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
           AvFunApplication applicationContext = (AvFunApplication) context.getApplicationContext();
           applicationContext.setLocked(true);
       }
    }
}