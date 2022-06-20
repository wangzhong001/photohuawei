package com.xinqidian.adcommon.ui.splash;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.xiaomi.ad.common.pojo.AdType;
import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.adview.TwiceFragmentLayout;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.util.SecretDialogUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.StatusBarUtil;
import com.xinqidian.adcommon.view.SecretInterface;

import java.lang.ref.WeakReference;

/**
 * Created by lipei on 2020/4/26.
 * 开屏广告
 */

public class SplashActivity extends AppCompatActivity implements SecretInterface {
    private static final String TAG = "SplashActivity";
    private TwiceFragmentLayout mContainer;
    private IAdWorker mWorker;
    private ImageView splashIv;
    private int defaultImageID = R.drawable.default_splash;
    private Bitmap bitmap;
    private Handler mHandler = new Handler();
    private SecretDialogUtil secretDialogUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        secretDialogUtil = new SecretDialogUtil(this);

        init();
        secertData();


    }


    private static final class UpdateCommissonRunable implements Runnable {

        private WeakReference<SplashActivity> mWeakReference;

        private UpdateCommissonRunable(SplashActivity spotFragment) {
            mWeakReference = new WeakReference<SplashActivity>(spotFragment);

        }

        @Override
        public void run() {
            SplashActivity splashActivity = (SplashActivity) mWeakReference.get();
            splashActivity.initAd();


//            final boolean isSet = (boolean) SharedPreferencesUtil.getParam(Contants.set, false);
//            if (isSet) {
//                splashActivity.toGeatureLoginActivity();
//            } else {
//                splashActivity.toMainActivity();
//            }


        }
    }


    private void init() {
        // 如果api >= 23 需要显式申请权限
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET}, 0);
//            }
//        }
        mContainer = (TwiceFragmentLayout) findViewById(R.id.splash_ad_container);
        mContainer.setComfirmed(!Contants.IS_NEED_COMFIRMED);
        splashIv = findViewById(R.id.splash_iv);
        bitmap = StatusBarUtil.scaleBitmap(StatusBarUtil.readBitMap(this, getDefaultImage()), 0.9F);
        splashIv.setImageBitmap(bitmap);


    }

    private void initAd() {
        if (Contants.IS_SHOW_SPLASH_AD) {

            if(SharedPreferencesUtil.isVip()){
                toMainActivity();

            }else {
                try {
                    mWorker = AdWorkerFactory.getAdWorker(this, mContainer, new MimoAdListener() {
                        @Override
                        public void onAdPresent() {
                            // 开屏广告展示
                            Log.d(TAG, "onAdPresent");
                        }

                        @Override
                        public void onAdClick() {
                            //用户点击了开屏广告
                            toMainActivity();
                            Log.d(TAG, "onAdClick");
                        }

                        @Override
                        public void onAdDismissed() {
                            //这个方法被调用时，表示从开屏广告消失。
                            Log.d(TAG, "onAdDismissed");
                            toMainActivity();

                        }

                        @Override
                        public void onAdFailed(String s) {
                            Log.e(TAG, "ad fail message : " + s);
                            toMainActivity();

                        }

                        @Override
                        public void onAdLoaded(int size) {
                            //do nothing
                        }

                        @Override
                        public void onStimulateSuccess() {
                        }
                    }, AdType.AD_SPLASH);
                    mWorker.loadAndShow(Contants.XIAOMI_SPLASH_ID);

                } catch (Exception e) {
                    e.printStackTrace();


                    toMainActivity();


                }
            }


        } else {


            toMainActivity();

        }


    }


    @Override
    protected void onDestroy() {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        try {
            super.onDestroy();
            if (mWorker != null) {
                if (mWorker.isReady()) {
                    mWorker.recycle();

                }
            }
        } catch (Exception e) {
        }

        mHandler.removeCallbacksAndMessages(null);

    }


    private void toMainActivity() {


        Boolean isNeedToOrtherActivity = (Boolean) SharedPreferencesUtil.getParam(Contants.IS_NEED_TO_ORTHER_ACTIVITY, false);

        if (isNeedToOrtherActivity) {
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
        } else {
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


    private int getDefaultImage() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            int defaultImageID = ai.metaData.getInt(Contants.DEFAUT_IMAGE);
            if (defaultImageID <= 0) {
                throw new RuntimeException("meta-data named \"" + Contants.DEFAUT_IMAGE + "\" must be set!");
            } else {
                this.defaultImageID = defaultImageID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return defaultImageID;
    }


    private void secertData() {
        if (Contants.IS_NEED_SERCERT) {
            //需要弹出隐私弹框
            boolean isHasShowSercert = (boolean) SharedPreferencesUtil.getParam(Contants.IS_NEED_SERCERT_STRING, false);
            if (isHasShowSercert) {
                doMainThing();
            } else {
                secretDialogUtil.showPrivacy(this, getString(R.string.app_name));
            }

        } else {
            doMainThing();

        }
    }

    @Override
    public void canelClick() {
        SharedPreferencesUtil.setParam(Contants.IS_NEED_SERCERT_STRING, false);
        finish();

    }

    @Override
    public void sureClick() {
        SharedPreferencesUtil.setParam(Contants.IS_NEED_SERCERT_STRING, true);
        doMainThing();


    }


    private void doMainThing() {
        mHandler.postDelayed(new UpdateCommissonRunable(this), Contants.SPLASH_TO_MAIN_TIME);

    }

    @Override
    public void xieyiClick() {
        try {
            String targetActivityClassName = getWebViewActivityClassNameFromMetaData();
            Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
            Intent intent = new Intent(this, targetActivityClass);
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
            String targetActivityClassName = getWebViewActivityClassNameFromMetaData();
            Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
            Intent intent = new Intent(this, targetActivityClass);
            intent.putExtra("url", getYinsiClassNameFromMetaData());
            intent.putExtra("title", "隐私政策");
            startActivity(intent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private String getWebViewActivityClassNameFromMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            String targetActivityClassName = ai.metaData.getString(Contants.WEBVIEW_ACTIVITY);
            if (targetActivityClassName == null || targetActivityClassName.length() == 0) {
                throw new RuntimeException("meta-data named \"" + Contants.WEBVIEW_ACTIVITY + "\" can not be empty!");
            }
            return targetActivityClassName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
}
