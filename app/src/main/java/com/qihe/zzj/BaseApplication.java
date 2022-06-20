package com.qihe.zzj;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.qihe.zzj.bean.IdPhotoBean;
import com.qihe.zzj.util.DaoManager;
import com.qihe.zzj.util.FrescoUtils;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.Common;
import com.xinqidian.adcommon.util.IdPhotoUtil;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.OKHttpUpdateHttpService;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;

/**
 * Created by cz on 2020/5/29.
 */
public class BaseApplication extends Application {

    private static Context context;

    private boolean isNeedShowToast;

    public static IdPhotoBean sIdPhotoBean = new IdPhotoBean();
    public static String mYu;
    public static String mJi;
    public static String mNian;

    public static String mPath = "";
    public static String fileName2 = "";

    public static String mNewPath = "";
    public static String fileNewName = "";

    public static String sName = "";

    public static String openId = "";
    public static String mercdName;//商品名称
    public static double mercdWorth;//商品价格
    public static String orderId;//时间戳表示的订单号productId
    public static int orderNumber;//0表示单次支付，1表示月vip，3表示季vip，12表示年vip
    public static String payTime;//2021-03-16 16:19:50


    private void initGreenDao() {
        DaoManager mManager = DaoManager.getInstance();
        mManager.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        NewOkGo.getInstance().init((Application) context);
        KLog.init(BuildConfig.DEBUG);
        context = getApplicationContext();
        Fresco.initialize(this, FrescoUtils.getInstance().getFrescoConfig(this));
        init();
        StatService.trackCustomEvent(this, "onCreate", "");

        initOkGo();
        initGreenDao();

        UMConfigure.init(this, "6041a5296ee47d382b72ae47", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        //通过接口获取VIP的价格
        UserUtil.getVip(new UserUtil.CallBack4() {
            @Override
            public void onSuccess(String yue, String ji, String nian) {
                Log.e("aaa...3",yue + "..." + ji + "..." + nian);
                mYu = yue;
                mJi = ji;
                mNian = nian;
            }

            @Override
            public void onFail() {

            }
        });
        IdPhotoUtil.setPrice();
    }

    private void init() {
//        Contants.XIAOMI_APPID = "2882303761518405271";//小米appID
//        Contants.XIAOMI_SPLASH_ID = "fb8c718e4fd4b233c83abeb18739d233";//小米开屏ID
//        Contants.XIAOMI_BANNER_ID = "f451381ce7263627e5c3883281713f24";//小米横幅ID
//        Contants.XIAOMI_NATIVE_ID = "ecdf590be6e665c67ce0295e0605a041";//小米原生ID
//        Contants.XIAOMI_INTERSTITIAL_ID = "904ee779ecef7a6367d73500edc442e9";//小米插屏ID
//        Contants.XIAOMI_STIMULATE_ID = "393586c7fcad674def6be155988ef77f";//小米激励视频ID

//        Contants.XIAOMI_APPID = "2882303761518428572";//小米appID
//        Contants.XIAOMI_SPLASH_ID = "babd7b247e45654787d3239d6a16b39b";//小米开屏ID
//        Contants.XIAOMI_BANNER_ID = "0b6795f90f07b71601c7ec207e126641";//小米横幅ID
//        Contants.XIAOMI_NATIVE_ID = "11f4363668307379a5081bc9af9eb8ed";//小米原生ID
//        Contants.XIAOMI_INTERSTITIAL_ID = "003353e055910028eeb4ba5d8b977217";//小米插屏ID
//        Contants.XIAOMI_STIMULATE_ID = "61cde47f15a60167c2572a691ed0d554";//小米激励视频ID
//
//        // 华为分流广告位
        Contants.TENCENT_APPID = "1110785639";//腾讯广告appID
        Contants.TENCENT_STIMULATE_ID = "5011219931259862";//腾讯激励视频ID
        Contants.TENCENT_BANNER_ID = "6011016991053833";//腾讯广告横幅ID
        Contants.TENCENT_NATIVE_ID = "7071914675706040";//腾讯广告原生ID
        Contants.TENCENT_INTERSTITIAL_ID = "9091912991053874";//腾讯广告插屏ID
        Contants.TENCENT_SPLASH_ID = "6021128565270108";//腾讯开屏广告ID

        //Vivo分流广告位
//        Contants.TENCENT_APPID = "1110525007";//腾讯广告appID
//        Contants.TENCENT_STIMULATE_ID = "6041819674840958 ";//腾讯激励视频ID
//        Contants.TENCENT_BANNER_ID = "2001419664904620";//腾讯广告横幅ID
//        Contants.TENCENT_NATIVE_ID = "4001212674548907";//腾讯广告原生ID
//        Contants.TENCENT_INTERSTITIAL_ID = "8081912694947989";//腾讯广告插屏ID
//        Contants.TENCENT_SPLASH_ID = "3081615634409508";//腾讯开屏广告ID

        //应用宝分流广告位
//        Contants.TENCENT_APPID = "1110525007";//腾讯广告appID
//        Contants.TENCENT_STIMULATE_ID = "6061018605600188 ";//腾讯激励视频ID
//        Contants.TENCENT_BANNER_ID = "2051610625101271";//腾讯广告横幅ID
//        Contants.TENCENT_NATIVE_ID = "6001217675306260";//腾讯广告原生ID
//        Contants.TENCENT_INTERSTITIAL_ID = "9041613635501139";//腾讯广告插屏ID
//        Contants.TENCENT_SPLASH_ID = "7051413615201222";//腾讯开屏广告ID

        //通用分流广告位
//        Contants.TENCENT_APPID = "1110525007";//腾讯广告appID
//        Contants.TENCENT_STIMULATE_ID = "6081110675709224 ";//腾讯激励视频ID
//        Contants.TENCENT_BANNER_ID = "4071617625806351";//腾讯广告横幅ID
//        Contants.TENCENT_NATIVE_ID = "2021619625702370";//腾讯广告原生ID
//        Contants.TENCENT_INTERSTITIAL_ID = "2041310635200247";//腾讯广告插屏ID
//        Contants.TENCENT_SPLASH_ID = "1071210695901322";//腾讯开屏广告ID

        //OPPO分流广告位
//        Contants.TENCENT_APPID = "1110525007";//腾讯广告appID
//        Contants.TENCENT_STIMULATE_ID = "2061015645907069 ";//腾讯激励视频ID
//        Contants.TENCENT_BANNER_ID = "2001115615404162";//腾讯广告横幅ID
//        Contants.TENCENT_NATIVE_ID = "1041911685707151";//腾讯广告原生ID
//        Contants.TENCENT_INTERSTITIAL_ID = "4021613645004180";//腾讯广告插屏ID
//        Contants.TENCENT_SPLASH_ID = "2091616615104195";//腾讯开屏广告ID

//        Contants.CHUANSHANJIA_APPID = "5100394";//穿山甲广告appID
        Contants.CHUANSHANJIA_APPID = "51003941";//穿山甲广告appID
//        Contants.CHUANSHANJIA_SPLASH_ID = "887370842";//穿山甲开屏广告ID
        Contants.CHUANSHANJIA_SPLASH_ID = "8873708421";//穿山甲开屏广告ID
        Contants.CHUANSHANJIA_BANNER_ID = "945286971";//穿山甲广告横幅ID
        Contants.CHUANSHANJIA_NATIVE_ID = "7091917232473114";//穿山甲广告原生ID
        Contants.CHUANSHANJIA_INTERSTITIAL_ID = "945286972";//穿山甲广告插屏ID
        Contants.CHUANSHANJIA_STIMULATE_ID = "945286970";//穿山甲激励视频ID


        /** 是否开启开屏广告**/
        Contants.IS_SHOW_SPLASH_AD = BuildConfig.IS_SHOW_SPLASH_AD;

        /** 是否开启横幅广告**/
        Contants.IS_SHOW_BANNER_AD = BuildConfig.IS_SHOW_BANNER_AD;

        /** 是否开启原生广告**/
        Contants.IS_SHOW_NATIVE_AD = BuildConfig.IS_SHOW_NATIVE_AD;

        /** 是否开启插屏广告**/
        Contants.IS_SHOW_VERTICALINTERSTITIAL_AD = BuildConfig.IS_SHOW_VERTICALINTERSTITIAL_AD;

        /** 是否开启激励视频广告**/
        Contants.IS_SHOW_STIMULATE_AD = BuildConfig.IS_SHOW_STIMULATE_AD;

        /** 免费使用功能的次数**/
        Contants.SHOW_STIMULATE_NUMBER = 5;

        /** 下载广告的时候是否需要已经提示 true点击下载先提示 false无需用提示,暂时只有华为平台需要*/
        Contants.IS_NEED_COMFIRMED = false;

        /** app启动是否需要显示隐私弹框*/
        Contants.IS_NEED_SERCERT = true;

        /** 多少次后显示好评弹框*/
        Contants.COMMENT_NUMBER = 5;

        /** leanClound appId*/
        Contants.LEANCLOUND_APPID = "xlpQHV45cGOTUt0UtfAGfgf4-gzGzoHsz";

        /** leanClound appKey*/
        Contants.LEANCLOUND_APPKEY = "K4JQk83O2GHOh16VwCJrunUI";

        Contants.BASE_URL = "http://www.qihe.website:8084/";

        Contants.QQ = "1156271983";

        /***平台*/
        Contants.PLATFORM = "huawei";

        Common.init(this);
    }

    public static Context getContext() {
        return context;
    }

    private void initOkGo() {
        LiveDataBus.get().with(LiveBusConfig.updateApp, Boolean.class).observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                isNeedShowToast = aBoolean;
            }
        });

        XUpdate.get()
                .debug(BuildConfig.DEBUG)
                .isWifiOnly(false)                                               //默认设置只在wifi下检查版本更新
                .isGet(true)                                                    //默认设置使用get请求检查版本
                .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
                .setOnUpdateFailureListener(new OnUpdateFailureListener() {     //设置版本更新出错的监听
                    @Override
                    public void onFailure(UpdateError error) {
                        if (isNeedShowToast) {
                            ToastUtils.show("已是最新版本了");
                        }
//                        KLog.e("error---->",new Gson().toJson(error));
                    }
                })
                .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
                .setIUpdateHttpService(new OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
                .init(this);
    }
}
