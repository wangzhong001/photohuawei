package com.xinqidian.adcommon.login;

import java.io.Serializable;

/**
 * Created by lipei on 2020/6/29.
 */

public class IdPhotoModel implements Serializable{

    /**
     * msg : 操作成功
     * code : 200
     * data : {"whiteUrl":"https://oapi.aisegment.com/static/photo/p1/20200629/result/71e/71e2aa6d7207499e86745bdc4c7ea318.jpg","redUrl":"https://oapi.aisegment.com/static/photo/p1/20200629/result/307/307ee8a9f6c649148aed77db2bac52ae.jpg","width":295,"blueUrl":"https://oapi.aisegment.com/static/photo/p1/20200629/apiresult/7f1/7f11f7d11d7741f1abe033cf85849129.jpg","status":"0","height":413}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * whiteUrl : https://oapi.aisegment.com/static/photo/p1/20200629/result/71e/71e2aa6d7207499e86745bdc4c7ea318.jpg
         * redUrl : https://oapi.aisegment.com/static/photo/p1/20200629/result/307/307ee8a9f6c649148aed77db2bac52ae.jpg
         * width : 295
         * blueUrl : https://oapi.aisegment.com/static/photo/p1/20200629/apiresult/7f1/7f11f7d11d7741f1abe033cf85849129.jpg
         * status : 0
         * height : 413
         */

        private String whiteUrl;
        private String redUrl;
        private int width;
        private String blueUrl;
        private String status;
        private int height;

        public String getWhiteUrl() {
            return whiteUrl;
        }

        public void setWhiteUrl(String whiteUrl) {
            this.whiteUrl = whiteUrl;
        }

        public String getRedUrl() {
            return redUrl;
        }

        public void setRedUrl(String redUrl) {
            this.redUrl = redUrl;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getBlueUrl() {
            return blueUrl;
        }

        public void setBlueUrl(String blueUrl) {
            this.blueUrl = blueUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
