package com.richie.utils.view.recycler;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Richie on 2017.10.20
 * RecyclerView 垂直边距装饰器
 */
public class VHSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int verticalSpace;
    private int horizontalSpace;

    public VHSpaceItemDecoration(int verticalSpace, int horizontalSpace) {
        this.verticalSpace = verticalSpace;
        this.horizontalSpace = horizontalSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        outRect.top = verticalSpace;
        outRect.left = horizontalSpace;
        outRect.right = horizontalSpace;
        outRect.bottom = verticalSpace;
    }

}
