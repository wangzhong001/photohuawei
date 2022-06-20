package com.xinqidian.adcommon.app;

/**
 * Created by lipei on 2019/1/9.
 */

public interface BaseUrlApi {

    /**
     * 登录
     */
    String login="/api/user/login/login.json";

    /**
     * 华为登录
     */
    String loginHuaWei ="/api/user/login/huaweilogin.json";

    /**
     * 华为支付
     */
    String payHuaWei ="/api/idphoto/huaweiPay.json";

    /**
     * 退出登录
     */
    String exitLogin="/api/user/login/loginOut.json";

    /**
     * 注册
     */
    String regist="/api/user/register/account.json";



    /**
     *支付宝创建订单
     */
    String alipayCreateOrdr="/api/payOrder/createAliOrder.json";

    /**
     *支付宝创建订单
     */
    String alipayPrintOrdr="/api/payOrder/createPrintOrder.json";




    /**
     *支付宝支付成功回调
     */
    String alipaySuccessCall="/api/alipay/payMessCallBack.json";



    /**
     *获取用户信息
     */
    String getUserInfo="/api/user/info/home.json";



    /**
     *制作证件照
     */
    String makeIdPhoto="/api/idphoto/idmake.json";



    /**
     *更新制作证件照次数
     */
    String updateMakeIdPhotoNUmber="/api/user/info/reduceIdMakeCount.json";




    /**
     *更新免费使用次数
     */
    String uodateFreeNumber="/api/user/info/freeCount.json";




    /**
     *保存图片
     */
    String savaPic="/api/idphoto/idsave.json";




    /**
     *用户注销
     */
    String userClean="/api/user/info/userClear.json";

//    /**
//     * 获取最新版本
//     */
//    String new_version="/underwriter/getVersion.json";


    /**
     *门店列表
     */
    String stores="/api/idphoto/stores.json";

    /**
     *订单列表
     */
    String orders="/api/user/info/orders.json";

    /**
     *打印照片价格
     */
    String printprice ="http://rest.apizza.net/mock/1d90760be4fb704a97582e37acf94f7c/printprice";

    /**
     * vip价格
     */
    String vipPrice ="http://rest.apizza.net/mock/1d90760be4fb704a97582e37acf94f7c/huiyuan";
}
