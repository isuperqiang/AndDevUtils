package com.richie.utils.listener;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Richie on 2018.05.11
 */
public class ActivityCallback implements Application.ActivityLifecycleCallbacks {
    private List<Activity> mActivities = new LinkedList<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        removeActivity(activity);
    }

    /**
     * 添加Activity
     */
    private void addActivity(Activity activity) {
        if (!mActivities.contains(activity)) {
            mActivities.add(activity);
        }
    }

    /**
     * 移除Activity
     */
    private void removeActivity(Activity activity) {
        mActivities.remove(activity);
    }

    /**
     * 销毁所有Activity
     */
    public void removeAllActivities() {
        for (Activity activity : mActivities) {
            if (null != activity) {
                activity.finish();
            }
        }
    }

}
