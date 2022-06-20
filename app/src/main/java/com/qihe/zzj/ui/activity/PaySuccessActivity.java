package com.qihe.zzj.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.BR;
import com.qihe.zzj.Contans;
import com.qihe.zzj.R;
import com.qihe.zzj.bean.MyRecycBean;
import com.qihe.zzj.databinding.ActivityPaySuccesssBinding;
import com.qihe.zzj.view.MakePhotoViewModel;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.ui.activity.FeedBackActivity;
import com.xinqidian.adcommon.util.AndroidShareUtils;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.SureDialog;

import java.io.File;

import gdut.bsx.share2.FileUtil;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;


/**
 * Created by lipei on 2020/6/30.
 */
public class PaySuccessActivity extends BaseActivity<ActivityPaySuccesssBinding, MakePhotoViewModel> {

    private MyRecycBean myRecycBean;

    private int number;

    private SureDialog goodCommentDialog;

    private boolean isFree;

    private boolean isHasToast;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_pay_successs;
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
    protected void onRestart() {
        super.onRestart();
        if (isFree) {
            if (!isHasToast) {
                ToastUtils.show("免费制作奖励领取成功");
                isHasToast = true;
            }
        }
    }

    private void setGoodCommnet() {
        boolean hasFreeChange = SharedPreferencesUtil.isHasFreeNumber();
        boolean isNeeRemind = (boolean) SharedPreferencesUtil.getParam(Contans.goodNumber, true);
        if (hasFreeChange && isNeeRemind) {
            if (goodCommentDialog == null) {
                goodCommentDialog = new SureDialog(PaySuccessActivity.this, "给五星好评可免费制作证件照哦!赶快去给好评领取吧", "您可以免费制作证件照哦", "去反馈", "给好评", true)
                        .addItemListener(new SureDialog.ItemListener() {
                            @Override
                            public void onClickSure() {
                                AndroidShareUtils.goToMarket(PaySuccessActivity.this);
                                isFree = true;
                                SharedPreferencesUtil.setParam(AppConfig.isHasFree, true);
                                UserUtil.updateFreeNumber(0);
                                SharedPreferencesUtil.setFreeNumber(false);
                                SharedPreferencesUtil.setParam(Contans.goodNumber, false);
                            }

                            @Override
                            public void onClickCanel() {
                                startActivity(FeedBackActivity.class);
                            }
                        });
            }
            goodCommentDialog.show();
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.backHomeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savaPic();
                LiveDataBus.get().with(LiveBusConfig.backHome, String.class).postValue(LiveBusConfig.backHome);
                finish();
            }
        });
        binding.seeOrderTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savaPic();
                LiveDataBus.get().with(LiveBusConfig.backHome, String.class).postValue(LiveBusConfig.backHome);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", myRecycBean);
                startActivity(OrderDetailActivity.class, bundle);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        showCommentFromFeatureDialog("觉得App用起来不错的话给个好评鼓励一下吧!");
//        setGoodCommnet();

        number = (int) SharedPreferencesUtil.getParam(Contans.orderNumber, 0);
        number -= 1;
        SharedPreferencesUtil.setParam(Contans.orderNumber, number);
        if (getIntent() != null) {
            myRecycBean = (MyRecycBean) getIntent().getSerializableExtra("data");
            savaPic();
            LiveDataBus.get().with(LiveBusConfig.backHome, String.class).postValue(LiveBusConfig.backHome);
        }
        ImmersionBar.with(this)
                .reset()
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();

        binding.shareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.picPath.get() != null) {
                    if (viewModel.picPath.get().equals("")) {
                        ToastUtils.show("图片保存中,请稍等");
                    } else {
                        new Share2.Builder(PaySuccessActivity.this)
                                .setContentType(ShareContentType.FILE)
                                .setShareFileUri(FileUtil.getFileUri(PaySuccessActivity.this, ShareContentType.FILE, new File(viewModel.picPath.get())))
                                .build()
                                .shareBySystem();
                    }
//                    AndroidShareUtils.goToMarket();
                } else {
                    ToastUtils.show("图片保存中,请稍等");
                }
            }
        });
    }

    private void savaPic() {
//        viewModel.saveic(PaySuccessActivity.this, myRecycBean.getUrl(), myRecycBean.getName(), true);
    }


}
