package cn.richie.anddevutils;

import android.app.Application;
import android.content.Context;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * @author Richie on 2018.12.16
 */
public class OneApp extends Application {
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        AutoSizeConfig.getInstance().setLog(BuildConfig.DEBUG);
    }
}
