package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.AbstractTableAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter.Filter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.ColumnSortHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.FilterHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.ScrollHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.SelectionHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.VisibilityHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.CellLayoutManager;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.ColumnHeaderLayoutManager;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.ITableViewListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.scroll.HorizontalRecyclerViewListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.scroll.VerticalRecyclerViewListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.SortState;

public interface ITableView {
    void addView(View view, ViewGroup.LayoutParams layoutParams);

    void clearHiddenColumnList();

    void clearHiddenRowList();

    void filter(Filter filter);

    AbstractTableAdapter getAdapter();

    CellLayoutManager getCellLayoutManager();

    CellRecyclerView getCellRecyclerView();

    ColumnHeaderLayoutManager getColumnHeaderLayoutManager();

    CellRecyclerView getColumnHeaderRecyclerView();

    ColumnSortHandler getColumnSortHandler();

    Context getContext();

    FilterHandler getFilterHandler();

    DividerItemDecoration getHorizontalItemDecoration();

    HorizontalRecyclerViewListener getHorizontalRecyclerViewListener();

    LinearLayoutManager getRowHeaderLayoutManager();

    CellRecyclerView getRowHeaderRecyclerView();

    SortState getRowHeaderSortingStatus();

    int getRowHeaderWidth();

    ScrollHandler getScrollHandler();

    int getSelectedColor();

    SelectionHandler getSelectionHandler();

    int getSeparatorColor();

    int getShadowColor();

    SortState getSortingStatus(int i);

    ITableViewListener getTableViewListener();

    int getUnSelectedColor();

    DividerItemDecoration getVerticalItemDecoration();

    VerticalRecyclerViewListener getVerticalRecyclerViewListener();

    VisibilityHandler getVisibilityHandler();

    boolean hasFixedWidth();

    void hideColumn(int i);

    void hideRow(int i);

    boolean isAllowClickInsideCell();

    boolean isColumnVisible(int i);

    boolean isIgnoreSelectionColors();

    boolean isRowVisible(int i);

    boolean isShowHorizontalSeparators();

    boolean isShowVerticalSeparators();

    boolean isSortable();

    void remeasureColumnWidth(int i);

    void scrollToColumnPosition(int i);

    void scrollToColumnPosition(int i, int i2);

    void scrollToRowPosition(int i);

    void scrollToRowPosition(int i, int i2);

    void setRowHeaderWidth(int i);

    void showAllHiddenColumns();

    void showAllHiddenRows();

    void showColumn(int i);

    void showRow(int i);

    void sortColumn(int i, SortState sortState);

    void sortRowHeader(SortState sortState);
}
