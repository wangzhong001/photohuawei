package com.qihe.zzj.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.R;
import com.qihe.zzj.databinding.ActivityLogin2Binding;
import com.qihe.zzj.view.LoadProgressDialog;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;

public class Login2Activity extends BaseActivity<ActivityLogin2Binding, BaseViewModel> {

    private boolean isHidePassword;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login2;
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

        if (StringUtils.isSpace(binding.editPassword.getText().toString()) || StringUtils.isSpace(binding.editPhone.getText().toString())) {
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
                LoadProgressDialog.getInstance().showDialog(Login2Activity.this);
                UserUtil.login(binding.editPhone.getText().toString(), binding.editPassword.getText().toString(),binding.editInvitecode.getText().toString(), new UserUtil.CallBack() {
                    @Override
                    public void onSuccess() {
                        String phone = binding.editPhone.getText().toString();
                        SharedPreferencesUtil.setParam("phone", phone);
                        SharedPreferencesUtil.setParam("login_type", "2");
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
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isLogin", false);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
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
