package com.qihe.zzj.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.qihe.zzj.R;

/**
 * Created by cz on 2020/6/22.
 */

public class LoadProgressDialog {

    private volatile static LoadProgressDialog instance;
    private static AlertDialog dialog;


    //单例模式双检查机制
    public static LoadProgressDialog getInstance() {
        if (instance == null) {
            synchronized (LoadProgressDialog.class) {
                if (instance == null) {
                    instance = new LoadProgressDialog();
                }
            }
        }
        return instance;
    }

    public void showDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_loading_progress, null);

        dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContentView(view);
        if (dialog.getWindow() != null) {
//            dialog.getWindow().setLayout(DensityUtil.getScreenWidth(context) / 6 * 5, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
