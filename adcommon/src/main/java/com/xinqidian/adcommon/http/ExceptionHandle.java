package com.xinqidian.adcommon.http;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.util.Common;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;


/**
 * @author lipei
 */
public class ExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int LOFIN_FAIL = 10000;


    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.message = Common.getApplication().getString(R.string.no_shouquan);
                    break;
                case FORBIDDEN:
                    ex.message = Common.getApplication().getString(R.string.request_not);
                    break;
                case NOT_FOUND:
                    ex.message = Common.getApplication().getString(R.string.raw_not);
                    break;
                case REQUEST_TIMEOUT:
                    ex.message = Common.getApplication().getString(R.string.ser_out_time);
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.message = Common.getApplication().getString(R.string.ser_error);
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.message = Common.getApplication().getString(R.string.ser_not_can_user);
                    break;
                case LOFIN_FAIL:
                    ex.message = Common.getApplication().getString(R.string.login_error);
                    SharedPreferencesUtil.exitLogin();
                    break;
                default:
                    ex.message = Common.getApplication().getString(R.string.net_error);
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message =Common.getApplication().getString(R.string.json_pase);
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = Common.getApplication().getString(R.string.connet_error);
            return ex;
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = Common.getApplication().getString(R.string.boor_error);
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = Common.getApplication().getString(R.string.connet_out_time);
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = Common.getApplication().getString(R.string.connet_out_time);
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = Common.getApplication().getString(R.string.zhuji_error);
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            KLog.e("error--->",e.toString()+"--->"+e.getMessage());
            ex.message = Common.getApplication().getString(R.string.weizhi_error);
            return ex;
        }
    }


    /**
     * 约定异常 这个具体规则需要与服务端或者领导商讨定义
     */
    class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
    }

}

