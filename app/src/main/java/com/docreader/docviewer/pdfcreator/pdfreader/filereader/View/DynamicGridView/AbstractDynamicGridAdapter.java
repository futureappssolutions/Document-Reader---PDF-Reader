package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.DynamicGridView;

import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractDynamicGridAdapter extends BaseAdapter implements DynamicGridAdapterInterface {
    public static final int INVALID_ID = -1;
    private final HashMap<Object, Integer> mIdMap = new HashMap<>();
    private int nextStableId = 0;

    public final boolean hasStableIds() {
        return true;
    }

    public void addStableId(Object obj) {
        int i = nextStableId;
        nextStableId = i + 1;
        mIdMap.put(obj, i);
    }

    public void addAllStableId(List<?> list) {
        for (Object addStableId : list) {
            addStableId(addStableId);
        }
    }

    public final long getItemId(int i) {
        if (i < 0 || i >= mIdMap.size()) {
            return -1;
        }
        return mIdMap.get(getItem(i));
    }

    public void clearStableIdMap() {
        mIdMap.clear();
    }

    public void removeStableID(Object obj) {
        mIdMap.remove(obj);
    }
}
