package com.xinqidian.adcommon.binding.viewadapter.textview;

import android.databinding.BindingAdapter;
import android.widget.TextView;

/**
 * Created by goldze on 2017/6/18.
 */

public class ViewAdapter {
    /**
     *
     *
     */
    @BindingAdapter("textBackground")
    public static void setTextBackground(TextView textView, int res) {
        textView.setBackgroundResource(res);
    }


}
