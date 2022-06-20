package com.xinqidian.adcommon.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.databinding.CommentDialogBinding;
import com.xinqidian.adcommon.ui.activity.FeedBackActivity;
import com.xinqidian.adcommon.util.AndroidShareUtils;
import com.xinqidian.adcommon.util.DensityUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;


/**
 * Created by lipei on 2018/5/14.
 */

public class CommentDialog {
    private CustomDialog customDialog;
    private ItemListener itemListener;//选择地址
    private Context mContext;
    private String content;
    private boolean isNeedSetCanel;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }


    //点击确认按钮回调接口
    public interface ItemListener {
        void onClickSure();


    }

    public CommentDialog(Context mContext, String content,boolean isNeedSetCanel) {
        this.mContext = mContext;
        this.content=content;
        this.isNeedSetCanel=isNeedSetCanel;
        customDialog = new CustomDialog(mContext);
    }


    public CommentDialog(Context mContext, String content) {
        this.mContext = mContext;
        this.content=content;
        customDialog = new CustomDialog(mContext);
    }

    public CommentDialog addItemListener(ItemListener itemListener) {
        setItemListener(itemListener);
        return this;
    }


    public CommentDialog show() {
        customDialog.show();
        return this;

    }


    public CommentDialog dismiss() {
        customDialog.dismiss();
        return this;

    }


    public final class CustomDialog extends Dialog {



        public CustomDialog(@NonNull Context context) {
            super(context, R.style.PrivacyThemeDialog);
            init();
        }

        private void init() {

            CommentDialogBinding payDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.comment_dialog, null, false);
            setContentView(payDialogBinding.getRoot());
            payDialogBinding.setContent(content);

            payDialogBinding.commentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNeedSetCanel){
                        SharedPreferencesUtil.setParam(Contants.IS_SHOW_COMMENT_DIALOG,false);

                    }
                    AndroidShareUtils.goToMarket(mContext);
                    dismiss();

                }
            });

            payDialogBinding.commentCanelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   AndroidShareUtils.feedBack(mContext);
                    dismiss();
                }
            });


            setCancelable(true);
            setCanceledOnTouchOutside(true);
            getWindow().setGravity(Gravity.CENTER);
            getWindow().setLayout(DensityUtil.getScreenWidth(mContext) / 6 * 5, WindowManager.LayoutParams.WRAP_CONTENT);

        }


    }





}
