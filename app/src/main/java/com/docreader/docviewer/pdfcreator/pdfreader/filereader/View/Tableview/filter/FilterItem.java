package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter;

public class FilterItem {
    private int column;
    private String filter;
    private FilterType filterType;

    public FilterItem(FilterType filterType2, int i, String str) {
        this.filterType = filterType2;
        this.column = i;
        this.filter = str;
    }

    public FilterType getFilterType() {
        return this.filterType;
    }

    public String getFilter() {
        return this.filter;
    }

    public int getColumn() {
        return this.column;
    }
}
