package com.richie.utils.common;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程工具类
 *
 * @author Richie on 2019.02.20
 */
public final class ThreadHelper {
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private ThreadPoolExecutor mThreadPoolExecutor;

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
        mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 30, TimeUnit.SECONDS, blockingQueue, threadFactory);
        mThreadPoolExecutor.allowCoreThreadTimeOut(true);
    }

    public static ThreadHelper getInstance() {
        return ThreadHelperHolder.INSTANCE;
    }

    /**
     * 有返回值的异步任务，主线程调用并接收回调
     *
     * @param callable
     * @param callback
     */
    public <T> void enqueueOnUiThread(final Callable<T> callable, final Callback<T> callback) {
        if (callable != null) {
            mThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final CountDownLatch countDownLatch = new CountDownLatch(1);
                        if (callback != null) {
                            mMainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onStart();
                                    countDownLatch.countDown();
                                }
                            });
                        }
                        countDownLatch.await(500, TimeUnit.MILLISECONDS);
                        final T t = callable.call();
                        if (callback != null) {
                            mMainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(t);
                                }
                            });
                        }
                    } catch (final Exception e) {
                        if (callback != null) {
                            mMainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure(e);
                                }
                            });
                        }
                    } finally {
                        if (callback != null) {
                            mMainHandler.post(new Runnable() {
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
     * 有返回值的异步任务，在工作线程回调
     *
     * @param callable
     * @param callback
     */
    public <T> void enqueue(final Callable<T> callable, final Callback<T> callback) {
        if (callable != null) {
            mThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (callback != null) {
                            callback.onStart();
                        }
                        final T t = callable.call();
                        if (callback != null) {
                            callback.onSuccess(t);
                        }
                    } catch (final Exception e) {
                        if (callback != null) {
                            callback.onFailure(e);
                        }
                    } finally {
                        if (callback != null) {
                            callback.onFinish();
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
    public void execute(final Runnable r) {
        if (r != null) {
            mThreadPoolExecutor.execute(r);
        }
    }

    /**
     * 有返回值的异步任务
     *
     * @param task
     * @param <T>
     * @return
     */
    public <T> Future<T> submit(final Callable<T> task) {
        if (task != null) {
            return mThreadPoolExecutor.submit(task);
        } else {
            return null;
        }
    }

    /**
     * 主线程任务
     *
     * @param r
     */
    public void runOnUiThread(final Runnable r) {
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
    public boolean runOnUiPostDelayed(final Runnable r, final long delay) {
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
    public boolean runOnUiPostAtTime(final Runnable r, final long uptimeMillis) {
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
    public void removeUiCallbacks(final Runnable r) {
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
     * 执行顺序：
     * onStart-->onSuccess-->onFinish
     * onStart-->onFailure-->onFinish
     */
    public static abstract class Callback<T> {
        protected void onStart() {
        }

        protected void onFinish() {
        }

        protected void onSuccess(T result) {
        }

        protected void onFailure(Exception exception) {
        }
    }

    private static class ThreadHelperHolder {
        private static final ThreadHelper INSTANCE = new ThreadHelper();
    }

}
