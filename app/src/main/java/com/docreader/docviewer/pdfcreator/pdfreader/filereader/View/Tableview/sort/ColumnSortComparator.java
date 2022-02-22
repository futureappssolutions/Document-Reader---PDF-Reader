package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort;

import java.util.Comparator;
import java.util.List;

public class ColumnSortComparator extends AbstractSortComparator implements Comparator<List<ISortableModel>> {
    private int mXPosition;

    public ColumnSortComparator(int i, SortState sortState) {
        this.mXPosition = i;
        this.mSortState = sortState;
    }

    public int compare(List<ISortableModel> list, List<ISortableModel> list2) {
        Object content = list.get(this.mXPosition).getContent();
        Object content2 = list2.get(this.mXPosition).getContent();
        if (this.mSortState == SortState.DESCENDING) {
            return compareContent(content2, content);
        }
        return compareContent(content, content2);
    }
}
