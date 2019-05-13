package com.richie.utils.common;

import android.widget.Toast;

import com.richie.utils.UtilsApp;

/**
 * @author Richie on 2019.05.13
 */
public final class ToastUtils {
    private static final Toast sToast;

    static {
        sToast = Toast.makeText(UtilsApp.getAppContext(), "", Toast.LENGTH_LONG);
    }

    private ToastUtils() {
    }

    /**
     * 弹 Toast , 复用一个单例，解决多次弹 Toast 的问题。
     *
     * @param msg
     */
    public static void show(final CharSequence msg) {
        ThreadHelper.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sToast.setText(msg);
                sToast.show();
            }
        });
    }

}
