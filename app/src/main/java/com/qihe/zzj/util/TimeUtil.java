package com.qihe.zzj.util;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by lipei on 2020/5/22.
 */
public class TimeUtil {

    public static String TIME = "yyyy-MM-dd HH:mm";

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty())
            format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    public static String nullString(String content) {
        if (content == null) {
            return "暂无";
        }
        if (content.isEmpty()) {
            return "暂无";
        }
        return content;
    }

    //获取指定当前日期，格式:2020-05-06
    public static String getTimeByDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        try {
            return simpleDateFormat.format(date);
        } catch (Exception ignored) {
            return "";
        }
    }

}
