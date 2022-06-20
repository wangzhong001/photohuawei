package com.qihe.zzj.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qihe.zzj.R;
import com.qihe.zzj.adapter.FunctionRecycAdapter;
import com.qihe.zzj.bean.FunctionRecycBean;
import com.qihe.zzj.databinding.FragmentAllBinding;
import com.qihe.zzj.ui.activity.PhotoDetailsActivity;
import com.xinqidian.adcommon.base.BaseFragment;
import com.xinqidian.adcommon.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends BaseFragment<FragmentAllBinding, BaseViewModel> {

    private int type;
    private List<FunctionRecycBean> datas;
    private FunctionRecycAdapter adapter;

    public AllFragment() {
        // Required empty public constructor
    }

    public static AllFragment newInstance(int type) {
        AllFragment allFragment = new AllFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        allFragment.setArguments(bundle);
        return allFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_all;
    }

    @Override
    public void initParam() {
        super.initParam();
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
        datas = new ArrayList<>();






        if (type == 1) {

            datas.add(new FunctionRecycBean("成人自考", "294", "413", "30", "40", "","35"));
            datas.add(new FunctionRecycBean("成人自考", "384", "512", "30", "40", "","33"));
            datas.add(new FunctionRecycBean("成人自考（本科） ", "480", "720", "30", "40", "","98"));
            datas.add(new FunctionRecycBean("公务员考试", "294", "413", "35", "45", "","108"));
            datas.add(new FunctionRecycBean("英语四六级考试", "144", "172", "35", "45", "0~40KB","40"));
            datas.add(new FunctionRecycBean("英语四六级考试", "144", "192", "35", "45", "10~20KB","42"));
            datas.add(new FunctionRecycBean("英语四六级考试", "240", "320", "35", "45", "20~30KB","46"));
            datas.add(new FunctionRecycBean("英语 AB 级考试", "390", "567", "35", "45", ""));
//                datas.add(new FunctionRecycBean("辽宁高等教育自学考试", "390", "567", "33", "48", "15~40KB"));
            datas.add(new FunctionRecycBean("学位英语", "390", "567", "33", "48", "","48"));

        } else if (type == 2) {




            datas.add(new FunctionRecycBean("入台证", "390", "567", "35", "45", "","81"));
            datas.add(new FunctionRecycBean("世界通用签证", "390", "567", "35", "45", "","74"));
            datas.add(new FunctionRecycBean("港澳通行证(无回执)", "390", "567", "33", "48", "","75"));
            datas.add(new FunctionRecycBean("日本签证", "531", "531", "45", "45", "","8"));
            datas.add(new FunctionRecycBean("美国签证", "602", "602", "51", "51", "","7"));
            datas.add(new FunctionRecycBean("泰国签证", "413", "531", "35", "45","","77"));
            datas.add(new FunctionRecycBean("韩国签证", "413", "531", "35", "45","","76"));
//                datas.add(new FunctionRecycBean("日本护照", "413", "531", "35", "45",""));
            datas.add(new FunctionRecycBean("印度签证", "413", "531", "51", "51","","78"));
            datas.add(new FunctionRecycBean("越南签证", "413", "531", "40", "60","","9"));
            datas.add(new FunctionRecycBean("以色列签证", "413", "531", "51", "51","","79"));
            datas.add(new FunctionRecycBean("马来西亚签证", "413", "531", "35", "45","","82"));
            datas.add(new FunctionRecycBean("新西兰签证", "413", "531", "76", "102","","83"));
            datas.add(new FunctionRecycBean("意大利签证", "413", "531", "40", "40","","84"));
            datas.add(new FunctionRecycBean("阿根廷签证", "413", "531", "40", "40","","85"));
            datas.add(new FunctionRecycBean("巴西、冰岛签证", "413", "531", "40", "50","","86"));
            datas.add(new FunctionRecycBean("肯尼亚签证", "413", "531", "50", "50","","87"));

        } else if (type == 3) {
            datas.add(new FunctionRecycBean("二级建造师证(2020)", "295", "413", "25", "35", "","29"));
            datas.add(new FunctionRecycBean("一级建造师证（电子版）", "472", "630", "25", "35", "","30"));
            datas.add(new FunctionRecycBean("二级建造师证", "455", "661", "39", "56", "","31"));
            datas.add(new FunctionRecycBean("二级建造师（天津）", "160", "200", "36", "51", "20~40KB","101"));
            datas.add(new FunctionRecycBean("二级建造师（湖南）", "130", "170", "18", "25", "20KB以下","102"));
            datas.add(new FunctionRecycBean("二级建造师（西藏）", "455", "661", "14", "17", "","107"));
        } else if (type == 4) {
            datas.add(new FunctionRecycBean("护士执业资格考试", "160", "210", "25", "35", "20~45KB","94"));
//                datas.add(new FunctionRecycBean("护师报名照", "413", "579", "35", "49", "45KB以上"));
            datas.add(new FunctionRecycBean("执业医师资格报名", "354", "472", "30", "40", "","62"));
            datas.add(new FunctionRecycBean("执业药师资格考试（一寸）", "295", "413", "30", "40", "","60"));
            datas.add(new FunctionRecycBean("执业药师资格考试（二寸）", "413", "579", "30", "40", "","61"));
            datas.add(new FunctionRecycBean("职业兽医资格证", "230", "334", "30", "40", "","32"));
        } else if (type == 5) {
            datas.add(new FunctionRecycBean("英语AB级考试", "390", "567", "12", "16", "10KB以下","49"));
            datas.add(new FunctionRecycBean("英语三级考试", "144", "192", "12", "16", "","105"));
            datas.add(new FunctionRecycBean("英语四级考试", "480", "640", "12", "16", "100KB","100"));
            datas.add(new FunctionRecycBean("英语四六级考试", "144", "172", "12", "16","0~40KB","40"));
            datas.add(new FunctionRecycBean("英语四六级考试", "144", "192", "12", "16","10~20KB","42"));
            datas.add(new FunctionRecycBean("英语四六级考试", "240", "320", "12", "16","20~30KB","46"));
            datas.add(new FunctionRecycBean("普通话水平测试", "390", "567", "33", "48", "20KB以下","50"));
            datas.add(new FunctionRecycBean("学位英语", "390", "567", "33", "48", "","48"));
        } else if (type == 6) {
            datas.add(new FunctionRecycBean("社保证(350DPI无回执)", "358", "441", "26", "32", "30~60KB","72"));
            datas.add(new FunctionRecycBean("社保卡(350DPI无回执)", "358", "441", "30", "40","50~100KB","73"));
            datas.add(new FunctionRecycBean("社保卡(350DPI)", "252", "312", "30", "40","","71"));
            datas.add(new FunctionRecycBean("身份证照(无回执)", "358", "441", "26", "32", "14~20KB","10"));
            datas.add(new FunctionRecycBean("居住证", "358", "441", "26", "32", "","11"));
            datas.add(new FunctionRecycBean("导游证", "285", "385", "26", "32", "","14"));
            datas.add(new FunctionRecycBean("教师资格证", "285", "385", "25", "35", "","15"));
            datas.add(new FunctionRecycBean("国考（二寸）", "285", "385", "35", "45", "","22"));
//                datas.add(new FunctionRecycBean("结婚登记照(无回执)", "626", "413", "53", "35", ""));
            datas.add(new FunctionRecycBean("健康证（一寸）", "295", "413", "25", "35", "","25"));
        } else if (type == 7) {
//            datas.add(new FunctionRecycBean("全国计算机等级考试", "144", "192", "33", "48", "25~200KB"));
//            datas.add(new FunctionRecycBean("全国计算机等级考试", "240", "320", "20", "27", "20KB以下"));
//            datas.add(new FunctionRecycBean("北京计算机等级考试", "432", "576", "37", "49","20~200KB"));
//            datas.add(new FunctionRecycBean("全国职称计算机考试", "158", "210", "13", "18", "30KB以下"));
//            datas.add(new FunctionRecycBean("山西计算机等级考试", "144", "192", "33", "48", "20KB以下"));
//            datas.add(new FunctionRecycBean("山西全国计算机等级考试", "295", "413", "25", "35", "20~40KB"));
//            datas.add(new FunctionRecycBean("计算机技术与软件专业技术资格", "295", "413", "25", "35", ""));
//            datas.add(new FunctionRecycBean("计算机中级操作员(ATA)", "144", "192", "36", "48", "20~200KB"));
////            datas.add(new FunctionRecycBean("计算机三级", "240", "320", "20", "27", "15KB以下"));
//            datas.add(new FunctionRecycBean("全国计算机等级考试", "144", "192", "33", "48", "25~200KB"));
//            datas.add(new FunctionRecycBean("全国计算机等级考试", "144", "192", "20", "27", "20KB以下"));
            datas.add(new FunctionRecycBean("全国计算机考试", "390", "567", "33", "48", "25~200KB","53"));

        }

        adapter = new FunctionRecycAdapter(R.layout.layout_item_function_recycler, datas);
    }

    @Override
    public void initData() {
        super.initData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), PhotoDetailsActivity.class);
                intent.putExtra("data", datas.get(position));
                startActivity(intent);
            }
        });
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
    public int initVariableId() {
        return 0;
    }

    @Override
    public void onStimulateSuccessCall() {

    }

    @Override
    public void onStimulateSuccessDissmissCall() {

    }

}
