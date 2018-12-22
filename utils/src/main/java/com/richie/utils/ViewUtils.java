package com.richie.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * @author Richie on 2017.10.12
 */
public class ViewUtils {

    private ViewUtils() {
    }

    // 两次点击按钮之间的点击间隔不能少于500毫秒
    public static final int MIN_CLICK_DELAY_TIME = 500;
    private static long sLastClickTime;

    /**
     * 判断按钮是否频繁快速点击
     *
     * @return
     */
    public static boolean isNormalClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - sLastClickTime) >= MIN_CLICK_DELAY_TIME) {
            sLastClickTime = curClickTime;
            flag = true;
        }
        return flag;
    }

    /**
     * 初始化 RecyclerView
     *
     * @param recyclerView
     * @param layoutManager
     * @param itemDecoration
     */
    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager,
                                        RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setHasFixedSize(true);
        if (itemDecoration != null) {
            recyclerView.addItemDecoration(itemDecoration);
        }
    }

    /**
     * 创建进度条对话框
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog createLoadingDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        Window window = progressDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDialog;
    }

    /**
     * 展示对话框
     *
     * @param dialog
     */
    public static void showDialog(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 隐藏对话框
     *
     * @param dialog
     */
    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 设置 View 选中，呈高亮状态
     *
     * @param view
     * @param selected
     */
    public static void setViewSelected(View view, boolean selected) {
        if (selected) {
            if (!view.isSelected()) {
                view.setSelected(true);
            }
        } else {
            if (view.isSelected()) {
                view.setSelected(false);
            }
        }
    }

    /**
     * 转换 view 的选中状态
     *
     * @param view
     */
    public static void toggleSelection(View view) {
        view.setSelected(!view.isSelected());
    }

    /**
     * 设置 view 的可见性
     *
     * @param view
     * @param visible
     */
    public static void setViewVisible(View view, boolean visible) {
        if (view == null) {
            return;
        }
        int visibility = view.getVisibility();
        if (visible) {
            if (visibility != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (visibility != View.INVISIBLE) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 转换 view 的可见性
     *
     * @param view
     */
    public static void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置 view 的可见性为 Gone
     *
     * @param view
     */
    public static void setViewGone(View view) {
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置选择框的选中状态
     *
     * @param view
     * @param checked
     */
    public static void setViewChecked(CompoundButton view, boolean checked) {
        boolean isChecked = view.isChecked();
        if (checked) {
            if (!isChecked) {
                view.setChecked(true);
            }
        } else {
            if (isChecked) {
                view.setChecked(false);
            }
        }
    }

    /**
     * 设置 view 的可用性
     *
     * @param view
     * @param enabled
     */
    public static void setEnabled(View view, boolean enabled) {
        boolean b = view.isEnabled();
        if (b) {
            if (!enabled) {
                view.setEnabled(false);
            }
        } else {
            if (enabled) {
                view.setEnabled(true);
            }
        }
    }

    /**
     * 设置 TextView 的颜色
     *
     * @param textView
     * @param textColor
     */
    public static void setTextColor(TextView textView, int textColor) {
        if (textView.getCurrentTextColor() != textColor) {
            textView.setTextColor(textColor);
        }
    }

    /**
     * 设置 TextView 的字体颜色
     *
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, CharSequence text) {
        if (!TextUtils.equals(text, textView.getText())) {
            textView.setText(text);
        }
    }

    /**
     * 设置 TextView 的文本
     *
     * @param textView
     * @param resId
     */
    public static void setText(TextView textView, @StringRes int resId) {
        String string = textView.getContext().getString(resId);
        setText(textView, string);
    }

    /**
     * 设置 TextView 空白文本
     *
     * @param textView
     */
    public static void setEmptyText(TextView textView) {
        setText(textView, "");
    }

    /**
     * 获取 TextView 上的字符串，并去掉首尾空格
     * 注意：getText() 返回值不可能为 null，空白时会是空字符串
     *
     * @param textView
     * @return
     */
    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * View 获取焦点
     *
     * @param view
     */
    public static void requestFocus(View view) {
        view.requestFocus();
        view.requestFocusFromTouch();
    }

}
