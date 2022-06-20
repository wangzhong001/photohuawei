package com.xinqidian.adcommon.base;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.ad.banner.BannerInterface;
import com.xinqidian.adcommon.ad.banner.BannerLayout;
import com.xinqidian.adcommon.ad.nativead.NativeLayout;
import com.xinqidian.adcommon.ad.stimulate.StimulateAdInterface;
import com.xinqidian.adcommon.ad.stimulate.StimulateAdLayout;
import com.xinqidian.adcommon.ad.verticalInterstitial.VerticalInterstitialLayout;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.ui.splash.SplashActivity;
import com.xinqidian.adcommon.util.ActivityManagerUtils;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;

import java.lang.ref.WeakReference;


/**
 * Created by lipei on 2018/11/27.
 */

public abstract class BaseNoMvvmActivity extends RxAppCompatActivity implements IBaseActivity, BannerInterface, StimulateAdInterface {


    private BannerLayout bannerLayout;//横幅广告

    private NativeLayout nativeLayout;//原生广告

    private VerticalInterstitialLayout verticalInterstitialLayout;//插屏广告

    private StimulateAdLayout stimulateAdLayout;//激励视频广告


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityManagerUtils.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        //接受页面的参数
        initParam();
        //私有的初始化Databinding和ViewModel方法
        //私有的ViewModel与View的契约事件回调逻辑
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
//        //注册RxBus
//        viewModel.registerRxBus();

    }

    protected abstract int getLayoutId();

    @Override
    public void initParam() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void refeshData() {

    }

    @Override
    public void initViewObservable() {

    }


    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManagerUtils.getInstance().finishActivity(this);
        ActivityManagerUtils.onDestory();


        if (bannerLayout != null && getBannerViewContainer() != null) {
            bannerLayout.destoryAdView();
        }

        if (nativeLayout != null && getNativeViewContainer() != null) {
            nativeLayout.destoryAdView();
        }


        if (stimulateAdLayout != null) {
            stimulateAdLayout.destoryAdView();
        }

        bannerHandler.removeCallbacksAndMessages(null);
        bannerHandler=null;

        nativeHandler.removeCallbacksAndMessages(null);
        nativeHandler=null;


    }


    /**
     * 横幅广告的布局容器。宽度参数建议为match_parent, 高度参数建议为wrap_content
     *
     * @return 用于容纳横幅广告的布局容器
     */

    protected ViewGroup getBannerViewContainer() {
        return (LinearLayout) findViewById(R.id.banner_view_container);
    }

    /**
     * 原生广告的布局容器。宽度参数建议为match_parent, 高度参数建议为wrap_content
     *
     * @return 用于容纳原生广告的布局容器
     */

    protected ViewGroup getNativeViewContainer() {

        return (LinearLayout) findViewById(R.id.native_view_container);
    }


    /**
     * 是否显示横幅广告。默认为true
     *
     * @return
     */
    protected boolean shouldShowBannerView() {
        return Contants.IS_SHOW_BANNER_AD;
    }


    /**
     * 是否显示原生广告。默认为true
     *
     * @return
     */
    protected boolean shouldShowNativeView() {
        return Contants.IS_SHOW_NATIVE_AD;
    }


    /**
     * 是否显示插屏广告。默认为true
     *
     * @return
     */
    protected boolean shouldShowVerticalInterstitialView() {
        return Contants.IS_SHOW_VERTICALINTERSTITIAL_AD;
    }


    /**
     * 是否显示激励广告广告。默认为true
     *
     * @return
     */
    protected boolean shouldShowStimulateAdView() {
        return Contants.IS_SHOW_STIMULATE_AD;
    }


    /**
     * 初始化banner
     */
    private Handler bannerHandler = new Handler();

    private static final class BannerRunable implements Runnable {

        private WeakReference<BaseNoMvvmActivity> mWeakReference;

        private BannerRunable(BaseNoMvvmActivity spotFragment) {
            mWeakReference = new WeakReference<BaseNoMvvmActivity>(spotFragment);

        }

        @Override
        public void run() {
            BaseNoMvvmActivity baseNoMvvmActivity = (BaseNoMvvmActivity) mWeakReference.get();
            baseNoMvvmActivity.initBannerLayout(baseNoMvvmActivity);


        }
    }


    private void initBannerLayout(BaseNoMvvmActivity baseNoMvvmActivity) {
         ViewGroup bannerViewContainer = getBannerViewContainer();
        if (bannerViewContainer != null) {
            if (bannerLayout == null) {
                bannerLayout = new BannerLayout(baseNoMvvmActivity);
                bannerLayout.setBannerInterface(this);
                onAddBannerView(bannerLayout,bannerViewContainer);
                bannerLayout.loadAd();
            }
        }
    }


    /**
     * 添加横幅广告到界面。
     *
     * @param bannerView 横幅广告
     * @param container  容纳横幅广告的容器布局
     */
    public ViewGroup onAddBannerView(View bannerView, ViewGroup container) {
        if (container != null) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            ViewParent parentView = bannerView.getParent();
            if (container.equals(parentView)) {
            } else {
                if (parentView instanceof ViewGroup) {
                    ((ViewGroup) parentView).removeView(bannerView);
                }
                container.addView(bannerView, layoutParams);
            }
        }

        return container;
    }


    /**
     * 初始化原生
     */
    private Handler nativeHandler = new Handler();

    private static final class NativeRunable implements Runnable {

        private WeakReference<BaseNoMvvmActivity> mWeakReference;

        private NativeRunable(BaseNoMvvmActivity spotFragment) {
            mWeakReference = new WeakReference<BaseNoMvvmActivity>(spotFragment);

        }

        @Override
        public void run() {
            BaseNoMvvmActivity baseNoMvvmActivity = (BaseNoMvvmActivity) mWeakReference.get();
            baseNoMvvmActivity.initNativeLayout(baseNoMvvmActivity);


        }
    }


    private void initNativeLayout(BaseNoMvvmActivity baseNoMvvmActivity) {
        ViewGroup nativeViewContainer = getNativeViewContainer();
        if (nativeViewContainer != null) {
            if (nativeLayout == null) {
                nativeLayout = new NativeLayout(baseNoMvvmActivity);
                nativeLayout.setBannerInterface(this);
                onAddBannerView(nativeLayout,nativeViewContainer);
                nativeLayout.loadAd();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (shouldShowBannerView() && !SharedPreferencesUtil.isVip()) {
            bannerHandler.post(new BannerRunable(this));

        }

        if (shouldShowNativeView() && !SharedPreferencesUtil.isVip()) {
            nativeHandler.post(new NativeRunable(this));

        }



        if (shouldShowVerticalInterstitialView() && !SharedPreferencesUtil.isVip()) {
            verticalInterstitialLayout = new VerticalInterstitialLayout(this, this);
            verticalInterstitialLayout.loadAd();
        }


        if (shouldShowStimulateAdView() && !SharedPreferencesUtil.isVip()) {
            stimulateAdLayout = new StimulateAdLayout(this, this);
            stimulateAdLayout.loadAd();


        }
    }

    /**
     * 展示插屏广告
     */

    public void showVerticalInterstitialAd() {
        verticalInterstitialLayout.showAd();
    }


    /**
     * 展示激励视频广告
     */

    public void showStimulateAd() {
        if (stimulateAdLayout != null) {
            stimulateAdLayout.showAd();

        }
    }


    /**
     * 腾讯banner广告回调
     */
    @Override
    public void onNoAD() {

    }

    @Override
    public void onADReceive() {

    }

    @Override
    public void onADExposure() {

    }

    @Override
    public void onADClosed() {

    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADLeftApplication() {

    }

    @Override
    public void onADOpenOverlay() {

    }

    @Override
    public void onADCloseOverlay() {

    }


    /**
     * 小米banner广告回调
     */
    @Override
    public void onAdPresent() {

    }

    @Override
    public void onAdClick() {

    }

    @Override
    public void onAdDismissed() {

    }

    @Override
    public void onAdFailed(String s) {

    }

    @Override
    public void onAdLoaded(int i) {

    }

    @Override
    public void onStimulateSuccess() {

    }


    /**
     * 激励视频回调
     */

    @Override
    public void onStimulateAdClick() {

    }

    @Override
    public void onStimulateAdFailed(String s) {

    }

    @Override
    public void onStimulateAdLoaded(int i) {

    }

    @Override
    public void onStimulateAdSuccess() {
        onStimulateSuccessCall();

    }

    @Override
    public void onStimulateAdPresent() {

    }

    @Override
    public void onStimulateAdDismissed() {

    }

    /**
     * 激励视频看完回调
     */
    public abstract void onStimulateSuccessCall();


}
