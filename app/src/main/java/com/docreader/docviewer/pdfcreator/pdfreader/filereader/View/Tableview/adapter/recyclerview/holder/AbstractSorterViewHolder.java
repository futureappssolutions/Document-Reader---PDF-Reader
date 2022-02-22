package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder;

import android.view.View;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.SortState;

public class AbstractSorterViewHolder extends AbstractViewHolder {
    private SortState mSortState = SortState.UNSORTED;

    public AbstractSorterViewHolder(View view) {
        super(view);
    }

    public void onSortingStatusChanged(SortState sortState) {
        this.mSortState = sortState;
    }

    public SortState getSortState() {
        return this.mSortState;
    }
}
