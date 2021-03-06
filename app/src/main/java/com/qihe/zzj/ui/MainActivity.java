package com.qihe.zzj.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.OwnedPurchasesReq;
import com.huawei.hms.iap.entity.OwnedPurchasesResult;
import com.huawei.hms.jos.AppUpdateClient;
import com.huawei.hms.jos.JosApps;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.api.client.Status;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;
import com.huawei.updatesdk.service.otaupdate.UpdateKey;
import com.qihe.zzj.BaseApplication;
import com.qihe.zzj.R;
import com.qihe.zzj.adapter.ViewPagerAdapter;
import com.qihe.zzj.bean.PayBean;
import com.qihe.zzj.databinding.ActivityMainBinding;
import com.qihe.zzj.ui.activity.LoginActivity;
import com.qihe.zzj.ui.fragment.FunctionFragment;
import com.qihe.zzj.ui.fragment.MyFragment;
import com.qihe.zzj.ui.fragment.PhotoFragment;
import com.qihe.zzj.ui.fragment.VIPFragment;
import com.qihe.zzj.view.VipDialog;
import com.smkj.zzj.gen.PayBeanHelp;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.UserModel;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import org.json.JSONException;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding, BaseViewModel> {

    private ViewPagerAdapter adapter;
    private FunctionFragment functionFragment;
    private PhotoFragment photoFragment;
    private VIPFragment vipFragment;
    private MyFragment myFragment;

    private List<String> titles;
    private List<Drawable> bgLists;
    private long mExitTime;
    private VipDialog vipDialog;
    private boolean isHasShow;

    private int tabPosition;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    //???????????????
    @Override
    public void initParam() {
        super.initParam();
        bgLists = new ArrayList<>();
        bgLists.add(getResources().getDrawable(R.drawable.selector_tab_home_iv_bg));
        bgLists.add(getResources().getDrawable(R.drawable.selector_tab_photo_iv_bg));
        bgLists.add(getResources().getDrawable(R.drawable.selector_tab_vip_iv_bg));
        bgLists.add(getResources().getDrawable(R.drawable.selector_tab_my_iv_bg));

        titles = new ArrayList<>();
        titles.add(getResources().getString(R.string.function));
        titles.add(getResources().getString(R.string.photo));
        titles.add(getResources().getString(R.string.vip));
        titles.add(getResources().getString(R.string.my));

        List<Fragment> fragments = new ArrayList<>();
        functionFragment = FunctionFragment.newInstance();
        photoFragment = PhotoFragment.newInstance();
        vipFragment = VIPFragment.newInstance();
        myFragment = MyFragment.newInstance();

        fragments.add(functionFragment);
        fragments.add(photoFragment);
        fragments.add(vipFragment);
        fragments.add(myFragment);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
    }

    /**
     * ????????????
     */
    @Override
    public void initData() {
        super.initData();
        checkMyPermission();
        createFile();

        AppUpdateClient client = JosApps.getAppUpdateClient(this);
        client.checkAppUpdate(this, new UpdateCallBack(this));

//        AppUpdateUtils.update(MainActivity.this, false);
        LiveDataBus.get().with(LiveBusConfig.updateApp, Boolean.class).postValue(false);

        if (!SharedPreferencesUtil.isVip()) {
            vipDialog = new VipDialog(MainActivity.this)
                    .addItemListener(new VipDialog.ItemListener() {
                        @Override
                        public void rechargeClick() {
                            binding.viewPager.setCurrentItem(2);
                        }

                        @Override
                        public void dissMissClick() {

                        }
                    });
            vipDialog.show();
            isHasShow = true;
        }

        Object login_type = SharedPreferencesUtil.getParam("login_type","");
        String type = (String) login_type;
        if ("1".equals(type)){//????????????
            Object miLogin = SharedPreferencesUtil.getParam("Huawei_name", "");
            String name = (String) miLogin;
            if ("".equals(name) || TextUtils.isEmpty(name)|| name == null){

            }else {
                // Authorization Code????????????code?????????token??????null
                AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                        .setProfile()
                        .setAuthorizationCode()
                        .createParams();
                // ID Token????????????token?????????code??????null
//                AccountAuthParams mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
//                        .setIdToken()
//                        .setAccessToken()
//                        .createParams();
                AccountAuthService service = AccountAuthManager.getService(MainActivity.this, authParams,0);
                Task<AuthAccount> task = service.silentSignIn();
                task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
                    @Override
                    public void onSuccess(AuthAccount authAccount) {
                        SharedPreferencesUtil.toLogin();
                        //??????????????????
                        BaseApplication.sName = authAccount.displayName;
                        BaseApplication.openId = authAccount.openId;
                        Log.e("hw_login???", "displayName:" + authAccount.getDisplayName());
                        Log.e("hw_login???", "openId:" + BaseApplication.openId);
                        //?????????????????????0?????????????????????1??????AppTouch??????
                        Log.e("hw_login???", "accountFlag:" + authAccount.getAccountFlag());
                        Log.e("hw_login???", "authorizationCode:" + authAccount.getAuthorizationCode());
                    }
                });


                //????????????????????????????????????????????????????????????
                OwnedPurchasesReq ownedPurchasesReq = new OwnedPurchasesReq();
                ownedPurchasesReq.setPriceType(0);// 0??????????????????; 1?????????????????????; 2??????????????????
                // ??????obtainOwnedPurchases?????????????????????????????????????????????????????????????????????
                Task<OwnedPurchasesResult> task2 = Iap.getIapClient(MainActivity.this).obtainOwnedPurchases(ownedPurchasesReq);
                task2.addOnSuccessListener(new OnSuccessListener<OwnedPurchasesResult>() {
                    @Override
                    public void onSuccess(OwnedPurchasesResult result) {
                        // ?????????????????????????????????
                        if (result != null && result.getInAppPurchaseDataList() != null) {
                            for (int i = 0; i < result.getInAppPurchaseDataList().size(); i++) {
                                String inAppPurchaseData = result.getInAppPurchaseDataList().get(i);
                                String inAppSignature = result.getInAppSignature().get(i);
                                // ???????????????IAP????????????inAppPurchaseData???????????????
                                // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                                try {
                                    InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
                                    String productId = inAppPurchaseDataBean.getProductId();
                                    //??????????????????-1????????????,0????????????,1????????????,2????????????
                                    int purchaseState = inAppPurchaseDataBean.getPurchaseState();
                                    int consumptionState = inAppPurchaseDataBean.getConsumptionState();
                                    Log.e("hw_login???", "??????:" + productId +"???purchaseState...:" + purchaseState
                                            +"???consumptionState...:" + consumptionState);
                                    List<PayBean> payBeanList = PayBeanHelp.getNewInstance(MainActivity.this).queryAllData();
                                    Log.e("hw_login???", "payBeanList2:" + payBeanList.size());
                                    if (payBeanList.size() > 0){
                                        for (int j = 0; j < payBeanList.size(); j++){
                                            final PayBean payBean = payBeanList.get(j);
                                            String productId2 = payBean.getOrderId();
                                            int orderNumber = payBean.getOrderNumber();
                                            Log.e("hw_login???", "productId2:" + productId2+"???productName...:" + orderNumber);
                                            if (productId2.equals(productId)){
                                                Log.e("hw_login???", "db...1");
                                                if (purchaseState == 0){
                                                    Log.e("hw_login???", "db...2");
                                                    if (orderNumber == 0){
                                                        UserUtil.updateFreeNumber(1, new UserUtil.CallBack() {
                                                            @Override
                                                            public void onSuccess() {
                                                                Log.e("hw_login???", "db...3...1");
                                                                Toast.makeText(MainActivity.this,"???????????????????????????????????????????????????????????????????????????",Toast.LENGTH_LONG).show();
                                                                PayBeanHelp.getNewInstance(MainActivity.this).deleteData(payBean);
                                                            }

                                                            @Override
                                                            public void onFail() {
                                                                Log.e("hw_login???", "db...3...2");
                                                            }

                                                            @Override
                                                            public void loginFial() {
                                                                Log.e("hw_login???", "db...3...3");
                                                                startActivity(LoginActivity.class);
                                                            }
                                                        });
                                                    }else if (orderNumber == 1){
                                                        Log.e("hw_login???", "db...4");
                                                        notifyService(payBean);
                                                        Toast.makeText(MainActivity.this,"??????????????????????????????????????????",Toast.LENGTH_LONG).show();
                                                        PayBeanHelp.getNewInstance(MainActivity.this).deleteData(payBean);
                                                    }else if (orderNumber == 3){
                                                        Log.e("hw_login???", "db...5");
                                                        notifyService(payBean);
                                                        Toast.makeText(MainActivity.this,"??????????????????????????????????????????",Toast.LENGTH_LONG).show();
                                                        PayBeanHelp.getNewInstance(MainActivity.this).deleteData(payBean);
                                                    }else if (orderNumber == 12){
                                                        Log.e("hw_login???", "db...6");
                                                        notifyService(payBean);
                                                        Toast.makeText(MainActivity.this,"??????????????????????????????????????????",Toast.LENGTH_LONG).show();
                                                        PayBeanHelp.getNewInstance(MainActivity.this).deleteData(payBean);
                                                    }
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {

                                }
                            }
                        }
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
                        }
                    }
                });


            }
        }else if ("2".equals(type)){//??????????????????

        }

//        showCommentFromFeatureDialog("??????App????????????????????????????????????????????????!");

//        FileUtils.createOrExistsDir(FileUtils.TAKE_PHOTO_FILE_PATH);
//        FileUtils.createOrExistsDir(FileUtils.UPDATE_PHOTO_FILE_PATH);

        ImmersionBar.with(this)
                .reset()
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .fitsSystemWindows(false)
                .init();

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.tablayout.setupWithViewPager(binding.viewPager);

        for (int i = 0; i < adapter.getCount(); i++) {
            TabLayout.Tab tab = binding.tablayout.getTabAt(i);//???????????????tab
            tab.setCustomView(R.layout.layout_tab_item);//????????????tab??????view
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_text);
            tab.getCustomView().findViewById(R.id.iv_tab_bg).setBackground(bgLists.get(i));
            if (i == 0) {
                // ???????????????tab???TextView?????????????????????
//                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//??????
//                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//????????????
                textView.setSelected(true);//?????????tab?????????
//                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
            textView.setText(titles.get(i));//??????tab????????????
        }

        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tv_tab_text).setSelected(true);
                tab.getCustomView().findViewById(R.id.iv_tab_bg).setSelected(true);
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_text);
//                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tabPosition = tab.getPosition();
                binding.viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1 ) {
                    ImmersionBar.with(MainActivity.this)
                            .reset()
                            .transparentStatusBar()
                            .statusBarDarkFont(true)
                            .fitsSystemWindows(false)
                            .init();
                }

                if (tab.getPosition() == 2 || tab.getPosition() == 3) {
                    ImmersionBar.with(MainActivity.this)
                            .reset()
                            .transparentStatusBar()
                            .statusBarDarkFont(false)
                            .fitsSystemWindows(false)
                            .init();
                }
                if (tab.getPosition() == 3) {
                    myFragment.upData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tv_tab_text).setSelected(false);
                tab.getCustomView().findViewById(R.id.iv_tab_bg).setSelected(false);
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_text);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public static String takeFileName;
    public static String updateFileName;
    public static String downFileName;

    /**
     * ???????????????
     */
    private void createFile() {
        takeFileName = Environment.getExternalStorageDirectory()+"/com.qihe.zzj/takePhoto/";
        updateFileName = Environment.getExternalStorageDirectory()+"/com.qihe.zzj/updatePhoto/";
        downFileName = Environment.getExternalStorageDirectory()+"/?????????/";
        File takeFile= new File(takeFileName);
        if (!takeFile.exists()) {
            takeFile.mkdirs();
        }
        File updateFile = new File(updateFileName);
        if (!updateFile.exists()) {
            updateFile.mkdirs();
        }
        File downName = new File(downFileName);
        if (!downName.exists()) {
            downName.mkdirs();
            Log.e("aaa1", "???????????????????????????");
        }else {
            Log.e("aaa1", "???????????????????????????");
        }
        Log.e("aaa1", "???????????????????????????");
    }

    private void notifyService(PayBean payBean){
        UserUtil.huaweiPay(payBean.getMercdName(), payBean.getMercdWorth(), payBean.getOpenId(),payBean.getOrderId(),
                payBean.getOrderNumber(), payBean.getPayTime(),new UserUtil.CallBack() {
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

    //??????VIP?????????
    private void showBuyVipDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        View view = View.inflate(MainActivity.this, R.layout.layout_main_dialog, null);
        TextView tv_text = view.findViewById(R.id.tv_text);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_background));
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(true);// true??????????????????
        dialog.show();
    }


    public void toVip(){
        binding.viewPager.setCurrentItem(2);
    }

    //????????????
    private boolean boolFirst = true;
    //????????????
    public void checkMyPermission() {
        if (Build.VERSION.SDK_INT >= 23 && boolFirst) {//????????????????????????????????????
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(perms[2]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 1024);
            }
            boolFirst = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            createFile();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("??????")
                    .setMessage("????????????????????????????????????????????????????????????")
                    .setCancelable(false)
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                            }
                        }
                    })
                    .show();
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setUserData(UserModel.DataBean data) {
        super.setUserData(data);
        if (!SharedPreferencesUtil.isVip() && !isHasShow) {
            if (vipDialog == null) {
                vipDialog = new VipDialog(MainActivity.this)
                        .addItemListener(new VipDialog.ItemListener() {
                            @Override
                            public void rechargeClick() {
                                binding.viewPager.setCurrentItem(1);
                            }

                            @Override
                            public void dissMissClick() {

                            }
                        });
            }
            isHasShow = true;
            vipDialog.show();
        }
    }

    @Override //????????????????????????
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime < 2000) {
            super.onBackPressed();
        } else {
            mExitTime = System.currentTimeMillis();
            ToastUtils.show("?????????????????????????????????");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3001) {
            if (tabPosition == 1){
                photoFragment.getBuyIntentWithPrice(MainActivity.this);
            }else if (tabPosition == 2){
                vipFragment.getBuyIntentWithPrice(MainActivity.this);
            }
        } else if (requestCode == 3002) {
            if (tabPosition == 1){
                Log.e("hw_login???", "pay...photo...3");
                photoFragment.handlePayResult(data);
            }else if (tabPosition == 2){
                Log.e("hw_login???", "pay...vip...3");
                vipFragment.handlePayResult(data);
            }
        } else {
            ToastUtils.show("unknown requestCode in onActivityResult");
//            showLog("unknown requestCode in onActivityResult");
        }
    }

    private ApkUpgradeInfo apkUpgradeInfo;

    private class UpdateCallBack implements CheckUpdateCallBack {

//        private WeakReference<MainActivity> weakMainActivity;
//
//        private UpdateCallBack(MainActivity apiActivity) {
//            this.weakMainActivity = new WeakReference<MainActivity>(apiActivity);
//        }

        private MainActivity apiActivity;

        private UpdateCallBack(MainActivity apiActivity) {
            this.apiActivity = apiActivity;
        }

        public void onUpdateInfo(Intent intent) {
            if (intent != null) {
//                MainActivity apiActivity = null;
//                if (weakMainActivity != null && weakMainActivity.get() != null){
//                    apiActivity = weakMainActivity.get();
//                }
                // ???????????????????????? Default_value????????????status?????????????????????????????????????????????
                int status = intent.getIntExtra(UpdateKey.STATUS, -1);
                // ????????????????????????
                int rtnCode = intent.getIntExtra(UpdateKey.FAIL_CODE, -1);
                // ???????????????????????????
                String rtnMessage = intent.getStringExtra(UpdateKey.FAIL_REASON);
                Serializable info = intent.getSerializableExtra(UpdateKey.INFO);
                //?????????????????????info????????????ApkUpgradeInfo????????????????????????????????????
                if (info instanceof ApkUpgradeInfo) {
                    // ????????????showUpdateDialog?????????????????????????????????demo????????????????????????????????????????????????????????????checkUpdatePop()??????
                    if (apiActivity != null) {
                        Log.e("hw_login???","There is a new update");
                        apiActivity.apkUpgradeInfo = (ApkUpgradeInfo) info;
                        AppUpdateClient client = JosApps.getAppUpdateClient(MainActivity.this);
                        client.showUpdateDialog(MainActivity.this, apkUpgradeInfo, false);
                    }
                }
                Log.e("hw_login???", "onUpdateInfo status: " + status + ", rtnCode: " + rtnCode + ", rtnMessage: " + rtnMessage);
            }
        }

        @Override
        public void onMarketInstallInfo(Intent intent) {
            Log.e("hw_login???", "update???info not instanceof ApkUpgradeInfo");
        }

        @Override
        public void onMarketStoreError(int i) {
            Log.e("hw_login???", "update???check update failed");
        }

        @Override
        public void onUpdateStoreError(int i) {
            Log.e("hw_login???", "update???check update failed");
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
