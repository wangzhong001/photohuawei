package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2020/6/9.
 */

public class AlipayResultModel {
    private int resultStatus;
    private String memo;

    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
