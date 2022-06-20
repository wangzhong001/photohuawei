package com.qihe.zzj.ui.activity;

import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.BR;
import com.qihe.zzj.R;
import com.qihe.zzj.bean.KefuModel;
import com.qihe.zzj.databinding.ActivityCenterBinding;
import com.qihe.zzj.view.MakePhotoViewModel;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.util.JumpUtils;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

/**
 * Created by lipei on 2020/7/30.
 */

public class KefuCenterActivity extends BaseActivity<ActivityCenterBinding, MakePhotoViewModel> {
    private JumpUtils jumpUtils;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_center;
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

    @Override
    public void initData() {
        super.initData();
        ImmersionBar.with(this)
                .reset()
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .fitsSystemWindows(false)
                .init();
        viewModel.getKefuData();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.kefuLiveData.observe(this, new Observer<KefuModel>() {
            @Override
            public void onChanged(@Nullable KefuModel kefuModel) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("kefu", kefuModel);
                startActivity(QuestionActivity.class, bundle);
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.qqTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumpUtils == null) {
                    jumpUtils = new JumpUtils(KefuCenterActivity.this);
                }
                jumpUtils.jumpQQ();
            }
        });

        binding.phoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkAndRequestPermission();
                } else {
                    toCall();
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {"android.permission.CALL_PHONE"};
            if (
                    checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 1024);
            } else {
                toCall();

            }
        } else {
            toCall();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            toCall();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            ToastUtils.show("没有权限无法进行此操作");
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

    private void toCall(){
//        String phonNumber= (String) SharedPreferencesUtil.getParam(AppConfig.phoneNumber,"13588341546");
        String phonNumber= "13588341546";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phonNumber));
        startActivity(intent);
    }
}
