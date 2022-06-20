package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2019/1/9.
 */

public class RegistRequestBody {

    /**
     * loginName : string
     * password : string
     */

    private String account;
    private String password;
    private String rePassword;
    private String inviteCode;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
