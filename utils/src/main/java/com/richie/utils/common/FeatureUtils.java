package com.richie.utils.common;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * @author Richie on 2018.12.22
 * 获取特征值
 */
public final class FeatureUtils {

    private FeatureUtils() {
    }

    /**
     * 获取 UUID 作为唯一标识 eg: c9ce6bdd155749be91153a6d76a484eb
     *
     * @return 32 个字符
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 使用 Mac 地址和 IMEI　的　MD5 作为设备唯一标识
     *
     * @param context
     * @return
     */
    public static String getDeviceToken(Context context) {
        String imei = DeviceUtils.getIMEI(context);
        String macAddress = DeviceUtils.getMacAddress(context);
        return md5Encode(imei + "-" + macAddress);
    }

    /**
     * 对字符串进行 MD5 编码 eg: b10a8db164e0754105b7a99be72e3fe5
     *
     * @param text
     * @return 32个字符
     */
    public static String md5Encode(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 加密转换
            byte[] digest = md.digest(text.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                // 取低8位 取正
                int a = b & 0xff;
                String hexString = Integer.toHexString(a);
                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (Exception e) {
            // ignored
        }
        return "";
    }


    /**
     * 计算文件的 MD5 eg: b7a8f4ca438b5ec437b11cc97fce233d
     *
     * @param file
     * @return 32个字符
     */
    public static String getMd5ByFile(File file) throws Exception {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            MappedByteBuffer byteBuffer = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            return bi.toString(16);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
