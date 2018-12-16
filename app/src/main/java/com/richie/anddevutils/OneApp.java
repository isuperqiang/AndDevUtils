package com.richie.anddevutils;

import android.app.Application;

import com.richie.utils.UtilsApp;

/**
 * @author Richie on 2018.12.16
 */
public class OneApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UtilsApp.init(this);
    }
}
