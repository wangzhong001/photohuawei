package com.qihe.zzj.ui.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.qihe.zzj.BaseApplication;
import com.qihe.zzj.R;
import com.qihe.zzj.databinding.FragmentMy2Binding;
import com.qihe.zzj.ui.MainActivity;
import com.qihe.zzj.ui.activity.KefuCenterActivity;
import com.qihe.zzj.ui.activity.LoginActivity;
import com.qihe.zzj.ui.activity.MineWorksActivity;
import com.qihe.zzj.util.TimeUtil;
import com.qihe.zzj.view.MainViewModel;
import com.xinqidian.adcommon.base.BaseFragment;
import com.xinqidian.adcommon.login.UserModel;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.ui.activity.FeedBackActivity;
import com.xinqidian.adcommon.ui.activity.WebViewActivity;
import com.xinqidian.adcommon.util.AppUpdateUtils;
import com.xinqidian.adcommon.util.KeyInformationData;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;


/**
 * 我的界面
 */
public class MyFragment extends BaseFragment<FragmentMy2Binding, MainViewModel> {

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_my2;
    }

    @Override
    public int initVariableId() {
        return com.qihe.zzj.BR.baseViewModel;
    }

    @Override
    public void initData() {
        super.initData();
        if (SharedPreferencesUtil.isLogin()) {
            binding.ivTouxiang.setImageDrawable(getResources().getDrawable(R.drawable.user_icon_cov));
            Object login_type = SharedPreferencesUtil.getParam("login_type","");
            String type = (String) login_type;
            if ("1".equals(type)){//华为登录
                binding.tvName.setText(BaseApplication.sName);
            }else if ("2".equals(type)){//账号密码登录
                Object phone = SharedPreferencesUtil.getParam("phone", "");
                String name = (String) phone;
                binding.tvName.setText(name);
            }
            UserUtil.getUserInfoCallBack(new UserUtil.UserinfoCallBack() {
                @Override
                public void getUserInfo(UserModel.DataBean dataBean) {
                    if (SharedPreferencesUtil.isVip()) {
                        binding.tvVipTime.setText("到期时间:" + TimeUtil.timeStamp2Date(dataBean.getExpireDate(), TimeUtil.TIME));
                    } else {
                        binding.tvVipTime.setText("点击会员标志，享更多特权");
                    }
                }
            });
            binding.tvExitLogin.setVisibility(View.VISIBLE);
        } else {
            binding.tvExitLogin.setVisibility(View.GONE);
            binding.ivTouxiang.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
            binding.tvName.setText(getResources().getString(R.string.click_register_to_log_in));
        }
        binding.tvVersion.setText("v" + KeyInformationData.getInstance(getActivity()).getAppVersionName());
    }

    public void upData(){
        if (SharedPreferencesUtil.isLogin()) {
            binding.ivTouxiang.setImageDrawable(getResources().getDrawable(R.drawable.user_icon_cov));
            Object login_type = SharedPreferencesUtil.getParam("login_type","");
            String type = (String) login_type;
            if ("1".equals(type)){//华为登录
                binding.tvName.setText(BaseApplication.sName);
            }else if ("2".equals(type)){//账号密码登录
                Object phone = SharedPreferencesUtil.getParam("phone", "");
                String name = (String) phone;
                binding.tvName.setText(name);
            }
            UserUtil.getUserInfoCallBack(new UserUtil.UserinfoCallBack() {
                @Override
                public void getUserInfo(UserModel.DataBean dataBean) {
                    if (SharedPreferencesUtil.isVip()) {
                        binding.tvVipTime.setText("到期时间:" + TimeUtil.timeStamp2Date(dataBean.getExpireDate(), TimeUtil.TIME));
                    } else {
                        binding.tvVipTime.setText("点击会员标志，享更多特权");
                    }
                }
            });
            binding.tvExitLogin.setVisibility(View.VISIBLE);
        } else {
            binding.tvExitLogin.setVisibility(View.GONE);
            binding.ivTouxiang.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
            binding.tvName.setText(getResources().getString(R.string.click_register_to_log_in));
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtil.isLogin()){

                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        binding.rlVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.toVip();
            }
        });
        binding.rlMoreApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MineWorksActivity.class);
                startActivity(intent);
            }
        });
        binding.rlFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.rlRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.rlYijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
            }
        });
        binding.ivService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(KefuCenterActivity.class);
            }
        });
        binding.rlYh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", getResources().getString(R.string.user_agreement));
                intent.putExtra("url", "https://www.qihe.website/yonghu_xieyi_qihe.html");
                startActivity(intent);
            }
        });
        binding.rlYs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", getResources().getString(R.string.privacy_policy));
                intent.putExtra("url", "https://www.qihe.website/yinsimoban_zzj_huawei.html");
                startActivity(intent);
            }
        });
        binding.rlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), AboutActivity.class);
//                startActivity(intent);
                AppUpdateUtils.update(getActivity(),true);
            }
        });
//        binding.rlLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //已经登陆，点击则退出登录
//                new AlertDialog.Builder(getActivity())
//                        .setTitle("提示")
//                        .setMessage("注销用户后,当前用户数据全部删除，且不可找回和恢复,确定注销吗?")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                UserUtil.UserClean(new UserUtil.CallBack() {
//                                    @Override
//                                    public void onSuccess() {
//                                        Object mobile = SharedPreferencesUtil.getParam("Phone", "");
//                                        String name = (String) mobile;
////                                        FreeNumberBean freeNumberBean = FreeNumberBeanHelp.getNewInstance(getActivity()).queryDataByName(name);
////                                        FreeNumberBeanHelp.getNewInstance(getActivity()).deleteData(freeNumberBean);
//                                        ToastUtils.show("注销成功");
//                                        SharedPreferencesUtil.setParam("Phone","");
//                                    }
//
//                                    @Override
//                                    public void onSuccess2(String msg) {
//
//                                    }
//
//                                    @Override
//                                    public void onFail() {
//
//                                    }
//
//                                    @Override
//                                    public void loginFial() {
//
//                                    }
//                                });
//                            }
//                        })
//                        .show();
//            }
//        });
        binding.tvExitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                        .setProfile()
                        .setAuthorizationCode()
                        .createParams();
                AccountAuthService service = AccountAuthManager.getService(getActivity(), authParams,0);
                Task<Void> signOutTask = service.signOut();
                signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("login", "signOut Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("login", "signOut fail");
                    }
                });
                SharedPreferencesUtil.setParam("Huawei_name", "");
                SharedPreferencesUtil.setParam("login_type", "");
                SharedPreferencesUtil.setParam("phone", "");
                UserUtil.exitLogin();
            }
        });
    }

    @Override
    public void setLoginState(boolean loginState) {
        super.setLoginState(loginState);
        if (loginState) {
            binding.ivTouxiang.setImageDrawable(getResources().getDrawable(R.drawable.user_icon_cov));
            Object login_type = SharedPreferencesUtil.getParam("login_type","");
            String type = (String) login_type;
            if ("1".equals(type)){//华为登录
                binding.tvName.setText(BaseApplication.sName);
            }else if ("2".equals(type)){//账号密码登录
                Object phone = SharedPreferencesUtil.getParam("phone", "");
                String name = (String) phone;
                binding.tvName.setText(name);
            }

            binding.tvExitLogin.setVisibility(View.VISIBLE);
        } else {
            binding.tvExitLogin.setVisibility(View.GONE);
            binding.ivTouxiang.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
            binding.tvName.setText(getResources().getString(R.string.click_register_to_log_in));
        }
    }

    @Override
    public void setUserData(UserModel.DataBean dataBean) {
        super.setUserData(dataBean);
        if (SharedPreferencesUtil.isVip()) {
            binding.tvVipTime.setText("到期时间:" + TimeUtil.timeStamp2Date(dataBean.getExpireDate(), TimeUtil.TIME));
        } else {
            binding.tvVipTime.setText("点击会员标志，享更多特权");
        }
    }

    @Override
    public void onStimulateSuccessCall() {

    }

    @Override
    public void onStimulateSuccessDissmissCall() {

    }

    @Override
    public void initParam() {
        super.initParam();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
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
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
