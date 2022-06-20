package com.xinqidian.adcommon;

import android.app.Application;

import com.bytedance.sdk.openadsdk.TTAdSdk;

/**
 * Created by lipei on 2020/5/1.
 */

public class CommonInit {
    public static void init(Application application) {
        TTAdManagerHolder.init(application);

    }
}
