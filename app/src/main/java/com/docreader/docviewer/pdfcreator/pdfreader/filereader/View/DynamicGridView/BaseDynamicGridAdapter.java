package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.DynamicGridView;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDynamicGridAdapter extends AbstractDynamicGridAdapter {
    private int mColumnCount;
    public Context mContext;
    private final ArrayList<Object> mItems = new ArrayList<>();

    public boolean canReorder(int i) {
        return true;
    }

    protected BaseDynamicGridAdapter(Context context, int i) {
        mContext = context;
        mColumnCount = i;
    }

    public BaseDynamicGridAdapter(Context context, List<?> list, int i) {
        mContext = context;
        mColumnCount = i;
        init(list);
    }

    private void init(List<?> list) {
        addAllStableId(list);
        mItems.addAll(list);
    }

    public void set(List<?> list) {
        clear();
        init(list);
        notifyDataSetChanged();
    }

    public void clear() {
        clearStableIdMap();
        mItems.clear();
        notifyDataSetChanged();
    }

    public void add(Object obj) {
        addStableId(obj);
        mItems.add(obj);
        notifyDataSetChanged();
    }

    public void add(int i, Object obj) {
        addStableId(obj);
        mItems.add(i, obj);
        notifyDataSetChanged();
    }

    public void add(List<?> list) {
        addAllStableId(list);
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(Object obj) {
        mItems.remove(obj);
        removeStableID(obj);
        notifyDataSetChanged();
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int i) {
        return mItems.get(i);
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    public void setColumnCount(int i) {
        mColumnCount = i;
        notifyDataSetChanged();
    }

    public void reorderItems(int i, int i2) {
        if (i2 < getCount()) {
            DynamicGridUtils.reorder(mItems, i, i2);
            notifyDataSetChanged();
        }
    }

    public List<Object> getItems() {
        return mItems;
    }


    public Context getContext() {
        return mContext;
    }
}
