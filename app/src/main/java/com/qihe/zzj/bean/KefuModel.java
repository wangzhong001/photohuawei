package com.qihe.zzj.bean;

import java.io.Serializable;

/**
 * Created by lipei on 2020/7/30.
 */

public class KefuModel implements Serializable{
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
