package com.xinqidian.adcommon.ui.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.ui.activity.WebViewActivity;
import com.xinqidian.adcommon.util.SecretDialogUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.view.SecretInterface;


/**
 * Created by lipei on 2020/4/26.
 * 开屏广告
 */

public class SplashActivity extends AppCompatActivity implements SecretInterface {
    private static final String TAG = "SplashActivity";

    private Context context;


    private SecretDialogUtil secretDialogUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        secretDialogUtil = new SecretDialogUtil(this);
        secertData();


    }


    private void toMainActivity() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
            if (
                    checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 1024);
            } else {
                Boolean isNeedToOrtherActivity= (Boolean) SharedPreferencesUtil.getParam(Contants.IS_NEED_TO_ORTHER_ACTIVITY,false);

                if(isNeedToOrtherActivity){
                    //跳转到其他页面
                    try {
                        String targetActivityClassName = getOrtherTargetActivityClassNameFromMetaData();
                        Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
                        Intent intent = new Intent(this, targetActivityClass);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    try {
                        String targetActivityClassName = getTargetActivityClassNameFromMetaData();
                        Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
                        Intent intent = new Intent(this, targetActivityClass);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            Boolean isNeedToOrtherActivity= (Boolean) SharedPreferencesUtil.getParam(Contants.IS_NEED_TO_ORTHER_ACTIVITY,false);

            if(isNeedToOrtherActivity){
                //跳转到其他页面
                try {
                    String targetActivityClassName = getOrtherTargetActivityClassNameFromMetaData();
                    Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
                    Intent intent = new Intent(this, targetActivityClass);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
                try {
                    String targetActivityClassName = getTargetActivityClassNameFromMetaData();
                    Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
                    Intent intent = new Intent(this, targetActivityClass);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }






    }




    private String getTargetActivityClassNameFromMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            String targetActivityClassName = ai.metaData.getString(Contants.TARGET_ACTIVITY);
            if (targetActivityClassName == null || targetActivityClassName.length() == 0) {
                throw new RuntimeException("meta-data named \"" + Contants.TARGET_ACTIVITY + "\" can not be empty!");
            }
            return targetActivityClassName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getOrtherTargetActivityClassNameFromMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            String targetActivityClassName = ai.metaData.getString(Contants.ORTHER_ACTIVITY);
            if (targetActivityClassName == null || targetActivityClassName.length() == 0) {
                throw new RuntimeException("meta-data named \"" + Contants.ORTHER_ACTIVITY + "\" can not be empty!");
            }
            return targetActivityClassName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void canelClick() {
        SharedPreferencesUtil.setParam(Contants.IS_NEED_SERCERT_STRING, false);
        finish();

    }

    @Override
    public void sureClick() {



        SharedPreferencesUtil.setParam(Contants.IS_NEED_SERCERT_STRING, true);
        toMainActivity();


    }

    @Override
    public void xieyiClick() {
        try {

            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", getXieyiClassNameFromMetaData());
            intent.putExtra("title", "用户协议");
            startActivity(intent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void yinsiClick() {
        try {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", getYinsiClassNameFromMetaData());
            intent.putExtra("title", "隐私政策");
            startActivity(intent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private void secertData() {
        if (Contants.IS_NEED_SERCERT) {
            //需要弹出隐私弹框
            boolean isHasShowSercert = (boolean) SharedPreferencesUtil.getParam(Contants.IS_NEED_SERCERT_STRING, false);
            if (isHasShowSercert) {
                toMainActivity();
            } else {
                secretDialogUtil.showPrivacy(this, getString(R.string.app_name));
            }

        } else {
            toMainActivity();

        }
    }





    private String getXieyiClassNameFromMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            String targetActivityClassName = ai.metaData.getString(Contants.XIEYI);
            if (targetActivityClassName == null || targetActivityClassName.length() == 0) {
                throw new RuntimeException("meta-data named \"" + Contants.XIEYI + "\" can not be empty!");
            }
            return targetActivityClassName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getYinsiClassNameFromMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            String targetActivityClassName = ai.metaData.getString(Contants.YINSIZHENGCE);
            if (targetActivityClassName == null || targetActivityClassName.length() == 0) {
                throw new RuntimeException("meta-data named \"" + Contants.YINSIZHENGCE + "\" can not be empty!");
            }
            return targetActivityClassName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            Boolean isNeedToOrtherActivity= (Boolean) SharedPreferencesUtil.getParam(Contants.IS_NEED_TO_ORTHER_ACTIVITY,false);

            if(isNeedToOrtherActivity){
                //跳转到其他页面
                try {
                    String targetActivityClassName = getOrtherTargetActivityClassNameFromMetaData();
                    Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
                    Intent intent = new Intent(this, targetActivityClass);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
                try {
                    String targetActivityClassName = getTargetActivityClassNameFromMetaData();
                    Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
                    Intent intent = new Intent(this, targetActivityClass);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

}
