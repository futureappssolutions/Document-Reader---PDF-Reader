package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRecyclerViewAdapter<T> extends RecyclerView.Adapter<AbstractViewHolder> {
    protected Context mContext;
    protected List<T> mItemList;

    public int getItemViewType(int i) {
        return 1;
    }

    public AbstractRecyclerViewAdapter(Context context) {
        this(context, (List) null);
    }

    public AbstractRecyclerViewAdapter(Context context, List<T> list) {
        this.mContext = context;
        if (list == null) {
            this.mItemList = new ArrayList();
        } else {
            setItems(list);
        }
    }

    public int getItemCount() {
        return this.mItemList.size();
    }

    public List<T> getItems() {
        return this.mItemList;
    }

    public void setItems(List<T> list) {
        this.mItemList = new ArrayList(list);
        notifyDataSetChanged();
    }

    public void setItems(List<T> list, boolean z) {
        this.mItemList = new ArrayList(list);
        if (z) {
            notifyDataSetChanged();
        }
    }

    public T getItem(int i) {
        if (this.mItemList.isEmpty() || i < 0 || i >= this.mItemList.size()) {
            return null;
        }
        return this.mItemList.get(i);
    }

    public void deleteItem(int i) {
        if (i != -1) {
            this.mItemList.remove(i);
            notifyItemRemoved(i);
        }
    }

    public void deleteItemRange(int i, int i2) {
        for (int i3 = (i + i2) - 1; i3 >= i; i3--) {
            if (i3 != -1) {
                this.mItemList.remove(i3);
            }
        }
        notifyItemRangeRemoved(i, i2);
    }

    public void addItem(int i, T t) {
        if (i != -1 && t != null) {
            this.mItemList.add(i, t);
            notifyItemInserted(i);
        }
    }

    public void addItemRange(int i, List<T> list) {
        if (list != null) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                this.mItemList.add(i2 + i, list.get(i2));
            }
            notifyItemRangeInserted(i, list.size());
        }
    }

    public void changeItem(int i, T t) {
        if (i != -1 && t != null) {
            this.mItemList.set(i, t);
            notifyItemChanged(i);
        }
    }

    public void changeItemRange(int i, List<T> list) {
        if (list != null && this.mItemList.size() > list.size() + i) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                this.mItemList.set(i2 + i, list.get(i2));
            }
            notifyItemRangeChanged(i, list.size());
        }
    }
}
