package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort;

import java.util.Comparator;
import java.util.List;

public class RowHeaderForCellSortComparator implements Comparator<List<ISortableModel>> {
    private List<List<ISortableModel>> mColumnList;
    private List<ISortableModel> mReferenceList;
    private RowHeaderSortComparator mRowHeaderSortComparator;
    private SortState mSortState;

    public RowHeaderForCellSortComparator(List<ISortableModel> list, List<List<ISortableModel>> list2, SortState sortState) {
        this.mReferenceList = list;
        this.mColumnList = list2;
        this.mSortState = sortState;
        this.mRowHeaderSortComparator = new RowHeaderSortComparator(sortState);
    }

    public int compare(List<ISortableModel> list, List<ISortableModel> list2) {
        Object content = this.mReferenceList.get(this.mColumnList.indexOf(list)).getContent();
        Object content2 = this.mReferenceList.get(this.mColumnList.indexOf(list2)).getContent();
        if (this.mSortState == SortState.DESCENDING) {
            return this.mRowHeaderSortComparator.compareContent(content2, content);
        }
        return this.mRowHeaderSortComparator.compareContent(content, content2);
    }
}
