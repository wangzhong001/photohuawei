package com.xinqidian.adcommon.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.xinqidian.adcommon.BR;
import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.databinding.ActivityNewWebviewBinding;

/**
 * Created by dafei on 2018/7/13.
 */

public class WebViewActivity extends BaseActivity<ActivityNewWebviewBinding,BaseViewModel> {
    private static final String TAG = "WebViewActivity";
    private String url ;
    private String titleText ;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_new_webview;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
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

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        titleText = intent.getStringExtra("title");
        binding.titleTv.setText(titleText);
        if (url!=null&&!url.equals("")){
            loadUrl();
        }
    }

    private void loadUrl() {
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//打开js支持

        /**
         * 打开js接口給H5调用，参数1为本地类名，参数2为别名；h5用window.别名.类名里的方法名才能调用方法里面的内容，例如：window.android.back();
         * */

        binding.webView.addJavascriptInterface(new JsInteration(), "android");
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.loadUrl(url);
        Log.d("webActivity",url);
    }


    /**
     * 自己写一个类，里面是提供给H5访问的方法
     */
    public class JsInteration {

        @JavascriptInterface   // 一定要写，不然H5调不到这个方法
        public String back() {
            return "我是java里的方法返回值";
        }
    }

    //点击按钮，访问H5里带返回值的方法
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onClick(View v) {
        Log.e("TAG", "onClick: ");

        //        mWebView.loadUrl("JavaScript:show()");//直接访问H5里不带返回值的方法，show()为H5里的方法

        //传固定字符串可以直接用单引号括起来
        binding.webView.loadUrl("javascript:alertMessage('哈哈')");//访问H5里带参数的方法，alertMessage(message)为H5里的方法

        //当出入变量名时，需要用转义符隔开
        String content="9880";
        binding.webView.loadUrl("javascript:alertMessage(\""   +content+   "\")"   );

        /**
         * Android调用有返回值js方法，安卓4.4以上才能用这个方法
         * 调用js有参数有返回值的函数时，只有安卓4.4以上才能用webView.evaluateJavascript方法直接拿到返回值；
         * 当版本低于4.4的时候，常用的思路是 java调用js方法，js方法执行完毕，再次调用java代码将值返回。
         */
        binding.webView.evaluateJavascript("sum(1,2)", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.d(TAG, "js返回的结果为=" + value);
                Toast.makeText(WebViewActivity.this,"js返回的结果为=" + value,Toast.LENGTH_LONG).show();
            }
        });

    }




}
