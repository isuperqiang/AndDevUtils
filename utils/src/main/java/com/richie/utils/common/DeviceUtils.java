package com.richie.utils.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 设备相关
 *
 * @author Richie on 2017.10.30
 */
public final class DeviceUtils {
    private static final String TAG = "DeviceUtils";

    private DeviceUtils() {
    }

    /**
     * 获取IMEI，需要 READ_PHONE_STATE 权限
     *
     * @return IMEI or null
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }

        if (PackageManager.PERMISSION_GRANTED == context.checkPermission(Manifest.permission.READ_PHONE_STATE,
                Process.myPid(), Process.myUid())) {
            String imei;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                imei = tm.getImei();
                if (TextUtils.isEmpty(imei)) {
                    imei = tm.getMeid();
                }
            } else {
                imei = tm.getDeviceId();
            }
            return imei;
        } else {
            return "";
        }
    }

    private static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            display.getSize(point);
        }
        return point;
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context
     * @return
     */
    public static String getScreenSizeS(Context context) {
        Point screenSize = getScreenSize(context);
        return screenSize.x + "x" + screenSize.y;
    }

    /**
     * 获取 Mac 地址，需要 INTERNET、ACCESS_WIFI_STATE、ACCESS_NETWORK_STATE 权限
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String macAddressFromIp = getMacAddressFromIp(context);
        if (TextUtils.isEmpty(macAddressFromIp)) {
            macAddressFromIp = getMacAddressInAndroidM();
        }
        return macAddressFromIp;
    }

    /**
     * 获取 Android 6.0 以上设备的 Mac 地址
     *
     * @return
     */
    private static String getMacAddressInAndroidM() {
        String defaultMacAddress = "00:00:00:00:00:00";
        String value = getSystemProperties("wifi.interface", "wlan0");
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(value);
            if (networkInterface == null) {
                return defaultMacAddress;
            }
            byte[] hardwareAddress = networkInterface.getHardwareAddress();
            if (hardwareAddress == null || hardwareAddress.length == 0) {
                return defaultMacAddress;
            }
            StringBuilder buf = new StringBuilder();
            for (byte b : hardwareAddress) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            return buf.toString().trim();
        } catch (Exception e) {
            Log.e(TAG, "getMacAddressInAndroidM: ", e);
        }
        return defaultMacAddress;
    }

    /**
     * 获取 IP 地址
     *
     * @param context
     * @return
     */
    public static String getIpAddress(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return "0.0.0.0";
        }

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getIpAddress: ", e);
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    return intIP2StringIP(wifiInfo.getIpAddress());
                }
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有线网络
                return getLocalIp();
            }
        }
        return "0.0.0.0";
    }

    private static String getSystemProperties(String key, String def) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getDeclaredMethod("get", String.class, String.class);
            method.setAccessible(true);
            return (String) method.invoke(null, key, def);
        } catch (Exception e) {
            Log.e(TAG, "getSystemProperties: ", e);
            return def;
        }
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取有线网IP
     *
     * @return
     */
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getLocalIp: ", e);
        }
        return "0.0.0.0";
    }

    /**
     * 从 IP 地址获取 Mac 地址
     *
     * @param context
     * @return
     */
    private static String getMacAddressFromIp(Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getIpAddress(context)));
            mac = ne.getHardwareAddress();
            for (byte b : mac) {
                sb.append(String.format("%02X:", b));
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        } catch (Exception e) {
            Log.e(TAG, "getMacAddressFromIp: ", e);
        }
        return sb.toString();
    }

}
