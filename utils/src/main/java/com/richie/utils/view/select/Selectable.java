package com.richie.utils.view.select;

/**
 * @author Richie on 2017.10.15
 */
public interface Selectable {
    /**
     * 是否选中
     *
     * @return
     */
    boolean isSelected();

    /**
     * 设置选中
     *
     * @param selected
     */
    void setSelected(boolean selected);

    /**
     * 设置选中监听器
     *
     * @param onViewSelectListener
     */
    void setOnSelectedListener(OnViewSelectListener onViewSelectListener);

    interface OnViewSelectListener {
        /**
         * 当前 View 被选中
         *
         * @param id
         */
        void onViewSelected(int id);
    }
}
