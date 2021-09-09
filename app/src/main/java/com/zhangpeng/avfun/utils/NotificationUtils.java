package com.zhangpeng.avfun.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.android.volley.VolleyError;
import com.zhangpeng.avfun.R;
import com.zhangpeng.avfun.VideoPlayActivity;
import com.zhangpeng.avfun.javabean.VideoCategoryEntity;
import com.zhangpeng.avfun.javabean.VideoItem;
import com.zhangpeng.avfun.network.HttpUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * 通知工具类
 */
public class NotificationUtils {
    private static final String CHANNEL_ID = "video_push";
    private static final int NOTIFICATION_ID = 0xabc;
    private static final int FUllSCREEN_NOTIFICATION_ID = 0xabcd;
    private Context context;
    private NotificationManager notificationManager;
    private final String KEY_WORD = "巨乳";

    private void getChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public void sendFullScreenNotification() throws IOException {
        String url = decorateUrl(KEY_WORD);
        //发送请求获取数据
        HttpUtils.getSingleton(context, url, HttpUtils.RequestType.GET, new HttpUtils.CallBack() {
            @Override
            public void success(String response) {
                //解析数据
                List<VideoItem> mapList = JsonUtils.parseJson(response, VideoCategoryEntity.class).getData().getMapList();
                if (mapList != null && mapList.size() > 0) {
                    VideoItem videoItem = mapList.get(0);
                    //自定义notification布局
                    RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
                    notificationLayout.setTextViewText(R.id.notification_info_textview, videoItem.getVideoTittle());
                    //申请通道
                    getChannel();
                    //启动通知详情intent
                    Intent desIntent = new Intent(context, VideoPlayActivity.class);
                    desIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    desIntent.putExtra("videodetail", videoItem);
                    //创建返回栈
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntentWithParentStack(desIntent);
                    PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    new Thread(() -> {
                        Bitmap bitmap = getBitmapByUrl(videoItem.getImageurl());
                        if (bitmap != null) {
                            notificationLayout.setImageViewBitmap(R.id.notification_imageview, bitmap);
                        }
                        //开启视频推送通知
                        Notification fullscreenNotification = new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher_round)
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//消息和振动
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setCustomContentView(notificationLayout)
                                .setCustomBigContentView(notificationLayout)
                                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                                .setFullScreenIntent(pendingIntent, true)
                                .setTicker("ticker")
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .build();
                        //开启视频推送通知
                        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher_round)
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//消息和振动
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setCustomContentView(notificationLayout)
                                .setCustomBigContentView(notificationLayout)
                                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                                .setTicker("ticker")
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .build();
                        //发送通知
                        notificationManager = context.getSystemService(NotificationManager.class);
                        notificationManager.notify(FUllSCREEN_NOTIFICATION_ID, fullscreenNotification);
                        notificationManager.notify(NOTIFICATION_ID, notification);
                        try {
                            Thread.currentThread().sleep(5000L);
                            notificationManager.cancel(FUllSCREEN_NOTIFICATION_ID);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }

            @Override
            public void error(VolleyError error) {
                Log.e("error", "视频推送请求数据失败");
            }
        }).httpRequest();
    }

    private String decorateUrl(String keywords) {
        int pageNumber = new Random().nextInt(1000) + 1;
        String url = UrlUtils.getUrlByType(UrlUtils.RequireType.Search, keywords, 1, pageNumber);
        return url;
    }

    public NotificationUtils(Context context) {
        this.context = context;
    }

    public void sendNotification() {
        String url = decorateUrl(KEY_WORD);
        //发送请求获取数据
        HttpUtils.getSingleton(context, url, HttpUtils.RequestType.GET, new HttpUtils.CallBack() {
            @Override
            public void success(String response) {
                //解析数据
                List<VideoItem> mapList = JsonUtils.parseJson(response, VideoCategoryEntity.class).getData().getMapList();
                if (mapList != null && mapList.size() > 0) {
                    VideoItem videoItem = mapList.get(0);
                    //自定义notification布局
                    RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
                    notificationLayout.setTextViewText(R.id.notification_info_textview, videoItem.getVideoTittle());
                    //申请通道
                    getChannel();
                    //启动通知详情intent
                    Intent desIntent = new Intent(context, VideoPlayActivity.class);
                    desIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    desIntent.putExtra("videodetail", videoItem);
                    //创建返回栈
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntentWithParentStack(desIntent);
                    PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    //开启视频推送通知
                    new Thread(() -> {
                        Bitmap bitmap = getBitmapByUrl(videoItem.getImageurl());
                        if (bitmap != null) {
                            notificationLayout.setImageViewBitmap(R.id.notification_imageview, bitmap);
                        }
                        //开启视频推送通知
                        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher_round)
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//消息和振动
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setCustomContentView(notificationLayout)
                                .setCustomBigContentView(notificationLayout)
                                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                                .setTicker("ticker")
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .build();
                        //发送通知
                        notificationManager = context.getSystemService(NotificationManager.class);
                        notificationManager.notify(NOTIFICATION_ID, notification);
                    }).start();
                }
            }

            @Override
            public void error(VolleyError error) {
                Log.e("error", "error");
            }
        }).httpRequest();
    }

    private Bitmap getBitmapByUrl(String url) {
        try {
            URL urs = new URL(url);
            Bitmap bitmap = BitmapFactory.decodeStream(urs.openStream());
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
