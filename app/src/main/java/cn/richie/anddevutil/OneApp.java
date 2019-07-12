package cn.richie.anddevutil;

import android.app.Application;
import android.content.Context;

import com.richie.easylog.LoggerConfig;
import com.richie.easylog.LoggerFactory;

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
        boolean debug = BuildConfig.DEBUG;
        AutoSizeConfig.getInstance().setLog(debug);
        LoggerFactory.init(new LoggerConfig.Builder()
                .context(this)
                .logcatEnabled(debug)
                .build());
    }
}
