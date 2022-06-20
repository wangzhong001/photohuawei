package com.xinqidian.adcommon.ad.nativead;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;
import com.xinqidian.adcommon.ad.banner.BannerInterface;
import com.xinqidian.adcommon.adview.TwiceFragmentLayout;
import com.xinqidian.adcommon.app.Contants;

import java.util.List;

/**
 * Created by lipei on 2020/5/9.
 */

public class NativeLayout extends TwiceFragmentLayout implements NativeExpressAD.NativeExpressADListener {
    private NativeExpressAD nativeExpressAD;
    private NativeExpressADView nativeExpressADView;
    private BannerInterface bannerInterface;
    private boolean isHasLoad=false;

    public NativeLayout(@NonNull Context context) {
        super(context);
        setComfirmed(!Contants.IS_NEED_COMFIRMED);

        nativeExpressAD=new NativeExpressAD(context,new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), Contants.TENCENT_APPID,Contants.TENCENT_NATIVE_ID,this);
//
        // 注意：如果您在平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音


                .build()); //

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
        nativeExpressAD.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO); // 本次拉回的视频广告，从用户的角度看是自动播放的


        nativeExpressAD.setMaxVideoDuration(1000);
    }


//    public NativeLayout(Context context , BannerInterface bannerInterface, ViewGroup bannerContainer){
//        this.context=context;
//        this.bannerContainer=bannerContainer;
//        nativeExpressAD=new NativeExpressAD(context,new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), Contants.TENCENT_APPID,Contants.TENCENT_NATIVE_ID,this);
//
//        // 注意：如果您在平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
//        nativeExpressAD.setVideoOption(new VideoOption.Builder()
//                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
//                .setAutoPlayMuted(true) // 自动播放时为静音
//
//
//                .build()); //
//
//        /**
//         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前调用setVideoPlayPolicy，有助于提高视频广告的eCPM值 <br/>
//         * 如果广告位仅支持图文广告，则无需调用
//         */
//
//        /**
//         * 设置本次拉取的视频广告，从用户角度看到的视频播放策略<p/>
//         *
//         * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br/>
//         *
//         * 如自动播放策略为AutoPlayPolicy.WIFI，但此时用户网络为4G环境，在用户看来就是手工播放的
//         */
//        nativeExpressAD.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO); // 本次拉回的视频广告，从用户的角度看是自动播放的
//
//
//        nativeExpressAD.setMaxVideoDuration(1000);
//    }

    public void loadAd(){
        if(nativeExpressAD!=null&&!isHasLoad){
            nativeExpressAD.loadAD(1);

        }
    }

    public void destoryAdView(){

    }

    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        // 释放前一个 NativeExpressADView 的资源
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
        // 3.返回数据后，SDK 会返回可以用于展示 NativeExpressADView 列表
        nativeExpressADView = list.get(0);
        if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            nativeExpressADView.setMediaListener(mediaListener);
        }
        nativeExpressADView.render();
        if (this.getChildCount() > 0) {
            this.removeAllViews();
        }

        // 需要保证 View 被绘制的时候是可见的，否则将无法产生曝光和收益。
        this.addView(nativeExpressADView);

    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
        isHasLoad=true;

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {
        this.setVisibility(GONE);

    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onNoAD(AdError adError) {
        this.setVisibility(GONE);

    }



    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoCached(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {

        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
        }
    };

    public void setBannerInterface(BannerInterface bannerInterface) {
        this.bannerInterface = bannerInterface;
    }
}
