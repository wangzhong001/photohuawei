package com.qihe.zzj.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.R;
import com.qihe.zzj.databinding.ActivitySettingBinding;
import com.qihe.zzj.util.AndroidShareUtils;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.ui.activity.FeedBackActivity;
import com.xinqidian.adcommon.util.AppUpdateUtils;
import com.xinqidian.adcommon.util.AppUtils;
import com.xinqidian.adcommon.util.JumpUtils;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.view.SureDialog;

public class SettingActivity extends BaseActivity<ActivitySettingBinding, BaseViewModel> {
    private JumpUtils jumpUtils;

    private SureDialog sureDialog;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_setting;
    }

    @Override
    public void initData() {
        super.initData();

        ImmersionBar.with(this)
                .reset()
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //意见反馈
        binding.rllFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, FeedBackActivity.class));
            }
        });

        //系统隐私权限
        binding.rllSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);
            }
        });

        //在线评论
        binding.rllContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidShareUtils.goToMarket(SettingActivity.this);
            }
        });

        //隐私政策
        binding.rllPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, com.xinqidian.adcommon.ui.activity.WebViewActivity.class);
                intent.putExtra("title", getResources().getString(R.string.privacy_policy));
                intent.putExtra("url", "http://www.shimukeji.cn/yinsimoban_zhengjianzhao_qihe.html");
                startActivity(intent);
            }
        });

        //用户协议
        binding.rllUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, com.xinqidian.adcommon.ui.activity.WebViewActivity.class);
                intent.putExtra("title", getResources().getString(R.string.user_agreement));
                intent.putExtra("url", "http://www.shimukeji.cn/yonghu_xieyi_qihe.html");
                startActivity(intent);
            }
        });


        binding.rllService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(KefuCenterActivity.class);
//                if(jumpUtils==null){
//                    jumpUtils=new JumpUtils(SettingActivity.this);
//                }
//                jumpUtils.jumpQQ();
            }
        });


        binding.rllUpVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUpdateUtils.update(SettingActivity.this,true);
            }
        });



        binding.tvVersion.setText(AppUtils.getVersionName(SettingActivity.this));

        if(SharedPreferencesUtil.isLogin()){
            binding.tvLoginOut.setVisibility(View.VISIBLE);
            binding.tvZhuxiao.setVisibility(View.VISIBLE);
        }else {
            binding.tvLoginOut.setVisibility(View.GONE);
            binding.tvZhuxiao.setVisibility(View.GONE);


        }

        binding.tvLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserUtil.exitLogin();
                SharedPreferencesUtil.setParam("miLogin","");
                finish();
            }
        });


        binding.tvZhuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sureDialog==null){
                    sureDialog=new SureDialog(SettingActivity.this,"注销用户后,当前用户数据全部删除，且不可找回和恢复","确定注销吗？","取消","确定")
                            .addItemListener(new SureDialog.ItemListener() {
                                @Override
                                public void onClickSure() {
                                    UserUtil.UserClean(new UserUtil.CallBack() {
                                        @Override
                                        public void onSuccess() {
                                            SharedPreferencesUtil.setParam("miLogin","");
                                            finish();
                                        }

                                        @Override
                                        public void onFail() {

                                        }

                                        @Override
                                        public void loginFial() {

                                        }
                                    });

                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });
                }
                sureDialog.show();
            }
        });


        LiveDataBus.get().with(LiveBusConfig.updateApp,Boolean.class).postValue(true);

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
}
