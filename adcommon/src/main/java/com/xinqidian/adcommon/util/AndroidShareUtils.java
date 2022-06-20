package com.xinqidian.adcommon.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;


import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.ui.activity.FeedBackActivity;

import java.util.List;

/**
 * Created by lipei on 2017/11/22.
 */

public class AndroidShareUtils {
    /**
     * 上下文
     */
    private Context context;

    /**
     * 文本类型
     */
    public static int TEXT = 0;

    /**
     * 图片类型
     */
    public static int DRAWABLE = 1;

    public AndroidShareUtils(Context context) {
        this.context = context;
    }

    /**
     * 分享到QQ好友
     *
     * @param type     (分享类型)
     * @param drawable (分享图片，若分享类型为AndroidShare.TEXT，则可以为null)
     */
    public void shareQQFriend(int type,
                              Uri drawable) {

        shareMsg("com.tencent.mobileqq",
                "com.tencent.mobileqq.activity.JumpActivity", "QQ", type, drawable);
    }


    /**
     * 分享到微信好友
     *
     * @param type     (分享类型)
     * @param drawable (分享图片，若分享类型为AndroidShare.TEXT，则可以为null)
     */
    public void shareWeChatFriend(int type,
                                  Uri drawable) {

        shareMsg("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI", "微信", type, drawable);
    }

    /**
     * 分享到微信朋友圈(分享朋友圈一定需要图片)
     *
     * @param drawable (分享图片)
     */
    public void shareWeChatFriendCircle(
            Uri drawable) {

        shareMsg("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI",
                "微信", AndroidShareUtils.DRAWABLE, drawable);
    }

    /**
     * 点击分享的代码
     *
     * @param packageName  (包名,跳转的应用的包名)
     * @param activityName (类名,跳转的页面名称)
     * @param appname      (应用名,跳转到的应用名称)
     * @param type         (发送类型：text or pic 微信朋友圈只支持pic)
     */
    @SuppressLint("NewApi")
    private void shareMsg(String packageName, String activityName,
                          String appname, int type,
                          Uri uri) {
        if (!packageName.isEmpty() && !isAvilible(context, packageName)) {// 判断APP是否存在
            Toast.makeText(context, context.getString(R.string.please) + appname, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Intent intent = new Intent("android.intent.action.SEND");
        if (type == AndroidShareUtils.TEXT) {
            intent.setType("text/plain");
        } else if (type == AndroidShareUtils.DRAWABLE) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!packageName.isEmpty()) {
            intent.setComponent(new ComponentName(packageName, activityName));
            context.startActivity(intent);
        } else {
            context.startActivity(intent);
        }
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 指定分享到qq
     *
     * @param context
     * @param bitmap
     */
    public void sharedQQ(Activity context, Uri bitmap) {
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setPackage("com.tencent.mobileqq");
        imageIntent.setType("image/*");
        imageIntent.putExtra(Intent.EXTRA_STREAM, bitmap);
        context.startActivity(imageIntent);
    }

    public static void goToMarket(Context mContext) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id="+ AppUtils.getPackageName(mContext)));
            mContext.startActivity(i);
        } catch (Exception e) {
//            Toast.makeText(MoreActivity.this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public static void feedBack(Context mContext){
//        Intent mEmailIntent=new Intent(Intent.ACTION_SEND);
//        mEmailIntent.setType("plain/text");
//        mEmailIntent.putExtra(Intent.EXTRA_EMAIL, Contans.strEmailReciver);
//        mEmailIntent.putExtra(Intent.EXTRA_CC,Contans.strEmailCC);
//        mEmailIntent.putExtra(Intent.EXTRA_SUBJECT,Contans.strEmailSubject);
//        mEmailIntent.putExtra(Intent.EXTRA_TEXT,Contans.strEmailBody);
//        mContext.startActivity(Intent.createChooser(mEmailIntent,mContext.getResources().getString(R.string.feedback)));
        Intent intent=new Intent(mContext, FeedBackActivity.class);
        mContext.startActivity(intent);
    }




}
