package com.xinqidian.adcommon.base;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.ad.banner.BannerInterface;
import com.xinqidian.adcommon.ad.banner.BannerLayout;
import com.xinqidian.adcommon.ad.nativead.NativeLayout;
import com.xinqidian.adcommon.ad.stimulate.StimulateAdInterface;
import com.xinqidian.adcommon.ad.stimulate.StimulateAdLayout;
import com.xinqidian.adcommon.ad.verticalInterstitial.VerticalInterstitialInterface;
import com.xinqidian.adcommon.ad.verticalInterstitial.VerticalInterstitialLayout;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.databinding.ActivityBaseBinding;
import com.xinqidian.adcommon.login.UserModel;
import com.xinqidian.adcommon.util.ACStatusBarUtil;
import com.xinqidian.adcommon.util.ActivityManagerUtils;
import com.xinqidian.adcommon.util.MyStatusBarUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.CommentDialog;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by lipei on 2018/11/27.
 */

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity implements IBaseActivity, BannerInterface, StimulateAdInterface {
    protected VM viewModel;
    protected V binding;
    private ActivityBaseBinding baseBinding;
    private String titleName;
    private int tabar = View.GONE;
    private boolean isEmptyFirst = true;
    private View emptyView;
    private View netErrorView;
    private boolean isNetErrorFirst = true;
    private boolean isHasSet = false;
    private boolean isWhite = true;
    private WeakReference<TitleViewModel> titleViewModel;

    private BannerLayout bannerLayout;//横幅广告

    private NativeLayout nativeLayout;//原生广告

    private VerticalInterstitialLayout verticalInterstitialLayout;//插屏广告

    private StimulateAdLayout stimulateAdLayout;//激励视频广告

    private CommentDialog commentDialog;//给好评弹框

    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityManagerUtils.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        mContext = this;
        //接受页面的参数
        initParam();
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
//        //注册RxBus
//        viewModel.registerRxBus();

    }


    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
//        if (!isWhite) {
//            ACStatusBarUtil.transparencyBar(this);//透明
//        } else {
//            MyStatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
//            MyStatusBarUtil.StatusBarLightMode(this);
////            ACStatusBarUtil.StatusBarLightMode(this); //设置白底黑字
//        }
        if (!isWhite) {
//            ACStatusBarUtil.transparencyBar(this);//透明
        } else {
            MyStatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
            MyStatusBarUtil.StatusBarLightMode(this);
//            ACStatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        }
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);

        }
        baseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        //创建TitleViewModel
        titleViewModel = new WeakReference<TitleViewModel>(createViewModel(this, TitleViewModel.class));
        titleViewModel.get().titleName.set(titleName);
        titleViewModel.get().tabar.set(tabar);
        titleViewModel.get().isWhite.set(isWhite);
        titleViewModel.get().backClick = new BindingCommand(new BindingAction() {
            @Override
            public void call() {
                finish();
            }
        });

        baseBinding.setBaseViewModel(titleViewModel.get());

        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包

        binding = DataBindingUtil.inflate(getLayoutInflater(), initContentView(savedInstanceState), null, false);
        binding.setVariable(initVariableId(), viewModel);
        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        binding.getRoot().setLayoutParams(params);
        baseBinding.contentLl.addView(binding.getRoot());
        getWindow().setContentView(baseBinding.getRoot());


        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this);


    }


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


    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setTabar(int tabar) {
        this.tabar = tabar;
    }

    public void setIsWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }


    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }


    /**
     * =====================================================================
     **/
    //注册ViewModel与View的契约UI回调事件
    private void registorUIChangeLiveDataCallBack() {
//        //加载对话框显示
//        viewModel.getUiChangeLiveData().getShowDialogEvent().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String title) {
//                showDialog(title);
//            }
//        });
//        //加载对话框消失
//        viewModel.getUiChangeLiveData().getDismissDialogEvent().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(@Nullable Boolean aBoolean) {
////                dismissDialog();
//            }
//        });
        //跳入新页面
        viewModel.getUiChangeLiveData().getStartActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startActivity(clz, bundle);
            }
        });

        //关闭界面
        viewModel.getUiChangeLiveData().getFinishEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                finish();
            }
        });

        //网络请求成功且数据不为空
        viewModel.getUiChangeLiveData().getSuccessEvent().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                successLayout();
            }
        });

        //网络请求失败
        viewModel.getUiChangeLiveData().getFailEvent().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                netErrorLayout();
            }
        });


        //网络请求成功数据为空
        viewModel.getUiChangeLiveData().getEmptyEvent().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                emptyLayout();
            }
        });


        /**
         * 登录状态回调 根据状态来更改ui或者逻辑
         */
        LiveDataBus.get().with(LiveBusConfig.login, Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                setLoginState(aBoolean);

            }
        });


        /**
         * 支付宝支付成功回调
         */
        LiveDataBus.get().with(LiveBusConfig.alipaySuccess, Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                setAlipaySuccess(aBoolean);

            }
        });


        /**
         * 登录成功之后返回的用户信息
         */
        LiveDataBus.get().with(LiveBusConfig.userData, UserModel.DataBean.class).observe(this, new Observer<UserModel.DataBean>() {
            @Override
            public void onChanged(@Nullable UserModel.DataBean dataBean) {
                setUserData(dataBean);
            }
        });


    }


    /**
     * 根据配置信息显示评论弹框
     *
     * @param comment
     */

    public void showCommentFromFeatureDialog(String comment) {
        boolean isShowCommnetDialog = (boolean) SharedPreferencesUtil.getParam(Contants.IS_SHOW_COMMENT_DIALOG, true);
        if (isShowCommnetDialog) {
            int number = (int) SharedPreferencesUtil.getParam(Contants.COMMENT_NUMBER_STRING, 0);
            if (number == getCommnetNumber()) {
                if (commentDialog == null) {
                    commentDialog = new CommentDialog(this, comment,true);
                }
                commentDialog.show();
                SharedPreferencesUtil.setParam(Contants.COMMENT_NUMBER_STRING, 0);
            } else {
                number += 1;
                SharedPreferencesUtil.setParam(Contants.COMMENT_NUMBER_STRING, number);
            }

        }


    }


    /**
     * 直接显示评论弹框
     *
     * @param comment
     */

    public void showCommentDialog(String comment) {

        if (commentDialog == null) {
            commentDialog = new CommentDialog(this, comment);
        }
        commentDialog.show();


    }


    /**
     * 设置登录状态
     *
     * @param loginState
     */
    public void setLoginState(boolean loginState) {

    }


    /**
     * 设置支付宝支付成功
     *
     * @param alipaySuccessState
     */
    public void setAlipaySuccess(boolean alipaySuccessState) {

    }


    /**
     * 设置用户信息
     */

    public void setUserData(UserModel.DataBean data) {
    }

    ;


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
        //解除Messenger注册
//        Messenger.getDefault().unregister(viewModel);
        //解除ViewModel生命周期感应
        commentDialog = null;

        getLifecycle().removeObserver(viewModel);
//        viewModel.removeRxBus();
        viewModel = null;
        if (titleViewModel.get().backClick != null) {
            titleViewModel.get().backClick = null;
        }
        titleViewModel.clear();
        titleViewModel = null;
        binding.unbind();
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


//        AppManager.getAppManager().finishActivity();

    }


    /**
     * 没有数据的时候
     */
    private void emptyLayout() {
        if (isEmptyFirst) {
            emptyView = baseBinding.emptyViewstub.getViewStub().inflate();
            isEmptyFirst = false;
        } else {
            if (emptyView != null && emptyView.getVisibility() == View.GONE) {
                emptyView.setVisibility(View.VISIBLE);
            }
        }

        if (baseBinding.contentLl.getVisibility() == View.VISIBLE) {
            baseBinding.contentLl.setVisibility(View.GONE);
        }

        if (netErrorView != null && netErrorView.getVisibility() == View.VISIBLE) {
            netErrorView.setVisibility(View.GONE);
        }

    }


    /**
     * 没有网络的时候
     */
    private void netErrorLayout() {
        if (isNetErrorFirst) {
            netErrorView = baseBinding.netErrorView.getViewStub().inflate();
            LinearLayout net_error_ll = netErrorView.findViewById(R.id.net_error_ll);
            net_error_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refeshData();
                }
            });
            isNetErrorFirst = false;
        } else {
            if (netErrorView != null && netErrorView.getVisibility() == View.GONE) {
                netErrorView.setVisibility(View.VISIBLE);
            }
        }

        if (baseBinding.contentLl.getVisibility() == View.VISIBLE) {
            baseBinding.contentLl.setVisibility(View.GONE);
        }

        if (emptyView != null && emptyView.getVisibility() == View.VISIBLE) {
            emptyView.setVisibility(View.GONE);
        }

    }


    /**
     * 请求成功
     */
    private void successLayout() {

        if (emptyView != null && emptyView.getVisibility() == View.VISIBLE) {
            emptyView.setVisibility(View.GONE);

        }

        if (baseBinding.contentLl != null && baseBinding.contentLl.getVisibility() == View.GONE) {
            baseBinding.contentLl.setVisibility(View.VISIBLE);
        }

        if (netErrorView != null && netErrorView.getVisibility() == View.VISIBLE) {
            netErrorView.setVisibility(View.GONE);
        }

    }


    /**
     * 横幅广告的布局容器。宽度参数建议为match_parent, 高度参数建议为wrap_content
     *
     * @return 用于容纳横幅广告的布局容器
     */

    protected ViewGroup getBannerViewContainer() {
        return (ViewGroup) findViewById(R.id.banner_view_container);
    }

    /**
     * 原生广告的布局容器。宽度参数建议为match_parent, 高度参数建议为wrap_content
     *
     * @return 用于容纳原生广告的布局容器
     */

    protected ViewGroup getNativeViewContainer() {

        return (ViewGroup) findViewById(R.id.native_view_container);
    }


    /**
     * 激励视频广告广告的布局容器。宽度参数建议为match_parent, 高度参数建议为wrap_content
     *
     * @return 用于容纳原生广告的布局容器
     */

    protected ViewGroup getStimulateAdViewContainer() {

        return (ViewGroup) findViewById(R.id.stimulate_view_container);
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
     * 好评弹框次数
     *
     * @return
     */
    protected int getCommnetNumber() {
        return Contants.COMMENT_NUMBER;
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

        private WeakReference<BaseActivity> mWeakReference;

        private BannerRunable(BaseActivity spotFragment) {
            mWeakReference = new WeakReference<BaseActivity>(spotFragment);

        }

        @Override
        public void run() {
            BaseActivity baseActivity = (BaseActivity) mWeakReference.get();
            baseActivity.initBannerLayout(baseActivity);


        }
    }


    private void initBannerLayout(BaseActivity baseActivity) {
        ViewGroup bannerViewContainer = getBannerViewContainer();


        if (bannerViewContainer != null) {

            if (bannerLayout == null) {
                bannerLayout = new BannerLayout(baseActivity);
                bannerLayout.setBannerInterface(this);
                onAddBannerView(bannerLayout, bannerViewContainer);
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

        private WeakReference<BaseActivity> mWeakReference;

        private NativeRunable(BaseActivity spotFragment) {
            mWeakReference = new WeakReference<BaseActivity>(spotFragment);

        }

        @Override
        public void run() {
            BaseActivity baseActivity = (BaseActivity) mWeakReference.get();
            baseActivity.initNativeLayout(baseActivity);


        }
    }


    private void initNativeLayout(BaseActivity baseActivity) {
        ViewGroup nativeViewContainer = getNativeViewContainer();
        if (nativeViewContainer != null) {
            if (nativeLayout == null) {
                nativeLayout = new NativeLayout(baseActivity);
                nativeLayout.setBannerInterface(baseActivity);
                onAddBannerView(nativeLayout, nativeViewContainer);
                nativeLayout.loadAd();
            }
        }
    }


    /**
     * 避免重复初始化广告
     */

    private boolean isHasShowAd;


    /**
     * 是否展示横幅广告 避免不需要展示广告的Fragment去初始化横幅广告
     *
     * @return
     */
    public abstract boolean isShowBannerAd();


    /**
     * 是否展示原生广告 避免不需要展示广告的Fragment去初始化原生广告
     *
     * @return
     */
    public abstract boolean isShowNativeAd();


    /**
     * 是否展示插屏和激励广告 避免不需要展示广告的Fragment去初始插屏生和激励广告
     *
     * @return
     */
    public abstract boolean isShowVerticllAndStimulateAd();

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(mContext);
        if (!isHasShowAd) {
            if (shouldShowBannerView() && !SharedPreferencesUtil.isVip() && isShowBannerAd()) {
//                new Thread(new BannerRunable(this)).start();
                bannerHandler.post(new BannerRunable(this));

            }

            if (shouldShowNativeView() && !SharedPreferencesUtil.isVip() && isShowNativeAd()) {
//                new Thread(new NativeRunable(this)).start();

                nativeHandler.post(new NativeRunable(this));

            }
//
//            if (shouldShowVerticalInterstitialView()&&!SharedPreferencesUtil.isVip()) {
//                verticalInterstitialLayout = new VerticalInterstitialLayout(this, this);
//                verticalInterstitialLayout.loadAd();
//
//
//            }
//
//
//            if (shouldShowStimulateAdView()&&!SharedPreferencesUtil.isVip()) {
//
//
//                stimulateAdLayout = new StimulateAdLayout(this, this);
//                stimulateAdLayout.loadAd();
//
//
//            }

//            videoHandler.postDelayed(new VideoRunable(this),1000);
            isHasShowAd = true;
        }

        if (isShowVerticllAndStimulateAd()) {
            videoHandler.postDelayed(new VideoRunable(this), 500);
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(mContext);
//    }

    /**
     * 初始化插屏和激励薯片
     */
    private Handler videoHandler = new Handler();

    private static final class VideoRunable implements Runnable {

        private WeakReference<BaseActivity> mWeakReference;

        private VideoRunable(BaseActivity spotFragment) {
            mWeakReference = new WeakReference<BaseActivity>(spotFragment);

        }

        @Override
        public void run() {
            BaseActivity baseFragment = (BaseActivity) mWeakReference.get();
            baseFragment.initVideoLayout(baseFragment);


        }
    }

    private void initVideoLayout(BaseActivity baseFragment) {
        if (shouldShowVerticalInterstitialView() && !SharedPreferencesUtil.isVip()) {
//            if(verticalInterstitialLayout==null){
            verticalInterstitialLayout = new VerticalInterstitialLayout(baseFragment, baseFragment);
            verticalInterstitialLayout.loadAd();
//            }

        }


        if (shouldShowStimulateAdView() && !SharedPreferencesUtil.isVip()) {

//            if(stimulateAdLayout==null){
            stimulateAdLayout = new StimulateAdLayout(baseFragment, baseFragment);
            stimulateAdLayout.loadAd();
//            }

        }
    }


    /**
     * 展示插屏广告
     */

    public void showVerticalInterstitialAd() {


        if (verticalInterstitialLayout != null) {
            verticalInterstitialLayout.showAd();

        } else {
            verticalInterstitialLayout = new VerticalInterstitialLayout(this, this);
            verticalInterstitialLayout.loadAd();
            verticalInterstitialLayout.showAd();

        }

    }


    /**
     * 展示激励视频广告,根据规定次数来决定显示
     */

    public void showStimulateNeedUserNumberAd(boolean isSuccessFalCall) {
        if (stimulateAdLayout != null) {
            /**
             * 配置多少次后需要弹出激励视频，默认五次后
             */
            int number = (int) SharedPreferencesUtil.getParam(Contants.SHOW_STIMULATE_NUMBER_STRING, Contants.SHOW_STIMULATE_NUMBER);


            if (number == 0) {
                ToastUtils.show("您的免费使用次数用完了,请观看视频领取使用次数");

                //当使用次数为0,弹出激励广告并重置使用次数5次
                stimulateAdLayout.showAd();
                SharedPreferencesUtil.setParam(Contants.SHOW_STIMULATE_NUMBER_STRING, Contants.SHOW_STIMULATE_NUMBER);


            } else {
                /**
                 * 根据自己的需要，在使用次数没有超过规定的要求的时候，设置回调
                 */
                number -= 1;
                SharedPreferencesUtil.setParam(Contants.SHOW_STIMULATE_NUMBER_STRING, number);
                if (isSuccessFalCall) {
                    onStimulateSuccessCall();
                } else {
                    onStimulateSuccessDissmissCall();
                }
            }


        }
    }

    /**
     * 直接展示激励视频广告
     */

    public void showStimulateAd() {
        if (stimulateAdLayout != null) {
            stimulateAdLayout.showAd();

        } else {
            stimulateAdLayout = new StimulateAdLayout(this, this);
            stimulateAdLayout.loadAd();
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
    public void onFail() {
        onStimulateFallCall();
    }

    @Override
    public void onStimulateAdPresent() {

    }

    @Override
    public void onStimulateAdDismissed() {
        onStimulateSuccessDissmissCall();
    }

    /**
     * 激励视频看完回调
     */
    public abstract void onStimulateSuccessCall();


    /**
     * 激励视频看完关闭回调
     */
    public abstract void onStimulateSuccessDissmissCall();


    /**
     * 激励视频失败回调
     */
    public void onStimulateFallCall() {
    }
}
