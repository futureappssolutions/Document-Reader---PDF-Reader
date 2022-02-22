package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort;

import java.util.Comparator;

public class RowHeaderSortComparator extends AbstractSortComparator implements Comparator<ISortableModel> {
    public RowHeaderSortComparator(SortState sortState) {
        this.mSortState = sortState;
    }

    public int compare(ISortableModel iSortableModel, ISortableModel iSortableModel2) {
        if (this.mSortState == SortState.DESCENDING) {
            return compareContent(iSortableModel2.getContent(), iSortableModel.getContent());
        }
        return compareContent(iSortableModel.getContent(), iSortableModel2.getContent());
    }
}
