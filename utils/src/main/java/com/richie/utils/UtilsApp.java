package com.richie.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

/**
 * @author Richie on 2018.12.16
 */
public class UtilsApp {
    private static Context sAppContext;

    public static Context getAppContext() {
        if (sAppContext == null) {
            sAppContext = getApplicationByReflection();
        }
        return sAppContext;
    }

    /**
     * 初始化 context
     *
     * @param context
     */
    public static void init(Context context) {
        if (context == null) {
            sAppContext = getApplicationByReflection();
        } else {
            sAppContext = context.getApplicationContext();
        }
    }

    /**
     * 获取 Application Context
     *
     * @return context
     */
    @SuppressLint("PrivateApi")
    static Context getApplicationByReflection() {
        try {
            ClassLoader loader = Context.class.getClassLoader();
            Class<?> c = loader.loadClass("android.app.ActivityThread");
            Method currentActivityThreadM = c.getDeclaredMethod("currentActivityThread");
            currentActivityThreadM.setAccessible(true);
            Object currentActivityThread = currentActivityThreadM.invoke(null);
            Method getApplicationM = c.getDeclaredMethod("getApplication");
            getApplicationM.setAccessible(true);
            Application application = (Application) getApplicationM.invoke(currentActivityThread);
            return application.getApplicationContext();
        } catch (Throwable e) {
            // ignored
        }
        return null;
    }
}
