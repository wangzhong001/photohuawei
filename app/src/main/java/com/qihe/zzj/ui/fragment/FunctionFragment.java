package com.qihe.zzj.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qihe.zzj.R;
import com.qihe.zzj.adapter.FunctionRecycAdapter;
import com.qihe.zzj.bean.FunctionRecycBean;
import com.qihe.zzj.databinding.FragmentHuaweiFunction2Binding;
import com.qihe.zzj.ui.activity.PhotoDetailsActivity;
import com.qihe.zzj.ui.activity.PhotoNormalActivity;
import com.qihe.zzj.view.UpdateFetaureDialog;
import com.xinqidian.adcommon.base.BaseFragment;
import com.xinqidian.adcommon.base.BaseViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 首页界面
 */
public class FunctionFragment extends BaseFragment<FragmentHuaweiFunction2Binding, BaseViewModel> {

    private List<FunctionRecycBean> datas;
    private FunctionRecycAdapter adaper;
    private UpdateFetaureDialog updateFetaureDialog;

    public static FunctionFragment newInstance() {
        FunctionFragment fragment = new FunctionFragment();
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_huawei_function2;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initParam() {
        super.initParam();
        datas = new ArrayList<>();
//        datas.add(new FunctionRecycBean("自定义规格","自定义","自定义","25", "35","","1"));

        datas.add(new FunctionRecycBean("一寸", "295", "413", "25", "35", "", "1"));
        datas.add(new FunctionRecycBean("二寸", "413", "579", "35", "49", "", "4"));
        datas.add(new FunctionRecycBean("中小学教师资格证", "295", "413", "25", "35", "100KB~200KB", "15"));
//        datas.add(new FunctionRecycBean("成人自考","360","480","30", "40","20KB~40KB"));
        datas.add(new FunctionRecycBean("成人自考", "384", "512", "30", "40", "20KB~40KB", "33"));
        datas.add(new FunctionRecycBean("小二寸", "413", "531", "35", "45", "", "5"));
        datas.add(new FunctionRecycBean("大一寸", "390", "567", "33", "48", "", "3"));
        datas.add(new FunctionRecycBean("小一寸", "260", "378", "22", "32", "", "2"));
        datas.add(new FunctionRecycBean("英语四六级考试", "144", "192", "12", "16", "10KB~20KB", "42"));
        datas.add(new FunctionRecycBean("普通话水平测试", "390", "567", "33", "48", "20KB以下", "50"));
        datas.add(new FunctionRecycBean("学籍照片", "358", "441", "26", "32", "60KB以下", "67"));
        datas.add(new FunctionRecycBean("社保证(350DPI无回执)", "358", "441", "26", "29", "50KB~100KB", "73"));

        adaper = new FunctionRecycAdapter(R.layout.layout_item_function_recycler, datas);
    }

    @Override
    public void initData() {
        super.initData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adaper);
        binding.recyclerView.setNestedScrollingEnabled(false);

//        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_function_recyc_header,null);
//        TextView tv1 = view.findViewById(R.id.tv_1);
//        adaper.addHeaderView(view);

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a >= 0 && a <= 6) {
            binding.tv1.setText(getResources().getString(R.string.hello_1));
        }
        if (a > 6 && a <= 12) {
            binding.tv1.setText(getResources().getString(R.string.hello_2));
        }
        if (a > 12 && a <= 13) {
            binding.tv1.setText(getResources().getString(R.string.hello_3));
        }
        if (a > 13 && a <= 18) {
            binding.tv1.setText(getResources().getString(R.string.hello_4));
        }
        if (a > 18 && a <= 24) {
            binding.tv1.setText(getResources().getString(R.string.hello_5));
        }
        initClick();
    }

    private void initClick() {
//        binding.tvCunzhao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), PhotoNormalActivity.class);
//                intent.putExtra("type",1);
//                intent.putExtra("title","常用寸照");
//                startActivity(intent);
//            }
//        });
        binding.tvZhengzhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoNormalActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("title", "常用证照");
                startActivity(intent);
            }
        });
        binding.tvXueli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoNormalActivity.class);
                intent.putExtra("type", 3);
                intent.putExtra("title", "学历考试");
                startActivity(intent);
            }
        });
        binding.tvLvyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoNormalActivity.class);
                intent.putExtra("type", 4);
                intent.putExtra("title", "旅游签证");
                startActivity(intent);
            }
        });
        binding.tvJianzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoNormalActivity.class);
                intent.putExtra("type", 5);
                intent.putExtra("title", "建筑工程");
                startActivity(intent);
            }
        });
        binding.tvYiyao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoNormalActivity.class);
                intent.putExtra("type", 6);
                intent.putExtra("title", "医药卫生");
                startActivity(intent);
            }
        });
        binding.tvYuyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoNormalActivity.class);
                intent.putExtra("type", 7);
                intent.putExtra("title", "语言考试");
                startActivity(intent);
            }
        });
        binding.tvShenfen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoNormalActivity.class);
                intent.putExtra("type", 8);
                intent.putExtra("title", "身份证件");
                startActivity(intent);
            }
        });
        binding.tvIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoNormalActivity.class);
                intent.putExtra("type", 9);
                intent.putExtra("title", "IT认证");
                startActivity(intent);
            }
        });
        binding.tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateFetaureDialog == null) {
                    updateFetaureDialog = new UpdateFetaureDialog(getContext())
                            .addItemListener(new UpdateFetaureDialog.ItemListener() {
                                @Override
                                public void onClickChose(FunctionRecycBean functionRecycBean) {
                                    Intent intent = new Intent(getActivity(), PhotoDetailsActivity.class);
                                    intent.putExtra("data", functionRecycBean);
                                    startActivity(intent);
                                }
                            });
                }
                updateFetaureDialog.show();
            }
        });
        adaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), PhotoDetailsActivity.class);
                intent.putExtra("data", datas.get(position));
                startActivity(intent);
            }
        });
//        binding.toInvit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(SharedPreferencesUtil.isLogin()){
//                    startActivity(InvitationRewardActivity.class);
//                }else {
//                    startActivity(LoginActivity.class);
//                }
//            }
//        });
    }

    @Override
    public boolean isShowBannerAd() {
        return false;
    }

    @Override
    public boolean isShowNativeAd() {
        return false;
    }

    @Override
    public boolean isShowVerticllAndStimulateAd() {
        return false;
    }

    @Override
    public void onStimulateSuccessCall() {

    }

    @Override
    public void onStimulateSuccessDissmissCall() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
