package com.xinqidian.adcommon.app;


import com.xinqidian.adcommon.http.BaseResponse;
import com.xinqidian.adcommon.login.AlipayModel;
import com.xinqidian.adcommon.login.AllipayRequestBody;
import com.xinqidian.adcommon.login.AllipayRequestBody2;
import com.xinqidian.adcommon.login.IdPhotoModel;
import com.xinqidian.adcommon.login.LoginHuawei;
import com.xinqidian.adcommon.login.LoginRequestBody;
import com.xinqidian.adcommon.login.PayHuawei;
import com.xinqidian.adcommon.login.PrintPriceBean;
import com.xinqidian.adcommon.login.RegistRequestBody;
import com.xinqidian.adcommon.login.RoutePriceBean;
import com.xinqidian.adcommon.login.SavaPicModel;
import com.xinqidian.adcommon.login.UserModel;
import com.xinqidian.adcommon.login.VipPriceBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by lipei on 2019/1/9.
 */

public interface BaseServiceApi {

    /**
     * 登录
     * @param loginRequestBody
     * @return
     */
    @POST(BaseUrlApi.login)
    Observable<BaseResponse> login(@Body LoginRequestBody loginRequestBody);


    /**
     * 华为登录
     * @param loginRequestBody
     * @return
     */
    @POST(BaseUrlApi.loginHuaWei)
    Observable<BaseResponse> loginHuaWei(@Body LoginHuawei loginRequestBody);


    /**
     * 华为支付通知
     * @param loginRequestBody
     * @return
     */
    @POST(BaseUrlApi.payHuaWei)
    Observable<BaseResponse> payHuaWei(@Body PayHuawei loginRequestBody);


    /**
     * 退出登录
     * @return
     */
    @GET(BaseUrlApi.exitLogin)
    Observable<BaseResponse> exitLogin();

    /**
     * 注册
     * @param loginRequestBody
     * @return
     */
    @POST(BaseUrlApi.regist)
    Observable<BaseResponse> regist(@Body RegistRequestBody loginRequestBody);





    /**
     * 支付宝创建订单
     * @param allipayRequestBody
     * @return
     */
    @POST(BaseUrlApi.alipayCreateOrdr)
    Observable<AlipayModel> alipayCreateOrdr(@Body AllipayRequestBody allipayRequestBody);

    /**
     * 支付宝创建打印照片的订单
     * @param allipayRequestBody
     * @return
     */
    @POST(BaseUrlApi.alipayPrintOrdr)
    Observable<AlipayModel> alipayPrintOrdr(@Body AllipayRequestBody2 allipayRequestBody);


    /**
     * 支付宝支付成功回调
     * @return
     */
    @POST(BaseUrlApi.alipaySuccessCall)
    Observable<BaseResponse> alipaySuceessCallBack();




    /**
     * 获取用户信息
     * @return
     */
    @GET(BaseUrlApi.getUserInfo)
    Observable<UserModel> getUserInfo();





    /**
     * 制作证件照
     *
     */
    @Multipart
    @POST(BaseUrlApi.makeIdPhoto)
    Observable<IdPhotoModel> makeIdPhoto(@Query("type") String type, @Part MultipartBody.Part part );




    /**
     * 制作证件照
     *
     */
    @Multipart
    @POST(BaseUrlApi.savaPic)
    Observable<SavaPicModel> savePhoto(@Part MultipartBody.Part part );





    /**
     * 更新证件照次数
     *
     */
    @GET(BaseUrlApi.updateMakeIdPhotoNUmber)
    Observable<BaseResponse> updateMakeIdPhotoNumber();





    /**
     * 更新免费使用次数
     *
     */
    @POST(BaseUrlApi.uodateFreeNumber)
    Observable<BaseResponse> updateFreeNumber(@Query("type") int type);



    /**
     * 用户注销
     * @return
     */
    @GET(BaseUrlApi.userClean)
    Observable<BaseResponse> userClean();




//    /**
//     * 获取最新版本
//     *
//     */
//
//    @GET(BaseUrlApi.new_version)
//    Observable<UpdateBean> getNewVersion(@Query("platform") String desc, @Header("Accept-Language") String lang);


    /**
     * 获取收货门店地址
     * @return
     */
    @GET(BaseUrlApi.stores)
    Observable<RoutePriceBean> getStores();

    /**
     * 获取收货门店地址
     * @return
     */
    @GET(BaseUrlApi.orders)
    Observable<RoutePriceBean> getOrdersList();

    /**
     * 获取打印照片的价格
     * @return
     */
    @GET(BaseUrlApi.printprice)
    Observable<PrintPriceBean> getPrice();

    /**
     * vip价格
     * @return
     */
    @GET(BaseUrlApi.vipPrice)
    Observable<VipPriceBean> getVipPrice();
}
