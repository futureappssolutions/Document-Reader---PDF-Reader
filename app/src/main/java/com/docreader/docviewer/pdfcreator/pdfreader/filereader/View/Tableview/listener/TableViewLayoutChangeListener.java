package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener;

import android.view.View;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.CellLayoutManager;

public class TableViewLayoutChangeListener implements View.OnLayoutChangeListener {
    private CellLayoutManager mCellLayoutManager;
    private CellRecyclerView mCellRecyclerView;
    private CellRecyclerView mColumnHeaderRecyclerView;

    public TableViewLayoutChangeListener(ITableView iTableView) {
        this.mCellRecyclerView = iTableView.getCellRecyclerView();
        this.mColumnHeaderRecyclerView = iTableView.getColumnHeaderRecyclerView();
        this.mCellLayoutManager = iTableView.getCellLayoutManager();
    }

    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (view.isShown() && i3 - i != i7 - i5) {
            if (this.mColumnHeaderRecyclerView.getWidth() > this.mCellRecyclerView.getWidth()) {
                this.mCellLayoutManager.remeasureAllChild();
            } else if (this.mCellRecyclerView.getWidth() > this.mColumnHeaderRecyclerView.getWidth()) {
                this.mColumnHeaderRecyclerView.getLayoutParams().width = -2;
                this.mColumnHeaderRecyclerView.requestLayout();
            }
        }
    }
}
