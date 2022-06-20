package com.qihe.zzj.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qihe.zzj.R;

/**
 * Created by lipei on 2018/1/29.
 */

public class PicUtils {


    // 下载一个图片 设置到ImageView
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        if (url == null || view.getContext() == null) {
            view.setImageResource(R.drawable.kong_order_icon);
        } else {

            Glide.with(view.getContext()).load(url).placeholder(R.drawable.kong_order_icon)
                    .error(R.drawable.kong_order_icon).into(view);


        }
    }








    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
