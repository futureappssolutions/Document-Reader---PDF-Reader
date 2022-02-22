package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter;

import java.util.List;

public abstract class AdapterDataSetChangedListener<CH, RH, C> {
    public void onCellItemsChanged(List<List<C>> list) {
    }

    public void onColumnHeaderItemsChanged(List<CH> list) {
    }

    public void onDataSetChanged(List<CH> list, List<RH> list2, List<List<C>> list3) {
    }

    public void onRowHeaderItemsChanged(List<RH> list) {
    }
}
