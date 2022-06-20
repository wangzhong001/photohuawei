package com.xinqidian.adcommon.util;

import java.io.Serializable;

/**
 * Created by lipei on 2020/7/20.
 */

public class UpdateBean implements Serializable{


    /**
     * Code : 0
     * Msg :
     * UpdateStatus : 1
     * VersionCode : 17
     * VersionName : 1.0.7
     * UploadTime : 2018-07-10 17:28:41
     * ModifyContent :
     1、新注册用户免费获取制作机会。
     2、优化订单无法删除的bug。
     3、会员制度改成无限次制作证件照
     * DownloadUrl : http://lc-rRwqwXD7.cn-n1.lcfile.com/487a84166f3ca925419c/app-tencent-release.apk
     * ApkSize : 31260
     * ApkMd5 :
     * updatePic : http://lc-xlpQHV45.cn-n1.lcfile.com/475baedaf7758c9481f7/update_pic.png
     */

    private boolean hasUpdate;
    private boolean isForce;
    private int Code;
    private String Msg;
    private int UpdateStatus;
    private int VersionCode;
    private String VersionName;
    private String UploadTime;
    private String ModifyContent;
    private String DownloadUrl;
    private int ApkSize;
    private String ApkMd5;
    private String updatePic;
    private String phoneNmber;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getUpdateStatus() {
        return UpdateStatus;
    }

    public void setUpdateStatus(int UpdateStatus) {
        this.UpdateStatus = UpdateStatus;
    }

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int VersionCode) {
        this.VersionCode = VersionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String VersionName) {
        this.VersionName = VersionName;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String UploadTime) {
        this.UploadTime = UploadTime;
    }

    public String getModifyContent() {
        return ModifyContent;
    }

    public void setModifyContent(String ModifyContent) {
        this.ModifyContent = ModifyContent;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String DownloadUrl) {
        this.DownloadUrl = DownloadUrl;
    }

    public int getApkSize() {
        return ApkSize;
    }

    public void setApkSize(int ApkSize) {
        this.ApkSize = ApkSize;
    }

    public String getApkMd5() {
        return ApkMd5;
    }

    public void setApkMd5(String ApkMd5) {
        this.ApkMd5 = ApkMd5;
    }

    public String getUpdatePic() {
        return updatePic;
    }

    public void setUpdatePic(String updatePic) {
        this.updatePic = updatePic;
    }

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean force) {
        isForce = force;
    }

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public String getPhoneNmber() {
        return phoneNmber;
    }

    public void setPhoneNmber(String phoneNmber) {
        this.phoneNmber = phoneNmber;
    }
}
