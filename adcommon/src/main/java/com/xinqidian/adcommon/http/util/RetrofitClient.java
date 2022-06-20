package com.xinqidian.adcommon.http.util;

import android.content.Context;
import android.text.TextUtils;


import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.http.cookie.CookieJarImpl;
import com.xinqidian.adcommon.http.cookie.store.PersistentCookieStore;
import com.xinqidian.adcommon.http.interceptor.BaseInterceptor;
import com.xinqidian.adcommon.http.interceptor.CacheInterceptor;

import com.xinqidian.adcommon.http.interceptor.logging.LoggingInterceptor;
import com.xinqidian.adcommon.util.Common;
import com.xinqidian.adcommon.util.KLog;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by goldze on 2017/5/10.
 * RetrofitClient封装单例类, 实现网络请求
 */
public class RetrofitClient {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    //服务端根路径
    public static String baseUrl = AppConfig.BASE_URL;

    private Context mContext = Common.getApplication();

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private Cache cache = null;
    private File httpCacheDirectory;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient() {
        this(baseUrl);
    }

    private RetrofitClient(String url) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "goldze_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            KLog.e("Could not create http cache", e);
        }
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.172.1.154", 1080));


        Map<String, String> map = new HashMap<>();
//        map.put("accept-language", LocalManageUtil.setRuestLanguage(Utils.getContext()));
        map.put("channel","app");
        KLog.e("map--->", map.toString());

//        //log相关
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
////        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
////        loggingInterceptor.setColorLevel(HttpLoggingInterceptor.Level.BODY);                               //log颜色级别，决定了log在控制台显示的颜色


        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
//                .proxy(proxy)
//                .cache(cache)
                .addInterceptor(new BaseInterceptor(map))
                .addInterceptor(new CacheInterceptor(mContext))
//                .addInterceptor(loggingInterceptor)
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//                .addInterceptor(new LoggingInterceptor
//                        .Builder()//构建者模式
//                        .loggable(true) //是否开启日志打印
//                        .setLevel(Level.BODY) //打印的等级
//                        .log(Platform.INFO) // 打印类型
//                        .request("Request") // request的Tag
//                        .response("Response")// Response的Tag
//                        .build()
//                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

//    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
//        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//
//        return null;
//    }
}
