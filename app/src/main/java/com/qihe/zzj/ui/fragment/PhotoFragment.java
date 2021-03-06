package com.qihe.zzj.ui.fragment;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.qihe.zzj.bean.IdPhotoBean;
import com.qihe.zzj.bean.IsPay;
import com.qihe.zzj.bean.MyRecycBean;
import com.qihe.zzj.databinding.FragmentPhotoBinding;
import com.qihe.zzj.ui.MainActivity;
import com.qihe.zzj.ui.activity.LoginActivity;
import com.qihe.zzj.ui.activity.OrderDetailActivity;
import com.qihe.zzj.ui.activity.PaySuccessActivity;
import com.qihe.zzj.ui.activity.PhotoAllActivity;
import com.qihe.zzj.util.IAPSupport;
import com.qihe.zzj.view.MakePhotoViewModel;
import com.qihe.zzj.view.PicItemViewModel;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseFragment;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.LoadingDialog;
import com.xinqidian.adcommon.view.SureDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;


/**
 * ????????????
 */
public class PhotoFragment extends BaseFragment<FragmentPhotoBinding, MakePhotoViewModel> {

    private SureDialog sureDialog;
    private LoadingDialog loadingDialog;

    public static PhotoFragment newInstance() {
        PhotoFragment fragment = new PhotoFragment();
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return R.layout.fragment_photo;
    }

    @Override
    public int initVariableId() {
        return com.qihe.zzj.BR.baseViewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
    }

    @Override
    public void initData() {
        super.initData();
        binding.recyclerView.setNestedScrollingEnabled(false);
        viewModel.getPic();
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext(), true);
        }
    }

    //EventBus ????????????????????????
    @Subscribe
    public void upData(IsPay isPay) {
        if (isPay.isIs_pay()){
            viewModel.getPic();
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.goPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PhotoAllActivity.class);
            }
        });
        viewModel.toDetailLiveData.observe(this, new Observer<IdPhotoBean>() {
            @Override
            public void onChanged(@Nullable IdPhotoBean idPhotoBean) {
                BaseApplication.sIdPhotoBean = idPhotoBean;
                MyRecycBean myRecycBean = new MyRecycBean();
                myRecycBean.setName(idPhotoBean.getName());
                myRecycBean.setX_px(idPhotoBean.getPxSize());
                myRecycBean.setMm(idPhotoBean.getMmSize());
                myRecycBean.setPay(idPhotoBean.getIsPay());
                myRecycBean.setMoney(idPhotoBean.getMoney());
                myRecycBean.setUrl(idPhotoBean.getUrl());
                myRecycBean.setTime(idPhotoBean.getCreateTime());
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("data", myRecycBean);
                startActivity(intent);
            }
        });
        viewModel.payLiveData.observe(this, new Observer<PicItemViewModel>() {
            @Override
            public void onChanged(@Nullable final PicItemViewModel picItemViewModel) {
                if (!SharedPreferencesUtil.isLogin()) {
                    startActivity(LoginActivity.class);
                    return;
                }
                if (SharedPreferencesUtil.isVip()) {
                    //vip
                    viewModel.setPaySuccess();
                } else {
                    //??????vip??????????????????????????????????????????
                    UserUtil.getUserFreeNumber(loadingDialog, new UserUtil.CallBack() {
                        @Override
                        public void onSuccess() {
                            //??????????????????????????????????????????????????????
                            UserUtil.updateFreeNumber(0, new UserUtil.CallBack() {
                                @Override
                                public void onSuccess() {
                                    //???????????????????????????
                                    viewModel.setPaySuccess();
                                }

                                @Override
                                public void onFail() {
                                    MainActivity activity = (MainActivity) getActivity();
                                    getBuyIntentWithPrice(activity);
                                }

                                @Override
                                public void loginFial() {
                                    startActivity(LoginActivity.class);
                                }
                            });
                        }

                        @Override
                        public void onFail() {
                            //??????????????????????????????????????????????????????
//                            UserUtil.alipayOrder(picItemViewModel.idPhotoBean.getName(), picItemViewModel.idPhotoBean.getMoney(), 0, getActivity(), new UserUtil.CallBack() {
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onFail() {
//
//                                }
//
//                                @Override
//                                public void loginFial() {
//                                    startActivity(LoginActivity.class);
//
//                                }
//                            });
                            MainActivity activity = (MainActivity) getActivity();
                            getBuyIntentWithPrice(activity);
                        }

                        @Override
                        public void loginFial() {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            }
        });
        LiveDataBus.get().with(LiveBusConfig.alipaySuccess, Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    viewModel.setPaySuccess();
                } else {
                    UserUtil.getUserInfo();
                }
            }
        });
        viewModel.paySuccessLiveData.observe(this, new Observer<MakePhotoViewModel>() {
            @Override
            public void onChanged(@Nullable MakePhotoViewModel makePhotoViewModel) {
                IdPhotoBean idPhotoBean = makePhotoViewModel.picItemViewModelObservableField.get().idPhotoBean;
                MyRecycBean myRecycBean = new MyRecycBean();
                myRecycBean.setName(idPhotoBean.getName());
                myRecycBean.setX_px(idPhotoBean.getPxSize());
                myRecycBean.setMm(idPhotoBean.getMmSize());
                myRecycBean.setMoney(idPhotoBean.getMoney());
                myRecycBean.setUrl(idPhotoBean.getUrl());
                myRecycBean.setTime(idPhotoBean.getCreateTime());
                myRecycBean.setPay(true);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", myRecycBean);
                startActivity(PaySuccessActivity.class, bundle);
            }
        });
        LiveDataBus.get().with(LiveBusConfig.canelOrder, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (isFirst) {
                    viewModel.canelOrder(viewModel.picItemViewModelObservableField.get(), false);
                }
            }
        });
        LiveDataBus.get().with(LiveBusConfig.deleteOrder, Integer.class).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer picItemViewModel) {
                if (isFirst) {
                    viewModel.getPic();
                }
            }
        });
        LiveDataBus.get().with(LiveBusConfig.updateOrder, Integer.class).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (isFirst) {
                    viewModel.getPic();
                }
            }
        });
        viewModel.deleteLiveData.observe(this, new Observer<PicItemViewModel>() {
            @Override
            public void onChanged(@Nullable final PicItemViewModel picItemViewModel) {
                if (picItemViewModel.isPay.get()) {
                    sureDialog = new SureDialog(getContext(), "", "?????????????????????")
                            .addItemListener(new SureDialog.ItemListener() {
                                @Override
                                public void onClickSure() {
                                    viewModel.canelOrder(picItemViewModel, false);
                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });
                } else {
                    sureDialog = new SureDialog(getContext(), "", "?????????????????????")
                            .addItemListener(new SureDialog.ItemListener() {
                                @Override
                                public void onClickSure() {
                                    viewModel.canelOrder(picItemViewModel, false);
                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });
                }
                sureDialog.show();
            }
        });
        LiveDataBus.get().with(LiveBusConfig.addPic, IdPhotoBean.class).observe(this, new Observer<IdPhotoBean>() {
            @Override
            public void onChanged(@Nullable IdPhotoBean aBoolean) {
                if (isFirst) {
                    int size = viewModel.list.size();
                    viewModel.list.add(new PicItemViewModel(viewModel, aBoolean, size));
                    viewModel.isEmpty.set(false);
                }
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
//                BaseApplication.mercdWorth, BaseApplication.orderNumber,"1","????????????"));
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
            MainActivity activity = (MainActivity) getActivity();
            startActivityForResult(activity, status, PAY_PROTOCOL_INTENT);
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
            MainActivity activity = (MainActivity) getActivity();
            getBuyIntentWithPrice(activity);
        } else if (PAY_INTENT == requestCode) {
            handlePayResult(data);
        } else {
            ToastUtils.show("???????????????");
//            ToastUtils.show("unknown requestCode in onActivityResult");
//            showLog("unknown requestCode in onActivityResult");
        }
    }

    public void handlePayResult(Intent data) {
        // ??????parsePurchaseResultInfoFromIntent??????????????????????????????
        PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(getActivity()).parsePurchaseResultInfoFromIntent(data);
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
                    viewModel.setPaySuccess();
                    viewModel.getPic();
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
        Activity activity = getActivity();
        // ??????????????????????????????????????????consumeOwnedPurchase??????????????????
        Task<ConsumeOwnedPurchaseResult> task = Iap.getIapClient(activity).consumeOwnedPurchase(req);
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
                Log.e("hw_login???", "notifyService...1");
            }

            @Override
            public void onFail() {
                Log.e("hw_login???", "notifyService...2");
            }

            @Override
            public void loginFial() {
                Log.e("hw_login???", "notifyService...3");
            }
        }, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

}
