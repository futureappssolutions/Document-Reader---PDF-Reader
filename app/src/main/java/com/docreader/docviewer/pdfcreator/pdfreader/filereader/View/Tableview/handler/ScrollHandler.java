package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.CellLayoutManager;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.ColumnHeaderLayoutManager;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.ColumnLayoutManager;

public class ScrollHandler {
    private CellLayoutManager mCellLayoutManager;
    private ColumnHeaderLayoutManager mColumnHeaderLayoutManager;
    private LinearLayoutManager mRowHeaderLayoutManager;
    private ITableView mTableView;

    public ScrollHandler(ITableView iTableView) {
        this.mTableView = iTableView;
        this.mCellLayoutManager = iTableView.getCellLayoutManager();
        this.mRowHeaderLayoutManager = iTableView.getRowHeaderLayoutManager();
        this.mColumnHeaderLayoutManager = iTableView.getColumnHeaderLayoutManager();
    }

    public void scrollToColumnPosition(int i) {
        if (!((View) this.mTableView).isShown()) {
            this.mTableView.getHorizontalRecyclerViewListener().setScrollPosition(i);
        }
        scrollColumnHeader(i, 0);
        scrollCellHorizontally(i, 0);
    }

    public void scrollToColumnPosition(int i, int i2) {
        if (!((View) this.mTableView).isShown()) {
            this.mTableView.getHorizontalRecyclerViewListener().setScrollPosition(i);
            this.mTableView.getHorizontalRecyclerViewListener().setScrollPositionOffset(i2);
        }
        scrollColumnHeader(i, i2);
        scrollCellHorizontally(i, i2);
    }

    public void scrollToRowPosition(int i) {
        this.mRowHeaderLayoutManager.scrollToPosition(i);
        this.mCellLayoutManager.scrollToPosition(i);
    }

    public void scrollToRowPosition(int i, int i2) {
        this.mRowHeaderLayoutManager.scrollToPositionWithOffset(i, i2);
        this.mCellLayoutManager.scrollToPositionWithOffset(i, i2);
    }

    private void scrollCellHorizontally(int i, int i2) {
        CellLayoutManager cellLayoutManager = this.mTableView.getCellLayoutManager();
        for (int findFirstVisibleItemPosition = cellLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition < cellLayoutManager.findLastVisibleItemPosition() + 1; findFirstVisibleItemPosition++) {
            RecyclerView recyclerView = (RecyclerView) cellLayoutManager.findViewByPosition(findFirstVisibleItemPosition);
            if (recyclerView != null) {
                ((ColumnLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(i, i2);
            }
        }
    }

    private void scrollColumnHeader(int i, int i2) {
        this.mTableView.getColumnHeaderLayoutManager().scrollToPositionWithOffset(i, i2);
    }

    public int getColumnPosition() {
        return this.mColumnHeaderLayoutManager.findFirstVisibleItemPosition();
    }

    public int getColumnPositionOffset() {
        ColumnHeaderLayoutManager columnHeaderLayoutManager = this.mColumnHeaderLayoutManager;
        View findViewByPosition = columnHeaderLayoutManager.findViewByPosition(columnHeaderLayoutManager.findFirstVisibleItemPosition());
        if (findViewByPosition != null) {
            return findViewByPosition.getLeft();
        }
        return 0;
    }

    public int getRowPosition() {
        return this.mRowHeaderLayoutManager.findFirstVisibleItemPosition();
    }

    public int getRowPositionOffset() {
        LinearLayoutManager linearLayoutManager = this.mRowHeaderLayoutManager;
        View findViewByPosition = linearLayoutManager.findViewByPosition(linearLayoutManager.findFirstVisibleItemPosition());
        if (findViewByPosition != null) {
            return findViewByPosition.getLeft();
        }
        return 0;
    }
}
