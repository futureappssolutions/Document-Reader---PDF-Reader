package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort;

import androidx.core.util.ObjectsCompat;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class ColumnSortCallback extends DiffUtil.Callback {
    private int mColumnPosition;
    private List<List<ISortableModel>> mNewCellItems;
    private List<List<ISortableModel>> mOldCellItems;

    public ColumnSortCallback(List<List<ISortableModel>> list, List<List<ISortableModel>> list2, int i) {
        this.mOldCellItems = list;
        this.mNewCellItems = list2;
        this.mColumnPosition = i;
    }

    public int getOldListSize() {
        return this.mOldCellItems.size();
    }

    public int getNewListSize() {
        return this.mNewCellItems.size();
    }

    public boolean areItemsTheSame(int i, int i2) {
        if (this.mOldCellItems.size() <= i || this.mNewCellItems.size() <= i2 || this.mOldCellItems.get(i).size() <= this.mColumnPosition || this.mNewCellItems.get(i2).size() <= this.mColumnPosition) {
            return false;
        }
        return ((ISortableModel) this.mOldCellItems.get(i).get(this.mColumnPosition)).getId().equals(((ISortableModel) this.mNewCellItems.get(i2).get(this.mColumnPosition)).getId());
    }

    public boolean areContentsTheSame(int i, int i2) {
        if (this.mOldCellItems.size() <= i || this.mNewCellItems.size() <= i2 || this.mOldCellItems.get(i).size() <= this.mColumnPosition || this.mNewCellItems.get(i2).size() <= this.mColumnPosition) {
            return false;
        }
        return ObjectsCompat.equals(((ISortableModel) this.mOldCellItems.get(i).get(this.mColumnPosition)).getContent(), ((ISortableModel) this.mNewCellItems.get(i2).get(this.mColumnPosition)).getContent());
    }
}
