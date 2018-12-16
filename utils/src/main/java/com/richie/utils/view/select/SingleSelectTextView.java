package com.richie.utils.view.select;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.richie.utils.R;

/**
 * @author Richie on 2017.10.15
 * 单选 TextView
 */
@SuppressLint("AppCompatCustomView")
public class SingleSelectTextView extends TextView implements Selectable {
    private OnViewSelectListener mOnViewSelectListener;

    public SingleSelectTextView(Context context) {
        super(context);
    }

    public SingleSelectTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SingleSelectTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SingleSelectView);
        boolean selected = typedArray.getBoolean(R.styleable.SingleSelectView_selected, false);
        setSelected(selected);
        typedArray.recycle();
    }

    @Override
    public void setOnSelectedListener(OnViewSelectListener onViewSelectListener) {
        mOnViewSelectListener = onViewSelectListener;
    }

    @Override
    public boolean performClick() {
        if (mOnViewSelectListener != null) {
            mOnViewSelectListener.onViewSelected(getId());
        }
        return super.performClick();
    }

}
