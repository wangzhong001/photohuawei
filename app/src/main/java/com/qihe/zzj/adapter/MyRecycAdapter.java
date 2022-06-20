package com.qihe.zzj.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qihe.zzj.BaseApplication;
import com.qihe.zzj.R;
import com.qihe.zzj.bean.IdPhotoBean;

import java.util.List;

/**
 * Created by cz on 2020/6/19.
 */

public class MyRecycAdapter extends BaseQuickAdapter<IdPhotoBean,BaseViewHolder>{
    private ItemClickInterface itemClickInterface;

    public MyRecycAdapter(int layoutResId, @Nullable List<IdPhotoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final IdPhotoBean item) {
        helper.setText(R.id.tv_name,item.getName()+"电子照")
                .setText(R.id.tv_money,item.getMoney())
                .setText(R.id.tv_x_px,item.getPxSize())
                .setText(R.id.tv_mm,item.getMmSize())
                .setText(R.id.tv_kb,item.getSize());
        TextView canelTv=helper.getView(R.id.canel_tv);
        TextView payTv=helper.getView(R.id.pay_tv);
        canelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickInterface!=null){
                    itemClickInterface.canelClick(item);
                }

            }
        });


        payTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickInterface!=null){
                    itemClickInterface.payClick(item);
                }

            }
        });

        ImageView ivBg=helper.getView(R.id.iv_bg);
        Glide.with(ivBg.getContext()).load(item.getUrl()).placeholder(R.drawable.kong_order_icon)
                .error(R.drawable.kong_order_icon).into(ivBg);


        if (item.getIsPay()){
            helper.setGone(R.id.pay_ll,false);


            helper.setText(R.id.tv_pay,"已付款")
                    .setTextColor(R.id.tv_pay, BaseApplication.getContext().getResources().getColor(R.color.color_55CD5F));
        }else {
            helper.setGone(R.id.pay_ll,true);
            helper.setText(R.id.tv_pay,"未付款")
                    .setTextColor(R.id.tv_pay, BaseApplication.getContext().getResources().getColor(R.color.color_FF1A1E));
        }
    }

    public void setItemClickInterface(ItemClickInterface itemClickInterface) {
        this.itemClickInterface = itemClickInterface;
    }


    public interface ItemClickInterface{
        void canelClick(IdPhotoBean idPhotoBean);

        void payClick(IdPhotoBean idPhotoBean);
    }
}
