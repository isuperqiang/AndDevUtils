package com.richie.utils.view.select;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.richie.utils.common.ViewUtils;

/**
 * @author Richie on 2017.10.15
 * 类似 RadioGroup 的单选布局，子 View 选中时高亮
 */
public class SingleSelectLayout extends RelativeLayout {
    public static final int NO_SELECTED = -1;
    private int mSelectedViewId = NO_SELECTED;
    private ChildViewSelectClickListener mSelectClickListener = new ChildViewSelectClickListener();

    public SingleSelectLayout(Context context) {
        super(context);
    }

    public SingleSelectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleSelectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState savedState = new SavedState(parcelable);
        savedState.state = mSelectedViewId;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        int selectedViewId = savedState.state;
        setChildSelected(selectedViewId);
    }

    /**
     * 设置子 View 的选中状态，
     *
     * @param viewId viewId
     */
    public void setChildSelected(int viewId) {
        if (mSelectedViewId == viewId) {
            return;
        }
        if (mSelectedViewId != NO_SELECTED) {
            ViewUtils.setViewSelected(findViewById(mSelectedViewId), false);
        }
        ViewUtils.setViewSelected(findViewById(viewId), true);
        mSelectedViewId = viewId;
    }

    /**
     * 获取选中的 View
     *
     * @return
     */
    public int getSelectedViewId() {
        return mSelectedViewId;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof Selectable) {
            ((Selectable) child).setOnSelectedListener(mSelectClickListener);
            child.setOnClickListener(mSelectClickListener);
            if (child.isSelected()) {
                mSelectedViewId = child.getId();
            }
        }
        super.addView(child, index, params);
    }

    static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int state;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            state = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(state);
        }
    }

    private class ChildViewSelectClickListener implements Selectable.OnViewSelectListener, OnClickListener {

        @Override
        public void onViewSelected(int id) {
            setChildSelected(id);
        }

        @Override
        public void onClick(View v) {
            setChildSelected(v.getId());
        }
    }

}
