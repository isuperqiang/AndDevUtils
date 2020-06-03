package com.richie.utils.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 所有 Activity 的父类
 *
 * @author Richie on 2019.01.12
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    /**
     * 获取布局资源
     *
     * @return 布局 ID
     */
    @LayoutRes
    protected abstract int getLayoutResId();

    /**
     * 初始化视图
     */
    protected void initView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 暂停
     */
    protected void pause() {
    }

    /**
     * 恢复
     */
    protected void resume() {
    }

    /**
     * 释放资源
     */
    protected void release() {
    }

}
