package com.qihe.zzj.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/*
 * Copyright (C) 2019
 * 版权所有
 *
 * 功能描述：转码工具
 * 作者：
 * 创建时间：2019/7/12
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class CodeUtil {

    /**
     * 跳转到QQ
     */
    public static void jumpToQQ(Context mContext) {
        // 跳转之前，可以先判断手机是否安装QQ
        if (isQQClientAvailable(mContext)) {
            // 跳转到客服的QQ
            String mQQNumber = "1156271983";
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + mQQNumber;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // 跳转前先判断Uri是否存在，如果打开一个不存在的Uri，App可能会崩溃
            if (isValidIntent(mContext, intent)) {
                mContext.startActivity(intent);
            } else {
//                ToastUtils.showToast(mContext, R.string.not_install_qq);
            }
        } else {
//            ToastUtils.showToast(mContext, R.string.not_install_qq);
        }
    }


    /**
     * 判断 用户是否安装QQ客户端
     */
    private static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            String pn = pinfo.get(i).packageName;
            if (pn.equalsIgnoreCase("com.tencent.qqlite")
                    || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断 Uri是否有效
     */
    private static boolean isValidIntent(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return !activities.isEmpty();
    }

    public static void jumpToReview(Context context) {
        try {
            String mAddress = "market://details?id=" + context.getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
        } catch (Exception e) {
//            ToastUtils.showToast(context, context.getString(R.string.no_market));
        }

    }

    public static void jumpToReview(Context context, String packageString) {
        try {
            String mAddress = "market://details?id=" + packageString;
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
        } catch (Exception e) {
//            ToastUtils.showToast(context, context.getString(R.string.no_market));
        }

    }


}
