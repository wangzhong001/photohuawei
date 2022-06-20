package com.xinqidian.adcommon.http.net;

import android.app.Dialog;


import com.xinqidian.adcommon.http.DialogCancelListener;
import com.xinqidian.adcommon.http.ExceptionHandle;
import com.xinqidian.adcommon.http.NetworkUtil;
import com.xinqidian.adcommon.http.ResponseThrowable;
import com.xinqidian.adcommon.util.Common;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.ToastUtils;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by 李佩
 *
 * @date 2018/01/28
 * @desrcption Subscriber的封装, 可配合后端自定义错误码，由于本人没有后台，暂时没写，后期再更新
 */

public abstract class NetWorkSubscriber<T> extends DisposableObserver<T> implements DialogCancelListener {
    private DialogHandler dialogHandler;
    private static final String TAG = NetWorkSubscriber.class.getName();


    public NetWorkSubscriber() {
        super();
    }


    @Override
    public void onStart() {
        if (!NetworkUtil.isNetworkAvailable(Common.getApplication())) {
            //没有网络
            onNoNetWork();
//            cancel();
            return;
        }

        if (showDialog() != null) {
            dialogHandler = new DialogHandler(showDialog(), this);
            showProgressDialog();
        }

    }


    protected abstract Dialog showDialog();


    protected abstract void onSuccess(T t);


    /**
     * 没有网络
     */
    protected void onNoNetWork() {

    }


    protected void onFail(ResponseThrowable responseThrowable) {

    }


    @Override
    public void onError(Throwable e) {
        onFail(new ResponseThrowable(e,400));
        KLog.e(TAG, "onError--->" + e.toString());
        dismissProgressDialog();
        ResponseThrowable responseThrowable= ExceptionHandle.handleException(e);
        ToastUtils.show(ExceptionHandle.handleException(e).message);
        onFail(responseThrowable);


    }

    @Override
    public void onComplete() {
//        dismissProgressDialog();


    }


    @Override
    public void onNext(T t) {
        KLog.e(TAG, "onNext");
        dismissProgressDialog();
        onSuccess(t);
    }

    /**
     * 取消网络请求
     */
    @Override
    public void onCancel() {
        KLog.e(TAG, "onCancel");
        /**
         * 如果订阅者和被订阅者 没有取消订阅 则取消订阅 以取消网络请求
         */

       //取消订阅
        if (isDisposed()) {
            dispose();
        }

    }

    /**
     * 显示对话框 发送一个显示对话框的消息给dialoghandler  由他自己处理（也就是dialog中hanldermesage处理该消息）
     */
    private void showProgressDialog() {
        if (dialogHandler != null) {
            dialogHandler.obtainMessage(DialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    /**
     * 隐藏对话框 ....
     */
    private void dismissProgressDialog() {
        if (dialogHandler != null) {
            dialogHandler.obtainMessage(DialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            dialogHandler = null;
        }
    }


}
