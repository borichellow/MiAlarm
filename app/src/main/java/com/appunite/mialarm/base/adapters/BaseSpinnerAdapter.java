package com.appunite.mialarm.base.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.appunite.mialarm.R;
import com.google.common.collect.ImmutableList;

import java.util.List;

public abstract class BaseSpinnerAdapter<T> extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private List<T> items = ImmutableList.of();

    public BaseSpinnerAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindData(List<T> newItems) {
        items = ImmutableList.<T>builder()
                .addAll(newItems)
                .build();
        notifyDataSetChanged();
    }

    protected abstract String getDisplayName(int position);

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        if (position < 0) {
            return null;
        } else {
            return items.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        return getLayoutView(position, view, parent, android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        return getLayoutView(position, view, parent, android.R.layout.simple_spinner_item);
    }

    public View getLayoutView(int position, View view, ViewGroup parent, @LayoutRes int layoutResId) {
        if (view == null) {
            view = layoutInflater.inflate(layoutResId, parent, false);
        }

        setData(view, position);

        return view;
    }

    public void setData(View view, int position) {
        final String name = getDisplayName(position);
        final TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(name);
    }

    public int getPosition(String option) {
        for (int i = 0; i < items.size(); i++) {
            if (getDisplayName(i).equalsIgnoreCase(option)) {
                return i;
            }
        }

        return 0;
    }

}
