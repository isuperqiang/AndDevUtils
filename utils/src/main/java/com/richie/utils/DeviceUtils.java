package com.richie.utils;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

import com.richie.easylog.ILogger;
import com.richie.easylog.LoggerFactory;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author Richie on 2017.10.30
 */
public class DeviceUtils {
    private static ILogger logger = LoggerFactory.getLogger(DeviceUtils.class);

    private DeviceUtils() {
    }

    /**
     * 获取IMEI码
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @return IMEI码
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getDeviceId() : "";
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
     * 获取 Mac 地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String macAddressFromIp = getMacAddressFromIp(context);
        if (macAddressFromIp.isEmpty()) {
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
            logger.error(e);
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
        ConnectivityManager systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (systemService != null) {
            NetworkInfo info = systemService.getActiveNetworkInfo();
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
                        logger.error(e);
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
            logger.error(e);
            return def;
        }
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    // 获取有线网IP
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
        } catch (Exception ex) {
            logger.error(ex);
        }
        return "0.0.0.0";
    }

    private static String getMacAddressFromIp(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getIpAddress(context)));
            mac = ne.getHardwareAddress();
            for (byte b : mac) {
                stringBuilder.append(String.format("%02X:", b));
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return stringBuilder.toString();
    }
}
