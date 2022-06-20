package com.qihe.zzj.view;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.qihe.zzj.R;
import com.qihe.zzj.databinding.VipDialogBinding;
import com.xinqidian.adcommon.util.DensityUtil;


/**
 * Created by lipei on 2018/5/14.
 */

public class VipDialog {
    private CustomDialog customDialog;
    private ItemListener itemListener;//选择地址
    private Context mContext;


    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }


    //点击确认按钮回调接口
    public interface ItemListener {
        void rechargeClick();

        void dissMissClick();

    }


    public VipDialog(Context mContext) {
        this.mContext = mContext;
        customDialog = new CustomDialog(mContext);
    }



    public VipDialog addItemListener(ItemListener itemListener) {
        setItemListener(itemListener);
        return this;
    }


    public VipDialog show() {
        customDialog.show();
        return this;

    }


    public VipDialog dismiss() {
        customDialog.dismiss();
        return this;

    }


    public final class CustomDialog extends Dialog {



        public CustomDialog(@NonNull Context context) {
            super(context, R.style.VipThemeDialog);
            init();
        }

        private void init() {

            VipDialogBinding payDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.vip_dialog, null, false);
            setContentView(payDialogBinding.getRoot());

            setCancelable(true);
            setCanceledOnTouchOutside(true);
            getWindow().setGravity(Gravity.CENTER);
            getWindow().setLayout(DensityUtil.getScreenWidth(mContext) / 6 * 5, WindowManager.LayoutParams.WRAP_CONTENT);

            payDialogBinding.chonzhiIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemListener!=null){
                        itemListener.rechargeClick();
                    }
                    dismiss();
                }
            });

            payDialogBinding.closeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });


        }


    }


}
