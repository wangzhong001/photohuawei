package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2020/6/9.
 */

public class AllipayRequestBody2 {

    private int id;
    private String mercdName;
    private String mercdWorth;
    private String mobile;
    private int orderNumber;
    private String remark;
    private String uname;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMercdName() {
        return mercdName;
    }

    public void setMercdName(String mercdName) {
        this.mercdName = mercdName;
    }

    public String getMercdWorth() {
        return mercdWorth;
    }

    public void setMercdWorth(String mercdWorth) {
        this.mercdWorth = mercdWorth;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
