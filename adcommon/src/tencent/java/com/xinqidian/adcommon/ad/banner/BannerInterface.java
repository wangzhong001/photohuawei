package com.xinqidian.adcommon.ad.banner;

import com.qq.e.comm.util.AdError;

/**
 * Created by lipei on 2020/5/1.
 */

public interface BannerInterface {

    void onNoAD();

    void onADReceive();

    void onADExposure();

    void onADClosed();

    void onADClicked();

    void onADLeftApplication();

    void onADOpenOverlay();

    void onADCloseOverlay();


    /**
     * 小米banner回调
     */
    void onAdPresent();

    void onAdClick();

    void onAdDismissed();

    void onAdFailed(String s);

    void onAdLoaded(int i);

    void onStimulateSuccess();


}
