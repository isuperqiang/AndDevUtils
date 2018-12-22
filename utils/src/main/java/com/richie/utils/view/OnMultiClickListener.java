package com.richie.utils.view;

import android.view.View;

import com.richie.utils.ViewUtils;

/**
 * @author Richie on 2018.11.09
 * 防止控件快速点击
 */
public abstract class OnMultiClickListener implements View.OnClickListener {

    private long mLastClickTime;
    private int mViewId = View.NO_ID;

    /**
     * 处理后的点击事件
     *
     * @param v
     */
    protected abstract void onMultiClick(View v);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        int viewId = v.getId();
        if (mViewId == viewId) {
            if ((curClickTime - mLastClickTime) >= ViewUtils.MIN_CLICK_DELAY_TIME) {
                mLastClickTime = curClickTime;
                onMultiClick(v);
            }
        } else {
            mViewId = viewId;
            mLastClickTime = curClickTime;
            onMultiClick(v);
        }
    }
}
