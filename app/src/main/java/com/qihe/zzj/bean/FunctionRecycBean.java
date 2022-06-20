package com.qihe.zzj.bean;

import java.io.Serializable;

/**
 * 证照
 * Created by cz on 2020/6/18.
 */

public class FunctionRecycBean implements Serializable{

    private String name;
    private String x_px;
    private String y_px;
    private String x_mm;
    private String y_mm;
    private String kb;
    private String specId;
    private int maxSize=100;

    public FunctionRecycBean(){

    }

    public FunctionRecycBean(String name, String x_px, String y_px, String x_mm, String y_mm,String kb) {
        this.name = name;
        this.x_px = x_px;
        this.y_px = y_px;
        this.x_mm = x_mm;
        this.y_mm = y_mm;
        this.kb = kb;
    }

    public FunctionRecycBean(String name, String x_px, String y_px, String x_mm, String y_mm,String kb,String specId) {
        this.name = name;
        this.x_px = x_px;
        this.y_px = y_px;
        this.x_mm = x_mm;
        this.y_mm = y_mm;
        this.kb = kb;
        this.specId=specId;
    }

    public FunctionRecycBean(String name, String x_px, String y_px, String x_mm, String y_mm, String kb, String specId, int maxSize) {
        this.name = name;
        this.x_px = x_px;
        this.y_px = y_px;
        this.x_mm = x_mm;
        this.y_mm = y_mm;
        this.kb = kb;
        this.specId = specId;
        this.maxSize = maxSize;
    }

    public String getX_mm() {
        return x_mm;
    }

    public void setX_mm(String x_mm) {
        this.x_mm = x_mm;
    }

    public String getY_mm() {
        return y_mm;
    }

    public void setY_mm(String y_mm) {
        this.y_mm = y_mm;
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

    public String getKb() {
        return kb;
    }

    public void setKb(String kb) {
        this.kb = kb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
