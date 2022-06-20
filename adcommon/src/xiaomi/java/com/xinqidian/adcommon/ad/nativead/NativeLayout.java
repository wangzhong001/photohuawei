package com.xinqidian.adcommon.ad.nativead;

import android.content.Context;
import android.support.annotation.NonNull;
import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.xiaomi.ad.common.pojo.AdType;
import com.xinqidian.adcommon.ad.banner.BannerInterface;
import com.xinqidian.adcommon.adview.TwiceFragmentLayout;
import com.xinqidian.adcommon.app.Contants;

/**
 * Created by lipei on 2020/5/1.
 */

public class NativeLayout extends TwiceFragmentLayout implements MimoAdListener {
    private IAdWorker mBannerAd;
    private BannerInterface bannerInterface;
    private boolean isSuccess=false;

    public NativeLayout(@NonNull Context context) {
        super(context);
    }


//    public NativeLayout(Context context, BannerInterface bannerInterface, ViewGroup bannerContainer) {
//        this.context = context;
//        this.bannerInterface = bannerInterface;
//        this.bannerContainer = bannerContainer;
//
//    }


    public void destoryAdView() {
        try {
            if (mBannerAd != null && isSuccess) {
                mBannerAd.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadAd() {
        setComfirmed(!Contants.IS_NEED_COMFIRMED);

        try {
            mBannerAd = AdWorkerFactory.getAdWorker(getContext(), this, this, AdType.AD_TEMPLATE);
            mBannerAd.loadAndShow(Contants.XIAOMI_NATIVE_ID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAdPresent() {

        if(bannerInterface==null){
            return;
        }

        bannerInterface.onAdPresent();

    }

    @Override
    public void onAdClick() {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onAdClick();

    }

    @Override
    public void onAdDismissed() {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onAdDismissed();

    }

    @Override
    public void onAdFailed(String s) {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onAdFailed(s);

    }

    @Override
    public void onAdLoaded(int i) {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onAdLoaded(i);

    }

    @Override
    public void onStimulateSuccess() {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onStimulateSuccess();
        isSuccess=true;

    }

    public void setBannerInterface(BannerInterface bannerInterface) {
        this.bannerInterface = bannerInterface;
    }
}
