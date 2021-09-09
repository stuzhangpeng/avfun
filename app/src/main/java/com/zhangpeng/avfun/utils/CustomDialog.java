package com.zhangpeng.avfun.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zhangpeng.avfun.R;
public class CustomDialog extends Dialog implements View.OnClickListener {
    private TextView dialogTittle;
    private TextView dialogMessage;
    private TextView dialogConfirm;
    private TextView dialogCancel;
    private String tittle, message, cancel, confirm;
    private IOnCancelListener onCacenlListener;
    private IOnConfirmListener onConfirmListener;
    private int layoutResourceId;
    private Integer gravity;
    public CustomDialog(@NonNull Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(gravity!=null){
            Window window = getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.gravity= gravity;
            window.setAttributes(attributes);
        }
        if(layoutResourceId>0){
            setContentView(layoutResourceId);
        }else{
            setContentView(R.layout.custom_dialog);
        }
        dialogTittle = findViewById(R.id.dialog_tittle);
        dialogMessage = findViewById(R.id.dialog_message);
        dialogConfirm = findViewById(R.id.dialog_confirm);
        dialogCancel = findViewById(R.id.dialog_cancel);
        if (!TextUtils.isEmpty(tittle)) {
            dialogTittle.setText(tittle);
        }
        if (!TextUtils.isEmpty(message)) {
            dialogMessage.setText(message);
        }
        if (!TextUtils.isEmpty(confirm)) {
            dialogConfirm.setText(confirm);
        }
        if (!TextUtils.isEmpty(cancel)) {
            dialogCancel.setText(cancel);
        }
        dialogConfirm.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);
    }
    public CustomDialog setTittle(String tittle) {
        this.tittle = tittle;
        return this;
    }

    public CustomDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public CustomDialog setCancel(String cancel, IOnCancelListener onCancelListener) {
        this.cancel = cancel;
        this.onCacenlListener = onCancelListener;
        return this;
    }

    public CustomDialog setConfirm(String confirm, IOnConfirmListener onConfirmListener) {
        this.confirm = confirm;
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                if (onCacenlListener != null)
                    onCacenlListener.onCancel(this);
                dismiss();
                break;
            case R.id.dialog_confirm:
                if (onConfirmListener != null)
                    onConfirmListener.onConfirm(this);
                dismiss();
                break;
        }
    }

    public interface IOnCancelListener {
        void onCancel(CustomDialog dialog);
    }

    public interface IOnConfirmListener {
        void onConfirm(CustomDialog dialog);
    }

    public CustomDialog setLayoutResourceId(int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
        return this;
    }

    public CustomDialog setGravity(Integer gravity) {
        this.gravity = gravity;
        return this;
    }
}
