package com.qihe.zzj.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PayBean {

    @Id(autoincrement = true)
    private Long dbid;
    private String mercdName;
    private double mercdWorth;
    private String openId;
    private String orderId;
    private int orderNumber;
    private String payTime;
    @Generated(hash = 172523031)
    public PayBean(Long dbid, String mercdName, double mercdWorth, String openId,
            String orderId, int orderNumber, String payTime) {
        this.dbid = dbid;
        this.mercdName = mercdName;
        this.mercdWorth = mercdWorth;
        this.openId = openId;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.payTime = payTime;
    }
    @Generated(hash = 1567866339)
    public PayBean() {
    }
    public Long getDbid() {
        return this.dbid;
    }
    public void setDbid(Long dbid) {
        this.dbid = dbid;
    }
    public String getMercdName() {
        return this.mercdName;
    }
    public void setMercdName(String mercdName) {
        this.mercdName = mercdName;
    }
    public double getMercdWorth() {
        return this.mercdWorth;
    }
    public void setMercdWorth(double mercdWorth) {
        this.mercdWorth = mercdWorth;
    }
    public String getOpenId() {
        return this.openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getOrderId() {
        return this.orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public int getOrderNumber() {
        return this.orderNumber;
    }
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
    public String getPayTime() {
        return this.payTime;
    }
    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    

    

}
