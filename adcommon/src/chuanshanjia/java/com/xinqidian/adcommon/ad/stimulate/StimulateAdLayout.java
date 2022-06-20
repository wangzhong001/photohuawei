package com.xinqidian.adcommon.ad.stimulate;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.xinqidian.adcommon.TTAdManagerHolder;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.util.KLog;

/**
 * Created by lipei on 2020/5/13.
 */

public class StimulateAdLayout {
    private static final String TAG = "StimulateAdLayout";
    private Context context;
    private StimulateAdInterface stimulateAdInterface;

    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mHasShowDownloadActive = false;


    public StimulateAdLayout(Context context, StimulateAdInterface stimulateAdInterface) {
        this.context = context;
        this.stimulateAdInterface = stimulateAdInterface;
        mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
//        TTAdManagerHolder.get().requestPermissionIfNecessary(context);

    }


    public void loadAd() {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档

        //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(Contants.CHUANSHANJIA_STIMULATE_ID)
                .setSupportDeepLink(true)
                .setRewardName("奖励次数领取成功") //奖励的名称
                .setRewardAmount(3)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(TTAdConstant.VERTICAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();


        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                KLog.e(TAG, "Callback --> onError: " + code + ", " + String.valueOf(message));
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                KLog.e(TAG, "Callback --> onRewardVideoCached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                KLog.e(TAG, "Callback --> onRewardVideoAdLoad");

                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        KLog.d(TAG, "Callback --> rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        KLog.d(TAG, "Callback --> rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        KLog.d(TAG, "Callback --> rewardVideoAd close");
                        if(stimulateAdInterface!=null){
                            stimulateAdInterface.onStimulateAdDismissed();

                        }

                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        KLog.d(TAG, "Callback --> rewardVideoAd complete");
                        if(stimulateAdInterface!=null){
                            stimulateAdInterface.onStimulateAdSuccess();

                        }

                    }

                    @Override
                    public void onVideoError() {
                        KLog.e(TAG, "Callback --> rewardVideoAd error");
                        if(stimulateAdInterface!=null){
                            stimulateAdInterface.onFail();
                        }
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        String logString = "verify:" + rewardVerify + " amount:" + rewardAmount +
                                " name:" + rewardName;
                        KLog.e(TAG, "Callback --> " + logString);

                    }

                    @Override
                    public void onSkippedVideo() {
                        KLog.e(TAG, "Callback --> rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        KLog.d("DML", "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);

                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        KLog.d("DML", "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        KLog.d("DML", "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        KLog.d("DML", "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        KLog.d("DML", "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
                    }
                });
            }
        });

        //step5:请求广告


    }


    public void showAd() {
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示,强烈建议在onRewardVideoCached回调后，展示广告，提升播放体验
            //该方法直接展示广告
                    mttRewardVideoAd.showRewardVideoAd((Activity) context);

            //展示广告，并传入广告展示的场景
//            mttRewardVideoAd.showRewardVideoAd((Activity) context, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
//            mttRewardVideoAd = null;
        } else {
            stimulateAdInterface.onFail();

        }

    }

    public void destoryAdView() {

    }


}
