package com.xinqidian.adcommon.util;

import android.app.Dialog;
import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.app.BaseServiceApi;
import com.xinqidian.adcommon.app.BaseUrlApi;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.http.BaseResponse;
import com.xinqidian.adcommon.http.ResponseThrowable;
import com.xinqidian.adcommon.http.net.NetWorkSubscriber;
import com.xinqidian.adcommon.http.util.RetrofitClient;
import com.xinqidian.adcommon.http.util.RxUtils;
import com.xinqidian.adcommon.login.IdPhotoModel;
import com.xinqidian.adcommon.login.SavaPicModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by lipei on 2020/6/29.
 */

public class IdPhotoUtil {

    /**
     *
     * @param type 证件照类型
     * @param filePth 需要制作证件照图片路径
     * @param context
     * @param dialog 上传加载框
     */

    public static void makeIdPhoto(String type, File filePth, Context context, final Dialog dialog){

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), filePth);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("srcfile", filePth.getName(), imageBody);

        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .makeIdPhoto(type,imageBodyPart)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.bindToLifecycle(context))
                .subscribeWith(new NetWorkSubscriber<IdPhotoModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return dialog;
                    }

                    @Override
                    protected void onSuccess(IdPhotoModel baseResponse) {
                        if(baseResponse.getCode()== AppConfig.success_code){
                            if(baseResponse.getData()!=null){
                                LiveDataBus.get().with(LiveBusConfig.idPhotoModel,IdPhotoModel.DataBean.class).postValue(baseResponse.getData());
                            }

                        }else if(baseResponse.getCode()==AppConfig.login_fail){
                            SharedPreferencesUtil.exitLogin();

                        }else {
                            LiveDataBus.get().with(LiveBusConfig.makeFail,String.class).postValue(LiveBusConfig.makeFail);
//                            ToastUtils.show("制作失败,请联系客服");
                        }

                    }
                });
    }




    public static void savaPhoto(File filePth, final Dialog dialog,Context context){
//        if(dialog!=null){
//            dialog.show();
//        }

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), filePth);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("srcfile", filePth.getName(), imageBody);

        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .savePhoto(imageBodyPart)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.bindToLifecycle(context))
                .subscribeWith(new NetWorkSubscriber<SavaPicModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return dialog;
                    }

                    @Override
                    protected void onSuccess(SavaPicModel baseResponse) {
                        if(baseResponse.getCode()== AppConfig.success_code){
                            if(baseResponse.getData()!=null){
                                LiveDataBus.get().with(LiveBusConfig.idPhotoModel,String.class).postValue(baseResponse.getData());
                            }

                        }else if(baseResponse.getCode()==AppConfig.login_fail){
                            SharedPreferencesUtil.exitLogin();

                        }else {
                            LiveDataBus.get().with(LiveBusConfig.makeFail,String.class).postValue(LiveBusConfig.makeFail);
//                            ToastUtils.show("制作失败,请联系客服");
                        }

                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        LiveDataBus.get().with(LiveBusConfig.makeFail,String.class).postValue(LiveBusConfig.makeFail);

                        if(dialog!=null){
                            dialog.dismiss();
                        }
                    }
                });


//        List<File> files=new ArrayList<>();
//        files.add(filePth);

//        OkGo.<String>post(Contants.BASE_URL + BaseUrlApi.savaPic)
//                .params("srcfile",filePth)
//                .isMultipart(true)
////                .upFile(filePth)
//                .execute(new com.lzy.okgo.callback.StringCallback() {
//                             @Override
//                             public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                                 String data = response.body();
//                                 SavaPicModel baseResponse = new Gson().fromJson(data, SavaPicModel.class);
//                                 if(baseResponse.getCode()== AppConfig.success_code){
//                                     if(baseResponse.getData()!=null){
//                                         LiveDataBus.get().with(LiveBusConfig.idPhotoModel,String.class).postValue(baseResponse.getData());
//                                     }
//
//                                 }else if(baseResponse.getCode()==AppConfig.login_fail){
//                                     SharedPreferencesUtil.exitLogin();
//
//                                 }else {
//                                     LiveDataBus.get().with(LiveBusConfig.makeFail,String.class).postValue(LiveBusConfig.makeFail);
////                            ToastUtils.show("制作失败,请联系客服");
//                                 }
//                                 dialog.dismiss();
//
//                             }
//
//                             @Override
//                             public void onError(com.lzy.okgo.model.Response<String> response) {
//                                 super.onError(response);
//                                 LiveDataBus.get().with(LiveBusConfig.makeFail,String.class).postValue(LiveBusConfig.makeFail);
//
//                                 dialog.dismiss();
//                             }
//                         }
//
//
//                );


    }



    public static void SerarchMakeIdPhotoNumber(Context context, final IdPhotoCallBack callBack){

//        OkGo.<String>get(Contants.BASE_URL + BaseUrlApi.updateMakeIdPhotoNUmber)
//                .execute(new com.lzy.okgo.callback.StringCallback() {
//                             @Override
//                             public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                                 String data = response.body();
//                                 BaseResponse baseResponse = new Gson().fromJson(data, BaseResponse.class);
//                                 if(baseResponse.getCode()==AppConfig.success_code){
//                                     int number=SharedPreferencesUtil.getMakeIdPhotoNumber();
//                                     number-=1;
//                                     SharedPreferencesUtil.setMakeIdPhotoNumber(number);
//                                     if(callBack!=null){
//                                         callBack.success();
//                                     }
//
//                                 }else if(baseResponse.getCode()==AppConfig.login_fail){
//                                     SharedPreferencesUtil.exitLogin();
//                                 }else {
//                                     if(callBack!=null){
//                                         callBack.fail();
//                                     }
//                                 }
//
//                             }
//
//                             @Override
//                             public void onError(com.lzy.okgo.model.Response<String> response) {
//                                 super.onError(response);
//                             }
//                         }
//
//
//                );



        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .updateMakeIdPhotoNumber()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.bindToLifecycle(context))
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }



                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        if(baseResponse.getCode()==AppConfig.success_code){
                            int number=SharedPreferencesUtil.getMakeIdPhotoNumber();
                            number-=1;
                            SharedPreferencesUtil.setMakeIdPhotoNumber(number);
                            if(callBack!=null){
                                callBack.success();
                            }

                        }else if(baseResponse.getCode()==AppConfig.login_fail){
                            SharedPreferencesUtil.exitLogin();
                        }else {
                            if(callBack!=null){
                                callBack.fail();
                            }
                        }

                    }
                });
    }

    public interface IdPhotoCallBack{
        void success();

        void fail();
    }


    public static void setPrice(){

        OkGo.<String>post("http://rest.apizza.net/mock/1d90760be4fb704a97582e37acf94f7c/qihe/zhengjianzhao").execute(new com.lzy.okgo.callback.StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                String data=response.body();
//                KLog.e("price--->",data+"---js");


                PriceModel priceModel=new Gson().fromJson(data,PriceModel.class);
//                KLog.e("price--->",priceModel.getPrice()+"---jss"+priceModel.getModifyContent());



                    if(Contants.PLATFORM.equals("huawei")){
                        if(priceModel.getHuaweiprice()!=null){
                            Contants.photoPrice=priceModel.getHuaweiprice();

                        }
                    }else if(Contants.PLATFORM.equals("xiaomi")){
                        if(priceModel.getXiaomiprice()!=null){
                            Contants.photoPrice=priceModel.getXiaomiprice();

                        }
                    }else if(Contants.PLATFORM.equals("vivo")){
                        if(priceModel.getVivoprice()!=null){
                            Contants.photoPrice=priceModel.getVivoprice();

                        }
                    }else if(Contants.PLATFORM.equals("oppo")){
                        if(priceModel.getOppoprice()!=null){
                            Contants.photoPrice=priceModel.getOppoprice();

                        }
                    }else if(Contants.PLATFORM.equals("应用宝")){
                        if(priceModel.getTencnetprice()!=null){
                            Contants.photoPrice=priceModel.getTencnetprice();

                        }
                    }else if(Contants.PLATFORM.equals("common")){
                        if(priceModel.getCommonnprice()!=null){
                            Contants.photoPrice=priceModel.getCommonnprice();

                        }
                    }else if(Contants.PLATFORM.equals("update")){
                        if(priceModel.getUpdateprice()!=null){
                            Contants.photoPrice=priceModel.getUpdateprice();
                        }
                    }else if(Contants.PLATFORM.equals("wuhan")){
                        if(priceModel.getUpdateprice()!=null){
                            Contants.photoPrice=priceModel.getTencnetprice();
                        }
                    }
//                    SharedPreferencesUtil.setParam(Contants.photoPriceString,priceModel.getPrice());
//                    KLog.e("price--->",SharedPreferencesUtil.getParam(Contants.photoPriceString,Contants.photoPrice+"---"));



            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                super.onError(response);
            }
        });
    }
}
