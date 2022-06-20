package com.xinqidian.adcommon.ad.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.xinqidian.adcommon.adview.TwiceFragmentLayout;


/**
 * Created by lipei on 2020/5/1.
 */

public class BannerLayout extends TwiceFragmentLayout{
    private static final String TAG = "BannerLayout";
    private ViewGroup bannerContainer;
    private Context context;
    private BannerInterface bannerInterface;

    public BannerLayout(@NonNull Context context) {
        super(context);
    }

//
//    public BannerLayout(Context context) {
//        this.context = context;
//        this.bannerInterface = bannerInterface;
//        this.bannerContainer = bannerContainer;
//
//    }

    public void loadAd() {

    }


    public void destoryAdView() {


    }


    public void setBannerInterface(BannerInterface bannerInterface) {
        this.bannerInterface = bannerInterface;
    }


}
