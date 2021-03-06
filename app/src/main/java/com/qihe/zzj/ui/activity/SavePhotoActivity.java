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

        sureDialog = new SureDialog(SavePhotoActivity.this, "?????????????????????????????????????????????", "???????????????")
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
        binding.tvMoney.setText("??" + Contants.photoPrice);

        if (getIntent() != null) {
            pic = getIntent().getStringExtra(PIC);
            type = getIntent().getIntExtra("type", 0);
            if (type == 0) {
                viewModel.nameString.set(photo_data.getName() + "???");
            } else if (type == 1) {
                viewModel.nameString.set(photo_data.getName() + "???");

            } else if (type == 2) {
                viewModel.nameString.set(photo_data.getName() + "???");

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
        Log.e("aaa","???????????????1???" + name);
        if (name.contains("??????")){
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
                    //???vip
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
                    //??????vip
                    UserUtil.getUserFreeNumber(loadingDialog, new UserUtil.CallBack() {
                        @Override
                        public void onSuccess() {
                            //???????????????
                            UserUtil.updateFreeNumber(0, new UserUtil.CallBack() {
                                @Override
                                public void onSuccess() {
                                    //???????????????
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
                                    //????????????????????????,????????????????????????,???????????????????????????
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
                            //???????????????????????????????????????
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
                ToastUtils.show("????????????");
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
        ToastUtils.show("?????????????????????");
        double newMoney = Double.parseDouble(Contants.photoPrice);
        int newMoney2 = (int) (newMoney * 100);
        Log.e("hw_login???", "money..." + newMoney2);
        BaseApplication.mercdWorth = newMoney;
//        BaseApplication.mercdWorth = 0.01;
        BaseApplication.orderNumber = 0;
        IapClient mClient = Iap.getIapClient(activity);
//        Task<PurchaseIntentResult> task = mClient.createPurchaseIntentWithPrice(IAPSupport.createGetBuyIntentWithPriceReq(
//                BaseApplication.mercdWorth, BaseApplication.orderNumber,newMoney2+"","????????????"));

        // ????????????PurchaseIntentReq??????
        PurchaseIntentReq req = new PurchaseIntentReq();
        // ??????createPurchaseIntent????????????????????????????????????AppGallery Connect????????????????????????
        req.setProductId("zzj1");
        // priceType: 0??????????????????; 1?????????????????????; 2??????????????????
        req.setPriceType(0);
        req.setDeveloperPayload("test");
        PurchaseIntentWithPriceReq getBuyIntentWithPriceReq = IAPSupport.createGetBuyIntentWithPriceReq(
                BaseApplication.mercdWorth, BaseApplication.orderNumber, newMoney2 + "", "????????????");
        Task<PurchaseIntentResult> task = mClient.createPurchaseIntent(req);

        task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
            @Override
            public void onSuccess(PurchaseIntentResult result) {
//                dealSuccess(result, activity);
//                Log.e("hw_login???", "getBuyIntentWithPrice success");

                // ???????????????????????????
                Status status = result.getStatus();
                if (status.hasResolution()) {
                    try {
                        // 6666????????????????????????
                        // ??????IAP????????????????????????
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
            Log.e("hw_login???","getBuyIntentWithPrice failed");
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
        Log.e("hw_login???", "paymentData???" + result.getPaymentData());
        Log.e("hw_login???", "paymentSignature???" + result.getPaymentSignature());
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
                //???????????????????????????
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
//            Log.e("hw_login???","unknown requestCode in onActivityResult");
        }
    }

    public void handlePayResult(Intent data) {
        // ??????parsePurchaseResultInfoFromIntent??????????????????????????????
        PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(SavePhotoActivity.this).parsePurchaseResultInfoFromIntent(data);
        if (purchaseResultInfo != null) {
            int iapRtnCode = purchaseResultInfo.getReturnCode();
            switch(iapRtnCode) {
                case OrderStatusCode.ORDER_STATE_CANCEL:
                    // ????????????
                    ToastUtils.show("????????????");
                    break;
                case OrderStatusCode.ORDER_STATE_FAILED:
                case OrderStatusCode.ORDER_PRODUCT_OWNED:
                    // ?????????????????????????????????
                    break;
                case OrderStatusCode.ORDER_STATE_SUCCESS:
                    // ????????????
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

                    ToastUtils.show("????????????");
                    String inAppPurchaseData = purchaseResultInfo.getInAppPurchaseData();
                    String inAppPurchaseDataSignature = purchaseResultInfo.getInAppDataSignature();
                    // ??????????????????IAP??????????????????
                    // ?????????????????????????????????
                    // ???????????????????????????????????????????????????????????????????????????consumeOwnedPurchase??????????????????
                    surePay(inAppPurchaseData);
                    break;
                default:
                    break;
            }

            Log.e("hw_login???", "pay...photo...4");
            Log.e("hw_login???", "iapRtnCode???" + iapRtnCode);
        } else {
            Log.e("hw_login???", "pay...photo...5");
            Log.e("hw_login???", "iap failed");
        }
    }

    private void surePay(String inAppPurchaseData){
        // ??????ConsumeOwnedPurchaseReq??????
        ConsumeOwnedPurchaseReq req = new ConsumeOwnedPurchaseReq();
        String purchaseToken = "";
        try {
            // purchaseToken??????????????????InAppPurchaseData?????????
            InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
            purchaseToken = inAppPurchaseDataBean.getPurchaseToken();
        } catch (JSONException e) {
        }
        req.setPurchaseToken(purchaseToken);
        // ?????????????????????Activity??????
        // ??????????????????????????????????????????consumeOwnedPurchase??????????????????
        Task<ConsumeOwnedPurchaseResult> task = Iap.getIapClient(SavePhotoActivity.this).consumeOwnedPurchase(req);
        task.addOnSuccessListener(new OnSuccessListener<ConsumeOwnedPurchaseResult>() {
            @Override
            public void onSuccess(ConsumeOwnedPurchaseResult result) {
                // ????????????????????????
                Log.e("hw_login???", "pay...photo...6");
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
                    // ??????????????????
                    Log.e("hw_login???", "pay...photo...7");
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
                        Log.e("hw_login???", "notifyService...1");
                    }

                    @Override
                    public void onFail() {
                        finish();
                        Log.e("hw_login???", "notifyService...2");
                    }

                    @Override
                    public void loginFial() {
                        Log.e("hw_login???", "notifyService...3");
                    }
                }, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveDataBus.get().with(LiveBusConfig.addPic, IdPhotoBean.class).postValue(idPhotoBean);
        KLog.e("????????????--->", "????????????--->");
    }

    @Override //????????????????????????
    public void onBackPressed() {
        if (sureDialog != null) {
            sureDialog.show();
        }
    }

}
