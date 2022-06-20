package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2020/6/9.
 */

public class UserModel {

    /**
     * msg : 操作成功
     * code : 200
     * data : {"uname":"","iconImg":"","mobile":"15171418960","expireDate":""}
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

    public static class DataBean {
        /**
         * uname :
         * iconImg :
         * mobile : 15171418960
         * expireDate :
         */

        private String uname;
        private String iconImg;
        private String mobile;
        private String expireDate;
        private int userLevel;
        private int idMakeCount;
        private int freeCount;
        private String inviteCode;

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getIconImg() {
            return iconImg;
        }

        public void setIconImg(String iconImg) {
            this.iconImg = iconImg;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }

        public int getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(int userLevel) {
            this.userLevel = userLevel;
        }

        public int getIdMakeCount() {
            return idMakeCount;
        }

        public void setIdMakeCount(int idMakeCount) {
            this.idMakeCount = idMakeCount;
        }

        public int getFreeCount() {
            return freeCount;
        }

        public void setFreeCount(int freeCount) {
            this.freeCount = freeCount;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }
    }
}
