package com.xinqidian.adcommon.ui.activity;

import android.os.Bundle;

import com.xinqidian.adcommon.BR;
import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.databinding.ActivityFeedbackBinding;

/**
 * Created by lipei on 2020/6/7.
 */

public class FeedBackActivity extends BaseActivity<ActivityFeedbackBinding,FeedBackViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_feedback;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
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
    }
}
