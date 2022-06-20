package com.qihe.zzj.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lipei on 2020/6/30.
 */
@Entity
public class IdPhotoBean  {
    @Id(autoincrement = true)
    private Long _id;

    @NotNull
    private String size;

    @NotNull
    private String url;

    @NotNull
    private String pxSize;
    
    @NotNull
    private String mmSize;

    @NotNull
    private String createTime;

    @NotNull
    private String name;

    @NotNull
    private String money;

    private Long effectiveTime;

    private boolean isPay;


    @Generated(hash = 1297450509)
    public IdPhotoBean(Long _id, @NotNull String size, @NotNull String url,
            @NotNull String pxSize, @NotNull String mmSize,
            @NotNull String createTime, @NotNull String name, @NotNull String money,
            Long effectiveTime, boolean isPay) {
        this._id = _id;
        this.size = size;
        this.url = url;
        this.pxSize = pxSize;
        this.mmSize = mmSize;
        this.createTime = createTime;
        this.name = name;
        this.money = money;
        this.effectiveTime = effectiveTime;
        this.isPay = isPay;
    }

    @Generated(hash = 2002462243)
    public IdPhotoBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPxSize() {
        return this.pxSize;
    }

    public void setPxSize(String pxSize) {
        this.pxSize = pxSize;
    }

    public String getMmSize() {
        return this.mmSize;
    }

    public void setMmSize(String mmSize) {
        this.mmSize = mmSize;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return this.money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public boolean getIsPay() {
        return this.isPay;
    }

    public void setIsPay(boolean isPay) {
        this.isPay = isPay;
    }

    public Long getEffectiveTime() {
        return this.effectiveTime;
    }

    public void setEffectiveTime(Long effectiveTime) {
        this.effectiveTime = effectiveTime;
    }
}
