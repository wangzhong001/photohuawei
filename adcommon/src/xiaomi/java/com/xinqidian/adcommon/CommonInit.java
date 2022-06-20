package com.xinqidian.adcommon;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.miui.zeus.mimo.sdk.MimoSdk;
import com.miui.zeus.mimo.sdk.api.IMimoSdkListener;
import com.xinqidian.adcommon.app.Contants;

/**
 * Created by lipei on 2020/4/27.
 */

public class CommonInit {
    private static final String TAG = "XiaomiCommon";

    public static void init(Application application) {
        // 如果担心sdk自升级会影响开发者自身app的稳定性可以关闭，
        // 但是这也意味着您必须得重新发版才能使用最新版本的sdk, 建议开启自升级
        MimoSdk.setEnableUpdate(Contants.AUTO_UPDATE);
//
//        MimoSdk.setDebug(Contants.IS_DEBUG); // 正式上线时候务必关闭debug模式
//        MimoSdk.setStaging(Contants.IS_DEBUG); // 正式上线时候务必关闭stage模式


        // 如需要在本地预置插件,请在assets目录下添加mimo_asset.apk;

        MimoSdk.init(application, Contants.XIAOMI_APPID, Contants.XIAOMI_APPKEY, Contants.XIAOMI_APPTOKEN, new IMimoSdkListener() {
            @Override
            public void onSdkInitSuccess() {
                Log.d(TAG, "初始化成功");

            }

            @Override
            public void onSdkInitFailed() {
                Log.d(TAG, "初始化失败");
            }
        });
    }

}
