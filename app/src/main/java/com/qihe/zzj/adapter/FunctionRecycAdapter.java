package com.qihe.zzj.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qihe.zzj.R;
import com.qihe.zzj.bean.FunctionRecycBean;

import java.util.List;

/**
 * Created by cz on 2020/6/18.
 */

public class FunctionRecycAdapter extends BaseQuickAdapter<FunctionRecycBean,BaseViewHolder> {

    public FunctionRecycAdapter(int layoutResId, @Nullable List<FunctionRecycBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FunctionRecycBean item) {
        helper.setText(R.id.tv_name,item.getName())
                .setText(R.id.tv_x_px,item.getX_px())
                .setText(R.id.tv_y_px,item.getY_px())
                .setText(R.id.tv_x_mm,item.getX_mm())
                .setText(R.id.tv_y_mm,item.getY_mm())
                .setText(R.id.tv_kb,item.getKb());
    }
}
