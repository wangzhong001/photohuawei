package com.xinqidian.adcommon.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipei on 2018/5/4.
 */

public class ActivityManagerUtils {


    private static ActivityManagerUtils mActivityManagerUtils;


    private ActivityManagerUtils() {
    }

    public static ActivityManagerUtils getInstance() {
        if (mActivityManagerUtils == null) {
            mActivityManagerUtils = new ActivityManagerUtils();
        }
        return mActivityManagerUtils;
    }


    /**
     * 打开的activity
     **/

    private List<Activity> activities = new ArrayList<Activity>();


    /**
     * 新建了一个activity
     *
     * @param activity
     */

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */

    public void finishActivity(Activity activity) {

        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
        }
    }

    /**
     * 应用退出，结束所有的activity
     */

    public void exit() {

        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);

    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivityclass(Class<?> cls) {

        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(cls)) {
                    this.activities.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }

    }

    public static void onDestory() {
        mActivityManagerUtils = null;
    }


}
