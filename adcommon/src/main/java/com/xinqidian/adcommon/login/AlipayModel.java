package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2020/6/9.
 */

public class AlipayModel {

    /**
     * msg : 操作成功
     * code : 200
     * data : {"msg":"订单生成成功","orderStr":"alipay_sdk=alipay-sdk-java-3.4.49.ALL&app_id=2021001167603095&biz_content=%7B%22extend_params%22%3A%7B%22sys_service_provider_id%22%3A%222088831679753551%22%7D%2C%22out_trade_no%22%3A%22159840252%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E4%BC%9A%E5%91%98%E8%AE%A2%E9%98%85%22%2C%22timeout_express%22%3A%2210m%22%2C%22total_amount%22%3A%221%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpansy.wicp.io%2Fapi%2Falipay%2FpayMessCallBack.json&sign=MCVY85NBay3BtdG42WxEwb%2BNbxigXBu0%2FOcW8xUdbvVCcD6fVycC1%2F%2B%2BtdzwnZYo24JFxJHxlG16xpWiMPqUjQCmueN%2FIf9Zo2QCA7iopwP2mTjLBqNNnXX2gUmKpRwULG%2B6hNE0NR64F51ymQMlTu2If8Bo%2Bs6GeHVBgCctdEg5Z8D6KOL0MRvyWDplyKkPB41ADeRRmqbwIhFhSjZtgGrEgx%2B91y9OaZXaRIifxkg6m7pPMdK8yd9C8X2lfLiQduftL2m7DhItTznnH87XPZLNLZqu7AOMLi%2FFCWT9NRT9aOwf4zeM1l8ak32Ni2fxuSrD56bn0U5l0kDeFP2G%2FA%3D%3D&sign_type=RSA2&timestamp=2020-06-09+21%3A43%3A36&version=1.0"}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * msg : 订单生成成功
         * orderStr : alipay_sdk=alipay-sdk-java-3.4.49.ALL&app_id=2021001167603095&biz_content=%7B%22extend_params%22%3A%7B%22sys_service_provider_id%22%3A%222088831679753551%22%7D%2C%22out_trade_no%22%3A%22159840252%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E4%BC%9A%E5%91%98%E8%AE%A2%E9%98%85%22%2C%22timeout_express%22%3A%2210m%22%2C%22total_amount%22%3A%221%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpansy.wicp.io%2Fapi%2Falipay%2FpayMessCallBack.json&sign=MCVY85NBay3BtdG42WxEwb%2BNbxigXBu0%2FOcW8xUdbvVCcD6fVycC1%2F%2B%2BtdzwnZYo24JFxJHxlG16xpWiMPqUjQCmueN%2FIf9Zo2QCA7iopwP2mTjLBqNNnXX2gUmKpRwULG%2B6hNE0NR64F51ymQMlTu2If8Bo%2Bs6GeHVBgCctdEg5Z8D6KOL0MRvyWDplyKkPB41ADeRRmqbwIhFhSjZtgGrEgx%2B91y9OaZXaRIifxkg6m7pPMdK8yd9C8X2lfLiQduftL2m7DhItTznnH87XPZLNLZqu7AOMLi%2FFCWT9NRT9aOwf4zeM1l8ak32Ni2fxuSrD56bn0U5l0kDeFP2G%2FA%3D%3D&sign_type=RSA2&timestamp=2020-06-09+21%3A43%3A36&version=1.0
         */

        private String msg;
        private String orderStr;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getOrderStr() {
            return orderStr;
        }

        public void setOrderStr(String orderStr) {
            this.orderStr = orderStr;
        }
    }
}
