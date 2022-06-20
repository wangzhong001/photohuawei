package com.xinqidian.adcommon.login;


import java.util.List;

/**
 *
 */
public class RoutePriceBean {

    private String msg;
    private int code;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private int id;
        private String storeName;
        private String sotreAddr;
        private String storeManager;
        private String storeMobile;
        private long createdAt;
        private long updatedAt;
        private String url;
        private int status;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getSotreAddr() {
            return sotreAddr;
        }

        public void setSotreAddr(String sotreAddr) {
            this.sotreAddr = sotreAddr;
        }

        public String getStoreManager() {
            return storeManager;
        }

        public void setStoreManager(String storeManager) {
            this.storeManager = storeManager;
        }

        public String getStoreMobile() {
            return storeMobile;
        }

        public void setStoreMobile(String storeMobile) {
            this.storeMobile = storeMobile;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
