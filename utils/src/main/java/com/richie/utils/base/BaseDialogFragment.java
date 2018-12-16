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
 * @author Richie on 2017.09.26
 * 所有 DialogFragment 的父类
 */
public abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResID(), container, false);
        initWindowParams();
        initView();
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
     * 获取布局
     *
     * @return 布局资源 ID
     */
    @LayoutRes
    protected abstract int getLayoutResID();

    /**
     * 初始化布局
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

    public interface OnDialogInputListener {

        /**
         * 对话框输入结果
         *
         * @param params
         */
        void onDialogInput(Object... params);
    }

}
