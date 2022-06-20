package com.xinqidian.adcommon.ad.banner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;
import com.xinqidian.adcommon.adview.TwiceFragmentLayout;
import com.xinqidian.adcommon.app.Contants;

/**
 * Created by lipei on 2020/5/1.
 */

public class BannerLayout extends TwiceFragmentLayout implements UnifiedBannerADListener {
    private static final String TAG = "BannerLayout";
    private UnifiedBannerView bv;
//    private ViewGroup bannerContainer;
    private BannerInterface bannerInterface;
    private boolean isHasLoad=false;

    public BannerLayout(@NonNull Context context) {
        super(context);
        setComfirmed(!Contants.IS_NEED_COMFIRMED);

    }

//
//    public BannerLayout(Context context ,BannerInterface bannerInterface,ViewGroup bannerContainer){
//        this.context=context;
//        this.bannerInterface=bannerInterface;
//        this.bannerContainer=bannerContainer;
//
//
//
//    }

    public void destoryAdView(){
        if(bv!=null){
            bv.destroy();
        }
    }

    public void loadAd(){
        if(!isHasLoad){
            getBanner().loadAD();


        }

    }


    private UnifiedBannerView getBanner() {
        if(this.bv != null){
            this.removeView(bv);
            bv.destroy();
        }
        String posId = Contants.TENCENT_BANNER_ID;
        this.bv = new UnifiedBannerView((Activity) getContext(), Contants.TENCENT_APPID, posId, this);
        bv.setRefresh(30);
        // 不需要传递tags使用下面构造函数
        // this.bv = new UnifiedBannerView(this, Constants.APPID, posId, this);
        this.addView(bv, getUnifiedBannerLayoutParams());
        return this.bv;
    }


    /**
     * banner2.0规定banner宽高比应该为6.4:1 , 开发者可自行设置符合规定宽高比的具体宽度和高度值
     *
     * @return
     */
    private FrameLayout.LayoutParams getUnifiedBannerLayoutParams() {
        Point screenSize = new Point();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new FrameLayout.LayoutParams(screenSize.x,  Math.round(screenSize.x / 6.4F));
    }

    @Override
    public void onNoAD(AdError adError) {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onNoAD();
        this.setVisibility(GONE);

    }

    @Override
    public void onADReceive() {
        isHasLoad=true;
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onADReceive();

    }

    @Override
    public void onADExposure() {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onADExposure();

    }

    @Override
    public void onADClosed() {
        if(bannerInterface==null){
            return;
        }
        this.setVisibility(GONE);
        bannerInterface.onADClosed();

    }

    @Override
    public void onADClicked() {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onADClicked();

    }

    @Override
    public void onADLeftApplication() {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onADLeftApplication();

    }

    @Override
    public void onADOpenOverlay() {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onADOpenOverlay();

    }

    @Override
    public void onADCloseOverlay() {
        if(bannerInterface==null){
            return;
        }
        bannerInterface.onADCloseOverlay();

    }

    public void setBannerInterface(BannerInterface bannerInterface) {
        this.bannerInterface = bannerInterface;
    }
}
