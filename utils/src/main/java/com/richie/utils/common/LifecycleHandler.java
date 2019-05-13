package com.richie.utils.common;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.os.Looper;

/**
 * @author Richie on 2019.05.13
 */
public final class LifecycleHandler extends Handler implements LifecycleObserver {
    private LifecycleOwner lifecycleOwner;

    public LifecycleHandler(final LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(final Callback callback, final LifecycleOwner lifecycleOwner) {
        super(callback);
        this.lifecycleOwner = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(final Looper looper, final LifecycleOwner lifecycleOwner) {
        super(looper);
        this.lifecycleOwner = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(final Looper looper, final Callback callback, final LifecycleOwner lifecycleOwner) {
        super(looper, callback);
        this.lifecycleOwner = lifecycleOwner;
        addObserver();
    }

    private void addObserver() {
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        removeCallbacksAndMessages(null);
        lifecycleOwner.getLifecycle().removeObserver(this);
    }
}
