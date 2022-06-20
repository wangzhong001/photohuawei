package com.qihe.zzj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qihe.zzj.R;
import com.qihe.zzj.bean.WorkBean;
import com.qihe.zzj.util.CodeUtil;

import java.util.ArrayList;
import java.util.List;


public class MineWorkAdapter extends RecyclerView.Adapter<MineWorkAdapter.ViewHolder> {

    private List<WorkBean> list = new ArrayList<>();
    private Context context;

    public MineWorkAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 2; i++) {
            WorkBean workBean = new WorkBean();
            switch (i) {
                case 0:
                    workBean.context = "再多文件也能快速压缩、解压";
                    workBean.title = "解压缩工厂";
                    workBean.iconRes = R.drawable.zip_icon;
                    workBean.packageName = "com.qihe.zipking";
                    break;
//                case 1:
//                    workBean.context = "超齐全的证件类型";
//                    workBean.title = "最美一寸证件照";
//                    workBean.iconRes = R.drawable.zzj_icon;
//                    workBean.packageName = "com.qihe.zzj";
//                    break;
//                case 2:
//                    workBean.context = "开启你的音频剪辑之旅~";
//                    workBean.title = "音频音乐剪辑器";
//                    workBean.iconRes = R.drawable.ypjjq_icon;
//                    workBean.packageName = "com.qihekj.audioclip";
//                    break;
                case 1:
                    workBean.context = "垃圾分类环保百科绿色查询";
                    workBean.title = "垃圾分类查询";
                    workBean.iconRes = R.drawable.ljfl_icon;
                    workBean.packageName = "com.qihe.rubbishClass";
                    break;
//                case 3:
//                    workBean.context = "开启你的视频转换之旅";
//                    workBean.title = "视频格式转换工具";
//                    workBean.iconRes = R.drawable.spzh_icon;
//                    workBean.packageName = "com.qihe.formatconverter";
//                    break;
            }

            list.add(workBean);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mine_work_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final WorkBean bean = list.get(position);
//        holder.icon_iv.setImageResource(R.mipmap.app_icon);
        holder.title_tv.setText(bean.title);
        holder.content_tv.setText(bean.context);
        holder.icon_iv.setImageResource(bean.iconRes);
        holder.right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeUtil.jumpToReview(context, bean.packageName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon_iv;
        private TextView title_tv;
        private TextView content_tv;
        private TextView right_tv;

        public ViewHolder( View itemView) {
            super(itemView);
            icon_iv = itemView.findViewById(R.id.icon_iv);
            title_tv = itemView.findViewById(R.id.title_tv);
            content_tv = itemView.findViewById(R.id.content_tv);
            right_tv = itemView.findViewById(R.id.right_tv);
        }
    }
}
