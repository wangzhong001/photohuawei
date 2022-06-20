package com.xinqidian.adcommon.login;

public class VipPriceBean {

    //{"yue":"9.99","ji":"19.99","nian":"39.99"}
    private String yue;
    private String ji;
    private String nian;

    public VipPriceBean() {
    }

    public VipPriceBean(String yue, String ji, String nian) {
        this.yue = yue;
        this.ji = ji;
        this.nian = nian;
    }

    public String getYue() {
        return yue;
    }

    public void setYue(String yue) {
        this.yue = yue;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getNian() {
        return nian;
    }

    public void setNian(String nian) {
        this.nian = nian;
    }
}
