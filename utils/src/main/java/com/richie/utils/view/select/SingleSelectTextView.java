package com.richie.utils.view.select;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.richie.utils.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 单选 TextView
 *
 * @author Richie on 2017.10.15
 */
public class SingleSelectTextView extends AppCompatTextView implements Selectable {
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

    private OnClickListener getOnClickListener() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Class listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
        Method getListenerInfoM = View.class.getDeclaredMethod("getListenerInfo");
        getListenerInfoM.setAccessible(true);
        Object mListenerInfo = getListenerInfoM.invoke(this);
        Field onClickListenerF = listenerInfoClz.getDeclaredField("mOnClickListener");
        onClickListenerF.setAccessible(true);
        return (OnClickListener) onClickListenerF.get(mListenerInfo);
    }

    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {
        if (l != null) {
            try {
                final OnClickListener onClickListener = getOnClickListener();
                if (onClickListener != null) {
                    super.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickListener.onClick(v);
                            l.onClick(v);
                        }
                    });
                } else {
                    super.setOnClickListener(l);
                }
            } catch (Exception e) {
                super.setOnClickListener(l);
            }
        }
    }

    @Override
    public boolean performClick() {
        if (mOnViewSelectListener != null) {
            mOnViewSelectListener.onViewSelected(getId());
        }
        return super.performClick();
    }

}
