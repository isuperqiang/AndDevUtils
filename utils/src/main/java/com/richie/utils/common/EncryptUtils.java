package com.richie.utils.common;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 加密解密相关
 *
 * @author Richie on 2017.11.10
 */
public final class EncryptUtils {
    private static final String DES_TRANSFORMATION = "DES/ECB/PKCS5Padding";
    private static final String ALGORITHM = "DES";
    /**
     * 本地 DES 加密的 key，随机 8 个长度字符串
     */
    private final static byte[] KEY_BYTES = "V4UfmGI0".getBytes();
    private static final String TAG = "EncryptUtils";

    private EncryptUtils() {
    }

    /**
     * DES 加密
     *
     * @param content
     * @return
     */
    public static String encryptDESString(String content) {
        byte[] bytes = encryptDES(content.getBytes(), KEY_BYTES);
        if (bytes != null) {
            byte[] output = base64Encode(bytes);
            return new String(output);
        } else {
            return "";
        }
    }

    /**
     * DES 加密
     *
     * @param content 待加密内容
     * @param key     加密的密钥
     * @return 加密后的字节数组
     */
    public static byte[] encryptDES(byte[] content, byte[] key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance(DES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            return cipher.doFinal(content);
        } catch (Exception e) {
            Log.e(TAG, "encryptDES: ", e);
        }
        return null;
    }

    /**
     * DES 解密
     *
     * @param content 待加密内容
     * @return 加密后的字符串
     */
    public static String decryptDESString(String content) {
        byte[] input = base64Decode(content.getBytes());
        byte[] output = decryptDES(input, KEY_BYTES);
        if (output != null) {
            return new String(output);
        } else {
            return null;
        }
    }

    /**
     * DES 解密
     *
     * @param content 待解密内容
     * @param key     解密的密钥
     * @return 解密的数据
     */
    public static byte[] decryptDES(byte[] content, byte[] key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance(DES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            return cipher.doFinal(content);
        } catch (Exception e) {
            Log.e(TAG, "decryptDES: ", e);
        }
        return null;
    }

    /**
     * Base64 编码
     *
     * @param input 内容
     * @return 结果
     */
    public static byte[] base64Encode(final byte[] input) {
        if (input != null) {
            return Base64.encode(input, Base64.NO_WRAP);
        } else {
            return null;
        }
    }

    /**
     * Base64 解码
     *
     * @param input 内容
     * @return 结果
     */
    public static byte[] base64Decode(final byte[] input) {
        if (input != null) {
            return Base64.decode(input, Base64.NO_WRAP);
        } else {
            return null;
        }
    }

}
