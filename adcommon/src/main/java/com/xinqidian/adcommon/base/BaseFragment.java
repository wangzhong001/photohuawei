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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.ad.banner.BannerInterface;
import com.xinqidian.adcommon.ad.banner.BannerLayout;
import com.xinqidian.adcommon.ad.nativead.NativeLayout;
import com.xinqidian.adcommon.ad.stimulate.StimulateAdInterface;
import com.xinqidian.adcommon.ad.stimulate.StimulateAdLayout;
import com.xinqidian.adcommon.ad.verticalInterstitial.VerticalInterstitialLayout;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.databinding.FragmentBaseBinding;
import com.xinqidian.adcommon.login.UserModel;
import com.xinqidian.adcommon.util.KLog;
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

public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends RxFragment implements IBaseActivity, BannerInterface, StimulateAdInterface {
    protected V binding;
    protected VM viewModel;
    private FragmentBaseBinding baseBinding;
    private static final String TAG = "BaseFragment";

    /**
     * rootView是否初始化标志，防止回调函数在rootView为空的时候触发
     */
    private boolean hasCreateView;

    /**
     * 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
     */
    private boolean isFragmentVisible;

    //是否是第一次开启网络加载
    public boolean isFirst;

    private View rootView;

    private boolean isEmptyFirst = true;
    private View emptyView;
    private View netErrorView;
    private boolean isNetErrorFirst = true;
    private String titleName;
    private int tabar = View.GONE;
    private WeakReference<VM> weakReference;
    private WeakReference<TitleViewModel> titleViewModel;
    private boolean isHasSet = false;

    private BannerLayout bannerLayout;//横幅广告

    private NativeLayout nativeLayout;//原生广告

    private VerticalInterstitialLayout verticalInterstitialLayout;//插屏广告

    private StimulateAdLayout stimulateAdLayout;//激励视频广告

    private CommentDialog commentDialog;//给好评弹框



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.e(TAG, "onCreate");
        initParam();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除Messenger注册
//        Messenger.getDefault().unregister(viewModel);
        //解除ViewModel生命周期感应
        getLifecycle().removeObserver(viewModel);
//        viewModel.removeRxBus();
        viewModel = null;
        if (titleViewModel != null) {
            titleViewModel.clear();
            titleViewModel = null;
        }

        commentDialog=null;
        binding.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        KLog.e(TAG, "onCreateView");


        if (rootView == null) {
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

            if (weakReference == null) {
                weakReference = new WeakReference<VM>(viewModel);
            }


            baseBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_base, null, false);

            //创建TitleViewModel
            titleViewModel = new WeakReference<TitleViewModel>(createViewModel(this, TitleViewModel.class));
            titleViewModel.get().titleName.set(titleName);
            titleViewModel.get().tabar.set(tabar);
            baseBinding.setBaseViewModel(titleViewModel.get());
            binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
            KLog.e("binding--->", binding);
            binding.setVariable(initVariableId(), viewModel);

            // content
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            binding.getRoot().setLayoutParams(params);
            baseBinding.contentLl.addView(binding.getRoot());

            //让ViewModel拥有View的生命周期感应
            getLifecycle().addObserver(viewModel);
            //注入RxLifecycle生命周期

            viewModel.injectLifecycleProvider(this);

            rootView = baseBinding.getRoot();

        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        KLog.e(TAG, "onViewCreated");

        super.onViewCreated(view, savedInstanceState);
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        viewModel.registerRxBus();

        //可见，但是并没有加载过
        if (isFragmentVisible && !isFirst) {
            onFragmentVisibleChange(true);
        }

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
//                dismissDialog();
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
                getActivity().finish();
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
                KLog.e("emptyLayout--->", "emptyLayout");
                emptyLayout();
            }
        });


        /**
         * 登录状态回调 根据状态来更改ui或者逻辑
         */
        LiveDataBus.get().with(LiveBusConfig.login,Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                setLoginState(aBoolean);

            }
        });


        /**
         * 支付宝支付成功回调
         */
        LiveDataBus.get().with(LiveBusConfig.alipaySuccess,Boolean.class).observe(this, new Observer<Boolean>() {
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
     * 设置登录状态
     * @param loginState
     */
    public void setLoginState(boolean loginState){

    }


    /**
     * 设置支付宝支付成功
     * @param alipaySuccessState
     */
    public void setAlipaySuccess(boolean alipaySuccessState){

    }



    /**
     * 评论弹框
     * @param comment
     */

    public void showCommentDialog(String comment){
        if(commentDialog==null){
            commentDialog=new CommentDialog(getContext(),comment);
        }
        commentDialog.show();
    }



    /**
     * 设置用户信息
     */

    public void setUserData(UserModel.DataBean data){};


    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setTabar(int tabar) {
        this.tabar = tabar;
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(getContext(), clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(getContext(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * =====================================================================
     **/

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(initVariableId(), viewModel);
        }
    }

    @Override
    public void initParam() {
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

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

    @Override
    public void initData() {

    }


    @Override
    public void initViewObservable() {

    }

    @Override
    public void refeshData() {

    }

    public boolean isBackPressed() {
        return false;
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isFragmentVisible = true;
        }
        if (rootView == null) {
            return;
        }
        //可见，并且没有加载过
        if (!isFirst && isFragmentVisible) {
            onFragmentVisibleChange(true);
            return;
        }
        //由可见——>不可见 已经加载过
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }

    }


    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

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
        return (LinearLayout) rootView.findViewById(R.id.banner_view_container);
    }

    /**
     * 原生广告的布局容器。宽度参数建议为match_parent, 高度参数建议为wrap_content
     *
     * @return 用于容纳原生广告的布局容器
     */

    protected ViewGroup getNativeViewContainer() {

        return (LinearLayout) rootView.findViewById(R.id.native_view_container);
    }


    /**
     * 激励视频广告广告的布局容器。宽度参数建议为match_parent, 高度参数建议为wrap_content
     *
     * @return 用于容纳原生广告的布局容器
     */

    protected ViewGroup getStimulateAdViewContainer() {

        return (ViewGroup) rootView.findViewById(R.id.stimulate_view_container);
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

        private WeakReference<BaseFragment> mWeakReference;

        private BannerRunable(BaseFragment spotFragment) {
            mWeakReference = new WeakReference<BaseFragment>(spotFragment);

        }

        @Override
        public void run() {
            BaseFragment baseFragment = (BaseFragment) mWeakReference.get();
            baseFragment.initBannerLayout(baseFragment);


        }
    }


    private void initBannerLayout(BaseFragment baseFragment) {
        ViewGroup bannerViewContainer = getBannerViewContainer();
        if (bannerViewContainer != null) {
            if (bannerLayout == null) {
                bannerLayout = new BannerLayout(getContext());
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

        private WeakReference<BaseFragment> mWeakReference;

        private NativeRunable(BaseFragment spotFragment) {
            mWeakReference = new WeakReference<BaseFragment>(spotFragment);

        }

        @Override
        public void run() {
            BaseFragment baseFragment = (BaseFragment) mWeakReference.get();
            baseFragment.initNativeLayout(baseFragment);


        }
    }


    private void initNativeLayout(BaseFragment baseFragment) {
        ViewGroup nativeViewContainer = getNativeViewContainer();
        if (nativeViewContainer != null) {
            if (nativeLayout == null) {
                nativeLayout = new NativeLayout(getContext());
                onAddBannerView(nativeLayout, nativeViewContainer);
                nativeLayout.loadAd();
            }
        }
    }


    /**
     * 初始化插屏和激励薯片
     */
    private Handler videoHandler = new Handler();

    private static final class VideoRunable implements Runnable {

        private WeakReference<BaseFragment> mWeakReference;

        private VideoRunable(BaseFragment spotFragment) {
            mWeakReference = new WeakReference<BaseFragment>(spotFragment);

        }

        @Override
        public void run() {
            BaseFragment baseFragment = (BaseFragment) mWeakReference.get();
            baseFragment.initVideoLayout(baseFragment);


        }
    }

    private void initVideoLayout(BaseFragment baseFragment) {
        if (shouldShowVerticalInterstitialView()&&!SharedPreferencesUtil.isVip()) {
//            if(verticalInterstitialLayout==null){
            verticalInterstitialLayout = new VerticalInterstitialLayout(getContext(), baseFragment);
            verticalInterstitialLayout.loadAd();
//            }

        }


        if (shouldShowStimulateAdView()&& !SharedPreferencesUtil.isVip()) {

//            if(stimulateAdLayout==null){
            stimulateAdLayout = new StimulateAdLayout(getContext(), baseFragment);
            stimulateAdLayout.loadAd();
//            }

        }
    }


    /**
     * 避免重复初始化广告
     */

    private boolean isHasShowAd;


    /**
     * 是否展示横幅广告 避免不需要展示广告的Fragment去初始化横幅广告
     * @return
     */
    public abstract boolean isShowBannerAd();



    /**
     * 是否展示原生广告 避免不需要展示广告的Fragment去初始化原生广告
     * @return
     */
    public abstract boolean isShowNativeAd();


    /**
     * 是否展示插屏和激励广告 避免不需要展示广告的Fragment去初始插屏生和激励广告
     * @return
     */
    public abstract boolean isShowVerticllAndStimulateAd();




    @Override
    public void onResume() {
        super.onResume();
        if(!isHasShowAd){
            if (shouldShowBannerView()&&!SharedPreferencesUtil.isVip()&&isShowBannerAd()) {
                bannerHandler.post(new BannerRunable(this));
//                new Thread(new BannerRunable(this) ).start();

            }

            if (shouldShowNativeView()&&!SharedPreferencesUtil.isVip()&&isShowNativeAd()) {
                nativeHandler.post(new NativeRunable(this));
//                new Thread(new NativeRunable(this) ).start();

            }



            isHasShowAd=true;
        }

        if(isShowVerticllAndStimulateAd()){
            videoHandler.postDelayed(new VideoRunable(this),500);

        }




    }


    /**
     * 展示插屏广告
     */

    public void showVerticalInterstitialAd() {
        if (verticalInterstitialLayout != null) {
            verticalInterstitialLayout.showAd();

        }else {
            verticalInterstitialLayout=new VerticalInterstitialLayout(getContext(),this);
            verticalInterstitialLayout.loadAd();
            verticalInterstitialLayout.showAd();
        }
    }


    /**
     * 展示激励视频广告
     */

    public void showStimulateAd() {
        if (stimulateAdLayout != null) {
            stimulateAdLayout.showAd();

        }else {
            stimulateAdLayout=new StimulateAdLayout(getContext(),this);
            stimulateAdLayout.loadAd();
            stimulateAdLayout.showAd();
        }
    }


    /**
     * 展示激励视频广告,根据规定次数来决定显示
     */


    public void neeedAddDialogAd() {
    }

    ;

    public void showStimulateNeedUserNumberAd(boolean isSuccessFalCall) {
        if (stimulateAdLayout != null) {
            /**
             * 配置多少次后需要弹出激励视频，默认五次后
             */
            int number = (int) SharedPreferencesUtil.getParam(Contants.SHOW_STIMULATE_NUMBER_STRING, Contants.SHOW_STIMULATE_NUMBER);


            Log.d("ty-->", number + "");
            if (number == 0) {
                ToastUtils.show("您的免费使用次数用完了,请观看视频领取使用次数");

                //当使用次数为0,弹出激励广告并重置使用次数5次
//                stimulateAdLayout.showAd();
                neeedAddDialogAd();


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
        onStimulateSuccessDissmissCall();


    }

    @Override
    public void onFail() {
        onStimulateFallCall();
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
    public  void onStimulateFallCall(){}
}
