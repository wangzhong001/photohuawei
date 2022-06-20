package com.qihe.zzj.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.qihe.zzj.R;
import com.qihe.zzj.bean.FunctionRecycBean;
import com.qihe.zzj.databinding.MoreFetrueDialogBinding;
import com.xinqidian.adcommon.util.ToastUtils;


/**
 * Created by lipei on 2019/1/7.
 */
public class UpdateFetaureDialog {

    private CustomDialog customDialog;
    private ItemListener itemListener;//选择地址
    private Context mContext;
    private FunctionRecycBean functionRecycBean;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    //点击确认按钮回调接口
    public interface ItemListener {
        void onClickChose(FunctionRecycBean functionRecycBean);
    }

    public UpdateFetaureDialog(Context mContext) {
        this.mContext = mContext;
        customDialog = new CustomDialog(mContext);
    }

    public UpdateFetaureDialog addItemListener(ItemListener itemListener) {
        setItemListener(itemListener);
        return this;
    }

    public UpdateFetaureDialog show() {
        customDialog.show();
        return this;
    }

    public UpdateFetaureDialog dismiss() {
        customDialog.dismiss();
        return this;
    }

    public final class CustomDialog extends Dialog {

        public CustomDialog(@NonNull Context context) {
            super(context, R.style.ActionChosePriceSheetDialogStyle);
            init();
        }

        private void init() {
            final MoreFetrueDialogBinding chosePriceDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.more_fetrue_dialog, null, false);
            setContentView(chosePriceDialogBinding.getRoot());
            chosePriceDialogBinding.canelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide_keyboard_from(getContext(), chosePriceDialogBinding.getRoot());
                    dismiss();
                }
            });
            chosePriceDialogBinding.sureIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (functionRecycBean == null) {
                        functionRecycBean = new FunctionRecycBean();
                    }

                    if (chosePriceDialogBinding.withEd.getText().toString().isEmpty()) {
                        ToastUtils.show("照片宽度不能为空");
                        return;
                    }

                    if (chosePriceDialogBinding.heightEd.getText().toString().isEmpty()) {
                        ToastUtils.show("照片高度不能为空");
                        return;
                    }

                    if (Integer.parseInt(chosePriceDialogBinding.withEd.getText().toString()) < 70) {
                        ToastUtils.show("照片宽度不能小于70");
                        return;
                    }

                    if (Integer.parseInt(chosePriceDialogBinding.withEd.getText().toString()) > 2000) {
                        ToastUtils.show("照片宽度不能大于2000");
                        return;
                    }

                    if (Integer.parseInt(chosePriceDialogBinding.heightEd.getText().toString()) < 70) {
                        ToastUtils.show("照片高度不能小于70");
                        return;
                    }

                    if (Integer.parseInt(chosePriceDialogBinding.heightEd.getText().toString()) > 2000) {
                        ToastUtils.show("照片高度不能大于2000");
                        return;
                    }

                    functionRecycBean.setName("自定义规格");
                    functionRecycBean.setX_mm("25");
                    functionRecycBean.setY_mm("35");
                    functionRecycBean.setX_px(chosePriceDialogBinding.withEd.getText().toString());
                    functionRecycBean.setY_px(chosePriceDialogBinding.heightEd.getText().toString());
                    functionRecycBean.setKb("");
                    if (chosePriceDialogBinding.maxEd.getText().toString().isEmpty()) {
                        functionRecycBean.setMaxSize(100);
                    } else {
                        functionRecycBean.setMaxSize(Integer.parseInt(chosePriceDialogBinding.maxEd.getText().toString()));
                    }
                    if (itemListener != null) {
                        itemListener.onClickChose(functionRecycBean);
                    }
                    hide_keyboard_from(getContext(), chosePriceDialogBinding.getRoot());
                    dismiss();
                }
            });
            setCancelable(true);
            setCanceledOnTouchOutside(true);
            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    public void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void show_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
