package com.xinqidian.adcommon.http.cookie;



import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by lipei on 2018/4/2.
 */

public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

//        final Request.Builder builder = chain.request().newBuilder();
//        Observable.just((String) SharedPreferencesUtil.getParam(Contants.cookies, ""))
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String cookie) {
//                        //添加cookie
//                        LogUtils.e("Cookie--->", cookie + "");
//                        builder.addHeader("Cookie", (String) SharedPreferencesUtil.getParam(Contants.cookies, ""));
////                        builder.addHeader("Cookie","hwz_PRINCIPAL=32739382d7a54567b7682d1f5af2c6f9;hwz_LASTLOGIN=1522598400000;hwz_VISITOR=13545280346;");
//                    }
//                });

        // 设置 Cookie
        String cookieStr = (String) SharedPreferencesUtil.getParam(AppConfig.cooike, "");
//        if (!(cookieStr.isEmpty())) {
            return chain.proceed(chain.request().newBuilder().header("Cookie", cookieStr).build());
//        }
//        return chain.proceed(chain.request());

//        return chain.proceed(builder.build());
    }
}
