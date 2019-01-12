package com.richie.utils.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Richie on 2019.01.12
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    /**
     * 获取布局
     *
     * @return layout resId
     */
    @LayoutRes
    protected abstract int getLayoutResID();

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
     * 释放资源
     */
    protected void release() {
    }

}
