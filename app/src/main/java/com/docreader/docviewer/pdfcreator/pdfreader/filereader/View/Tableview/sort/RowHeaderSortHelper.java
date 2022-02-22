package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort;

public class RowHeaderSortHelper {
    private SortState mSortState;

    private void sortingStatusChanged(SortState sortState) {
        this.mSortState = sortState;
    }

    public void setSortingStatus(SortState sortState) {
        this.mSortState = sortState;
        sortingStatusChanged(sortState);
    }

    public void clearSortingStatus() {
        this.mSortState = SortState.UNSORTED;
    }

    public boolean isSorting() {
        return this.mSortState != SortState.UNSORTED;
    }

    public SortState getSortingStatus() {
        return this.mSortState;
    }
}
