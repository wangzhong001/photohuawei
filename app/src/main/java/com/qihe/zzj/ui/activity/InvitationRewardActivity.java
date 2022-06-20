package com.qihe.zzj.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.R;
import com.qihe.zzj.databinding.ActivityInvitationRewardBinding;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

/**
 * Created by lipei on 2020/7/24.
 */

public class InvitationRewardActivity extends BaseActivity<ActivityInvitationRewardBinding,BaseViewModel> {
    private String innCodel;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_invitation_reward;
    }

    @Override
    public int initVariableId() {
        return 0;
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
        innCodel= (String) SharedPreferencesUtil.getParam(Contants.inviteCode,"");
        binding.codeTv.setText(innCodel);
        ImmersionBar.with(InvitationRewardActivity.this)
                .reset()
                .transparentStatusBar()
                .fitsSystemWindows(false)
                .init();

        binding.shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Share2.Builder(InvitationRewardActivity.this)
                        .setContentType(ShareContentType.TEXT)
                        .setTextContent("赶快点击"+((String) SharedPreferencesUtil.getParam(AppConfig.downUrl,""))+"下载吧")
                        .build()
                        .shareBySystem();
            }
        });


        binding.copyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //复制
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText(null, innCodel);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);

                    ToastUtils.show("复制成功");
                }catch (Exception e){
                    ToastUtils.show("复制失败");

                }

            }
        });

        String url= (String) SharedPreferencesUtil.getParam(AppConfig.updatePic,"");
        Glide.with(InvitationRewardActivity.this).load(url).placeholder(R.drawable.kong_order_icon)
                .error(R.drawable.kong_order_icon).into(binding.urlPic);

    }
}
