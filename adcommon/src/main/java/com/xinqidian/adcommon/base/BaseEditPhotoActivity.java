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
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.databinding.ActivityBaseBinding;
import com.xinqidian.adcommon.login.UserModel;
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

public abstract class BaseEditPhotoActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity implements IBaseActivity, BannerInterface, StimulateAdInterface {
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

    private BannerLayout bannerLayout;//????????????

    private NativeLayout nativeLayout;//????????????

    private VerticalInterstitialLayout verticalInterstitialLayout;//????????????

    private StimulateAdLayout stimulateAdLayout;//??????????????????

    private CommentDialog commentDialog;//???????????????

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityManagerUtils.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        //?????????????????????
        initParam();
        //??????????????????Databinding???ViewModel??????
        initViewDataBinding(savedInstanceState);
        //?????????ViewModel???View???????????????????????????
        registorUIChangeLiveDataCallBack();
        //???????????????????????????
        initData();
        //??????????????????????????????????????????ViewModel?????????View??????????????????
        initViewObservable();
//        //??????RxBus
//        viewModel.registerRxBus();

    }


    /**
     * ????????????
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
//        if (!isWhite) {
//            ACStatusBarUtil.transparencyBar(this);//??????
//        } else {
//            MyStatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
//            MyStatusBarUtil.StatusBarLightMode(this);
////            ACStatusBarUtil.StatusBarLightMode(this); //??????????????????
//        }
        if (!isWhite) {
//            ACStatusBarUtil.transparencyBar(this);//??????
        } else {
            MyStatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
            MyStatusBarUtil.StatusBarLightMode(this);
//            ACStatusBarUtil.StatusBarLightMode(this); //??????????????????
        }
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //????????????????????????????????????????????????BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);

        }
        baseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        //??????TitleViewModel
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

        //DataBindingUtil????????????project???build????????? dataBinding {enabled true }, ????????????????????????android.databinding???

        binding = DataBindingUtil.inflate(getLayoutInflater(), initContentView(savedInstanceState), null, false);
        binding.setVariable(initVariableId(), viewModel);
        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        binding.getRoot().setLayoutParams(params);
        baseBinding.contentLl.addView(binding.getRoot());
        getWindow().setContentView(baseBinding.getRoot());


        //???ViewModel??????View?????????????????????
        getLifecycle().addObserver(viewModel);
        //??????RxLifecycle????????????
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
     * ??????????????????
     *
     * @return ??????layout???id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * ?????????ViewModel???id
     *
     * @return BR???id
     */
    public abstract int initVariableId();

    /**
     * ?????????ViewModel
     *
     * @return ??????BaseViewModel???ViewModel
     */
    public VM initViewModel() {
        return null;
    }


    /**
     * ??????ViewModel
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
    //??????ViewModel???View?????????UI????????????
    private void registorUIChangeLiveDataCallBack() {
//        //?????????????????????
//        viewModel.getUiChangeLiveData().getShowDialogEvent().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String title) {
//                showDialog(title);
//            }
//        });
//        //?????????????????????
//        viewModel.getUiChangeLiveData().getDismissDialogEvent().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(@Nullable Boolean aBoolean) {
////                dismissDialog();
//            }
//        });
        //???????????????
        viewModel.getUiChangeLiveData().getStartActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startActivity(clz, bundle);
            }
        });

        //????????????
        viewModel.getUiChangeLiveData().getFinishEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                finish();
            }
        });

        //????????????????????????????????????
        viewModel.getUiChangeLiveData().getSuccessEvent().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                successLayout();
            }
        });

        //??????????????????
        viewModel.getUiChangeLiveData().getFailEvent().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                netErrorLayout();
            }
        });


        //??????????????????????????????
        viewModel.getUiChangeLiveData().getEmptyEvent().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                emptyLayout();
            }
        });


        /**
         * ?????????????????? ?????????????????????ui????????????
         */
        LiveDataBus.get().with(LiveBusConfig.login, Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                setLoginState(aBoolean);

            }
        });


        /**
         * ???????????????????????????
         */
        LiveDataBus.get().with(LiveBusConfig.alipaySuccess, Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                setAlipaySuccess(aBoolean);

            }
        });


        /**
         * ???????????????????????????????????????
         */
        LiveDataBus.get().with(LiveBusConfig.userData, UserModel.DataBean.class).observe(this, new Observer<UserModel.DataBean>() {
            @Override
            public void onChanged(@Nullable UserModel.DataBean dataBean) {
                setUserData(dataBean);
            }
        });


    }


    /**
     * ????????????????????????????????????
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
     * ????????????????????????
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
     * ??????????????????
     *
     * @param loginState
     */
    public void setLoginState(boolean loginState) {

    }


    /**
     * ???????????????????????????
     *
     * @param alipaySuccessState
     */
    public void setAlipaySuccess(boolean alipaySuccessState) {

    }


    /**
     * ??????????????????
     */

    public void setUserData(UserModel.DataBean data) {
    }

    ;


    /**
     * ????????????
     *
     * @param clz ??????????????????Activity???
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * ????????????
     *
     * @param clz    ??????????????????Activity???
     * @param bundle ????????????????????????
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
        //??????Messenger??????
//        Messenger.getDefault().unregister(viewModel);
        //??????ViewModel??????????????????
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
     * ?????????????????????
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
     * ?????????????????????
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
     * ????????????
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
     * ???????????????????????????????????????????????????match_parent, ?????????????????????wrap_content
     *
     * @return ???????????????????????????????????????
     */

    protected ViewGroup getBannerViewContainer() {
        return (ViewGroup) findViewById(R.id.banner_view_container);
    }

    /**
     * ???????????????????????????????????????????????????match_parent, ?????????????????????wrap_content
     *
     * @return ???????????????????????????????????????
     */

    protected ViewGroup getNativeViewContainer() {

        return (ViewGroup) findViewById(R.id.native_view_container);
    }


    /**
     * ???????????????????????????????????????????????????????????????match_parent, ?????????????????????wrap_content
     *
     * @return ???????????????????????????????????????
     */

    protected ViewGroup getStimulateAdViewContainer() {

        return (ViewGroup) findViewById(R.id.stimulate_view_container);
    }


    /**
     * ????????????????????????????????????true
     *
     * @return
     */
    protected boolean shouldShowBannerView() {
        return Contants.IS_SHOW_BANNER_AD;
    }


    /**
     * ??????????????????
     *
     * @return
     */
    protected int getCommnetNumber() {
        return Contants.COMMENT_NUMBER;
    }


    /**
     * ????????????????????????????????????true
     *
     * @return
     */
    protected boolean shouldShowNativeView() {
        return Contants.IS_SHOW_NATIVE_AD;
    }


    /**
     * ????????????????????????????????????true
     *
     * @return
     */
    protected boolean shouldShowVerticalInterstitialView() {
        return Contants.IS_SHOW_VERTICALINTERSTITIAL_AD;
    }


    /**
     * ??????????????????????????????????????????true
     *
     * @return
     */
    protected boolean shouldShowStimulateAdView() {
        return Contants.IS_SHOW_STIMULATE_AD;
    }


    /**
     * ?????????banner
     */
    private Handler bannerHandler = new Handler();

    private static final class BannerRunable implements Runnable {

        private WeakReference<BaseEditPhotoActivity> mWeakReference;

        private BannerRunable(BaseEditPhotoActivity spotFragment) {
            mWeakReference = new WeakReference<BaseEditPhotoActivity>(spotFragment);

        }

        @Override
        public void run() {
            BaseEditPhotoActivity baseActivity = (BaseEditPhotoActivity) mWeakReference.get();
            baseActivity.initBannerLayout(baseActivity);


        }
    }


    private void initBannerLayout(BaseEditPhotoActivity baseActivity) {
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
     * ??????????????????????????????
     *
     * @param bannerView ????????????
     * @param container  ?????????????????????????????????
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
     * ???????????????
     */
    private Handler nativeHandler = new Handler();

    private static final class NativeRunable implements Runnable {

        private WeakReference<BaseEditPhotoActivity> mWeakReference;

        private NativeRunable(BaseEditPhotoActivity spotFragment) {
            mWeakReference = new WeakReference<BaseEditPhotoActivity>(spotFragment);

        }

        @Override
        public void run() {
            BaseEditPhotoActivity baseActivity = (BaseEditPhotoActivity) mWeakReference.get();
            baseActivity.initNativeLayout(baseActivity);


        }
    }


    private void initNativeLayout(BaseEditPhotoActivity baseActivity) {
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
     * ???????????????????????????
     */

    private boolean isHasShowAd;


    /**
     * ???????????????????????? ??????????????????????????????Fragment????????????????????????
     *
     * @return
     */
    public abstract boolean isShowBannerAd();


    /**
     * ???????????????????????? ??????????????????????????????Fragment????????????????????????
     *
     * @return
     */
    public abstract boolean isShowNativeAd();


    /**
     * ????????????????????????????????? ??????????????????????????????Fragment?????????????????????????????????
     *
     * @return
     */
    public abstract boolean isShowVerticllAndStimulateAd();

    @Override
    protected void onResume() {
        super.onResume();
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


    /**
     * ??????????????????????????????
     */
    private Handler videoHandler = new Handler();

    private static final class VideoRunable implements Runnable {

        private WeakReference<BaseEditPhotoActivity> mWeakReference;

        private VideoRunable(BaseEditPhotoActivity spotFragment) {
            mWeakReference = new WeakReference<BaseEditPhotoActivity>(spotFragment);

        }

        @Override
        public void run() {
            BaseEditPhotoActivity baseFragment = (BaseEditPhotoActivity) mWeakReference.get();
            baseFragment.initVideoLayout(baseFragment);


        }
    }

    private void initVideoLayout(BaseEditPhotoActivity baseFragment) {
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
     * ??????????????????
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
     * ????????????????????????,?????????????????????????????????
     */

    public void showStimulateNeedUserNumberAd(boolean isSuccessFalCall) {
        if (stimulateAdLayout != null) {
            /**
             * ????????????????????????????????????????????????????????????
             */
            int number = (int) SharedPreferencesUtil.getParam(Contants.SHOW_STIMULATE_NUMBER_STRING, Contants.SHOW_STIMULATE_NUMBER);


            if (number == 0) {
                ToastUtils.show("?????????????????????????????????,?????????????????????????????????");

                //??????????????????0,???????????????????????????????????????5???
                stimulateAdLayout.showAd();
                SharedPreferencesUtil.setParam(Contants.SHOW_STIMULATE_NUMBER_STRING, Contants.SHOW_STIMULATE_NUMBER);


            } else {
                /**
                 * ??????????????????????????????????????????????????????????????????????????????????????????
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
     * ??????????????????????????????
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
     * ??????banner????????????
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
     * ??????banner????????????
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
     * ??????????????????
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
     * ????????????????????????
     */
    public abstract void onStimulateSuccessCall();


    /**
     * ??????????????????????????????
     */
    public abstract void onStimulateSuccessDissmissCall();


    /**
     * ????????????????????????
     */
    public void onStimulateFallCall() {
    }


    protected void doFinishAction(){
        finish();
    }
}
