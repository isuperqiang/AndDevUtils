package com.richie.utils.view.select;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.richie.utils.R;

/**
 * @author Richie on 2019.01.06
 */
public class SelectableViewDispatcher {
    private View mView;
    private Selectable.OnViewSelectListener mOnViewSelectListener;

    SelectableViewDispatcher(View view) {
        mView = view;
    }

    public void setOnViewSelectListener(Selectable.OnViewSelectListener onViewSelectListener) {
        mOnViewSelectListener = onViewSelectListener;
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SingleSelectView);
        boolean selected = typedArray.getBoolean(R.styleable.SingleSelectView_selected, false);
        mView.setSelected(selected);
        typedArray.recycle();
    }

    public void onViewSelected(int id) {
        if (mOnViewSelectListener != null) {
            mOnViewSelectListener.onViewSelected(id);
        }
    }
}
