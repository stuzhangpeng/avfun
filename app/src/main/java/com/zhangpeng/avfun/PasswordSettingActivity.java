package com.zhangpeng.avfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordSettingActivity extends AppCompatActivity {
    private EditText screctEditText;
    private EditText screctEditText1;
    private EditText screctEditText2;
    private EditText screctEditText3;
    private StringBuilder stringBuilder;
    private TextView textView;
    private String password;
    private Toolbar toolbar;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting);
        intent= getIntent();
        String action = intent.getStringExtra("action");
        stringBuilder=new StringBuilder();
        toolbar=findViewById(R.id.password_setting_toolbar);
        setSupportActionBar(toolbar);
        View back = toolbar.findViewById(R.id.password_setting_back_icon);
        back.setOnClickListener((view)->{
            finish();
        });
        TextView tittle = toolbar.findViewById(R.id.password_setting_tittle);
        if(!TextUtils.isEmpty(action)){
            tittle.setText("重设密码");
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        textView=findViewById(R.id.message_info);
        screctEditText=findViewById(R.id.setting_screct_editText);
        screctEditText1=findViewById(R.id.setting_screct_editText1);
        screctEditText2=findViewById(R.id.setting_screct_editText2);
        screctEditText3=findViewById(R.id.setting_screct_editText3);
        screctEditText.requestFocus();
        screctEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (TextUtils.isEmpty(text)){
                    editable.clear();
                    return;
                }
                if(isNumber(text)){
                    stringBuilder.append(text);
                    screctEditText1.requestFocus();
                }else{
                    Toast.makeText(PasswordSettingActivity.this,"亲,请输入数字",Toast.LENGTH_SHORT).show();
                    editable.clear();
                }
            }
        });
        screctEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (TextUtils.isEmpty(text)){
                    editable.clear();
                    return;
                }
                if(isNumber(text)){
                    stringBuilder.append(text);
                    screctEditText2.requestFocus();
                }else{
                    Toast.makeText(PasswordSettingActivity.this,"亲,请输入数字",Toast.LENGTH_SHORT).show();
                    editable.clear();
                }
            }
        });
        screctEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (TextUtils.isEmpty(text)){
                    editable.clear();
                    return;
                }
                if(isNumber(text)){
                    stringBuilder.append(text);
                    screctEditText3.requestFocus();
                }else{
                    Toast.makeText(PasswordSettingActivity.this,"亲,请输入数字",Toast.LENGTH_SHORT).show();
                    editable.clear();
                }
            }
        });
        screctEditText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (TextUtils.isEmpty(text)){
                    editable.clear();
                    return;
                }
                if(isNumber(text)){
                    stringBuilder.append(text);
                    if(password==null){
                        password = stringBuilder.toString();
                        textView.setText("确认密码");
                        screctEditText.requestFocus();
                        stringBuilder.delete(0,stringBuilder.length());
                        clearAllText();
                    }else{
                        //密码比对
                        if(password.equals(stringBuilder.toString())){
                            //密码保存
                            SharedPreferences sharedPreferences= getSharedPreferences("system_setting", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("app_password",password);
                            edit.commit();
                            finish();
                        }else{
                           // 重新设置密码
                            Toast.makeText(PasswordSettingActivity.this,"前后输入密码不一致，重新输入",Toast.LENGTH_SHORT).show();
                            textView.setText("重新设置密码");
                            password=null;
                            stringBuilder.delete(0,stringBuilder.length());
                            clearAllText();
                        }
                    }
                }else{
                    Toast.makeText(PasswordSettingActivity.this,"亲,请输入数字",Toast.LENGTH_SHORT).show();
                    editable.clear();
                }
            }
        });
    }
    public boolean isNumber(String text){
        try {
            Integer.parseInt(text);
            return  true;
        }catch ( NumberFormatException  ex){
            return false;
        }
    }
    void clearAllText(){
        screctEditText.setText("");
        screctEditText1.setText("");
        screctEditText2.setText("");
        screctEditText3.setText("");
    };
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
       return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        AvFunApplication application = (AvFunApplication) getApplication();
        if(application.isLocked()==true){
            Intent intent=new Intent(this,SecrectActivity.class);
            startActivity(intent);
        }
    }
}