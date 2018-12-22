package com.richie.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.richie.easylog.ILogger;
import com.richie.easylog.LoggerFactory;

/**
 * @author Richie on 2017.11.16
 * 广播接收器
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {
    /**
     * 处理广播事件的最长时长
     */
    private static final int HANDLE_RECEIVE_TIME = 1000;
    private final ILogger logger = LoggerFactory.getLogger(BaseBroadcastReceiver.class);

    @Override
    public final void onReceive(Context context, Intent intent) {
        try {
            long beginning = System.currentTimeMillis();
            doReceive(context, intent);
            long duration = System.currentTimeMillis() - beginning;
            if (duration > HANDLE_RECEIVE_TIME) {
                logger.warn("****** 你是一个大坏蛋，占用广播的 onReceive 时间太长了 ****** class:{}, time:{}ms", getClass().getName(), duration);
            }
        } catch (Throwable e) {
            logger.error(e);
        }
    }

    /**
     * 接收到广播事件
     *
     * @param context
     * @param intent
     */
    public abstract void doReceive(Context context, Intent intent);
}
