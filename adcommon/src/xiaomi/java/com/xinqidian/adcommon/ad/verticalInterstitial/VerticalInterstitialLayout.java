package com.xinqidian.adcommon.ad.verticalInterstitial;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.xiaomi.ad.common.pojo.AdType;
import com.xinqidian.adcommon.ad.banner.BannerInterface;
import com.xinqidian.adcommon.app.Contants;

/**
 * Created by lipei on 2020/5/9.
 */

public class VerticalInterstitialLayout implements MimoAdListener {
    private IAdWorker mAdWorker;
    private BannerInterface bannerInterface;

    public VerticalInterstitialLayout(Context context , BannerInterface bannerInterface){
        this.bannerInterface=bannerInterface;
        try {
            mAdWorker= AdWorkerFactory.getAdWorker(context, (ViewGroup) ((Activity) context).getWindow().getDecorView(),this,AdType.AD_INTERSTITIAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadAd(){
        if(mAdWorker!=null){
            try {
                if (!mAdWorker.isReady()) {
                    mAdWorker.load(Contants.XIAOMI_INTERSTITIAL_ID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showAd(){
        try {
            if(mAdWorker!=null&& mAdWorker.isReady()){
                try {
                    mAdWorker.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAdPresent() {
        bannerInterface.onAdPresent();

    }

    @Override
    public void onAdClick() {
        bannerInterface.onAdClick();

    }

    @Override
    public void onAdDismissed() {
        bannerInterface.onAdDismissed();

    }

    @Override
    public void onAdFailed(String s) {
        bannerInterface.onAdFailed(s);

    }

    @Override
    public void onAdLoaded(int i) {
        bannerInterface.onAdLoaded(i);

    }

    @Override
    public void onStimulateSuccess() {
        bannerInterface.onStimulateSuccess();

    }
}
