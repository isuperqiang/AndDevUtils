package com.richie.utils.common;


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

/**
 * @author Richie on 2019.02.20
 */
public final class OkioUtils {
    private static final String TAG = "OkioUtils";

    private OkioUtils() {
    }

    /**
     * 从文件读取字节数组
     *
     * @param srcPath
     * @return
     */
    public static byte[] read(String srcPath) {
        if (isEmpty(srcPath)) {
            return null;
        }
        return read(new File(srcPath));
    }

    /**
     * 从文件读取字节数组
     *
     * @param src
     * @return
     */
    public static byte[] read(File src) {
        if (src == null) {
            return null;
        }
        try (BufferedSource bufferedSource = Okio.buffer(Okio.source(src))) {
            return bufferedSource.readByteArray();
        } catch (IOException e) {
            Log.e(TAG, "read: ", e);
        }
        return null;
    }

    /**
     * 从输入流读取字节数组
     *
     * @param is
     * @return
     */
    public static byte[] read(InputStream is) {
        if (is == null) {
            return null;
        }
        try (BufferedSource bufferedSource = Okio.buffer(Okio.source(is))) {
            return bufferedSource.readByteArray();
        } catch (IOException e) {
            Log.e(TAG, "read: ", e);
        }
        return null;
    }

    /**
     * 从文件读取字符串
     *
     * @param srcPath
     * @return
     */
    public static String readString(String srcPath) {
        if (isEmpty(srcPath)) {
            return null;
        }
        return readString(new File(srcPath));
    }

    /**
     * 从文件读取字符串
     *
     * @param src
     * @return
     */
    public static String readString(File src) {
        if (src == null) {
            return null;
        }
        try (BufferedSource bufferedSource = Okio.buffer(Okio.source(src))) {
            return bufferedSource.readUtf8();
        } catch (IOException e) {
            Log.e(TAG, "readString: ", e);
        }
        return null;
    }

    /**
     * 从输入流读取字符串
     *
     * @param is
     * @return
     */
    public static String readString(InputStream is) {
        if (is == null) {
            return null;
        }
        try {
            ByteString byteString = ByteString.read(is, is.available());
            return byteString.utf8();
        } catch (IOException e) {
            Log.e(TAG, "readString: ", e);
        }
        return null;
    }

    /**
     * 写字符串到文件
     *
     * @param data
     * @param destPath
     */
    public static boolean writeString(String data, String destPath) {
        if (isEmpty(data) || isEmpty(destPath)) {
            return false;
        }
        return writeString(data, new File(destPath));
    }

    /**
     * 写字符串到文件
     *
     * @param data
     * @param dest
     */
    public static boolean writeString(String data, File dest) {
        if (isEmpty(data) || dest == null) {
            return false;
        }
        try (BufferedSink bufferedSink = Okio.buffer(Okio.sink(dest))) {
            bufferedSink.writeUtf8(data);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "writeString: ", e);
        }
        return false;
    }

    /**
     * 写字节数组到文件
     *
     * @param data
     * @param destPath
     */
    public static boolean write(byte[] data, String destPath) {
        if (data == null || isEmpty(destPath)) {
            return false;
        }
        return write(data, new File(destPath));
    }

    /**
     * 写字节数组到文件
     *
     * @param data
     * @param dest
     */
    public static boolean write(byte[] data, File dest) {
        if (data == null || dest == null) {
            return false;
        }
        try (BufferedSink bufferedSink = Okio.buffer(Okio.sink(dest))) {
            bufferedSink.write(data);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "write: ", e);
        }
        return false;
    }

    /**
     * 拷贝文件
     *
     * @param srcPath
     * @param destPath
     */
    public static boolean copyFile(String srcPath, String destPath) {
        if (isEmpty(srcPath) || isEmpty(destPath)) {
            return false;
        }
        return copyFile(new File(srcPath), new File(destPath));
    }

    /**
     * 拷贝文件
     *
     * @param src
     * @param dest
     */
    public static boolean copyFile(File src, File dest) {
        if (src == null || dest == null) {
            return false;
        }
        try (BufferedSource bufferedSource = Okio.buffer(Okio.source(src));
             BufferedSink bufferedSink = Okio.buffer(Okio.sink(dest))) {
            byte[] bytes = bufferedSource.readByteArray();
            bufferedSink.write(bytes);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "copyFile: ", e);
        }
        return false;
    }

    /**
     * 计算字符串的 md5
     *
     * @param data
     * @return
     */
    public static String md5(String data) {
        if (isEmpty(data)) {
            return null;
        }
        ByteString byteString = ByteString.encodeUtf8(data);
        return byteString.md5().hex();
    }

    /**
     * 计算字符串的 sha256
     *
     * @param data
     * @return
     */
    public static String sha256(String data) {
        if (isEmpty(data)) {
            return null;
        }
        ByteString byteString = ByteString.encodeUtf8(data);
        return byteString.sha256().hex();
    }

    /**
     * 计算字符串的 sha512
     *
     * @param data
     * @return
     */
    public static String sha512(String data) {
        if (isEmpty(data)) {
            return null;
        }
        ByteString byteString = ByteString.encodeUtf8(data);
        return byteString.sha512().hex();
    }

    /**
     * base64 编码字符串
     *
     * @param data
     * @return
     */
    public static String base64encode(String data) {
        if (isEmpty(data)) {
            return null;
        }
        ByteString byteString = ByteString.encodeUtf8(data);
        return byteString.base64();
    }

    /**
     * base64 解码字符串
     *
     * @param data
     * @return
     */
    public static String base64decode(String data) {
        if (isEmpty(data)) {
            return null;
        }
        ByteString byteString = ByteString.decodeBase64(data);
        if (byteString != null) {
            return byteString.utf8();
        }
        return null;
    }

    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    public static String serialize(Object obj) {
        if (obj == null) {
            return null;
        }
        try (Buffer buffer = new Buffer();
             ObjectOutputStream objectOut = new ObjectOutputStream(buffer.outputStream())) {
            objectOut.writeObject(obj);
            ByteString byteString = buffer.readByteString();
            return byteString.base64();
        } catch (IOException e) {
            Log.e(TAG, "serialize: ", e);
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param data
     * @return
     */
    public static Object deserialize(String data) {
        if (isEmpty(data)) {
            return null;
        }
        ByteString byteString = ByteString.decodeBase64(data);
        if (byteString == null) {
            return null;
        }
        Buffer buffer = new Buffer();
        buffer.write(byteString);
        try (ObjectInputStream objectIn = new ObjectInputStream(buffer.inputStream())) {
            return objectIn.readObject();
        } catch (Exception e) {
            Log.e(TAG, "deserialize: ", e);
        }
        return null;
    }

    private static boolean isEmpty(CharSequence data) {
        return data == null || data.length() == 0;
    }

}
