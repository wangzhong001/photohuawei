package com.qihe.zzj.ui.activity;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qihe.zzj.BR;
import com.qihe.zzj.R;
import com.qihe.zzj.bean.IdPhotoBean;
import com.qihe.zzj.bean.MyRecycBean;
import com.qihe.zzj.databinding.ActivityOrderBinding;
import com.qihe.zzj.view.MakePhotoViewModel;
import com.qihe.zzj.view.PicItemViewModel;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.IdPhotoUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.SureDialog;

/**
 * Created by lipei on 2020/7/5.
 */

public class OrderActivity extends BaseActivity<ActivityOrderBinding,MakePhotoViewModel> {

    private SureDialog sureDialog;


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_order;
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
        viewModel.getPic();
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


        binding.goPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PhotoAllActivity.class);
            }
        });

        viewModel.toDetailLiveData.observe(this, new Observer<IdPhotoBean>() {
            @Override
            public void onChanged(@Nullable IdPhotoBean idPhotoBean) {
                Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                MyRecycBean myRecycBean=new MyRecycBean();
                myRecycBean.setName(idPhotoBean.getName());
                myRecycBean.setX_px(idPhotoBean.getPxSize());
                myRecycBean.setMm(idPhotoBean.getMmSize());
                myRecycBean.setPay(idPhotoBean.getIsPay());
                myRecycBean.setMoney(idPhotoBean.getMoney());
                myRecycBean.setUrl(idPhotoBean.getUrl());
                myRecycBean.setTime(idPhotoBean.getCreateTime());
                intent.putExtra("data", myRecycBean);
                startActivity(intent);
            }
        });


        viewModel.payLiveData.observe(this, new Observer<PicItemViewModel>() {
            @Override
            public void onChanged(@Nullable final PicItemViewModel picItemViewModel) {
                if(!SharedPreferencesUtil.isLogin()){
                    startActivity(LoginActivity.class);
                    return;
                }
                if(SharedPreferencesUtil.isVip()){
                    if(SharedPreferencesUtil.getMakeIdPhotoNumber()>0){
                        IdPhotoUtil.SerarchMakeIdPhotoNumber(OrderActivity.this, new IdPhotoUtil.IdPhotoCallBack() {
                            @Override
                            public void success() {
                                viewModel.setPaySuccess();
                            }

                            @Override
                            public void fail() {
                                ToastUtils.show("您的会员此时已使用完毕");
                                UserUtil.alipayOrder(picItemViewModel.idPhotoBean.getName(), picItemViewModel.idPhotoBean.getMoney(), 0, OrderActivity.this, new UserUtil.CallBack() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onFail() {

                                    }

                                    @Override
                                    public void loginFial() {
                                        startActivity(LoginActivity.class);

                                    }
                                });
                            }
                        });
                    }else {
                        ToastUtils.show("您的会员此时已使用完毕");
                        UserUtil.alipayOrder(picItemViewModel.idPhotoBean.getName(), picItemViewModel.idPhotoBean.getMoney(), 0, OrderActivity.this, new UserUtil.CallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFail() {

                            }

                            @Override
                            public void loginFial() {
                                startActivity(LoginActivity.class);

                            }
                        });
                    }


                }else {
                    UserUtil.alipayOrder(picItemViewModel.idPhotoBean.getName(), picItemViewModel.idPhotoBean.getMoney(), 0, OrderActivity.this, new UserUtil.CallBack() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void loginFial() {
                            startActivity(LoginActivity.class);

                        }
                    });
                }

            }
        });

        LiveDataBus.get().with(LiveBusConfig.alipaySuccess,Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(!aBoolean){
                    viewModel.setPaySuccess();

                }else {
                    UserUtil.getUserInfo();
                }
            }
        });


        viewModel.paySuccessLiveData.observe(this, new Observer<MakePhotoViewModel>() {
            @Override
            public void onChanged(@Nullable MakePhotoViewModel makePhotoViewModel) {
                IdPhotoBean idPhotoBean=makePhotoViewModel.picItemViewModelObservableField.get().idPhotoBean;
                LiveDataBus.get().with(LiveBusConfig.updateOrder,Integer.class).postValue(makePhotoViewModel.picItemViewModelObservableField.get().postion);
                MyRecycBean myRecycBean=new MyRecycBean();
                myRecycBean.setName(idPhotoBean.getName());
                myRecycBean.setX_px(idPhotoBean.getPxSize());
                myRecycBean.setMm(idPhotoBean.getMmSize());
                myRecycBean.setMoney(idPhotoBean.getMoney());
                myRecycBean.setUrl(idPhotoBean.getUrl());
                myRecycBean.setTime(idPhotoBean.getCreateTime());
                myRecycBean.setPay(true);
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",myRecycBean);
                startActivity(PaySuccessActivity.class,bundle);
            }
        });

        LiveDataBus.get().with(LiveBusConfig.canelOrder,String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                viewModel.canelOrder(viewModel.picItemViewModelObservableField.get(),true);
            }
        });

        viewModel.deleteLiveData.observe(this, new Observer<PicItemViewModel>() {
            @Override
            public void onChanged(@Nullable final PicItemViewModel picItemViewModel) {
                if(picItemViewModel.isPay.get()){
                    sureDialog=new SureDialog(OrderActivity.this,"","确定删除订单吗")
                            .addItemListener(new SureDialog.ItemListener() {
                                @Override
                                public void onClickSure() {
                                    viewModel.canelOrder(picItemViewModel,true);
                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });

                }else {
                    sureDialog=new SureDialog(OrderActivity.this,"","确定取消订单吗")
                            .addItemListener(new SureDialog.ItemListener() {
                                @Override
                                public void onClickSure() {
                                    viewModel.canelOrder(picItemViewModel,true);
                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });
                }
                sureDialog.show();
            }
        });

        LiveDataBus.get().with(LiveBusConfig.backHome,String.class).observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                finish();
            }
        });


    }
}
