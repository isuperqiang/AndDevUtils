package com.richie.utils.view;

import android.view.View;
import android.widget.AdapterView;

/**
 * 屏蔽 Spinner 首次触发的监听器
 *
 * @author Richie on 2018.02.07
 */
public abstract class NoFirstTriggerSpinnerListener implements AdapterView.OnItemSelectedListener {
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
     * 屏蔽首次触发的选中事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected abstract void onSpItemSelected(AdapterView<?> parent, View view, int position, long id);
}
