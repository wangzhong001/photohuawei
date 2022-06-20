package com.xinqidian.adcommon.adview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.SureDialog;


/**
 * Created by lipei on 2018/1/11.
 */

public class TwiceFragmentLayout extends FrameLayout {

    /**
     * 是否已提示过
     */
    private boolean comfirmed = false;

    public TwiceFragmentLayout(@NonNull Context context) {
        super(context);
    }

    public TwiceFragmentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TwiceFragmentLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TwiceFragmentLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_UP: {

                if (!isComfirmed()) {
                    SureDialog sureDialog=new SureDialog(getContext(),"确认点击下载应用或打开网址",true)
                            .addItemListener(new SureDialog.ItemListener() {
                                @Override
                                public void onClickSure() {
                                    comfirmed = true;

                                }

                                @Override
                                public void onClickCanel() {
                                    comfirmed = true;

                                }
                            });
                    sureDialog.show();
//                    ToastUtils.show("确认点击下载应用或打开网址");
                    return true;
                }

                break;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    private boolean isComfirmed() {
        return comfirmed;
    }

    public void setComfirmed(boolean comfirmed) {
        this.comfirmed = comfirmed;

    }
}
