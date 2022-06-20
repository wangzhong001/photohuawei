package com.xinqidian.adcommon.ad.banner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.xiaomi.ad.common.pojo.AdType;
import com.xinqidian.adcommon.adview.TwiceFragmentLayout;
import com.xinqidian.adcommon.app.Contants;


/**
 * Created by lipei on 2020/5/1.
 */

public class BannerLayout extends TwiceFragmentLayout implements MimoAdListener {
    private static final String TAG = "BannerLayout";
    private IAdWorker mBannerAd;
    private BannerInterface bannerInterface;
    private boolean isSuccess=false;

    public BannerLayout(@NonNull Context context) {
        super(context);
    }


//    public BannerLayout(Context context, BannerInterface bannerInterface, ViewGroup bannerContainer) {
//        this.context = context;
//        this.bannerInterface = bannerInterface;
//        this.bannerContainer = bannerContainer;
//
//    }

    public void loadAd() {
        setComfirmed(!Contants.IS_NEED_COMFIRMED);


        try {
            mBannerAd = AdWorkerFactory.getAdWorker(getContext(), this, this, AdType.AD_BANNER);
            mBannerAd.loadAndShow(Contants.XIAOMI_BANNER_ID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void destoryAdView() {
        try {
            if (mBannerAd != null && isSuccess ) {
                mBannerAd.recycle();
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
        isSuccess=true;
        Log.d(TAG,"--->onStimulateSuccess");

    }

    public void setBannerInterface(BannerInterface bannerInterface) {
        this.bannerInterface = bannerInterface;
    }
}
