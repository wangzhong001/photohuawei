package com.xinqidian.adcommon.app;

/**
 * Created by lipei on 2018/11/23.
 */

public interface AppConfig {

    String BASE_URL = Contants.BASE_URL;//线上


    int success_code = 200;

    String cooike = "cooike";

    String alipay = "alipay";

    String vip = "vip";

    String freeNumber = "freeNumber";


    String idPhotoNumber="idPhotoNumber";

    String titleType="titleType";

    String isHasFree="isHasFree";

    String freeUseNumber="freeUseNumber";

    String updatePic="updatePic";

    String downUrl="downUrl";

    String phoneNumber="phoneNumber";


    String isThisPhoneHasUserInviteCode="isThisPhoneHasUserInviteCode";


    /**
     * 防止刷免费次数
     */
    String brush="00000000";




    int login_fail=10000;//登录失败

    int user_not_regist=10006;//未注册












}
