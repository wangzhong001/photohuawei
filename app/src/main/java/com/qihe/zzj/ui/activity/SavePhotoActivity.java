package com.qihe.zzj.ui.activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
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
import com.qihe.zzj.BR;
import com.qihe.zzj.BaseApplication;
import com.qihe.zzj.R;
import com.qihe.zzj.adapter.PicAdapter;
import com.qihe.zzj.bean.FunctionRecycBean;
import com.qihe.zzj.bean.IdPhotoBean;
import com.qihe.zzj.bean.IsPay;
import com.qihe.zzj.bean.MyRecycBean;
import com.qihe.zzj.databinding.ActivitySavePhotoBinding;
import com.qihe.zzj.util.IAPSupport;
import com.qihe.zzj.util.PhotoDaoUtils;
import com.qihe.zzj.view.MakePhotoViewModel;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.LoadingDialog;
import com.xinqidian.adcommon.view.SureDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;



public class SavePhotoActivity extends BaseActivity<ActivitySavePhotoBinding, MakePhotoViewModel> {

    public final static String PIC = "pic";
    private String pic;
    private PicAdapter picAdapter;
    private List<String> picList = new ArrayList<>();
    private FunctionRecycBean photo_data;
    private int type;

    private PhotoDaoUtils photoDaoUtils;
    private String time;

    private IdPhotoBean idPhotoBean;

    private SureDialog sureDialog;

    private SureDialog goodCommentDialog;

    private boolean isFree;

    private boolean isHasToast;

    private boolean isFail;

    private String price;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_save_photo;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
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

    @Override
    public void onStimulateSuccessCall() {

    }

    @Override
    public void onStimulateSuccessDissmissCall() {

    }

    private LoadingDialog loadingDialog;

    @Override
    public void initData() {
        super.initData();
        loadingDialog = new LoadingDialog(this, true);
        isFree = (boolean) SharedPreferencesUtil.getParam(AppConfig.isHasFree, false);

        sureDialog = new SureDialog(SavePhotoActivity.this, "主人离生成证件照只有一步之遥离", "确定退出吗")
                .addItemListener(new SureDialog.ItemListener() {
                    @Override
                    public void onClickSure() {
                        IsPay isPay = new IsPay();
                        isPay.setIs_pay(true);
                        EventBus.getDefault().post(isPay);
                        finish();
                    }

                    @Override
                    public void onClickCanel() {

                    }
                });
        photoDaoUtils = new PhotoDaoUtils(this);
        photo_data = (FunctionRecycBean) getIntent().getSerializableExtra("data");
        binding.tvMoney.setText("¥" + Contants.photoPrice);

        if (getIntent() != null) {
            pic = getIntent().getStringExtra(PIC);
            type = getIntent().getIntExtra("type", 0);
            if (type == 0) {
                viewModel.nameString.set(photo_data.getName() + "红");
            } else if (type == 1) {
                viewModel.nameString.set(photo_data.getName() + "白");

            } else if (type == 2) {
                viewModel.nameString.set(photo_data.getName() + "蓝");

            }
            viewModel.pxString.set(photo_data.getX_px() + "x" + photo_data.getY_px() + "px");
            viewModel.mmString.set(photo_data.getX_mm() + "x" + photo_data.getY_mm() + "mm");
            Long nowTime = System.currentTimeMillis();
            time = nowTime + "";
            Long sevenDay = 24 * 7 * 3600 * 1000L;
//            Long sevenDay=120*1000L;
            Long effectiveTime = nowTime + sevenDay;
            idPhotoBean = new IdPhotoBean(nowTime, photo_data.getKb(), pic, viewModel.pxString.get(), viewModel.mmString.get(), time, photo_data.getName(), Contants.photoPrice, effectiveTime, false);
            viewModel.insertPic(idPhotoBean);
        }

        ImmersionBar.with(this)
                .reset()
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .fitsSystemWindows(false)
                .init();
        viewModel.picUrl.set(pic);

        String name = photo_data.getName();
        Log.e("aaa","证件照名字1：" + name);
        if (name.contains("二寸")){
            addData2();
            binding.picRel.setLayoutManager(new GridLayoutManager(SavePhotoActivity.this, 2));
            picAdapter = new PicAdapter(R.layout.pic_item2, picList);
        }else {
            addData();
            binding.picRel.setLayoutManager(new GridLayoutManager(SavePhotoActivity.this, 4));
            picAdapter = new PicAdapter(R.layout.pic_item, picList);
        }
        binding.picRel.setAdapter(picAdapter);

//        FileUtils.deleteFile(pic);
    }

    public void addData() {
        for (int i = 0; i < 8; i++) {
            picList.add(pic);
        }
    }

    public void addData2() {
        for (int i = 0; i < 4; i++) {
            picList.add(pic);
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureDialog != null) {
                    sureDialog.show();

                }
            }
        });
        binding.savaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedPreferencesUtil.isLogin()) {
                    startActivity(LoginActivity.class);
                    return;
                }
                if (SharedPreferencesUtil.isVip()) {
                    //是vip
                    idPhotoBean.setIsPay(true);
                    photoDaoUtils.updateMeizi(idPhotoBean);
                    MyRecycBean myRecycBean = new MyRecycBean();
                    myRecycBean.setName(photo_data.getName());
                    myRecycBean.setX_px(viewModel.pxString.get());
                    myRecycBean.setMm(viewModel.mmString.get());
                    myRecycBean.setMoney(Contants.photoPrice);
                    myRecycBean.setUrl(pic);
                    myRecycBean.setTime(time);
                    myRecycBean.setPay(true);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", myRecycBean);
                    startActivity(PaySuccessActivity.class, bundle);
                    IsPay isPay = new IsPay();
                    isPay.setIs_pay(true);
                    EventBus.getDefault().post(isPay);
                } else {
                    //不是vip
                    UserUtil.getUserFreeNumber(loadingDialog, new UserUtil.CallBack() {
                        @Override
                        public void onSuccess() {
                            //有免费次数
                            UserUtil.updateFreeNumber(0, new UserUtil.CallBack() {
                                @Override
                                public void onSuccess() {
                                    //有免费次数
                                    idPhotoBean.setIsPay(true);
                                    photoDaoUtils.updateMeizi(idPhotoBean);
                                    MyRecycBean myRecycBean = new MyRecycBean();
                                    myRecycBean.setName(photo_data.getName());
                                    myRecycBean.setX_px(viewModel.pxString.get());
                                    myRecycBean.setMm(viewModel.mmString.get());
                                    myRecycBean.setMoney(Contants.photoPrice);
                                    myRecycBean.setUrl(pic);
                                    myRecycBean.setTime(time);
                                    myRecycBean.setPay(true);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("data", myRecycBean);
                                    startActivity(PaySuccessActivity.class, bundle);
                                    isFree = false;
                                    SharedPreferencesUtil.setParam(AppConfig.isHasFree, false);
                                    IsPay isPay = new IsPay();
                                    isPay.setIs_pay(true);
                                    EventBus.getDefault().post(isPay);
                                }

                                @Override
                                public void onFail() {
                                    //更新免费次数失败,此次免费次数不算,引导用户去付费制作
                                    getBuyIntentWithPrice(SavePhotoActivity.this);
                                }

                                @Override
                                public void loginFial() {
                                    startActivity(LoginActivity.class);
                                }
                            });
                        }

                        @Override
                        public void onFail() {
                            //没有免费次数，进行付费制作
                            getBuyIntentWithPrice(SavePhotoActivity.this);
                        }

                        @Override
                        public void loginFial() {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            }
        });
        LiveDataBus.get().with(LiveBusConfig.backHome, String.class).observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                finish();
            }
        });
        LiveDataBus.get().with(LiveBusConfig.alipaySuccess, Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                ToastUtils.show("支付成功");
                idPhotoBean.setIsPay(true);
                photoDaoUtils.updateMeizi(idPhotoBean);
                MyRecycBean myRecycBean = new MyRecycBean();
                myRecycBean.setName(photo_data.getName());
                myRecycBean.setX_px(viewModel.pxString.get());
                myRecycBean.setMm(viewModel.mmString.get());
                myRecycBean.setMoney(Contants.photoPrice);
                myRecycBean.setUrl(pic);
                myRecycBean.setTime(time);
                myRecycBean.setPay(true);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", myRecycBean);
                startActivity(PaySuccessActivity.class, bundle);
                finish();
            }
        });
    }

    private static final int PAY_PROTOCOL_INTENT = 3001;
    private static final int PAY_INTENT = 3002;

    public void getBuyIntentWithPrice(final Activity activity) {
        ToastUtils.show("加载中，请稍等");
        double newMoney = Double.parseDouble(Contants.photoPrice);
        int newMoney2 = (int) (newMoney * 100);
        Log.e("hw_login：", "money..." + newMoney2);
        BaseApplication.mercdWorth = newMoney;
//        BaseApplication.mercdWorth = 0.01;
        BaseApplication.orderNumber = 0;
        IapClient mClient = Iap.getIapClient(activity);
//        Task<PurchaseIntentResult> task = mClient.createPurchaseIntentWithPrice(IAPSupport.createGetBuyIntentWithPriceReq(
//                BaseApplication.mercdWorth, BaseApplication.orderNumber,newMoney2+"","单次购买"));

        // 构造一个PurchaseIntentReq对象
        PurchaseIntentReq req = new PurchaseIntentReq();
        // 通过createPurchaseIntent接口购买的商品必须是您在AppGallery Connect网站配置的商品。
        req.setProductId("zzj1");
        // priceType: 0：消耗型商品; 1：非消耗型商品; 2：订阅型商品
        req.setPriceType(0);
        req.setDeveloperPayload("test");
        PurchaseIntentWithPriceReq getBuyIntentWithPriceReq = IAPSupport.createGetBuyIntentWithPriceReq(
                BaseApplication.mercdWorth, BaseApplication.orderNumber, newMoney2 + "", "单次购买");
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
            startActivityForResult(SavePhotoActivity.this, status, PAY_PROTOCOL_INTENT);
        } else {
            ToastUtils.show("getBuyIntentWithPrice failed");
            Log.e("hw_login：","getBuyIntentWithPrice failed");
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
                startActivityForResult(activity, status, PAY_INTENT);
            } else {
                ToastUtils.show("check sign failed");
            }
        }
    }

    private void startActivityForResult(Activity activity, Status status, int reqCode) {
        if (status.hasResolution()) {
            try {
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
            getBuyIntentWithPrice(SavePhotoActivity.this);
        } else if (PAY_INTENT == requestCode) {
            handlePayResult(data);
        } else {
            ToastUtils.show("unknown requestCode in onActivityResult");
//            Log.e("hw_login：","unknown requestCode in onActivityResult");
        }
    }

    public void handlePayResult(Intent data) {
        // 调用parsePurchaseResultInfoFromIntent方法解析支付结果数据
        PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(SavePhotoActivity.this).parsePurchaseResultInfoFromIntent(data);
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
                    idPhotoBean.setIsPay(true);
                    photoDaoUtils.updateMeizi(idPhotoBean);
                    MyRecycBean myRecycBean = new MyRecycBean();
                    myRecycBean.setName(photo_data.getName());
                    myRecycBean.setX_px(viewModel.pxString.get());
                    myRecycBean.setMm(viewModel.mmString.get());
                    myRecycBean.setMoney(Contants.photoPrice);
                    myRecycBean.setUrl(pic);
                    myRecycBean.setTime(time);
                    myRecycBean.setPay(true);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", myRecycBean);
                    startActivity(PaySuccessActivity.class, bundle);
                    IsPay isPay = new IsPay();
                    isPay.setIs_pay(true);
                    EventBus.getDefault().post(isPay);

                    ToastUtils.show("支付成功");
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

            Log.e("hw_login：", "pay...photo...4");
            Log.e("hw_login：", "iapRtnCode：" + iapRtnCode);
        } else {
            Log.e("hw_login：", "pay...photo...5");
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
        // 消耗型商品发货成功后，需调用consumeOwnedPurchase接口进行消耗
        Task<ConsumeOwnedPurchaseResult> task = Iap.getIapClient(SavePhotoActivity.this).consumeOwnedPurchase(req);
        task.addOnSuccessListener(new OnSuccessListener<ConsumeOwnedPurchaseResult>() {
            @Override
            public void onSuccess(ConsumeOwnedPurchaseResult result) {
                // 获取接口请求结果
                Log.e("hw_login：", "pay...photo...6");
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
                    Log.e("hw_login：", "pay...photo...7");
                }
            }
        });
    }

    private void notifyService(){
        UserUtil.huaweiPay(BaseApplication.mercdName, BaseApplication.mercdWorth, BaseApplication.openId, BaseApplication.orderId,
                BaseApplication.orderNumber, BaseApplication.payTime,new UserUtil.CallBack() {
                    @Override
                    public void onSuccess() {
                        finish();
                        Log.e("hw_login：", "notifyService...1");
                    }

                    @Override
                    public void onFail() {
                        finish();
                        Log.e("hw_login：", "notifyService...2");
                    }

                    @Override
                    public void loginFial() {
                        Log.e("hw_login：", "notifyService...3");
                    }
                }, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveDataBus.get().with(LiveBusConfig.addPic, IdPhotoBean.class).postValue(idPhotoBean);
        KLog.e("订单生产--->", "订单生产--->");
    }

    @Override //再按一次退出程序
    public void onBackPressed() {
        if (sureDialog != null) {
            sureDialog.show();
        }
    }

}
