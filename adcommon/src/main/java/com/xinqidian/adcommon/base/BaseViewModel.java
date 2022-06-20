package com.xinqidian.adcommon.base;

import android.app.Application;
import android.app.Dialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.xinqidian.adcommon.bus.event.SingleLiveEvent;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lipei on 2018/11/27.
 */

public class BaseViewModel extends AndroidViewModel implements IBaseViewModel {

    private WeakReference<LifecycleProvider> lifecycleProviderWeakReference;

    public UIChangeLiveData uiChangeLiveData;

    public ObservableBoolean isLogin=new ObservableBoolean();

    public Dialog loadingDialog;

    public String language="en-us";


    public BaseViewModel(@NonNull Application application) {
        super(application);
    }


    public void setLoadingDialog(Dialog dialog){
        this.loadingDialog=dialog;
    }

    public void setLanguage(String language){
        this.language=language;
    }



    /**
     * 注入RxLifecycle生命周期
     * @param lifecycleProvider
     */
    public void injectLifecycleProvider(LifecycleProvider lifecycleProvider){
        lifecycleProviderWeakReference=new WeakReference<LifecycleProvider>(lifecycleProvider);
//        this.lifecycleProvider=lifecycleProvider;
    }

    /**
     * 获取RxLifecycle生命周期
     * @return
     */

    public LifecycleProvider getLifecycleProvider() {
        return lifecycleProviderWeakReference.get();
    }


    /**
     * 获取UI更新
     * @return
     */
    public UIChangeLiveData getUiChangeLiveData() {
        if(uiChangeLiveData==null){
            uiChangeLiveData=new UIChangeLiveData();
        }

        return uiChangeLiveData;
    }





    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uiChangeLiveData.startActivityEvent.postValue(params);
    }

    /**
     * 请求成功且数据不为空
     */
    public void success(){
        uiChangeLiveData.successEvent.call();

    }


    /**
     * 网络问题
     */
    public void fail(){
        uiChangeLiveData.failEvent.call();


    }


    /**
     * 数据为空
     */
    public void empty(){
        uiChangeLiveData.emptyEvent.call();


    }




    public void showDialog(String title) {
        uiChangeLiveData.showDialogEvent.postValue(title);
    }

    public void dismissDialog() {
        uiChangeLiveData.dismissDialogEvent.call();
    }


    /**
     * 关闭界面
     */
    public void finish() {
        uiChangeLiveData.finishEvent.call();
    }


    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {

    }

    @Override
    public void onCreate() {
//        if(uiChangeLiveData==null){
//            uiChangeLiveData=new UIChangeLiveData();
//        }

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestory() {
        if(uiChangeLiveData!=null){
            uiChangeLiveData=null;
        }
        if(loadingDialog!=null){
            loadingDialog=null;
        }

    }

    @Override
    public void registerRxBus() {

    }

    @Override
    public void removeRxBus() {

    }




    public class UIChangeLiveData extends SingleLiveEvent {
        private SingleLiveEvent<String> showDialogEvent;
        private SingleLiveEvent dismissDialogEvent;
        private SingleLiveEvent successEvent;
        private SingleLiveEvent emptyEvent;
        private SingleLiveEvent failEvent;


        private SingleLiveEvent<Map<String, Object>> startActivityEvent;
        private SingleLiveEvent<Map<String, Object>> startContainerActivityEvent;
        private SingleLiveEvent finishEvent;
        private SingleLiveEvent onBackPressedEvent;

        public SingleLiveEvent<String> getShowDialogEvent() {
            return showDialogEvent = createLiveData(showDialogEvent);
        }

        public SingleLiveEvent getDismissDialogEvent() {
            return dismissDialogEvent = createLiveData(dismissDialogEvent);
        }

        public SingleLiveEvent getSuccessEvent() {
            return successEvent = createLiveData(successEvent);
        }

        public SingleLiveEvent getEmptyEvent() {
            return emptyEvent = createLiveData(emptyEvent);
        }

        public SingleLiveEvent getFailEvent() {
            return failEvent = createLiveData(failEvent);
        }


        public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
            return startActivityEvent = createLiveData(startActivityEvent);
        }

        public SingleLiveEvent<Map<String, Object>> getStartContainerActivityEvent() {
            return startContainerActivityEvent = createLiveData(startContainerActivityEvent);
        }

        public SingleLiveEvent getFinishEvent() {
            return finishEvent = createLiveData(finishEvent);
        }

        public SingleLiveEvent getOnBackPressedEvent() {
            return onBackPressedEvent = createLiveData(onBackPressedEvent);
        }

        private SingleLiveEvent createLiveData(SingleLiveEvent liveData) {
            if (liveData == null) {
                liveData = new SingleLiveEvent();
            }
            return liveData;
        }

        @Override
        public void observe(LifecycleOwner owner, Observer observer) {
            super.observe(owner, observer);
        }
    }



    public static class ParameterField {
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }

}
