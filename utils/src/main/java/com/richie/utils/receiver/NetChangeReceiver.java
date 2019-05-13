package com.richie.utils.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 网络变化接收器，为每个 Activity 注册，在网络变化的时候通知用户
 *
 * @author Richie on 2017.11.28
 */
public final class NetChangeReceiver extends BaseBroadcastReceiver {
    /**
     * 最小触发时间, 避免多次回调
     */
    private static final int MIN_TRIGGER_TIME = 1000;
    private long mLastTime = System.currentTimeMillis();
    private OnNetChangedListener mOnNetChangedListener;
    private static final String TAG = "NetChangeReceiver";

    @Override
    protected void doReceive(Context context, Intent intent) {
        Log.d(TAG, intent != null ? intent.toUri(0) : null);
        if (intent != null) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = null;
                if (connectivityManager != null) {
                    activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                }
                if (activeNetworkInfo != null) {
                    if (activeNetworkInfo.isConnected()) {
                        if (System.currentTimeMillis() - mLastTime > MIN_TRIGGER_TIME) {
                            Log.i(TAG, "network connected");
                            if (mOnNetChangedListener != null) {
                                mOnNetChangedListener.onNetChanged(true);
                            }
                        }
                        mLastTime = System.currentTimeMillis();
                    } else {
                        if (System.currentTimeMillis() - mLastTime > MIN_TRIGGER_TIME) {
                            Log.i(TAG, "network disconnected");
                            if (mOnNetChangedListener != null) {
                                mOnNetChangedListener.onNetChanged(false);
                            }
                        }
                        mLastTime = System.currentTimeMillis();
                    }
                } else {
                    if (System.currentTimeMillis() - mLastTime > MIN_TRIGGER_TIME) {
                        Log.i(TAG, "network disconnected");
                        mOnNetChangedListener.onNetChanged(false);
                    }
                    mLastTime = System.currentTimeMillis();
                }
            }
        }
    }

    public void setOnNetChangedListener(OnNetChangedListener onNetChangedListener) {
        mOnNetChangedListener = onNetChangedListener;
    }

    public interface OnNetChangedListener {
        /**
         * 网络状态变化
         *
         * @param connected
         */
        void onNetChanged(boolean connected);
    }

}
