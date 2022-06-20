package com.qihe.zzj.ui.activity;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.R;
import com.qihe.zzj.adapter.FunctionRecycAdapter;
import com.qihe.zzj.databinding.ActivityPhotoNormalBinding;
import com.qihe.zzj.bean.FunctionRecycBean;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.bus.LiveDataBus;

import java.util.ArrayList;
import java.util.List;

public class PhotoNormalActivity extends BaseActivity<ActivityPhotoNormalBinding, BaseViewModel> {

    private List<FunctionRecycBean> datas;
    private FunctionRecycAdapter adaper;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_photo_normal;
    }

    @Override
    public void initParam() {
        super.initParam();
        datas = new ArrayList<>();
        if (getIntent() != null) {
            if (getIntent().getIntExtra("type", 1) == 1) {
                datas.add(new FunctionRecycBean("一寸", "295", "413", "25", "35", "","1"));
                datas.add(new FunctionRecycBean("二寸", "413", "579", "35", "49", "","4"));
                datas.add(new FunctionRecycBean("大一寸", "390", "567", "33", "48", "","3"));
                datas.add(new FunctionRecycBean("小一寸", "260", "378", "22", "32", "","2"));
                datas.add(new FunctionRecycBean("小二寸", "413", "531", "35", "45", "","5"));
                datas.add(new FunctionRecycBean("五寸", "1050", "1499", "89", "127", "","21"));
                datas.add(new FunctionRecycBean("三寸", "649", "991", "54", "84", "","19"));
                datas.add(new FunctionRecycBean("四寸", "898", "1205", "76", "102", "","20"));
                datas.add(new FunctionRecycBean("大二寸", "413", "626", "35", "53", "","6"));

            } else if (getIntent().getIntExtra("type", 1) == 2) {
                datas.add(new FunctionRecycBean("教师资格证", "295", "413", "25", "35", "","15"));
//                datas.add(new FunctionRecycBean("2020年特岗教师", "295", "413", "25", "35", ""));
//                datas.add(new FunctionRecycBean("特岗教师", "358", "441", "26", "32", ""));
                datas.add(new FunctionRecycBean("驾驶证、驾照（无回执）", "358", "441", "22", "32", "","16"));
                datas.add(new FunctionRecycBean("国家司法考试", "413", "626", "35", "53", "","23"));
                datas.add(new FunctionRecycBean("执法证", "358", "441", "26", "32", "","26"));
                datas.add(new FunctionRecycBean("导游证", "285", "385", "23", "33", "","14"));
//                datas.add(new FunctionRecycBean("保健按摩师", "118", "146", "10", "12", ""));
                datas.add(new FunctionRecycBean("职业兽医资格证", "230", "334", "20", "29", "","32"));
                datas.add(new FunctionRecycBean("注册会计师", "178", "220", "20", "29", "","110"));
                datas.add(new FunctionRecycBean("保险职业认证", "210", "270", "17", "25", "10~25KB","109"));
                datas.add(new FunctionRecycBean("保险职业证", "210", "370", "17", "25", "10~25KB","111"));
            }else if (getIntent().getIntExtra("type", 1) == 3){
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
//                datas.add(new FunctionRecycBean("研究生考试报名", "531", "708", "45", "60", "100KB以上"));
//                datas.add(new FunctionRecycBean("广西自治区自考考试", "384", "512", "32", "43", "0`200KB"));
//                datas.add(new FunctionRecycBean("硕士研究生考试", "390", "567", "33", "48", "50KB以下"));
//                datas.add(new FunctionRecycBean("江西专升本(江西自考)", "480", "640", "41", "54", "20~40KB"));
//                datas.add(new FunctionRecycBean("在职研究生考试", "390", "567", "33", "48", "20~200KB"));
//                datas.add(new FunctionRecycBean("安徽自考", "144", "192", "12", "16", "15~16KB"));
            }else if (getIntent().getIntExtra("type", 1) == 4){
//                datas.add(new FunctionRecycBean("中国护照(无回执)", "390", "567", "33", "48", ""));
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
            }else if (getIntent().getIntExtra("type", 1) == 5){
                datas.add(new FunctionRecycBean("一级建造师证（电子版）", "295", "413", "25", "35", "10~30KB","1",30));
                datas.add(new FunctionRecycBean("一级建造师证(冲印版)", "401", "531", "34", "45", "10~30KB","109",30));
                datas.add(new FunctionRecycBean("二级建造师证(2020)", "295", "413", "25", "35", "10~30KB","29",30));
//                datas.add(new FunctionRecycBean("一级建造师证（电子版）", "472", "630", "25", "35", "","30"));
                datas.add(new FunctionRecycBean("二级建造师证", "455", "661", "39", "56", "10~30KB","31",30));
                datas.add(new FunctionRecycBean("二级建造师（天津）", "160", "200", "36", "51", "20~40KB","101",30));
                datas.add(new FunctionRecycBean("二级建造师（湖南）", "130", "170", "18", "25", "20KB以下","102",20));
                datas.add(new FunctionRecycBean("二级建造师（西藏）", "455", "661", "14", "17", "10~30KB","107",30));
//                datas.add(new FunctionRecycBean("辽宁二级建造证", "295", "413", "25", "35", "30KB以上"));
                datas.add(new FunctionRecycBean("二级建造师", "160", "180", "14 ", "15", "20KB以下","108",20));
            }else if (getIntent().getIntExtra("type", 1) == 6){
                datas.add(new FunctionRecycBean("护士执业资格考试", "160", "210", "25", "35", "20~45KB","94"));
//                datas.add(new FunctionRecycBean("护师报名照", "413", "579", "35", "49", "45KB以上"));
                datas.add(new FunctionRecycBean("执业医师资格报名", "354", "472", "30", "40", "","62"));
                datas.add(new FunctionRecycBean("执业药师资格考试（一寸）", "295", "413", "30", "40", "","60"));
                datas.add(new FunctionRecycBean("执业药师资格考试（二寸）", "413", "579", "30", "40", "","61"));
                datas.add(new FunctionRecycBean("职业兽医资格证", "230", "334", "30", "40", "","32"));
//                datas.add(new FunctionRecycBean("康复治疗师考试(一寸)", "295", "413", "14", "18", "45KB以上"));
//                datas.add(new FunctionRecycBean("医疗类报名", "295", "413", "25", "35", "40KB以上"));
//                datas.add(new FunctionRecycBean("护士执业资格考试", "413", "531", "35", "45", "20~45KB"));
//                datas.add(new FunctionRecycBean("护士执业资格考试", "160", "210", "14", "18", "20~45KB"));
//                datas.add(new FunctionRecycBean("广东省育婴师", "390", "567", "20", "27", "40KB以下"));
//                datas.add(new FunctionRecycBean("执业医师", "215", "300", "18", "25", "30KB以下"));
            }else if (getIntent().getIntExtra("type", 1) == 7){
                datas.add(new FunctionRecycBean("英语AB级考试", "390", "567", "12", "16", "10KB以下","49"));
                datas.add(new FunctionRecycBean("英语三级考试", "144", "192", "12", "16", "","105"));
                datas.add(new FunctionRecycBean("英语四级考试", "480", "640", "12", "16", "100KB","100"));
                datas.add(new FunctionRecycBean("英语四六级考试", "144", "172", "12", "16","0~40KB","40"));
                datas.add(new FunctionRecycBean("英语四六级考试", "144", "192", "12", "16","10~20KB","42"));
                datas.add(new FunctionRecycBean("英语四六级考试", "240", "320", "12", "16","20~30KB","46"));
                datas.add(new FunctionRecycBean("普通话水平测试", "390", "567", "33", "48", "20KB以下","50"));
                datas.add(new FunctionRecycBean("学位英语", "390", "567", "33", "48", "","48"));
//                datas.add(new FunctionRecycBean("翻译资格考试报名注册", "295", "413", "25", "35", "30KB以上"));
//                datas.add(new FunctionRecycBean("实用日本语检定考试(J.TEST)", "90", "120", "30", "40", "20~60KB"));
//                datas.add(new FunctionRecycBean("英语四六级考试", "150", "200", "30", "40","20~60KB"));
//                datas.add(new FunctionRecycBean("普通话水平测试", "144", "192", "12", "16", "10KB以下"));
//                datas.add(new FunctionRecycBean("中高级口译", "413", "626", "35", "53", "50~200KB"));
//                datas.add(new FunctionRecycBean("雅思考试", "390", "567", "33", "48", ""));
            }else if (getIntent().getIntExtra("type", 1) == 8){
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
//                datas.add(new FunctionRecycBean("事业单位招聘(295*413)", "295", "413", "10", "13", "20~100KB"));
//                datas.add(new FunctionRecycBean("兰陵农商银行社保照(无回执)", "358", "441", "26", "32", "20~30KB"));
//                datas.add(new FunctionRecycBean("社保证(上海无回执)", "358", "441", "26", "32","15~50KB"));
//                datas.add(new FunctionRecycBean("西安高新区住房保障", "110", "154", "35", "49", ""));
            }else if (getIntent().getIntExtra("type", 1) == 9){
                datas.add(new FunctionRecycBean("全国计算机考试", "390", "567", "33", "48", "25~200KB","53"));
//                datas.add(new FunctionRecycBean("全国计算机等级考试", "144", "192", "20", "27", "20KB以下"));
//                datas.add(new FunctionRecycBean("北京计算机等级考试", "432", "576", "37", "49", "20~200KB"));
//                datas.add(new FunctionRecycBean("全国职称计算机考试", "158", "210", "13", "18", "30KB以下"));
//                datas.add(new FunctionRecycBean("山西计算机等级考试", "144", "192", "33", "48", "20KB以下"));
//                datas.add(new FunctionRecycBean("山西全国计算机等级考试", "295", "413", "25", "35","20~40KB"));
//                datas.add(new FunctionRecycBean("计算机技术与软件专业技术资格", "295", "413", "25", "35", ""));
//                datas.add(new FunctionRecycBean("计算机中级操作员(ATA)", "144", "192", "36", "48", "20~200KB"));
//                datas.add(new FunctionRecycBean("计算机三级", "240", "320", "20", "27", "15KB以下"));
            }
            adaper = new FunctionRecycAdapter(R.layout.layout_item_function_recycler, datas);
        }
    }

    @Override
    public void initData() {
        super.initData();

        ImmersionBar.with(this)
                .reset()
                .statusBarColor(R.color.color_FAFAFA)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();

        if (getIntent() != null) {
            binding.titleTv.setText(getIntent().getStringExtra("title"));
//            if (getIntent().getIntExtra("type", 1) == 1) {
//                binding.titleTv.setText(getResources().getString(R.string.normal_cunzhao));
//            } else if (getIntent().getIntExtra("type", 1) == 2) {
//                binding.titleTv.setText(getResources().getString(R.string.normal_zhengzhao));
//            }
        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adaper);

        initClick();
    }

    private void initClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PhotoNormalActivity.this, PhotoDetailsActivity.class);
                intent.putExtra("data", datas.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public int initVariableId() {
        return 0;
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
    public void initViewObservable() {
        super.initViewObservable();
        LiveDataBus.get().with(LiveBusConfig.backHome,String.class).observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                finish();
            }
        });

    }
}
