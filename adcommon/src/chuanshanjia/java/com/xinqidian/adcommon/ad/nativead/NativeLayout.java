package com.xinqidian.adcommon.ad.nativead;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.xinqidian.adcommon.ad.banner.BannerInterface;
import com.xinqidian.adcommon.adview.TwiceFragmentLayout;
import com.xinqidian.adcommon.app.Contants;

import java.util.List;

/**
 * Created by lipei on 2020/5/9.
 */

public class NativeLayout extends TwiceFragmentLayout  {

    private BannerInterface bannerInterface;

    public NativeLayout(@NonNull Context context) {
        super(context);
        setComfirmed(!Contants.IS_NEED_COMFIRMED);


    }




    public void loadAd(){

    }

    public void destoryAdView(){

    }



//    private void loadBannerAd(String codeId) {
//        //step4:创建广告请求参数AdSlot,注意其中的setNativeAdtype方法，具体参数含义参考文档
//        final AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId(codeId)
//                .setSupportDeepLink(true)
//                .setImageAcceptedSize(600, 257)
//                .setNativeAdType(AdSlot.TYPE_BANNER) //请求原生广告时候，请务必调用该方法，设置参数为TYPE_BANNER或TYPE_INTERACTION_AD
//                .setAdCount(1)
//                .build();
//
//        //step5:请求广告，对请求回调的广告作渲染处理
//        mTTAdNative.loadNativeAd(adSlot, new TTAdNative.NativeAdListener() {
//            @Override
//            public void onError(int code, String message) {
//                TToast.show(NativeBannerActivity.this, "load error : " + code + ", " + message);
//            }
//
//            @Override
//            public void onNativeAdLoad(List<TTNativeAd> ads) {
//                if (ads.get(0) == null) {
//                    return;
//                }
//                View bannerView = LayoutInflater.from(mContext).inflate(R.layout.native_ad, mBannerContainer, false);
//                if (bannerView == null) {
//                    return;
//                }
//                if (mCreativeButton != null) {
//                    //防止内存泄漏
//                    mCreativeButton = null;
//                }
//                mBannerContainer.removeAllViews();
//                mBannerContainer.addView(bannerView);
//                //绑定原生广告的数据
//                setAdData(bannerView, ads.get(0));
//            }
//        });
//    }
//



    public void setBannerInterface(BannerInterface bannerInterface) {
        this.bannerInterface = bannerInterface;
    }
}
