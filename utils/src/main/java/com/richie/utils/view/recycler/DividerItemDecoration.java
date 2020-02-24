package com.richie.utils.view.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView 分割线装饰器
 *
 * @author Richie on 2018.01.10
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    /**
     * 显示在底部
     */
    private boolean mDrawBottom;
    /**
     * 边距
     */
    private int mMargin;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 方向
     */
    private int mOrientation;

    public DividerItemDecoration(Context context, int orientation, int margin, int color, boolean drawBottom) {
        this.mDrawBottom = drawBottom;
        this.mMargin = margin;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(color));
        mPaint.setStyle(Paint.Style.FILL);
    }

    public DividerItemDecoration(Context context, int orientation, int colorId, boolean drawBottom) {
        this(context, orientation, 0, colorId, drawBottom);
    }

    public DividerItemDecoration(Context context, int orientation, int colorId) {
        this(context, orientation, 0, colorId, true);
    }

    public DividerItemDecoration(Context context, int orientation) {
        this(context, orientation, android.R.color.darker_gray);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + mMargin;
        final int right = parent.getWidth() - parent.getPaddingRight() - mMargin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (!mDrawBottom && i == childCount - 1) {
                continue;
            }

            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            c.drawRect(left, top, right, bottom, mPaint);
            // mDivider.setBounds(left, top, right, bottom);
            // mDivider.draw(c);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop() + mMargin;
        final int bottom = parent.getHeight() - parent.getPaddingBottom() - mMargin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (!mDrawBottom && i == childCount - 1) {
                continue;
            }

            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            // mDivider.setBounds(left, top, right, bottom);
            // mDivider.draw(c);
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

}
