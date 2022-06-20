package com.xinqidian.adcommon.view;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.util.DensityUtil;


/**
 * Created by lipei on 2019/1/23.
 */

public class ErrorDialog extends Dialog {
    public ErrorDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        init();
    }

    public ErrorDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected ErrorDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setCanCanel(){
        setCancelable(true);
        setCanceledOnTouchOutside(false);

    }


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.error_view, null);

        setContentView(view);


        final TextView timeTv=view.findViewById(R.id.sure_tv);



        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setLayout(DensityUtil.getScreenWidth(getContext()) / 3 * 2, WindowManager.LayoutParams.WRAP_CONTENT);

    }
}
