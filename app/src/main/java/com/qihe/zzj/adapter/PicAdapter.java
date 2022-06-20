package com.qihe.zzj.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qihe.zzj.R;

import java.util.List;

/**
 * Created by cz on 2020/6/19.
 */

public class PicAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    public PicAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        ImageView imageView=(ImageView) helper.getView(R.id.pic_iv);

        Glide.with(imageView.getContext()).load(item).placeholder(R.drawable.kong_order_icon)
                .error(R.drawable.kong_order_icon).into(imageView);


    }
}
