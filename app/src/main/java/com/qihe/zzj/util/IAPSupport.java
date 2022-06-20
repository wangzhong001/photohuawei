
/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.qihe.zzj.util;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.huawei.hms.iap.entity.PurchaseIntentWithPriceReq;
import com.qihe.zzj.BaseApplication;
import com.qihe.zzj.bean.PayBean;
import com.smkj.zzj.gen.PayBeanHelp;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class IAPSupport {

    private static final String TAG = "HMS_LOG_CipherUtil";

    private static final String SIGN_ALGORITHMS = "SHA256WithRSA";
//    private static final String SIGN_ALGORITHMS = "920D493BB43BBDF24FD558E22AF03A95045FFC2E7DF3F97FBDB80AA2CBA21C56";

    private static final String PUBLIC_KEY =
            "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAsVkk4sWBmDbYAGX7Kqi/1Bu6ZaUCZl7aRCW3rrA4/zcRILgTQn83SWRo6coU7AubcLSoc1fUz3e0JqoavoOT3k2FBhfBkH6i3Se2ZU8suS+CWoc36pQ9sKwZ3QxqT+bUp9Ht+dffAK8E5VboUZuhGmCtmc/aNg7E6vGmptKnNK8aJK8LAQCdUesdCkm7Bod3o1KSXcnwZiSsd/mMJp0XzrUWHNvY3L6S5M9OThl2mqXyv/wXREjFF2gVdO5PwhWHdUbODHKNNZnEiYHIPcnGABwxP7b0snJsOv0vApwlZcL/8lTrr/pkBOIymc/XQlEKmiOhRp9jWp4pzeJ41KoHnoOeTBbuP7qRy35dnQLDpaqqyVlsqaS5USNeMUiJ2swIN1MdeLqml5suth4zPnHyHdorQhOlBN8MD9FgGt+PDV9YnrM/oJP+BowoCBKBr8mwxPkntgOHkXG3ileh9ZB9UHX5d+tSvYd9ilLjzyOzy5ENuWwnFlap+KpSUnArGESLAgMBAAE=";

    public static boolean doCheck(String content, String sign) {
        if (TextUtils.isEmpty(PUBLIC_KEY)) {
            Log.e(TAG, "publicKey is null");
            return false;
        }
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            return signature.verify(Base64.decode(sign, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "doCheck NoSuchAlgorithmException" + e);
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "doCheck InvalidKeySpecException" + e);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "doCheck InvalidKeyException" + e);
        } catch (SignatureException e) {
            Log.e(TAG, "doCheck SignatureException" + e);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "doCheck UnsupportedEncodingException" + e);
        }
        return false;
    }

    //配置商品参数
    public static PurchaseIntentWithPriceReq createGetBuyIntentWithPriceReq(double mercdWorth, int orderNumber, String money,String productName) {
        PurchaseIntentWithPriceReq request = new PurchaseIntentWithPriceReq();
        request.setCurrency("CNY");
        request.setDeveloperPayload("test");
        request.setPriceType(0);// priceType: 0：消耗型商品; 1：非消耗型商品; 2：订阅型商品
        request.setSdkChannel("1");
        request.setProductName(productName);//商品名称
        request.setAmount(money);//商品价格
        String productId = String.valueOf(System.currentTimeMillis());
        request.setProductId(productId);//商品id
//        request.setProductId(String.valueOf(System.currentTimeMillis()));//商品id
        request.setServiceCatalog("X5");
        request.setCountry("CN");
        BaseApplication.mercdName = productName;
        BaseApplication.orderId = productId;
        String timeByDate = getTimeByDate(Calendar.getInstance().getTime());
        BaseApplication.payTime = timeByDate;
        Log.e("hw_login：", "productId...1:" + productId);
        PayBean payBean = new PayBean();
        payBean.setMercdName(productName);
        payBean.setMercdWorth(mercdWorth);
        payBean.setOpenId(BaseApplication.openId);
        payBean.setOrderId(productId);
        payBean.setOrderNumber(orderNumber);
        payBean.setPayTime(timeByDate);
        PayBeanHelp.getNewInstance(BaseApplication.getContext()).insertData(payBean);
        List<PayBean> payBeanList = PayBeanHelp.getNewInstance(BaseApplication.getContext()).queryAllData();
        Log.e("hw_login：", "payBeanList:" + payBeanList.size());
        return request;
    }

    /**
     * {
     *   "mercdName": "string", 商品名称
     *   "mercdWorth": 0,       商品价格
     *   "openId": "string",    openId
     *   "orderId": "string",   productId
     *   "orderNumber": 0,      0表示单次支付，1表示月vip，3表示季vip，12表示年vip
     *   "payTime": "string"    2021-03-16 16:19:50
     * }
     */

    //获取指定当前日期，格式:2021-03-16 16:19:50
    public static String getTimeByDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.format(date);
        }catch (Exception ignored){
            return "";
        }
    }
}
