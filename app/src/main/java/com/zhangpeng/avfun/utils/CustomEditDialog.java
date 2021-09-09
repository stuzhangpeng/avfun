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

public class CustomEditDialog extends Dialog implements View.OnClickListener {
    public CustomEditDialog(@NonNull Context context) {
        super(context);
    }

    private TextView dialogConfirm;
    private TextView dialogCancel;
    private String cancel, confirm;
    private IOnCancelListener onCacenlListener;
    private IOnConfirmListener onConfirmListener;
    private int layoutResourceId;
    private Integer gravity;
    private Integer width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (layoutResourceId > 0) {
            setContentView(layoutResourceId);
        } else {
            setContentView(R.layout.custom_edit_dialog);
        }
        if (gravity != null || width != null) {
            Window window = getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (gravity != null)
                attributes.gravity = gravity;
            if (width != null) {
                attributes.width = width;
            }
            window.setAttributes(attributes);
            //去除dialog左右边距
            window.setBackgroundDrawableResource(android.R.color.white);
        }
        dialogConfirm = findViewById(R.id.dialog_edit_confirm);
        dialogCancel = findViewById(R.id.dialog_edit_cancel);
        if (!TextUtils.isEmpty(confirm)) {
            dialogConfirm.setText(confirm);
        }
        if (!TextUtils.isEmpty(cancel)) {
            dialogCancel.setText(cancel);
        }
        dialogConfirm.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);
    }

    public CustomEditDialog setCancel(String cancel, IOnCancelListener onCancelListener) {
        this.cancel = cancel;
        this.onCacenlListener = onCancelListener;
        return this;
    }

    public CustomEditDialog setConfirm(String confirm, IOnConfirmListener onConfirmListener) {
        this.confirm = confirm;
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_edit_cancel:
                if (onCacenlListener != null)
                    onCacenlListener.onCancel(this);
                break;
            case R.id.dialog_edit_confirm:
                if (onConfirmListener != null)
                    onConfirmListener.onConfirm(this);
                break;
        }
    }

    public interface IOnCancelListener {
        void onCancel(CustomEditDialog dialog);
    }

    public interface IOnConfirmListener {
        void onConfirm(CustomEditDialog dialog);
    }

    public CustomEditDialog setLayoutResourceId(int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
        return this;
    }

    public CustomEditDialog setGravity(Integer gravity) {
        this.gravity = gravity;
        return this;
    }

    public CustomEditDialog setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public CustomEditDialog setConfirmText(String text) {
        dialogConfirm.setText(text);
        return this;
    }

    public CustomEditDialog setCancelText(String text) {
        dialogCancel.setText(text);
        return this;
    }
}
