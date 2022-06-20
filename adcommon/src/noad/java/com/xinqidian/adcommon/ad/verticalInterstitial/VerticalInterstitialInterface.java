package com.xinqidian.adcommon.ad.verticalInterstitial;

/**
 * Created by lipei on 2020/5/9.
 */

public interface VerticalInterstitialInterface {


    void onAdPresent();


    void onAdClick();


    void onAdDismissed();


    void onAdFailed();


    void onAdLoaded();


    void onStimulateSuccess();
}
