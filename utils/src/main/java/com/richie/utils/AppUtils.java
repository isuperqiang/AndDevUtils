package com.richie.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;

import java.util.List;

/**
 * @author Richie on 2017.11.08
 */
public class AppUtils {

    private AppUtils() {
    }

    /**
     * 获取当前进程名称
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps == null || runningApps.size() <= 0) {
                return "";
            }
            for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
                if (proInfo.pid == android.os.Process.myPid()) {
                    if (proInfo.processName != null) {
                        return proInfo.processName;
                    }
                }
            }
        }
        return "";
    }

    /**
     * 获取应用的版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            // ignored
        }
        return "1.0.0";
    }

    /**
     * 获取应用的版本号码
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            // ignored
        }
        return 100;
    }

}
