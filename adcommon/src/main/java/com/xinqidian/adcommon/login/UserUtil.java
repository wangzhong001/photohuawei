package com.xinqidian.adcommon.login;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;

import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.app.BaseServiceApi;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.http.BaseResponse;
import com.xinqidian.adcommon.http.ResponseThrowable;
import com.xinqidian.adcommon.http.net.NetWorkSubscriber;
import com.xinqidian.adcommon.http.util.RetrofitClient;
import com.xinqidian.adcommon.http.util.RxUtils;
import com.xinqidian.adcommon.util.AesUtil;
import com.xinqidian.adcommon.util.AppUtils;
import com.xinqidian.adcommon.util.Common;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import java.util.List;

/**
 * Created by lipei on 2020/6/6.
 */

public class UserUtil {


    private static CallBack2 callBack;

    /**
     * 更新免费使用次数
     * 0减少 1增加
     * @param type
     */
    public static void updateFreeNumber(int type){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .updateFreeNumber(type)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {

                        if(baseResponse.getCode()==AppConfig.success_code){
                            getUserInfo();

                        }
                    }
                });

    }


    /**
     * 更新免费使用次数
     * 0减少 1增加
     * @param type
     */
    public static void updateFreeNumber(int type, final CallBack callBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .updateFreeNumber(type)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {

                        if(baseResponse.getCode()==AppConfig.success_code){
                            getUserInfo();
                            callBack.onSuccess();

                        }else if(baseResponse.getCode()==AppConfig.login_fail){
                            SharedPreferencesUtil.exitLogin();

                            callBack.loginFial();

                        }else {
                            callBack.onFail();

                        }
                    }
                });

    }




    /**
     * 登录
     *
     * @param account
     * @param password
     */
    public static void login(final String account, final String password, final String inviteCode, final CallBack callBack, final Dialog dialog) {

        LoginRequestBody loginRequestBody = new LoginRequestBody();
        loginRequestBody.setLoginName(account);
        loginRequestBody.setPassword(AesUtil.aesEncrypt(password, AesUtil.KEY));
        KLog.e("passeor--->",AesUtil.aesEncrypt(password, AesUtil.KEY));
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .login(loginRequestBody)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return dialog;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        if (callBack != null) {
                            callBack.onFail();
                        }
                    }

                    @Override
                    protected void onSuccess(BaseResponse o) {
                        if (o.getCode() == AppConfig.user_not_regist) {
                            //用户未注册
                            regist(account, password, inviteCode,new NetWorkSubscriber<BaseResponse>() {
                                @Override
                                protected Dialog showDialog() {
                                    return dialog;
                                }

                                @Override
                                protected void onSuccess(BaseResponse o) {
                                    if (o.getCode() == AppConfig.success_code) {
                                        SharedPreferencesUtil.toLogin();
                                        SharedPreferencesUtil.setParam(AppConfig.isThisPhoneHasUserInviteCode,true);
//                                        updateFreeNumber(1);

                                        if (callBack != null) {
                                            callBack.onSuccess();
                                        }

                                    } else {
                                        ToastUtils.show(o.getMsg());
                                        if (callBack != null) {
                                            callBack.onFail();
                                        }

                                    }

                                }
                            });

                        } else if (o.getCode() == AppConfig.success_code) {
                            SharedPreferencesUtil.toLogin();
                            getUserInfo();
                            if (callBack != null) {
                                callBack.onSuccess();
                            }

                        } else {
                            ToastUtils.show(o.getMsg());
                            if (callBack != null) {
                                callBack.onFail();
                            }

                        }

                    }
                });
    }

    /**
     * 华为登录
     * @param code
     */
    public static void huaweiLogin(final String code, final CallBack callBack, final Dialog dialog) {
        LoginHuawei huawei = new LoginHuawei();
        huawei.setCode(code);
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .loginHuaWei(huawei)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return dialog;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        if (callBack != null) {
                            callBack.onFail();
                        }
                    }

                    @Override
                    protected void onSuccess(BaseResponse o) {
                        if (o.getCode() == AppConfig.success_code) {
                            SharedPreferencesUtil.toLogin();
                            getUserInfo();
                            if (callBack != null) {
                                callBack.onSuccess();
                            }
                        } else {
                            ToastUtils.show(o.getMsg());
                            if (callBack != null) {
                                callBack.onFail();
                            }
                        }
                    }
                });
    }

    /**
     * 华为支付通知
     */
    public static void huaweiPay(String mercdName, double mercdWorth, String openId, String orderId, int orderNumber,String payTime,
                                 final CallBack callBack, final Dialog dialog) {
        PayHuawei payHuawei = new PayHuawei();
        payHuawei.setMercdName(mercdName);
        payHuawei.setMercdWorth(mercdWorth);
        payHuawei.setOpenId(openId);
        payHuawei.setOrderId(orderId);
        payHuawei.setOrderNumber(orderNumber);
        payHuawei.setPayTime(payTime);
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .payHuaWei(payHuawei)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return dialog;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        if (callBack != null) {
                            callBack.onFail();
                        }
                    }

                    @Override
                    protected void onSuccess(BaseResponse o) {
                        if (o.getCode() == AppConfig.success_code) {
                            if (callBack != null) {
                                callBack.onSuccess();
                            }
                        } else {
//                            ToastUtils.show(o.getMsg());
                            if (callBack != null) {
                                callBack.onFail();
                            }
                        }
                    }
                });
    }

    /**
     * 退出登录
     */
    public static void exitLogin(){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .exitLogin()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        if(baseResponse.getCode()==AppConfig.success_code){
                            SharedPreferencesUtil.exitLogin();
                        }else {
                            ToastUtils.show(baseResponse.getMsg());
                        }

                    }
                });
    }


    /**
     * 获取用户信息
     */
    public static void getUserFreeNumber(final Dialog dialog, final CallBack callBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getUserInfo()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<UserModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return dialog;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        SharedPreferencesUtil.exitLogin();
                        if(dialog!=null){
                            dialog.dismiss();
                        }
                    }

                    @Override
                    protected void onSuccess(UserModel userModel) {
                        if(userModel.getCode()==AppConfig.success_code){
                            if(userModel.getData()!=null){

                                int number=userModel.getData().getFreeCount();

                                SharedPreferencesUtil.setFreeUserNumber(number);

                                if(number>0){
                                    callBack.onSuccess();
                                }else {
                                    callBack.onFail();

                                }


                            }

                        }else if(userModel.getCode()==AppConfig.login_fail){
                            callBack.loginFial();
                            SharedPreferencesUtil.exitLogin();
                        }else {
                            callBack.onFail();

                        }

                    }
                });
    }


    /**
     * 获取用户信息
     */
    public static void getUserInfo(){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getUserInfo()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<UserModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        SharedPreferencesUtil.exitLogin();
                    }

                    @Override
                    protected void onSuccess(UserModel userModel) {
                        if(userModel.getCode()==AppConfig.success_code){
                            if(userModel.getData()!=null){
                                String expireDate=userModel.getData().getExpireDate();
                                SharedPreferencesUtil.setMakeIdPhotoNumber(userModel.getData().getIdMakeCount());
                                SharedPreferencesUtil.setParam(Contants.inviteCode,userModel.getData().getInviteCode());
                                if(expireDate!=null){
                                    //是会员
                                    SharedPreferencesUtil.setVip(true);
                                }else {
                                    //不是会员
                                    SharedPreferencesUtil.setVip(false);

                                }

                                int number=userModel.getData().getFreeCount();

                                SharedPreferencesUtil.setFreeUserNumber(number);

                                if(number<=0){
                                    SharedPreferencesUtil.setParam("goodNumber",false);
                                    SharedPreferencesUtil.setFreeNumber(false);

                                }else {
                                    SharedPreferencesUtil.setParam("goodNumber",true);

                                    SharedPreferencesUtil.setFreeNumber(true);

                                }

                                LiveDataBus.get().with(LiveBusConfig.userData,UserModel.DataBean.class).postValue(userModel.getData());

                            }

                        }else if(userModel.getCode()==AppConfig.login_fail){
                            SharedPreferencesUtil.exitLogin();
                        }

                    }
                });
    }

    public static void getStoresList(final CallBack2 callBack){
//        RetrofitClient.getInstance().create(BaseServiceApi.class)
//                .getStores()
//                .compose(RxUtils.schedulersTransformer())
//                .subscribe(new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
////                        BaseResponse baseResponse = (BaseResponse) o;
////                        RoutePriceBean routePriceBean = (RoutePriceBean) o;
//                        Log.e("aaa...1",o+"");
//                        Gson gson = new Gson();
//                        BaseResponse routePriceBean = gson.fromJson(String.valueOf(o), BaseResponse.class);
//                        int code = routePriceBean.getCode();
////                        Gson gson = new Gson();
////                        RoutePriceBean routePriceBean  = gson.fromJson(String.valueOf(o),RoutePriceBean.class);
//
////                        JsonObject jsonObject = (JsonObject) o;
////                        boolean isLock = jsonObject.get("isLock").getAsBoolean();
////                        JSONObject j = new JSONObject();
////                        Gson gson = new Gson();
////                        RoutePriceBean routePriceBean = gson.fromJson(String.valueOf(o), RoutePriceBean.class);
////                        islockListener.isLock(isLock);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.e("aaa...2",throwable.toString());
//                    }
//                });
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getStores()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<RoutePriceBean>() {

                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(RoutePriceBean routePriceBean) {
                        RoutePriceBean routePriceBean1 = routePriceBean;
                        List<RoutePriceBean.DataBean> data = routePriceBean1.getData();
                        if (data.size() > 0){
                            if (callBack != null) {
                                callBack.onSuccess(data);
                            }
                            Log.e("aaa...1",data.toString());
                        }else {
                            if (callBack != null) {
                                callBack.onFail();
                            }
                        }
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        if (callBack != null) {
                            callBack.onFail();
                        }
                    }
                });
    }


    public static void getOrdersList(final CallBack2 callBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getOrdersList()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<RoutePriceBean>() {

                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(RoutePriceBean routePriceBean) {
                        RoutePriceBean routePriceBean1 = routePriceBean;
                        List<RoutePriceBean.DataBean> data = routePriceBean1.getData();
                        if (data != null){
                            if (data.size() > 0){
                                if (callBack != null) {
                                    callBack.onSuccess(data);
                                }
                                Log.e("aaa...2",data.size()+"");
                            }else {
                                if (callBack != null) {
                                    callBack.onFail();
                                }
                            }
                        }
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        if (callBack != null) {
                            callBack.onFail();
                        }
                    }
                });
    }

    public static void getPrintprice(final CallBack3 callBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getPrice()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<PrintPriceBean>() {

                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(PrintPriceBean routePriceBean) {
                        PrintPriceBean printPriceBean = routePriceBean;
                        String printprice = printPriceBean.getPrintprice();
                        Log.e("aaa...2",printprice);
                        if (printPriceBean != null){
                            if (callBack != null) {
                                callBack.onSuccess(printprice);
                            }
                        }else {
                            if (callBack != null) {
                                callBack.onFail();
                            }
                        }
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        ToastUtils.show("onFail");
                        if (callBack != null) {
                            callBack.onFail();
                        }
                    }
                });
    }



    public interface UserinfoCallBack{
        void getUserInfo(UserModel.DataBean dataBean);
    }


    /**
     * 获取用户信息
     */
    public static void getUserInfoCallBack(final UserinfoCallBack userinfoCallBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getUserInfo()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<UserModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        SharedPreferencesUtil.exitLogin();
                    }

                    @Override
                    protected void onSuccess(UserModel userModel) {
                        if(userModel.getCode() == AppConfig.success_code){
                            if(userModel.getData() != null){
                                String phoneNumber = userModel.getData().getMobile();
                                String expireDate=userModel.getData().getExpireDate();
                                SharedPreferencesUtil.setParam(Contants.inviteCode,userModel.getData().getInviteCode());

                                int number=userModel.getData().getFreeCount();

                                SharedPreferencesUtil.setFreeUserNumber(number);

                                if(number<=0){
                                    SharedPreferencesUtil.setParam("goodNumber",false);
                                    SharedPreferencesUtil.setFreeNumber(false);
                                }else {
                                    SharedPreferencesUtil.setParam("goodNumber",true);
                                    SharedPreferencesUtil.setFreeNumber(true);
                                }
                                SharedPreferencesUtil.setMakeIdPhotoNumber(userModel.getData().getIdMakeCount());
                                if(expireDate != null){
                                    //是会员
                                    SharedPreferencesUtil.setVip(true);
                                }else {
                                    //不是会员
                                    SharedPreferencesUtil.setVip(false);
                                }
                                if(userinfoCallBack!=null){
                                    userinfoCallBack.getUserInfo(userModel.getData());
                                }
                            }
                        }else if(userModel.getCode()==AppConfig.login_fail){
                            SharedPreferencesUtil.exitLogin();
                        }
                    }
                });
    }



    /**
     *
     * @param mercdName 会员订阅名称 一个月会员、三个月会员、一年会员
     * @param mercdWorth 订阅金额
     * @param orderNumber 订阅类型  1:一个月  3:3个月   12 一年
     * @param activity
     * @param callBack
     */
    public static void alipayOrder(String mercdName, String mercdWorth, final int orderNumber, final Activity activity, final CallBack callBack) {
        AllipayRequestBody allipayRequestBody = new AllipayRequestBody();
        allipayRequestBody.setMercdName(mercdName);
        allipayRequestBody.setMercdWorth(mercdWorth);
        allipayRequestBody.setOrderNumber(orderNumber);
        allipayRequestBody.setRemark(Contants.PLATFORM+"--版本:"+ AppUtils.getVersionName(Common.getApplication()));
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .alipayCreateOrdr(allipayRequestBody)
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<AlipayModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(AlipayModel alipayModel) {
                        if (alipayModel.getCode() == AppConfig.success_code) {
                            if (alipayModel.getData() != null) {
                                String orderString = alipayModel.getData().getOrderStr();
//                                if(orderNumber==0){
//                                    PayUtils.pay(PayUtils.PAY_TYPE_ALI, orderString, activity,false);
//
//                                }else {
//                                    PayUtils.pay(PayUtils.PAY_TYPE_ALI, orderString, activity,true);
//
//                                }
                            } else {
                                ToastUtils.show("支付失败");

                            }

                        } else if (alipayModel.getCode()==AppConfig.login_fail) {

                            if(callBack!=null){
                                callBack.loginFial();
                            }
                            SharedPreferencesUtil.exitLoginNotSend();

                            //登录失败

                        } else {
                            ToastUtils.show(alipayModel.getMsg());
                        }

                    }
                });
    }

    /**
     * @param id
     * mercdName 打印照片
     * @param mercdWorth 打印照片的金额
     * @param mobile 手机号
     * @param orderNumber 10000
     * remark 备注
     * @param uname 用户名
     * @param url 照片路径
     * @param activity
     * @param callBack
     */
    public static void alipayPrintOrdr(int id, String mercdWorth, String mobile,final int orderNumber, String uname, String url,
                                    final Activity activity, final CallBack callBack) {
        AllipayRequestBody2 allipayRequestBody = new AllipayRequestBody2();
        allipayRequestBody.setId(id);
        allipayRequestBody.setMercdName("打印照片");
        allipayRequestBody.setMercdWorth(mercdWorth);
        allipayRequestBody.setMobile(mobile);
        allipayRequestBody.setOrderNumber(orderNumber);
        allipayRequestBody.setRemark(Contants.PLATFORM+"--版本:"+ AppUtils.getVersionName(Common.getApplication()));
        allipayRequestBody.setUname(uname);
        allipayRequestBody.setUrl(url);
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .alipayPrintOrdr(allipayRequestBody)
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<AlipayModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(AlipayModel alipayModel) {
                        if (alipayModel.getCode() == AppConfig.success_code) {
                            if (alipayModel.getData() != null) {
                                String orderString = alipayModel.getData().getOrderStr();
//                                if(orderNumber == 0){
//                                    PayUtils.pay(PayUtils.PAY_TYPE_ALI, orderString, activity,false);
//                                }else {
//                                    PayUtils.pay(PayUtils.PAY_TYPE_ALI, orderString, activity,true);
//                                }
                            } else {
                                ToastUtils.show("支付失败");
                            }
                        } else if (alipayModel.getCode()==AppConfig.login_fail) {

                            if(callBack!=null){
                                callBack.loginFial();
                            }
                            SharedPreferencesUtil.exitLoginNotSend();
                            //登录失败

                        } else {
                            ToastUtils.show(alipayModel.getMsg());
                        }

                    }
                });
    }

    /**
     * 注册
     *
     * @param account
     * @param password
     * @param observable
     */
    public static void regist(String account, String password,String inviteCode, NetWorkSubscriber observable) {

        RegistRequestBody registRequestBody = new RegistRequestBody();
        registRequestBody.setAccount(account);
        boolean isThisPhoneHasUserInviteCode= (boolean) SharedPreferencesUtil.getParam(AppConfig.isThisPhoneHasUserInviteCode,false);
        if(!isThisPhoneHasUserInviteCode){
            registRequestBody.setInviteCode(inviteCode);

        }else {
//            ToastUtils.show("系统检测您正在做违规的事情");
//            registRequestBody.setInviteCode(AppConfig.brush);

        }
        registRequestBody.setPassword(AesUtil.aesEncrypt(password, AesUtil.KEY));
        registRequestBody.setRePassword(AesUtil.aesEncrypt(password, AesUtil.KEY));


        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .regist(registRequestBody)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(observable);


    }





    /**
     * 支付宝成功回调
     * @param observable
     */
    public static void alipaySuccess(NetWorkSubscriber observable) {




        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .alipaySuceessCallBack()
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(observable);


    }




    public static void UserClean(final CallBack callBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .userClean()
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        if(baseResponse.getCode()==AppConfig.success_code){
                            ToastUtils.show("注销成功");
                            SharedPreferencesUtil.exitLogin();
                            if(callBack!=null){
                                callBack.onSuccess();
                            }
                        }else if(baseResponse.getCode()==AppConfig.login_fail){
                            SharedPreferencesUtil.exitLogin();

                        }else {
                            ToastUtils.show("注销失败");

                        }
                    }
                });
    }

    public static void getVip(final CallBack4 callBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getVipPrice()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<VipPriceBean>() {

                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(VipPriceBean routePriceBean) {
                        VipPriceBean printPriceBean = routePriceBean;
                        String yue = printPriceBean.getYue();
                        String ji = printPriceBean.getJi();
                        String nian = printPriceBean.getNian();
                        Log.e("aaa...2",yue + "..." + ji + "..." + nian);
                        if (printPriceBean != null){
                            if (callBack != null) {
                                callBack.onSuccess(yue, ji, nian);
                            }
                        }else {
                            if (callBack != null) {
                                callBack.onFail();
                            }
                        }
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        if (callBack != null) {
                            callBack.onFail();
                        }
                    }
                });
    }


    public interface CallBack {
        void onSuccess();

        void onFail();

        void loginFial();
    }

    public interface CallBack2 {
        void onSuccess(List<RoutePriceBean.DataBean> data);
        void onFail();
    }

    public interface CallBack3 {
        void onSuccess(String yue);
        void onFail();
    }

    public interface CallBack4 {
        void onSuccess(String yue, String ji, String nian);
        void onFail();
    }
}
