package com.xinqidian.adcommon.util;

import java.io.Serializable;

/**
 * Created by lipei on 2020/7/21.
 */

public class PriceModel implements Serializable{


    /**
     * huaweiprice : 1.99
     * xiaomiprice : 1.99
     * oppoprice : 1.99
     * vivoprice : 1.99
     * tencnetprice : 1.99
     * commonnprice : 1.99
     */

    private String huaweiprice;
    private String xiaomiprice;
    private String oppoprice;
    private String vivoprice;
    private String tencnetprice;
    private String commonnprice;
    private String updateprice;

    public String getHuaweiprice() {
        return huaweiprice;
    }

    public void setHuaweiprice(String huaweiprice) {
        this.huaweiprice = huaweiprice;
    }

    public String getXiaomiprice() {
        return xiaomiprice;
    }

    public void setXiaomiprice(String xiaomiprice) {
        this.xiaomiprice = xiaomiprice;
    }

    public String getOppoprice() {
        return oppoprice;
    }

    public void setOppoprice(String oppoprice) {
        this.oppoprice = oppoprice;
    }

    public String getVivoprice() {
        return vivoprice;
    }

    public void setVivoprice(String vivoprice) {
        this.vivoprice = vivoprice;
    }

    public String getTencnetprice() {
        return tencnetprice;
    }

    public void setTencnetprice(String tencnetprice) {
        this.tencnetprice = tencnetprice;
    }

    public String getCommonnprice() {
        return commonnprice;
    }

    public void setCommonnprice(String commonnprice) {
        this.commonnprice = commonnprice;
    }

    public String getUpdateprice() {
        return updateprice;
    }

    public void setUpdateprice(String updateprice) {
        this.updateprice = updateprice;
    }
}
