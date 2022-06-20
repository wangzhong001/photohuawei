package com.qihe.zzj.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.R;
import com.qihe.zzj.databinding.ActivityShootTipsBinding;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;

/**
 * 拍摄攻略
 */
public class ShootTipsActivity extends BaseActivity<ActivityShootTipsBinding,BaseViewModel> {




    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_shoot_tips;
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
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    }
}
