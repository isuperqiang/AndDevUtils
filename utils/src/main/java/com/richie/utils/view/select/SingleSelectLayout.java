package com.richie.utils.view.select;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.richie.utils.ViewUtils;

/**
 * @author Richie on 2017.10.15
 * 类似 RadioGroup 的单选布局，子 View 选中时高亮
 */
public class SingleSelectLayout extends RelativeLayout {
    // NOT SELECTED
    public static final int NOT_SELECTED = -1;
    private Selectable.OnViewSelectListener mOnViewSelectListener;
    private int mSelectedViewId;

    public SingleSelectLayout(Context context) {
        super(context);
        init();
    }

    public SingleSelectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleSelectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mOnViewSelectListener = new ChildViewSelectedListener();
    }

    /**
     * 设置子 View 的选中状态，
     *
     * @param viewId viewId
     */
    public void setChildSelected(int viewId) {
        mSelectedViewId = viewId;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view.getId() == viewId) {
                ViewUtils.setViewSelected(view, true);
            } else {
                ViewUtils.setViewSelected(view, false);
            }
        }
    }

    public void setChildEnable(int viewId, boolean enable) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view.getId() == viewId) {
                ViewUtils.setEnabled(view, enable);
                //} else {
                //    ViewUtils.setEnabled(view, !enable);
            }
        }
    }

    /**
     * 获取选中的 view ID
     *
     * @return
     */
    public int getSelectedViewId() {
        return mSelectedViewId;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof Selectable) {
            ((Selectable) child).setOnSelectedListener(mOnViewSelectListener);
        }
        super.addView(child, index, params);
    }

    private class ChildViewSelectedListener implements Selectable.OnViewSelectListener {
        @Override
        public void onViewSelected(int id) {
            setChildSelected(id);
        }
    }

}
