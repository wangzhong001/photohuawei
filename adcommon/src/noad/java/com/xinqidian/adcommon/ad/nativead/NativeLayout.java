package com.xinqidian.adcommon.ad.nativead;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.xinqidian.adcommon.ad.banner.BannerInterface;
import com.xinqidian.adcommon.adview.TwiceFragmentLayout;

/**
 * Created by lipei on 2020/5/1.
 */

public class NativeLayout extends TwiceFragmentLayout {
    private ViewGroup bannerContainer;
    private Context context;
    private BannerInterface bannerInterface;

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

    }

    public void loadAd() {

    }


    public void setBannerInterface(BannerInterface bannerInterface) {
        this.bannerInterface = bannerInterface;
    }
}
