package com.qihe.zzj.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseReq;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseResult;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.PurchaseIntentReq;
import com.huawei.hms.iap.entity.PurchaseIntentResult;
import com.huawei.hms.iap.entity.PurchaseIntentWithPriceReq;
import com.huawei.hms.iap.entity.PurchaseResultInfo;
import com.huawei.hms.support.api.client.Status;
import com.qihe.zzj.BaseApplication;
import com.qihe.zzj.R;
import com.qihe.zzj.databinding.FragmentViBinding;
import com.qihe.zzj.ui.MainActivity;
import com.qihe.zzj.ui.activity.LoginActivity;
import com.qihe.zzj.util.IAPSupport;
import com.xinqidian.adcommon.base.BaseFragment;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import org.json.JSONException;


/**
 * VIP界面
 */
public class VIPFragment extends BaseFragment<FragmentViBinding, BaseViewModel> {

    private boolean one_year;
    private boolean three_month;
    private boolean one_month;

    private int order_month = 12;
    private String vip_name = "一年会员";
    private String money = "39.99";


    public static VIPFragment newInstance() {
        VIPFragment fragment = new VIPFragment();
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_vi;
    }

    @Override
    public void initParam() {
        super.initParam();
    }

    @Override
    public void initData() {
        super.initData();
        binding.tv13.setText("￥" + BaseApplication.mNian);
        binding.tv23.setText("￥" + BaseApplication.mJi);
        binding.tv33.setText("￥" + BaseApplication.mYu);

        SpannableString spannableString01 = new SpannableString("￥300.99");
        SpannableString spannableString02 = new SpannableString("￥83.99");
        SpannableString spannableString03 = new SpannableString("￥32.99");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString01.setSpan(strikethroughSpan, 0, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString02.setSpan(strikethroughSpan, 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString03.setSpan(strikethroughSpan, 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.tv14.setText(spannableString01);
        binding.tv24.setText(spannableString02);
        binding.tv34.setText(spannableString03);

        one_year = true;
        binding.rll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_year = true;
                three_month = false;
                one_month = false;
                setTextColorAndBg(false, binding.rll1, binding.tv11, binding.tv12, binding.tv13, binding.tv14);
                setTextColorAndBg(true, binding.rll2, binding.tv21, binding.tv22, binding.tv23, binding.tv24);
                setTextColorAndBg(true, binding.rll3, binding.tv31, binding.tv32, binding.tv33, binding.tv34);

            }
        });

        binding.rll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_year = false;
                three_month = true;
                one_month = false;
                setTextColorAndBg(true, binding.rll1, binding.tv11, binding.tv12, binding.tv13, binding.tv14);
                setTextColorAndBg(false, binding.rll2, binding.tv21, binding.tv22, binding.tv23, binding.tv24);
                setTextColorAndBg(true, binding.rll3, binding.tv31, binding.tv32, binding.tv33, binding.tv34);
            }
        });

        binding.rll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_year = false;
                three_month = false;
                one_month = true;
                setTextColorAndBg(true, binding.rll1, binding.tv11, binding.tv12, binding.tv13, binding.tv14);
                setTextColorAndBg(true, binding.rll2, binding.tv21, binding.tv22, binding.tv23, binding.tv24);
                setTextColorAndBg(false, binding.rll3, binding.tv31, binding.tv32, binding.tv33, binding.tv34);
            }
        });


        binding.tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtil.isLogin()) {
                    if (one_year) {
                        order_month = 12;
                        vip_name = "一年会员";
//                        money = "39.99";
                        money = BaseApplication.mNian;
                    } else if (three_month) {
                        order_month = 3;
                        vip_name = "三个月会员";
//                        money = "19.99";
                        money = BaseApplication.mJi;
                    } else if (one_month) {
                        order_month = 1;
                        vip_name = "一个月会员";
//                        money = "9.99";
                        money = BaseApplication.mYu;
                    }

                    MainActivity activity = (MainActivity) getActivity();
                    getBuyIntentWithPrice(activity);
                } else {
                    ToastUtils.show("请先登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
    }

    private static final int PAY_PROTOCOL_INTENT = 3001;
    private static final int PAY_INTENT = 3002;

    public void getBuyIntentWithPrice(final Activity activity) {
        ToastUtils.show("加载中，请稍等");
        double newMoney = 0;
        int newMoney2 = 0;
        if("三个月会员".equals(vip_name)){
            newMoney = 19.99;
            newMoney2 = 1999;
        }else {
            newMoney = Double.parseDouble(money);
            newMoney2 = (int) (newMoney * 100);
        }
        Log.e("hw_login：", "money..." + newMoney2);
        BaseApplication.mercdWorth = newMoney;
//        BaseApplication.mercdWorth = 0.01;
        BaseApplication.orderNumber = order_month;
        IapClient mClient = Iap.getIapClient(activity);
//        Task<PurchaseIntentResult> task = mClient.createPurchaseIntentWithPrice(IAPSupport.createGetBuyIntentWithPriceReq(
//                BaseApplication.mercdWorth, BaseApplication.orderNumber,newMoney2+"",vip_name));
//                BaseApplication.mercdWorth, BaseApplication.orderNumber,"1",vip_name));

        // 构造一个PurchaseIntentReq对象
        PurchaseIntentReq req = new PurchaseIntentReq();
        // 通过createPurchaseIntent接口购买的商品必须是您在AppGallery Connect网站配置的商品。
        if(order_month == 1){
            req.setProductId("zzj_month");
        }else if(order_month == 3){
            req.setProductId("zzj_ji");
        }else if(order_month == 12){
            req.setProductId("zzj_year");
        }
        // priceType: 0：消耗型商品; 1：非消耗型商品; 2：订阅型商品
        req.setPriceType(0);
        req.setDeveloperPayload("test");
        PurchaseIntentWithPriceReq getBuyIntentWithPriceReq = IAPSupport.createGetBuyIntentWithPriceReq(
                BaseApplication.mercdWorth, BaseApplication.orderNumber, newMoney2 + "", vip_name);
        Task<PurchaseIntentResult> task = mClient.createPurchaseIntent(req);

        task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
            @Override
            public void onSuccess(PurchaseIntentResult result) {
//                dealSuccess(result, activity);
//                Log.e("hw_login：", "getBuyIntentWithPrice success");

                // 获取创建订单的结果
                Status status = result.getStatus();
                if (status.hasResolution()) {
                    try {
                        // 6666是您自定义的常量
                        // 启动IAP返回的收银台页面
                        status.startResolutionForResult(activity, PAY_INTENT);
                    } catch (IntentSender.SendIntentException exp) {
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    dealIAPFailed(statusCode, ((IapApiException) e).getStatus());
                }
            }
        });
    }

    public void dealIAPFailed(int statusCode, Status status) {
        if (statusCode == OrderStatusCode.ORDER_NOT_ACCEPT_AGREEMENT) {
            MainActivity activity = (MainActivity) getActivity();
            startActivityForResult(activity, status, PAY_PROTOCOL_INTENT);
        } else {
            ToastUtils.show("getBuyIntentWithPrice failed");
            Log.e("hw_login：", "getBuyIntentWithPrice failed");
        }
    }

    public void dealSuccess(PurchaseIntentResult result, Activity activity) {
        if (result == null) {
            ToastUtils.show("dealSuccess, result is null");
            return;
        }
        Status status = result.getStatus();
        if (status.getResolution() == null) {
            ToastUtils.show("intent is null");
            return;
        }
        Log.e("hw_login：", "paymentData：" + result.getPaymentData());
        Log.e("hw_login：", "paymentSignature：" + result.getPaymentSignature());
        if (result.getPaymentSignature() != null && result.getPaymentData() != null) {
            // check sign
            boolean success = IAPSupport.doCheck(result.getPaymentData(), result.getPaymentSignature());
            if (success) {
                Log.e("hw_login：", "pay...1");
                startActivityForResult(activity, status, PAY_INTENT);
            } else {
                ToastUtils.show("check sign failed");
            }
        }
    }

    private void startActivityForResult(Activity activity, Status status, int reqCode) {
        if (status.hasResolution()) {
            try {
                Log.e("hw_login：", "pay...2");
                //打开华为支付的界面
                status.startResolutionForResult(activity, reqCode);
            } catch (IntentSender.SendIntentException exp) {
                ToastUtils.show(exp.getMessage());
            }
        } else {
            ToastUtils.show("intent is null");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PAY_PROTOCOL_INTENT == requestCode) {
            MainActivity activity = (MainActivity) getActivity();
            getBuyIntentWithPrice(activity);
        } else if (PAY_INTENT == requestCode) {
            Log.e("hw_login：", "pay...3");
            handlePayResult(data);
        } else {
            ToastUtils.show("unknown requestCode in onActivityResult");
//            showLog("unknown requestCode in onActivityResult");
        }

//        if (requestCode == 6666) {
//            if (data == null) {
//                Log.e("onActivityResult", "data is null");
//                return;
//            }
//            // 调用parsePurchaseResultInfoFromIntent方法解析支付结果数据
//            PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(getActivity()).parsePurchaseResultInfoFromIntent(data);
//            switch(purchaseResultInfo.getReturnCode()) {
//                case OrderStatusCode.ORDER_STATE_CANCEL:
//                    // 用户取消
//                    ToastUtils.show("取消支付");
//                    break;
//                case OrderStatusCode.ORDER_STATE_FAILED:
//                case OrderStatusCode.ORDER_PRODUCT_OWNED:
//                    // 检查是否存在未发货商品
//                    break;
//                case OrderStatusCode.ORDER_STATE_SUCCESS:
//                    // 支付成功
//                    ToastUtils.show("支付成功");
//                    String inAppPurchaseData = purchaseResultInfo.getInAppPurchaseData();
//                    String inAppPurchaseDataSignature = purchaseResultInfo.getInAppDataSignature();
//                    // 使用您应用的IAP公钥验证签名
//                    // 若验签成功，则进行发货
//                    // 若用户购买商品为消耗型商品，您需要在发货成功后调用consumeOwnedPurchase接口进行消耗
//                    break;
//                default:
//                    break;
//            }
//        }
    }

    public void handlePayResult(Intent data) {
        // 调用parsePurchaseResultInfoFromIntent方法解析支付结果数据
        PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(getActivity()).parsePurchaseResultInfoFromIntent(data);
        if (purchaseResultInfo != null) {
            int iapRtnCode = purchaseResultInfo.getReturnCode();
            switch(iapRtnCode) {
                case OrderStatusCode.ORDER_STATE_CANCEL:
                    // 用户取消
                    ToastUtils.show("取消支付");
                    break;
                case OrderStatusCode.ORDER_STATE_FAILED:
                case OrderStatusCode.ORDER_PRODUCT_OWNED:
                    // 检查是否存在未发货商品
                    break;
                case OrderStatusCode.ORDER_STATE_SUCCESS:
                    // 支付成功
                    ToastUtils.show("支付成功");
                    SharedPreferencesUtil.setVip(true);
                    String inAppPurchaseData = purchaseResultInfo.getInAppPurchaseData();
                    String inAppPurchaseDataSignature = purchaseResultInfo.getInAppDataSignature();
                    // 使用您应用的IAP公钥验证签名
                    // 若验签成功，则进行发货
                    // 若用户购买商品为消耗型商品，您需要在发货成功后调用consumeOwnedPurchase接口进行消耗
                    surePay(inAppPurchaseData);
                    break;
                default:
                    break;
            }

            Log.e("hw_login：", "pay...vip...4");
            Log.e("hw_login：", "iapRtnCode：" + iapRtnCode);
        } else {
            Log.e("hw_login：", "pay...vip...5");
            Log.e("hw_login：", "iap failed");
        }
    }

    private void surePay(String inAppPurchaseData){
        // 构造ConsumeOwnedPurchaseReq对象
        ConsumeOwnedPurchaseReq req = new ConsumeOwnedPurchaseReq();
        String purchaseToken = "";
        try {
            // purchaseToken需从购买信息InAppPurchaseData中获取
            InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
            purchaseToken = inAppPurchaseDataBean.getPurchaseToken();
        } catch (JSONException e) {
        }
        req.setPurchaseToken(purchaseToken);
        // 获取调用接口的Activity对象
        Activity activity = getActivity();
        // 消耗型商品发货成功后，需调用consumeOwnedPurchase接口进行消耗
        Task<ConsumeOwnedPurchaseResult> task = Iap.getIapClient(activity).consumeOwnedPurchase(req);
        task.addOnSuccessListener(new OnSuccessListener<ConsumeOwnedPurchaseResult>() {
            @Override
            public void onSuccess(ConsumeOwnedPurchaseResult result) {
                // 获取接口请求结果
                Log.e("hw_login：", "pay...vip...6");
                notifyService();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    Status status = apiException.getStatus();
                    int returnCode = apiException.getStatusCode();
                } else {
                    // 其他外部错误
                    Log.e("hw_login：", "pay...vip...7");
                }
            }
        });
    }

    private void notifyService(){
        UserUtil.huaweiPay(BaseApplication.mercdName, BaseApplication.mercdWorth, BaseApplication.openId, BaseApplication.orderId,
                BaseApplication.orderNumber, BaseApplication.payTime,new UserUtil.CallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("hw_login：", "notifyService...1");
                    }

                    @Override
                    public void onFail() {
                        Log.e("hw_login：", "notifyService...2");
                    }

                    @Override
                    public void loginFial() {
                        Log.e("hw_login：", "notifyService...3");
                    }
                }, null);
    }

    @Override
    public boolean isShowBannerAd() {
        return false;
    }

    @Override
    public boolean isShowNativeAd() {
        return false;
    }

    @Override
    public boolean isShowVerticllAndStimulateAd() {
        return false;
    }

    private void setTextColorAndBg(boolean isClick, RelativeLayout rll, TextView tv1, TextView tv2, TextView tv3, TextView tv4) {
        if (isClick) {
            rll.setBackground(getResources().getDrawable(R.drawable.vip_uncheck));
            tv1.setTextColor(getResources().getColor(R.color.color_000000));
            tv2.setTextColor(getResources().getColor(R.color.color_000000));
            tv3.setTextColor(getResources().getColor(R.color.color_000000));
            tv4.setTextColor(getResources().getColor(R.color.color_000000));
        } else {
            rll.setBackground(getResources().getDrawable(R.drawable.vip_check));
            tv1.setTextColor(getResources().getColor(R.color.color_E8489E));
            tv2.setTextColor(getResources().getColor(R.color.color_E8489E));
            tv3.setTextColor(getResources().getColor(R.color.color_E8489E));
            tv4.setTextColor(getResources().getColor(R.color.color_E8489E));
        }
    }

    /**
     * 支付成功回调
     *
     * @param alipaySuccessState
     */

    @Override
    public void setAlipaySuccess(boolean alipaySuccessState) {
        super.setAlipaySuccess(alipaySuccessState);
        if (alipaySuccessState) {
            ToastUtils.show("欢迎尊贵的会员,赶快去制作心仪的证件照吧");
        }

    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void onStimulateSuccessCall() {
    }

    @Override
    public void onStimulateSuccessDissmissCall() {
    }

}
