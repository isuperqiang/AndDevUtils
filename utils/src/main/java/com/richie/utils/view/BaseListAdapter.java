package com.richie.utils.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * ListView 通用适配器
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    private List<T> mData;
    private int mLayoutRes;

    public BaseListAdapter(@NonNull List<T> mData, @LayoutRes int mLayoutRes) {
        this.mData = mData;
        this.mLayoutRes = mLayoutRes;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.bindViewHolder(parent.getContext(), convertView, parent, mLayoutRes);
        bindViewHolder(holder, getItem(position));
        return holder.getConvertView();
    }

    /**
     * 绑定 ViewHolder
     *
     * @param holder
     * @param obj
     */
    protected abstract void bindViewHolder(ViewHolder holder, T obj);

    public void add(T data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    public void add(int position, T data) {
        mData.add(position, data);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        if (mData.contains(data)) {
            mData.remove(data);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (position < getCount()) {
            mData.remove(position);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }


    protected static class ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        private ViewHolder(Context context, ViewGroup parent, int layoutRes) {
            mViews = new SparseArray<>();
            mConvertView = LayoutInflater.from(context).inflate(layoutRes, parent, false);
            mConvertView.setTag(this);
        }

        static ViewHolder bindViewHolder(Context context, View convertView, ViewGroup parent, int layoutRes) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder(context, parent, layoutRes);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            return holder;
        }

        @SuppressWarnings("unchecked")
        protected <T extends View> T getViewById(int id) {
            T view = (T) mViews.get(id);
            if (view == null) {
                view = (T) mConvertView.findViewById(id);
                mViews.put(id, view);
            }
            return view;
        }

        /**
         * 获取当前条目
         */
        protected View getConvertView() {
            return mConvertView;
        }

        /**
         * 设置文字
         */
        public ViewHolder setText(int id, CharSequence text) {
            View view = getViewById(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
            return this;
        }

        /**
         * 设置图片
         */
        public ViewHolder setImageResource(int id, int drawableRes) {
            View view = getViewById(id);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(drawableRes);
            } else {
                view.setBackgroundResource(drawableRes);
            }
            return this;
        }

        /**
         * 设置点击监听
         */
        public ViewHolder setOnClickListener(int id, View.OnClickListener listener) {
            getViewById(id).setOnClickListener(listener);
            return this;
        }

        /**
         * 设置可见
         */
        public ViewHolder setVisibility(int id, int visible) {
            getViewById(id).setVisibility(visible);
            return this;
        }

        /**
         * 设置标签
         */
        public ViewHolder setTag(int id, Object obj) {
            getViewById(id).setTag(obj);
            return this;
        }

        /**
         * 设置标签
         */
        public ViewHolder setTag(int id, int key, Object obj) {
            getViewById(id).setTag(key, obj);
            return this;
        }

        public ViewHolder setBackground(int id, int color) {
            View view = getViewById(id);
            view.setBackgroundColor(color);
            return this;
        }

        //其他方法可自行扩展
    }
}

