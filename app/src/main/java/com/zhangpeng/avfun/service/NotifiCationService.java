package com.zhangpeng.avfun.service;

import android.app.IntentService;
import android.content.Intent;

import com.zhangpeng.avfun.utils.NotificationUtils;

import java.io.IOException;

public class NotifiCationService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public NotifiCationService() {
        super("notification");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            NotificationUtils notificationUtils=new NotificationUtils(this);
            notificationUtils.sendFullScreenNotification();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}