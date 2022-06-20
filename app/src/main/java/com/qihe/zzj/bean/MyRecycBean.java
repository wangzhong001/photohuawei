package com.qihe.zzj.bean;

import java.io.Serializable;

/**
 * 订单
 * Created by cz on 2020/6/19.
 */

public class MyRecycBean implements Serializable{
    
    private String name;//名字
    private String x_px;//宽 尺寸
    private String y_px;//高 尺寸
    private String mm;
    private String kb;//图片大小 kb
    private String money;//金额
    private String order_num;//订单编号
    private String dpi;
    private boolean isPay;
    private String url;
    private String time;

    public MyRecycBean(){

    }


    public MyRecycBean(String name, String x_px, String y_px, String mm, String kb, String money, boolean isPay) {
        this.name = name;
        this.x_px = x_px;
        this.y_px = y_px;
        this.mm = mm;
        this.kb = kb;
        this.money = money;
        this.isPay = isPay;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getX_px() {
        return x_px;
    }

    public void setX_px(String x_px) {
        this.x_px = x_px;
    }

    public String getY_px() {
        return y_px;
    }

    public void setY_px(String y_px) {
        this.y_px = y_px;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getKb() {
        return kb;
    }

    public void setKb(String kb) {
        this.kb = kb;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
