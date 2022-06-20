package com.xinqidian.adcommon.ad.stimulate;

/**
 * Created by lipei on 2020/5/13.
 */

public interface StimulateAdInterface {

    /**
     * 小米banner回调
     */
    void onStimulateAdPresent();

    void onStimulateAdClick();

    void onStimulateAdDismissed();

    void onStimulateAdFailed(String s);

    void onStimulateAdLoaded(int i);

    void onStimulateAdSuccess();

    void onFail();
}
