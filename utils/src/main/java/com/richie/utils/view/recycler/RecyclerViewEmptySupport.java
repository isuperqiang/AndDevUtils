package com.richie.utils.view.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 支持空白视图的 RecyclerView
 *
 * @author Richie on 2018.04.09
 */
public class RecyclerViewEmptySupport extends RecyclerView {
    private View mEmptyView;
    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            onDataSetChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onDataSetChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            onDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            onDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onDataSetChanged();
        }

        private void onDataSetChanged() {
            Adapter adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() <= 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置空白视图，view 需要在 Rv 的父布局中
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
        }
        mAdapterDataObserver.onChanged();
    }
}
