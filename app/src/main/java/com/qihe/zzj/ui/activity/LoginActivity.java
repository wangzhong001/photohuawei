package com.qihe.zzj.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.qihe.zzj.BaseApplication;
import com.qihe.zzj.R;
import com.qihe.zzj.databinding.ActivityLoginBinding;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import io.reactivex.annotations.Nullable;


public class LoginActivity extends BaseActivity<ActivityLoginBinding, BaseViewModel> {

    private boolean isHidePassword;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public void initParam() {
        super.initParam();
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

        /*if (StringUtils.isSpace(binding.editPassword.getText().toString()) || StringUtils.isSpace(binding.editPhone.getText().toString())) {
            binding.tvLogin.setEnabled(false);
            binding.tvLogin.setBackground(getResources().getDrawable(R.drawable.shape_login_tv_bg_false));
        }

        final Drawable[] drawables = binding.editPassword.getCompoundDrawables();
        final int eyeWidth = drawables[2].getBounds().width();// 眼睛图标的宽度
        final Drawable drawableEyeOpen = getResources().getDrawable(R.drawable.xianshi_icon);
        drawableEyeOpen.setBounds(drawables[2].getBounds());//这一步不能省略

        //设置右边图标点击事件
        binding.editPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = binding.editPassword.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > binding.editPassword.getWidth()
                        - binding.editPassword.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    if (isHidePassword) {
                        isHidePassword = false;
                        binding.editPassword.setCompoundDrawables(null,
                                null,
                                drawables[2], null);
                        //隐藏密码
                        binding.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else {
                        isHidePassword = true;
                        binding.editPassword.setCompoundDrawables(null, null,
                                drawableEyeOpen,
                                null);
                        //显示密码
                        binding.editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }

                }
                return false;
            }
        });


        binding.editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isSpace(binding.editPassword.getText().toString()) || StringUtils.isSpace(s.toString())) {
                    binding.tvLogin.setEnabled(false);
                    binding.tvLogin.setBackground(getResources().getDrawable(R.drawable.shape_login_tv_bg_false));
                } else {
                    binding.tvLogin.setEnabled(true);
                    binding.tvLogin.setBackground(getResources().getDrawable(R.drawable.selector_vip_tv_pay_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isSpace(binding.editPhone.getText().toString()) || StringUtils.isSpace(s.toString())
                        || binding.editPassword.getText().toString().length() < 6) {
                    binding.tvLogin.setEnabled(false);
                    binding.tvLogin.setBackground(getResources().getDrawable(R.drawable.shape_login_tv_bg_false));
                } else {
                    binding.tvLogin.setEnabled(true);
                    binding.tvLogin.setBackground(getResources().getDrawable(R.drawable.selector_login_tv_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadProgressDialog.getInstance().showDialog(LoginActivity.this);
                UserUtil.login(binding.editPhone.getText().toString(), binding.editPassword.getText().toString(),binding.editInvitecode.getText().toString(), new UserUtil.CallBack() {
                    @Override
                    public void onSuccess() {
                        LoadProgressDialog.getInstance().hideDialog();
                        ToastUtils.showShort(getResources().getString(R.string.log_success));

                        //隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        }

                        Intent intent = new Intent();
                        intent.putExtra("isLogin", true);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                    @Override
                    public void onFail() {
                        LoadProgressDialog.getInstance().hideDialog();
                    }

                    @Override
                    public void loginFial() {
                        LoadProgressDialog.getInstance().hideDialog();
                    }
                }, null);
            }
        });*/

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.rlHwLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HuaweiIdAuthParams params = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
//                        .setIdToken().setAccessToken()
//                        .createParams();
//                Intent intent = HuaweiIdAuthManager.getService(LoginActivity.this, params).getSignInIntent();
//                startActivityForResult(intent, SIGN_IN_INTENT);

                // Authorization Code登录登录code有值，token值为null
                AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                        .setProfile()
                        .setAuthorizationCode()
                        .createParams();
                // ID Token登录登录token有值，code值为null
//                AccountAuthParams mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
//                        .setIdToken()
//                        .setAccessToken()
//                        .createParams();

                AccountAuthService service = AccountAuthManager.getService(LoginActivity.this, authParams,0);
                startActivityForResult(service.getSignInIntent(), SIGN_IN_INTENT);
            }
        });

        binding.rlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Login2Activity.class);
                startActivityForResult(intent,100);
            }
        });
    }

    private static final int SIGN_IN_INTENT = 3000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SIGN_IN_INTENT == requestCode) {
//            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
//            if (authHuaweiIdTask.isSuccessful()) {
//                //登录成功，获取用户的华为帐号信息和ID Token
//                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
//                String accessToken = huaweiAccount.accessToken;
//                String authorizationCode = huaweiAccount.getAuthorizationCode();
//                Log.e("huawei：", "idToken:" + huaweiAccount.getIdToken());
//                Log.e("huawei：", "authorizationCode:" + authorizationCode);
//                Log.e("huawei：", "accessToken:" + accessToken);
//
//                SharedPreferencesUtil.toLogin();
//                BaseApplication.sName = huaweiAccount.displayName;
//                SharedPreferencesUtil.setParam("Huawei_name",BaseApplication.sName);
//                ToastUtils.show("登录成功");
//                finish();
//            } else {
//                //登录失败，不需要做处理，打点日志方便定位
//                Log.e("huawei：", "sign in failed : " +((ApiException) authHuaweiIdTask.getException()).getStatusCode());
//            }

            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                //登录成功，获取用户的帐号信息和ID Token
                AuthAccount authAccount = authAccountTask.getResult();
                //获取帐号类型，0表示华为帐号、1表示AppTouch帐号
                Log.e("hw_login：", "accountFlag:" + authAccount.getAccountFlag());
                Log.e("hw_login：", "idToken:" + authAccount.getIdToken());
                String authorizationCode = authAccount.getAuthorizationCode();
                Log.e("hw_login：", "authorizationCode:" + authorizationCode);

                BaseApplication.sName = authAccount.displayName;
                BaseApplication.openId = authAccount.openId;
                Log.e("hw_login：", "openId:" + BaseApplication.openId );
                SharedPreferencesUtil.setParam("Huawei_name",BaseApplication.sName);
//                SharedPreferencesUtil.toLogin();
//                ToastUtils.show("登录成功");
//                finish();
                againLogin(authorizationCode);
            } else {
                //登录失败，不需要做处理，打点日志方便定位
                Log.e("hw_login：", "sign in failed : " +((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
        if (requestCode == 100){
            if (data.getBooleanExtra("isLogin", false)) {
                finish();
            }
        }
    }

    private void againLogin(String authorizationCode){
        UserUtil.huaweiLogin(authorizationCode, new UserUtil.CallBack() {
            @Override
            public void onSuccess() {
                SharedPreferencesUtil.setParam("login_type", "1");
                Log.e("hw_login：", "...1");
                SharedPreferencesUtil.toLogin();
                ToastUtils.show("登录成功");
                finish();
            }

            @Override
            public void onFail() {
                Log.e("hw_login：", "...2");
            }

            @Override
            public void loginFial() {
                Log.e("hw_login：", "...3");
            }
        }, null);

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
