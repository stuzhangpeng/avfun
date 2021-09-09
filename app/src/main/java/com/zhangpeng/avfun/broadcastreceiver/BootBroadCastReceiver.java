package com.zhangpeng.avfun.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zhangpeng.avfun.utils.NotificationUtils;

/**
 * 监听开机广播，发出通知
 */
public class BootBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Toast.makeText(context,action,Toast.LENGTH_LONG).show();
            NotificationUtils notificationUtils=new NotificationUtils(context);
            notificationUtils.sendNotification();
        }
    }
}