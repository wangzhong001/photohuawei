package com.qihe.zzj.ui.activity;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.R;
import com.qihe.zzj.adapter.ViewPagerAdapter;
import com.qihe.zzj.databinding.ActivityPhotoAllBinding;
import com.qihe.zzj.ui.fragment.AllFragment;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.bus.LiveDataBus;

import java.util.ArrayList;
import java.util.List;

public class PhotoAllActivity extends BaseActivity<ActivityPhotoAllBinding, BaseViewModel> {

    private ViewPagerAdapter adapter;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_photo_all;
    }

    @Override
    public void initParam() {
        super.initParam();
        List<Fragment>  fragments = new ArrayList<>();
        fragments.add(AllFragment.newInstance(1));
        fragments.add(AllFragment.newInstance(2));
        fragments.add(AllFragment.newInstance(3));
        fragments.add(AllFragment.newInstance(4));
        fragments.add(AllFragment.newInstance(5));
        fragments.add(AllFragment.newInstance(6));
        fragments.add(AllFragment.newInstance(7));

        List<String>  titles = new ArrayList<>();
        titles.add(getResources().getString(R.string.function_string_xueli));
        titles.add(getResources().getString(R.string.function_string_lvyou));
        titles.add(getResources().getString(R.string.function_string_jianzhu));
        titles.add(getResources().getString(R.string.function_string_yiyao));
        titles.add(getResources().getString(R.string.function_string_yuyan));
        titles.add(getResources().getString(R.string.function_string_shenfen));
        titles.add(getResources().getString(R.string.function_string_it));

        adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments,titles);
    }

    @Override
    public void initData() {
        super.initData();

//        ImmersionBar.with(this)
//                .reset()
//                .statusBarColor(R.color.color_2C36FD)
//                .fitsSystemWindows(true)
//                .init();
        ImmersionBar.with(this)
                .reset()
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .fitsSystemWindows(false)
                .init();

        binding.viewPager.setAdapter(adapter);
        binding.tablayout.setupWithViewPager(binding.viewPager);

        initClick();
    }

    private void initClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
