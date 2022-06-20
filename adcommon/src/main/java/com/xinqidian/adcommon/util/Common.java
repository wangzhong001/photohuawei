package com.xinqidian.adcommon.util;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.xinqidian.adcommon.CommonInit;
import com.xinqidian.adcommon.app.Contants;



/**
 * Created by lipei on 2020/4/27.
 * 广告初始化,需在宿主的application中初始化
 */

public class Common {
    private static Application application;

    public static void init(Application application1){
        application=application1;
        CommonInit.init(application1);
        AVOSCloud.initialize(application1,Contants.LEANCLOUND_APPID,Contants.LEANCLOUND_APPKEY);
        JumpUtils.SetQQ(Contants.QQ);
        JumpUtils.SetEmail(Contants.EMIAL);
    }


    public static Application getApplication() {
        return application;
    }
}
