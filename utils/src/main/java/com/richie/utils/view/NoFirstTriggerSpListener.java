package com.richie.utils.view;

import android.view.View;
import android.widget.AdapterView;

/**
 * @author Richie on 2018.02.07
 * 屏蔽 spinner 首次触发的监听器
 */
public abstract class NoFirstTriggerSpListener implements AdapterView.OnItemSelectedListener {
    /**
     * 判定 spinner 的最小触发时间
     */
    private static final int SPINNER_MIN_TRIGGER_TIME = 300;
    private long mFirstTime = System.currentTimeMillis();

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (System.currentTimeMillis() - mFirstTime <= SPINNER_MIN_TRIGGER_TIME) {
            return;
        }
        onSpItemSelected(parent, view, position, id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 排除首次触发的选中事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected abstract void onSpItemSelected(AdapterView<?> parent, View view, int position, long id);
}
