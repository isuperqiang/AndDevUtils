package com.richie.utils.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 所有 DialogFragment 的父类
 *
 * @author Richie on 2017.09.26
 */
public abstract class BaseDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResID(), container, false);
        initWindowParams();
        initView(view);
        return view;
    }

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        release();
    }

    private void initWindowParams() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            windowAttributes.width = WindowManager.LayoutParams.WRAP_CONTENT;
            windowAttributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(windowAttributes);
        }
    }

    /**
     * 获取布局 ID
     *
     * @return layout resId
     */
    @LayoutRes
    protected abstract int getLayoutResID();

    /**
     * 初始化视图
     *
     * @param rootView
     */
    protected void initView(View rootView) {
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
