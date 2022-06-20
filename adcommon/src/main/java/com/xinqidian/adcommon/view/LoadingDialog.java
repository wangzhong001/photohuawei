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

public class LoadingDialog extends Dialog {

    private boolean isNeedSet;
    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        init();
    }

    public LoadingDialog(@NonNull Context context,boolean isNeedSet) {
        super(context, R.style.ActionSheetDialogStyle);
        this.isNeedSet=isNeedSet;
        init();
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setCanCanel(){
        setCancelable(true);
        setCanceledOnTouchOutside(false);

    }


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.loading_view, null);

        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        final TextView timeTv=view.findViewById(R.id.time_tv);

        TextView contentTv=view.findViewById(R.id.content_tv);

        if(isNeedSet){
            contentTv.setText(getContext().getString(R.string.is_loading));
        }else {
            contentTv.setText(getContext().getString(R.string.make_loading));

        }

        LiveDataBus.get().with(LiveBusConfig.time,String.class).observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(timeTv.getVisibility()==View.GONE){
                    timeTv.setVisibility(View.VISIBLE);
                }
                timeTv.setText(s);
            }
        });
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setLayout(DensityUtil.getScreenWidth(getContext()) / 2 * 1, WindowManager.LayoutParams.WRAP_CONTENT);

    }
}
