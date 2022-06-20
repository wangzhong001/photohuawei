package com.qihe.zzj.util;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.qihe.zzj.BaseApplication;
import com.qihe.zzj.R;


/**
 * Created by lipei on 2017/12/15.
 */

public class AnimationUtil {
    private static final String TAG = AnimationUtil.class.getSimpleName();

    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }


    /**
     * 出来
     * @return
     */
    public static Animation TopTranslateAnimation(){
        Animation outAnima = AnimationUtils
                .loadAnimation(BaseApplication.getContext(), R.anim.put_out_frombuttom);
        return outAnima;
    }

    /**
     * 进入
     * @return
     */
    public static Animation DwonTranslateAnimation(){
        Animation outAnima = AnimationUtils.loadAnimation(BaseApplication.getContext(), R.anim.put_in_frombuttom);
        return outAnima;
    }



}
