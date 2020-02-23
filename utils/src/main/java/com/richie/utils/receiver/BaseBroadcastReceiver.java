package com.richie.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 所有 BroadcastReceiver 的父类
 *
 * @author Richie on 2017.11.16
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {
    /**
     * 处理广播事件的最长时长
     */
    private static final int HANDLE_RECEIVE_TIME = 1000;
    private static final String TAG = "BaseBroadcastReceiver";

    @Override
    public final void onReceive(Context context, Intent intent) {
        try {
            long beginning = System.currentTimeMillis();
            doReceive(context, intent);
            long duration = System.currentTimeMillis() - beginning;
            if (duration > HANDLE_RECEIVE_TIME) {
                Log.w(TAG, getClass().getName() + " call doReceive duration " + duration + "ms");
            }
        } catch (Exception e) {
            Log.e(TAG, "onReceive: ", e);
        }
    }

    /**
     * 接收到广播事件
     *
     * @param context
     * @param intent
     */
    protected abstract void doReceive(Context context, Intent intent);
}
