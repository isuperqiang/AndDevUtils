package com.richie.utils.common;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.richie.easylog.ILogger;
import com.richie.easylog.LoggerFactory;
import com.richie.utils.BuildConfig;
import com.richie.utils.listener.ActivityCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Richie on 2018.03.01
 * 崩溃处理
 */
public final class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler sInstance;
    private final ILogger logger = LoggerFactory.getLogger(CrashHandler.class);
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private ActivityCallback mCallback;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (null == sInstance) {
            sInstance = new CrashHandler();
        }
        return sInstance;
    }

    /**
     * 在 Application 里面初始化
     *
     * @param context application
     */
    public void init(@NonNull Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为系统默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
        mCallback = new ActivityCallback();
        ((Application) mContext).registerActivityLifecycleCallbacks(mCallback);
    }

    /**
     * uncaughtException 回调函数
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果自己没处理交给系统处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                //延迟3秒杀进程
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                logger.error(e);
            }
            // 3秒后重启
//            AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            //Intent intent = new Intent(mContext, LoginActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            //if (mgr != null) {
            //    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
            //}
            //退出程序
            mCallback.removeAllActivities();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 收集错误信息.发送到服务器
     *
     * @return 处理了该异常返回true, 否则false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 在有 looper 的线程里面 toast
        ThreadHelper.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "哎呀，系统出了点小问题\n请重新打开试试吧", Toast.LENGTH_LONG).show();
            }
        });

        String deviceInfo = collectDeviceInfo();
        String exceptionString = getExceptionString(ex);
        String message = deviceInfo + "\n" + exceptionString;

        //保存日志文件
        saveCrashInfo2File(message);
        return true;
    }

    private String collectDeviceInfo() {
        Map<String, String> paramsMap = new LinkedHashMap<>();
        paramsMap.put("imei", DeviceUtils.getIMEI(mContext));
        paramsMap.put("macAddress", DeviceUtils.getMacAddress(mContext));
        paramsMap.put("ipAddress", DeviceUtils.getIpAddress(mContext));
        paramsMap.put("screenSize", DeviceUtils.getScreenSizeS(mContext));
        paramsMap.put("networkType", NetworkUtils.getNetworkType(mContext));
        paramsMap.put("versionName", AppUtils.getVersionName(mContext));
        paramsMap.put("versionCode", String.valueOf(AppUtils.getVersionCode(mContext)));
        paramsMap.put("buildType", BuildConfig.DEBUG ? "debug" : "release");
        paramsMap.put("processName", AppUtils.getProcessName(mContext));

        //获取所有系统信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                paramsMap.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                logger.error(e);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        return sb.toString();
    }

    private void saveCrashInfo2File(String message) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
            String time = format.format(new Date());
            String fileName = "crash_" + time + ".log";
            File dir = new File(FileUtils.getExternalFileDir(mContext), "crash");
            if (FileUtils.createOrExistsDir(dir)) {
                FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
                fos.write(message.getBytes());
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private String getExceptionString(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }

}
