package com.qihe.zzj.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by cz on 2020/6/3.
 */

public class ProgressDialogUtil {
    ///------Progress Loading
    private ProgressDialog mLoading;
    private static ProgressDialogUtil util;

    public ProgressDialogUtil() {
    }

    public static ProgressDialogUtil getInstance() {
        if (util == null){
            util = new ProgressDialogUtil();
        }
        return util;
    }

    public void loadingShow(int percent,Context context) {
        if (mLoading == null) {
            mLoading = new ProgressDialog(context);
            mLoading.setProgressStyle(android.app.ProgressDialog.STYLE_HORIZONTAL);
            mLoading.setMax(100);
        }
        if (percent > 0) {
            mLoading.setProgress(percent);
            mLoading.setMessage(String.valueOf(percent) + "%");
        }
        if (!mLoading.isShowing()) mLoading.show();
    }

    public void loadingHide() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            mLoading = null;
        }
    }
}
