package com.xinqidian.adcommon.http;

/**
 * Created by lipei on 2018/11/28.
 */

public class BaseResponse {
    private int code;

    private String msg;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
