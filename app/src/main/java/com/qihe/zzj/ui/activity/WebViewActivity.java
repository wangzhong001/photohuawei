package com.qihe.zzj.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qihe.zzj.R;
import com.qihe.zzj.databinding.ActivityWebViewBinding;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;

public class WebViewActivity extends BaseActivity<ActivityWebViewBinding, BaseViewModel> {

    private String url;
    private String title;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_web_view;
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
    public void initParam() {
        super.initParam();
        if (getIntent() != null){
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
        }
    }

    @Override
    public void initData() {
        super.initData();
        binding.bannerViewContainer.setVisibility(com.xinqidian.adcommon.util.SharedPreferencesUtil.isVip() ? View.GONE : View.VISIBLE);
        binding.nativeViewContainer.setVisibility(com.xinqidian.adcommon.util.SharedPreferencesUtil.isVip() ? View.GONE : View.VISIBLE);
        TextView title_tv =  findViewById(R.id.title_tv);
        binding.webview.loadUrl(url);
        if (title != null){
           title_tv.setText(title);
        }
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
