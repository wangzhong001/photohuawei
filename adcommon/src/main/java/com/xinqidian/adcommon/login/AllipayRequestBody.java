package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2020/6/9.
 */

public class AllipayRequestBody {
    private String mercdName;
    private String mercdWorth;
    private int orderNumber;
    private String remark;

    public String getMercdName() {
        return mercdName;
    }

    public void setMercdName(String mercdName) {
        this.mercdName = mercdName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getMercdWorth() {
        return mercdWorth;
    }

    public void setMercdWorth(String mercdWorth) {
        this.mercdWorth = mercdWorth;
    }
}
