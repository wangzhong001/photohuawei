package com.xinqidian.adcommon.ad.verticalInterstitial;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTInteractionAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.xinqidian.adcommon.TTAdManagerHolder;
import com.xinqidian.adcommon.ad.banner.BannerInterface;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.view.DislikeDialog;

import java.util.List;


/**
 * Created by lipei on 2020/5/9.
 */

public class VerticalInterstitialLayout  {
    private static final String TAG = "VerticalInterstitialLay";
    private BannerInterface bannerInterface;
    private TTAdNative mTTAdNative;
    private Context context;
    private TTAdDislike mTTAdDislike;
    private TTNativeExpressAd mTTAd;
    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;


    public VerticalInterstitialLayout(Context context, BannerInterface bannerInterface) {
        this.bannerInterface = bannerInterface;
        this.context=context;
        mTTAdNative = TTAdManagerHolder.get().createAdNative(context);




    }

    public void loadAd() {
//        //step4:创建插屏广告请求参数AdSlot,具体参数含义参考文档
//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId(Contants.CHUANSHANJIA_INTERSTITIAL_ID)
//                .setSupportDeepLink(true)
//                .setImageAcceptedSize(600, 600) //根据广告平台选择的尺寸，传入同比例尺寸
//                .build();
//        //step5:请求广告，调用插屏广告异步请求接口
//        mTTAdNative.loadInteractionAd(adSlot, new TTAdNative.InteractionAdListener() {
//            @Override
//            public void onError(int code, String message) {
//                KLog.e(TAG,"code: " + code + "  message: " + message);
//            }
//
//            @Override
//            public void onInteractionAdLoad(TTInteractionAd ttInteractionAd) {
//                interactionAd=ttInteractionAd;
//                ttInteractionAd.setAdInteractionListener(new TTInteractionAd.AdInteractionListener() {
//                    @Override
//                    public void onAdClicked() {
//                        KLog.d(TAG, "被点击");
//                    }
//
//                    @Override
//                    public void onAdShow() {
//                        KLog.d(TAG, "被展示");
//                    }
//
//                    @Override
//                    public void onAdDismiss() {
//                        KLog.d(TAG, "插屏广告消失");
//                    }
//                });
//                //如果是下载类型的广告，可以注册下载状态回调监听
//                if (ttInteractionAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
//                    ttInteractionAd.setDownloadListener(new TTAppDownloadListener() {
//                        @Override
//                        public void onIdle() {
//                            KLog.d(TAG, "点击开始下载");
//                        }
//
//                        @Override
//                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                            KLog.d(TAG, "下载中");
//                        }
//
//                        @Override
//                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                            KLog.d(TAG, "下载暂停");
//                        }
//
//                        @Override
//                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                            KLog.d(TAG, "下载失败");
//                        }
//
//                        @Override
//                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                            KLog.d(TAG, "下载完成");
//                        }
//
//                        @Override
//                        public void onInstalled(String fileName, String appName) {
//                            KLog.d(TAG, "安装完成");
//                        }
//                    });
//                }
//                ttInteractionAd.setShowDislikeIcon(new TTAdDislike.DislikeInteractionCallback() {
//                    @Override
//                    public void onSelected(int position, String value) {
//                        //TToast.show(mContext, "反馈了 " + value);
//
//                        KLog.d(TAG, "\t\t\t\t\t\t\t感谢您的反馈!\t\t\t\t\t\t\n我们将为您带来更优质的广告体验");
//
//                    }
//
//                    @Override
//                    public void onCancel() {
////                        TToast.show(mContext, "点击取消 ");
//                        KLog.d(TAG, "点击取消");
//
//                    }
//
//                    @Override
//                    public void onRefuse() {
//                        KLog.d(TAG, "您已成功提交反馈，请勿重复提交哦!");
//
//                    }
//                });
//                //弹出插屏广告
//
//            }
//        });


        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(Contants.CHUANSHANJIA_INTERSTITIAL_ID) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(3) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(300, 300) //期望模板广告view的size,单位dp
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
//                TToast.show(InteractionExpressActivity.this, "load error : " + code + ", " + message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                mTTAd.render();
            }
        });

    }

    public void showAd() {
//        KLog.e("interactionAd-->",interactionAd);
//        if(interactionAd!=null){
//            interactionAd.showInteractionAd((Activity) context);
//
//        }
        if(mTTAd!=null){
            mTTAd.showInteractionExpressAd((Activity) context);

        }

    }



    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
            @Override
            public void onAdDismiss() {
//                TToast.show(mContext, "广告关闭");
            }

            @Override
            public void onAdClicked(View view, int type) {
//                TToast.show(mContext, "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
//                TToast.show(mContext, "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
//                TToast.show(mContext, msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
//                TToast.show(mContext, "渲染成功");

            }
        });
//        bindDislike(ad, true);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
//                TToast.show(InteractionExpressActivity.this, "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
//                    TToast.show(InteractionExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
            }
        });
    }

    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(context, words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
//                    TToast.show(mContext, "点击 " + filterWord.getName());
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback((Activity) context, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                //TToast.show(mContext, "反馈了 " + value);
//                TToast.show(mContext,"\t\t\t\t\t\t\t感谢您的反馈!\t\t\t\t\t\t\n我们将为您带来更优质的广告体验",3);
            }

            @Override
            public void onCancel() {
//                TToast.show(mContext, "点击取消 ");
            }

            @Override
            public void onRefuse() {
//                TToast.show(mContext,"您已成功提交反馈，请勿重复提交哦！",3);
            }

        });
    }

}
