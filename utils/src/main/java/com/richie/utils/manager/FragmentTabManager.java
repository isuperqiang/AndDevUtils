package com.richie.utils.manager;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Richie on 2018.12.29
 */
public class FragmentTabManager {
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private Map<String, Fragment> mFragmentMap;
    private String mShownTag;

    public FragmentTabManager(@NonNull FragmentManager fragmentManager, @IdRes int containerId) {
        mFragmentManager = fragmentManager;
        mContainerId = containerId;
        mFragmentMap = new HashMap<>(8);
    }

    /**
     * 用于 Activity savedInstanceState 不为空时，恢复 Fragment
     *
     * @param fragmentList
     * @param shownTag
     */
    public void restoreFragment(@NonNull List<Fragment> fragmentList, @NonNull String shownTag) {
        for (Fragment fragment : fragmentList) {
            mFragmentMap.put(fragment.getClass().getName(), fragment);
        }
        mShownTag = shownTag;
    }

    /**
     * 添加 fragment
     *
     * @param fragment
     */
    public void addUniqueFragment(@NonNull Fragment fragment) {
        String tag = fragment.getClass().getName();
        if (TextUtils.isEmpty(tag)) {
            tag = fragment.getClass().getName();
        }
        if (mFragmentMap.containsKey(tag)) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment hideFragment = mFragmentMap.get(mShownTag);
        if (hideFragment != null) {
            transaction.hide(hideFragment);
        }
        transaction.add(mContainerId, fragment, tag)
                .commit();
        mFragmentMap.put(tag, fragment);
        mShownTag = tag;
    }

    /**
     * 显示 fragment
     *
     * @param fragment
     */
    public void showUniqueFragment(@NonNull Fragment fragment) {
        String tag = fragment.getClass().getName();
        if (TextUtils.equals(tag, mShownTag)) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment hideFragment = mFragmentMap.get(mShownTag);
        if (hideFragment != null) {
            transaction.hide(hideFragment);
        }
        Fragment showFragment = mFragmentMap.get(tag);
        if (showFragment != null) {
            transaction.show(showFragment);
        }
        transaction.commit();
        mShownTag = tag;
    }

    /**
     * 通过 tag 获取 fragment
     *
     * @param tag
     * @return
     */
    public Fragment getFragmentByTag(@NonNull String tag) {
        return mFragmentMap.get(tag);
    }

    /**
     * 获取正在显示的 fragment
     *
     * @return
     */
    public Fragment getShownFragment() {
        return mFragmentMap.get(mShownTag);
    }

    /**
     * 获取正在显示的 fragment 的 tag
     *
     * @return
     */
    public String getShownTag() {
        return mShownTag;
    }
}
