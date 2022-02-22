package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;

public class ColumnWidthHandler {
    private ITableView mTableView;

    public ColumnWidthHandler(ITableView iTableView) {
        this.mTableView = iTableView;
    }

    public void setColumnWidth(int i, int i2) {
        this.mTableView.getColumnHeaderLayoutManager().setCacheWidth(i, i2);
        this.mTableView.getCellLayoutManager().setCacheWidth(i, i2);
    }
}
