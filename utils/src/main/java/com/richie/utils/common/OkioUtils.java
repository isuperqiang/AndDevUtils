package com.richie.utils.common;

import com.richie.easylog.ILogger;
import com.richie.easylog.LoggerFactory;

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
    private static ILogger logger = LoggerFactory.getLogger(OkioUtils.class);

    private OkioUtils() {
    }

    /**
     * 从文件读取字节数组
     *
     * @param src
     * @return
     */
    public static byte[] read(String src) {
        if (isEmpty(src)) {
            return null;
        }
        try (BufferedSource bufferedSource = Okio.buffer(Okio.source(new File(src)))) {
            return bufferedSource.readByteArray();
        } catch (IOException e) {
            logger.error(e);
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
            logger.error(e);
        }
        return null;
    }

    /**
     * 从文件读取字符串
     *
     * @param src
     * @return
     */
    public static String readString(String src) {
        if (isEmpty(src)) {
            return null;
        }
        try (BufferedSource bufferedSource = Okio.buffer(Okio.source(new File(src)))) {
            return bufferedSource.readUtf8();
        } catch (IOException e) {
            logger.error(e);
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
            logger.error(e);
        }
        return null;
    }

    /**
     * 写字符串到文件
     *
     * @param data
     * @param dest
     */
    public static boolean writeString(String data, String dest) {
        if (isEmpty(data) || isEmpty(dest)) {
            return false;
        }
        try (BufferedSink bufferedSink = Okio.buffer(Okio.sink(new File(dest)))) {
            bufferedSink.writeUtf8(data);
            return true;
        } catch (IOException e) {
            logger.error(e);
        }
        return false;
    }

    /**
     * 写字节数组到文件
     *
     * @param data
     * @param dest
     */
    public static boolean write(byte[] data, String dest) {
        if (data == null || isEmpty(dest)) {
            return false;
        }
        try (BufferedSink bufferedSink = Okio.buffer(Okio.sink(new File(dest)))) {
            bufferedSink.write(data);
            return true;
        } catch (IOException e) {
            logger.error(e);
        }
        return false;
    }

    /**
     * 拷贝文件
     *
     * @param src
     * @param dest
     */
    public static boolean copyFile(String src, String dest) {
        if (isEmpty(src) || isEmpty(dest)) {
            return false;
        }
        try (BufferedSource bufferedSource = Okio.buffer(Okio.source(new File(src)));
             BufferedSink bufferedSink = Okio.buffer(Okio.sink(new File(dest)))) {
            byte[] bytes = bufferedSource.readByteArray();
            bufferedSink.write(bytes);
            return true;
        } catch (IOException e) {
            logger.error(e);
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
            logger.error(e);
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
            logger.error(e);
        }
        return null;
    }

    private static boolean isEmpty(CharSequence data) {
        return data == null || data.length() == 0;
    }

}
