package com.richie.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Richie
 * 多线程工具类
 */
public class ThreadHelper {
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private ThreadPoolExecutor mExecutorService;

    private ThreadHelper() {
        // copy from AsyncTask THREAD_POOL_EXECUTOR
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
            }
        };
        int cpuCount = Runtime.getRuntime().availableProcessors();
        int corePoolSize = Math.max(2, Math.min(cpuCount - 1, 4));
        int maxPoolSize = cpuCount * 2 + 1;
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(128);
        mExecutorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 30, TimeUnit.SECONDS, blockingQueue, threadFactory);
        mExecutorService.allowCoreThreadTimeOut(true);
    }

    public static ThreadHelper getInstance() {
        return ThreadHelperHolder.INSTANCE;
    }

    /**
     * 有返回值的异步任务，主线程回调事件
     *
     * @param callable
     * @param callback
     */
    public <T> void enqueue(final Callable<T> callable, final Callback callback) {
        if (callable != null) {
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (callback != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onStart();
                                }
                            });
                        }
                        Future<T> future = mExecutorService.submit(callable);
                        final T t = future.get();
                        if (callback != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(t);
                                }
                            });
                        }
                    } catch (final Throwable throwable) {
                        if (callback != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure(throwable);
                                }
                            });
                        }
                    } finally {
                        if (callback != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFinish();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    /**
     * 无返回值的异步任务
     *
     * @param r
     */
    public void execute(Runnable r) {
        if (r != null) {
            mExecutorService.execute(r);
        }
    }

    /**
     * 有返回值的异步任务
     *
     * @param task
     * @param <T>
     * @return
     */
    public <T> Future<T> submit(Callable<T> task) {
        if (task != null) {
            return mExecutorService.submit(task);
        } else {
            return null;
        }
    }

    /**
     * 主线程任务
     *
     * @param r
     */
    public void runOnUiThread(Runnable r) {
        if (r != null) {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                r.run();
            } else {
                mMainHandler.post(r);
            }
        }
    }

    /**
     * 主线程延时任务
     *
     * @param r
     * @param delay
     * @return
     */
    public boolean runOnUiPostDelayed(Runnable r, long delay) {
        if (r != null) {
            return mMainHandler.postDelayed(r, delay);
        } else {
            return false;
        }
    }

    /**
     * 主线程定时任务
     *
     * @param r
     * @param uptimeMillis
     * @return
     */
    public boolean runOnUiPostAtTime(Runnable r, long uptimeMillis) {
        if (r != null) {
            return mMainHandler.postAtTime(r, uptimeMillis);
        } else {
            return false;
        }
    }

    /**
     * 移除主线程的任务
     *
     * @param r
     */
    public void removeUiCallbacks(Runnable r) {
        if (r != null) {
            mMainHandler.removeCallbacks(r);
        }
    }

    /**
     * 移除主线程所有任务
     */
    public void removeUiAllTasks() {
        mMainHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 主线程的回调
     */
    public static abstract class Callback {
        protected void onStart() {
        }

        protected void onFinish() {
        }

        protected void onSuccess(Object result) {
        }

        protected void onFailure(Throwable throwable) {
        }
    }

    private static class ThreadHelperHolder {
        private static final ThreadHelper INSTANCE = new ThreadHelper();
    }

}
