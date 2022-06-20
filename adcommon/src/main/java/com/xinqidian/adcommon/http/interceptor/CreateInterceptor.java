package com.xinqidian.adcommon.http.interceptor;


import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.util.Common;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.ToastUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by lipei on 2019/6/24.
 */

public class CreateInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());  //如果401了，会先执行TokenAuthenticator
        ToastUtils.show("登录失败");

        if (response.code() == AppConfig.login_fail) {
            KLog.e("login--->","fail");

            CreateInterceptorExceptioin interceptorExceptioin = new CreateInterceptorExceptioin();

            interceptorExceptioin.setErrorCode(AppConfig.login_fail);
            interceptorExceptioin.setRetry_after(Common.getApplication().getString(R.string.login_error));
            throw  interceptorExceptioin;
        }
        return response;
    }



    public class CreateInterceptorExceptioin extends Error{
        private int errorCode;
        private String retry_after;


        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getRetry_after() {
            return retry_after;
        }

        public void setRetry_after(String retry_after) {
            this.retry_after = retry_after;
        }
    }
}
