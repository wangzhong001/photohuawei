package com.qihe.zzj.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.R;
import com.qihe.zzj.bean.KefuModel;
import com.qihe.zzj.databinding.ActivityQuestionDetailBinding;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;

/**
 * Created by lipei on 2020/7/30.
 */

public class QuestionActivity extends BaseActivity<ActivityQuestionDetailBinding,BaseViewModel> {
    private KefuModel kefuModel;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_question_detail;
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
    public void initData() {
        super.initData();
        ImmersionBar.with(this)
                .reset()
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();
        if(getIntent()!=null){
            kefuModel= (KefuModel) getIntent().getSerializableExtra("kefu");
            if(kefuModel!=null){
                binding.setBaseViewModel(kefuModel);
            }

        }


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
