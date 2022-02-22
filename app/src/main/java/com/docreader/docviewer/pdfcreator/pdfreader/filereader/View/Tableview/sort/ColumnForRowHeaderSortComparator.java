package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort;

import java.util.Comparator;
import java.util.List;

public class ColumnForRowHeaderSortComparator implements Comparator<ISortableModel> {
    private int column;
    private ColumnSortComparator mColumnSortComparator;
    private List<List<ISortableModel>> mReferenceList;
    private List<ISortableModel> mRowHeaderList;
    private SortState mSortState;

    public ColumnForRowHeaderSortComparator(List<ISortableModel> list, List<List<ISortableModel>> list2, int i, SortState sortState) {
        this.mRowHeaderList = list;
        this.mReferenceList = list2;
        this.column = i;
        this.mSortState = sortState;
        this.mColumnSortComparator = new ColumnSortComparator(i, sortState);
    }

    public int compare(ISortableModel iSortableModel, ISortableModel iSortableModel2) {
        Object content = ((ISortableModel) this.mReferenceList.get(this.mRowHeaderList.indexOf(iSortableModel)).get(this.column)).getContent();
        Object content2 = ((ISortableModel) this.mReferenceList.get(this.mRowHeaderList.indexOf(iSortableModel2)).get(this.column)).getContent();
        if (this.mSortState == SortState.DESCENDING) {
            return this.mColumnSortComparator.compareContent(content2, content);
        }
        return this.mColumnSortComparator.compareContent(content, content2);
    }
}
