package com.xinqidian.adcommon.ad.verticalInterstitial;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener;
import com.qq.e.comm.util.AdError;
import com.xinqidian.adcommon.ad.banner.BannerInterface;
import com.xinqidian.adcommon.app.Contants;


/**
 * Created by lipei on 2020/5/9.
 */

public class VerticalInterstitialLayout implements UnifiedInterstitialADListener, UnifiedInterstitialMediaListener {
    private UnifiedInterstitialAD unifiedInterstitialAD;
    private BannerInterface bannerInterface;
    private Context context;

    public VerticalInterstitialLayout(Context context, BannerInterface bannerInterface) {
        this.bannerInterface = bannerInterface;
        this.context=context;

        unifiedInterstitialAD = new UnifiedInterstitialAD((Activity) context, Contants.TENCENT_APPID, Contants.TENCENT_INTERSTITIAL_ID, this);
        setVideoOption();

    }

    public void loadAd() {
        if (unifiedInterstitialAD != null) {
            unifiedInterstitialAD.loadAD();
        }
    }

    public void showAd() {
        if (unifiedInterstitialAD != null) {
            unifiedInterstitialAD.showAsPopupWindow((Activity) context);

        }
    }


    private void setVideoOption() {
        VideoOption.Builder builder = new VideoOption.Builder();
        VideoOption option = builder.build();

        unifiedInterstitialAD.setVideoOption(option);
        unifiedInterstitialAD.setMinVideoDuration(10);
        unifiedInterstitialAD.setMaxVideoDuration(1000);

        /**
         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前调用setVideoPlayPolicy，有助于提高视频广告的eCPM值 <br/>
         * 如果广告位仅支持图文广告，则无需调用
         */

        /**
         * 设置本次拉取的视频广告，从用户角度看到的视频播放策略<p/>
         *
         * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br/>
         *
         * 如自动播放策略为AutoPlayPolicy.WIFI，但此时用户网络为4G环境，在用户看来就是手工播放的
         */
        unifiedInterstitialAD.setVideoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI);
    }


    @Override
    public void onADReceive() {

    }

    @Override
    public void onVideoCached() {

    }

    @Override
    public void onNoAD(AdError adError) {

    }

    @Override
    public void onADOpened() {

    }

    @Override
    public void onADExposure() {

    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADLeftApplication() {

    }

    @Override
    public void onADClosed() {

    }

    @Override
    public void onVideoInit() {

    }

    @Override
    public void onVideoLoading() {

    }

    @Override
    public void onVideoReady(long l) {

    }

    @Override
    public void onVideoStart() {

    }

    @Override
    public void onVideoPause() {

    }

    @Override
    public void onVideoComplete() {

    }

    @Override
    public void onVideoError(AdError adError) {

    }

    @Override
    public void onVideoPageOpen() {

    }

    @Override
    public void onVideoPageClose() {

    }
}
