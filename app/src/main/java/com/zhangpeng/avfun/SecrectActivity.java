package com.zhangpeng.avfun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class SecrectActivity extends AppCompatActivity {
    private EditText screctEditText;
    private EditText screctEditText1;
    private EditText screctEditText2;
    private EditText screctEditText3;
    private StringBuilder stringBuilder;
    private SharedPreferences sharedPreferences;
    private String appPassword;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secrect);
        stringBuilder=new StringBuilder();
        toolbar=findViewById(R.id.secrect_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("输入密码");
        Intent intent = getIntent();
        String boot = intent.getStringExtra("boot");
        sharedPreferences = getSharedPreferences("system_setting", Context.MODE_PRIVATE);
        if (sharedPreferences!=null){
            appPassword = sharedPreferences.getString("app_password", "");
        }
        screctEditText=findViewById(R.id.screct_editText);
        screctEditText1=findViewById(R.id.screct_editText1);
        screctEditText2=findViewById(R.id.screct_editText2);
        screctEditText3=findViewById(R.id.screct_editText3);
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
                  Toast.makeText(SecrectActivity.this,"亲,请输入数字",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SecrectActivity.this,"亲,请输入数字",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SecrectActivity.this,"亲,请输入数字",Toast.LENGTH_SHORT).show();
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
                    String password = stringBuilder.toString();
                    if (password.equals(appPassword)){
                        AvFunApplication application = (AvFunApplication) getApplication();
                        application.setLocked(false);
                        if (!TextUtils.isEmpty(boot)){
                            //进入mainactivity
                            Intent intent=new Intent(SecrectActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            finish();
                        }
                    }else{
                        clearAllText();
                        stringBuilder.delete(0,stringBuilder.length());
                        Toast.makeText(SecrectActivity.this,"亲，密码输入错误！",Toast.LENGTH_SHORT).show();
                        screctEditText.requestFocus();
                    }
                }else{
                    Toast.makeText(SecrectActivity.this,"亲,请输入数字",Toast.LENGTH_SHORT).show();
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
   }
}