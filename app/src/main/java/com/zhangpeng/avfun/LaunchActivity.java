package com.zhangpeng.avfun;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zhangpeng.avfun.service.NotifiCationService;

/**
 * 启动界面
 */
public class LaunchActivity extends Activity {
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);
        constraintLayout=findViewById(R.id.launcher);
        showAnimation();
        Integer time = 4000;    //设置等待时间，单位为毫秒
        Handler handler = new Handler();
        SharedPreferences sharedPreferences = getSharedPreferences("system_setting", Context.MODE_PRIVATE);
        String app_password = sharedPreferences.getString("app_password", "");

        //当计时结束时，跳转至主界面
        handler.postDelayed(()->{
            AvFunApplication application = (AvFunApplication) getApplication();
            if(application.isLocked()==true){
                Intent intent=new Intent(LaunchActivity.this, SecrectActivity.class);
                intent.putExtra("boot","yes");
                startActivity(intent);
            } else{
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
            }
            overridePendingTransition(R.anim.rightin,R.anim.leftout);
            LaunchActivity.this.finish();
            }, time);
    }
    void showAnimation(){
        RotateAnimation rotateAnimation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(2000);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(2000);
        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        constraintLayout.startAnimation(animationSet);
    };


}