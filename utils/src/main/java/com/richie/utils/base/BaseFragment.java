package com.richie.utils.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * 所有普通 Fragment 的父类
 *
 * @author Richie on 2018.12.30
 */
public abstract class BaseFragment extends Fragment {
    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutResId = getLayoutResID();
        View rootView = inflater.inflate(layoutResId, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

