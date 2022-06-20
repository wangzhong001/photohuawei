package com.qihe.zzj.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.qihe.zzj.BaseApplication;

/**
 * Created by cz on 2020/6/5.
 */

public class AppUtil {

    /**
     * 获取apk文件应用图标
     * @param apkPath apk文件路径
     * @return
     */

    public static Drawable getApkIcon(String apkPath) {
        PackageManager packageManager = BaseApplication.getContext().getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            ApplicationInfo info = packageInfo.applicationInfo;
            info.sourceDir = apkPath;
            info.publicSourceDir = apkPath;
            try {
                return info.loadIcon(packageManager);
            } catch (Exception e) {

            }
        }
        return null;
    }
}
