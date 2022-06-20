package com.xinqidian.adcommon.ad.banner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTBannerAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.xinqidian.adcommon.TTAdManagerHolder;
import com.xinqidian.adcommon.adview.TwiceFragmentLayout;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.util.KLog;

import java.util.List;

/**
 * Created by lipei on 2020/5/1.
 */

public class BannerLayout extends TwiceFragmentLayout {
    private static final String TAG = "BannerLayout";
    private TTAdNative mTTAdNative;
    //    private ViewGroup bannerContainer;
    private BannerInterface bannerInterface;
    private boolean isHasLoad = false;
    private TTNativeExpressAd mTTAd;
    private long startTime = 0;



    public BannerLayout(@NonNull Context context) {
        super(context);
        setComfirmed(!Contants.IS_NEED_COMFIRMED);
        mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
//        TTAdManagerHolder.get().requestPermissionIfNecessary(context);

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

    public void destoryAdView() {


    }

    public void loadAd() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadBannerAd();

            }
        },1000);



    }


    public void setBannerInterface(BannerInterface bannerInterface) {
        this.bannerInterface = bannerInterface;
    }


    private void loadBannerAd() {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(Contants.CHUANSHANJIA_BANNER_ID) //广告位id
                .setSupportDeepLink(true)
                .setExpressViewAcceptedSize(600, 50) //期望模板广告view的size,单位dp

                .build();
        //step5:请求广告，对请求回调的广告作渲染处理


        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                mTTAd.render();
            }
        });


//        mTTAdNative.loadBannerAd(adSlot, new TTAdNative.BannerAdListener() {
//
//            @Override
//            public void onError(int code, String message) {
//               removeAllViews();
//            }
//
//            @Override
//            public void onBannerAdLoad(final TTBannerAd ad) {
//                if (ad == null) {
//                    return;
//                }
//                View bannerView = ad.getBannerView();
//                if (bannerView == null) {
//                    return;
//                }
//                //设置轮播的时间间隔  间隔在30s到120秒之间的值，不设置默认不轮播
//                ad.setSlideIntervalTime(30 * 1000);
//                removeAllViews();
//                addView(bannerView);
//                //设置广告互动监听回调
//                ad.setBannerInteractionListener(new TTBannerAd.AdInteractionListener() {
//                    @Override
//                    public void onAdClicked(View view, int type) {
//                        KLog.e(TAG,"广告被点击");
//                    }
//
//                    @Override
//                    public void onAdShow(View view, int type) {
//                        KLog.e(TAG,"广告展示");
//                    }
//                });
//                //（可选）设置下载类广告的下载监听
//                bindDownloadListener(ad);
//                //在banner中显示网盟提供的dislike icon，有助于广告投放精准度提升
//                ad.setShowDislikeIcon(new TTAdDislike.DislikeInteractionCallback() {
//                    @Override
//                    public void onSelected(int position, String value) {
//                        KLog.e(TAG,"点击"+value);
//
//                        //用户选择不喜欢原因后，移除广告展示
//                        removeAllViews();
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        KLog.e(TAG,"点击取消");
//
//                    }
//
//                    @Override
//                    public void onRefuse() {
//
//                    }
//                });
//
//                //获取网盟dislike dialog，您可以在您应用中本身自定义的dislike icon 按钮中设置 mTTAdDislike.showDislikeDialog();
//                /*mTTAdDislike = ad.getDislikeDialog(new TTAdDislike.DislikeInteractionCallback() {
//                        @Override
//                        public void onSelected(int position, String value) {
//                            TToast.show(mContext, "点击 " + value);
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            TToast.show(mContext, "点击取消 ");
//                        }
//                    });
//                if (mTTAdDislike != null) {
//                    XXX.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mTTAdDislike.showDislikeDialog();
//                        }
//                    });
//                } */
//
//            }
//        });
    }

    private boolean mHasShowDownloadActive = false;

    private void bindDownloadListener(TTBannerAd ad) {
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                KLog.e(TAG, "点击图片开始下载");

            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    KLog.e(TAG, "下载中，点击图片暂停");

                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                KLog.e(TAG, "下载暂停，点击图片继续");

            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                KLog.e(TAG, "下载失败，点击图片重新下载");

            }

            @Override
            public void onInstalled(String fileName, String appName) {
                KLog.e(TAG, "安装完成，点击图片打开");

            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                KLog.e(TAG, "点击图片安装");

            }
        });
    }


    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                KLog.e(TAG, "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                KLog.e(TAG, "广告展示");

            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
                KLog.e(TAG, msg + " code:" + code);

            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
                KLog.e(TAG, "渲染成功");
                removeAllViews();
                addView(view);
            }
        });
        //dislike设置
//        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                KLog.e(TAG, "点击图片开始下载");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    KLog.e(TAG, "下载中，点击图片暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                KLog.e(TAG, "下载暂停，点击图片继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                KLog.e(TAG, "下载失败，点击图片重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                KLog.e(TAG, "安装完成，点击图片打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                KLog.e(TAG, "点击图片安装");
            }
        });
    }

//    /**
//     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
//     *
//     * @param ad
//     * @param customStyle 是否自定义样式，true:样式自定义
//     */
//    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
//        if (customStyle) {
//            //使用自定义样式
//            List<FilterWord> words = ad.getFilterWords();
//            if (words == null || words.isEmpty()) {
//                return;
//            }
//
//            final DislikeDialog dislikeDialog = new DislikeDialog(this, words);
//            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
//                @Override
//                public void onItemClick(FilterWord filterWord) {
//                    //屏蔽广告
//                    TToast.show(mContext, "点击 " + filterWord.getName());
//                    //用户选择不喜欢原因后，移除广告展示
//                    mExpressContainer.removeAllViews();
//                }
//            });
//            ad.setDislikeDialog(dislikeDialog);
//            return;
//        }
//        //使用默认模板中默认dislike弹出样式
//        ad.setDislikeCallback(BannerExpressActivity.this, new TTAdDislike.DislikeInteractionCallback() {
//            @Override
//            public void onSelected(int position, String value) {
//                TToast.show(mContext, "点击 " + value);
//                //用户选择不喜欢原因后，移除广告展示
//                mExpressContainer.removeAllViews();
//            }
//
//            @Override
//            public void onCancel() {
//                TToast.show(mContext, "点击取消 ");
//            }
//
//            @Override
//            public void onRefuse() {
//
//            }
//
//        });
//    }


}
