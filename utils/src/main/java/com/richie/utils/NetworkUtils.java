package com.richie.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Richie on 2017.11.10
 */
public class NetworkUtils {

    private NetworkUtils() {
    }

    /**
     * 判断网络是否连接¬
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return connected
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            return false;
        }
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return type
     */
    public static String getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                int type = activeNetworkInfo.getType();
                switch (type) {
                    case ConnectivityManager.TYPE_WIFI:
                        return "wifi";
                    case ConnectivityManager.TYPE_MOBILE:
                        return "mobile";
                    case ConnectivityManager.TYPE_ETHERNET:
                        return "ethernet";
                    case ConnectivityManager.TYPE_BLUETOOTH:
                        return "bluetooth";
                    default:
                }
            }
        }
        return "unknown";
    }

}
