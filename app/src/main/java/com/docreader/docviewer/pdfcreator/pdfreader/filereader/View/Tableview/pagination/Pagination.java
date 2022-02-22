package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.pagination;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.AdapterDataSetChangedListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter.FilterChangedListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ColumnForRowHeaderSortComparator;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ColumnSortComparator;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ColumnSortStateChangedListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ISortableModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.RowHeaderForCellSortComparator;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.RowHeaderSortComparator;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.SortState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pagination implements IPagination {
    private static final int DEFAULT_ITEMS_PER_PAGE = 10;
    private AdapterDataSetChangedListener adapterDataSetChangedListener;
    private ColumnSortStateChangedListener columnSortStateChangedListener;
    private int currentPage;
    private FilterChangedListener<ISortableModel> filterChangedListener;
    private int itemsPerPage;
    private CellRecyclerViewAdapter<List<ISortableModel>> mCellRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter<ISortableModel> mRowHeaderRecyclerViewAdapter;
    private OnTableViewPageTurnedListener onTableViewPageTurnedListener;

    public List<List<ISortableModel>> originalCellData;

    public List<ISortableModel> originalRowData;
    private int pageCount;

    public interface OnTableViewPageTurnedListener {
        void onPageTurned(int i, int i2, int i3);
    }

    public Pagination(ITableView iTableView) {
        this(iTableView, 10, (OnTableViewPageTurnedListener) null);
    }

    public Pagination(ITableView iTableView, int i) {
        this(iTableView, i, (OnTableViewPageTurnedListener) null);
    }

    public Pagination(ITableView iTableView, int i, OnTableViewPageTurnedListener onTableViewPageTurnedListener2) {
        this.adapterDataSetChangedListener = new AdapterDataSetChangedListener() {
            public void onRowHeaderItemsChanged(List list) {
                List unused = Pagination.this.originalRowData = new ArrayList(list);
                Pagination.this.reloadPages();
            }

            public void onCellItemsChanged(List list) {
                List unused = Pagination.this.originalCellData = new ArrayList(list);
                Pagination.this.reloadPages();
            }
        };
        this.filterChangedListener = new FilterChangedListener<ISortableModel>() {
            public void onFilterChanged(List<List<ISortableModel>> list, List<ISortableModel> list2) {
                List unused = Pagination.this.originalCellData = new ArrayList(list);
                List unused2 = Pagination.this.originalRowData = new ArrayList(list2);
                Pagination.this.reloadPages();
            }

            public void onFilterCleared(List<List<ISortableModel>> list, List<ISortableModel> list2) {
                List unused = Pagination.this.originalCellData = new ArrayList(list);
                List unused2 = Pagination.this.originalRowData = new ArrayList(list2);
                Pagination.this.reloadPages();
            }
        };
        this.columnSortStateChangedListener = new ColumnSortStateChangedListener() {
            public void onColumnSortStatusChanged(int i, SortState sortState) {
                Pagination.this.paginateOnColumnSort(i, sortState);
            }

            public void onRowHeaderSortStatusChanged(SortState sortState) {
                Pagination.this.paginateOnColumnSort(-1, sortState);
            }
        };
        initialize(iTableView, i, onTableViewPageTurnedListener2);
    }

    private void initialize(ITableView iTableView, int i, OnTableViewPageTurnedListener onTableViewPageTurnedListener2) {
        this.onTableViewPageTurnedListener = onTableViewPageTurnedListener2;
        this.itemsPerPage = i;
        this.mRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter) iTableView.getRowHeaderRecyclerView().getAdapter();
        this.mCellRecyclerViewAdapter = (CellRecyclerViewAdapter) iTableView.getCellRecyclerView().getAdapter();
        iTableView.getColumnSortHandler().addColumnSortStateChangedListener(this.columnSortStateChangedListener);
        iTableView.getAdapter().addAdapterDataSetChangedListener(this.adapterDataSetChangedListener);
        iTableView.getFilterHandler().addFilterChangedListener(this.filterChangedListener);
        this.originalCellData = iTableView.getAdapter().getCellRecyclerViewAdapter().getItems();
        this.originalRowData = iTableView.getAdapter().getRowHeaderRecyclerViewAdapter().getItems();
        this.currentPage = 1;
        reloadPages();
    }


    public void reloadPages() {
        paginateData();
        goToPage(this.currentPage);
    }

    private void paginateData() {
        int i;
        int i2;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i3 = this.itemsPerPage;
        if (i3 == 0) {
            arrayList.addAll(this.originalCellData);
            arrayList2.addAll(this.originalRowData);
            this.pageCount = 1;
            i2 = 0;
            i = arrayList.size();
        } else {
            int i4 = this.currentPage;
            int i5 = (i4 * i3) - i3;
            i = i4 * i3 > this.originalCellData.size() ? this.originalCellData.size() : this.currentPage * this.itemsPerPage;
            for (int i6 = i5; i6 < i; i6++) {
                arrayList.add(this.originalCellData.get(i6));
                arrayList2.add(this.originalRowData.get(i6));
            }
            double size = (double) this.originalCellData.size();
            double d = (double) this.itemsPerPage;
            Double.isNaN(size);
            Double.isNaN(d);
            this.pageCount = (int) Math.ceil(size / d);
            i2 = i5;
        }
        this.mRowHeaderRecyclerViewAdapter.setItems(arrayList2, true);
        this.mCellRecyclerViewAdapter.setItems(arrayList, true);
        OnTableViewPageTurnedListener onTableViewPageTurnedListener2 = this.onTableViewPageTurnedListener;
        if (onTableViewPageTurnedListener2 != null) {
            onTableViewPageTurnedListener2.onPageTurned(arrayList.size(), i2, i - 1);
        }
    }

    public void nextPage() {
        int i = this.currentPage;
        if (i + 1 <= this.pageCount) {
            i++;
            this.currentPage = i;
        }
        this.currentPage = i;
        paginateData();
    }

    public void previousPage() {
        int i = this.currentPage;
        if (i - 1 != 0) {
            i--;
            this.currentPage = i;
        }
        this.currentPage = i;
        paginateData();
    }

    public void goToPage(int i) {
        int i2 = this.pageCount;
        if (i > i2 || i < 1) {
            i = (i <= i2 || i2 <= 0) ? this.currentPage : i2;
        }
        this.currentPage = i;
        paginateData();
    }

    public void setItemsPerPage(int i) {
        this.itemsPerPage = i;
        this.currentPage = 1;
        paginateData();
    }

    public void setOnTableViewPageTurnedListener(OnTableViewPageTurnedListener onTableViewPageTurnedListener2) {
        this.onTableViewPageTurnedListener = onTableViewPageTurnedListener2;
    }

    public void removeOnTableViewPageTurnedListener() {
        this.onTableViewPageTurnedListener = null;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getItemsPerPage() {
        return this.itemsPerPage;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public boolean isPaginated() {
        return this.itemsPerPage > 0;
    }


    public void paginateOnColumnSort(int i, SortState sortState) {
        ArrayList arrayList = new ArrayList(this.originalRowData);
        ArrayList arrayList2 = new ArrayList(this.originalCellData);
        if (sortState != SortState.UNSORTED) {
            if (i == -1) {
                Collections.sort(arrayList, new RowHeaderSortComparator(sortState));
                Collections.sort(arrayList2, new RowHeaderForCellSortComparator(this.originalRowData, this.originalCellData, sortState));
            } else {
                Collections.sort(arrayList2, new ColumnSortComparator(i, sortState));
                Collections.sort(arrayList, new ColumnForRowHeaderSortComparator(this.originalRowData, this.originalCellData, i, sortState));
            }
        }
        this.originalRowData = new ArrayList(arrayList);
        this.originalCellData = new ArrayList(arrayList2);
        reloadPages();
    }
}
