package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter;

import java.util.List;

public abstract class FilterChangedListener<T> {
    public void onFilterChanged(List<List<T>> list, List<T> list2) {
    }

    public void onFilterCleared(List<List<T>> list, List<T> list2) {
    }
}
