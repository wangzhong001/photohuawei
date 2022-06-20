package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2019/1/9.
 */

public class PayHuawei {

    /**
     *
     * {
     *   "mercdName": "string", 商品名称
     *   "mercdWorth": 0,       商品价格
     *   "openId": "string",    openId
     *   "orderId": "string",   productId
     *   "orderNumber": 0,      0表示单次支付，1表示月vip，3表示季vip，12表示年vip
     *   "payTime": "string"    2021-03-16 16:19:50
     * }
     */

    private String mercdName;
    private double mercdWorth;
    private String openId;
    private String orderId;
    private int orderNumber;
    private String payTime;

    public String getMercdName() {
        return mercdName;
    }

    public void setMercdName(String mercdName) {
        this.mercdName = mercdName;
    }

    public double getMercdWorth() {
        return mercdWorth;
    }

    public void setMercdWorth(double mercdWorth) {
        this.mercdWorth = mercdWorth;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
}
