package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class RowHeaderSortCallback extends DiffUtil.Callback {
    private List<ISortableModel> mNewCellItems;
    private List<ISortableModel> mOldCellItems;

    public RowHeaderSortCallback(List<ISortableModel> list, List<ISortableModel> list2) {
        this.mOldCellItems = list;
        this.mNewCellItems = list2;
    }

    public int getOldListSize() {
        return this.mOldCellItems.size();
    }

    public int getNewListSize() {
        return this.mNewCellItems.size();
    }

    public boolean areItemsTheSame(int i, int i2) {
        if (this.mOldCellItems.size() <= i || this.mNewCellItems.size() <= i2) {
            return false;
        }
        return this.mOldCellItems.get(i).getId().equals(this.mNewCellItems.get(i2).getId());
    }

    public boolean areContentsTheSame(int i, int i2) {
        if (this.mOldCellItems.size() <= i || this.mNewCellItems.size() <= i2) {
            return false;
        }
        return this.mOldCellItems.get(i).getContent().equals(this.mNewCellItems.get(i2).getContent());
    }
}
