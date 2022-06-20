package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2019/1/9.
 */

public class LoginRequestBody {

    /**
     * loginName : string
     * password : string
     */

    private String loginName;
    private String password;
    private String googleCode;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoogleCode() {
        return googleCode;
    }

    public void setGoogleCode(String googleCode) {
        this.googleCode = googleCode;
    }
}
