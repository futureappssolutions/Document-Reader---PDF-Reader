package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.ColumnHeaderRecyclerViewAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ColumnForRowHeaderSortComparator;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ColumnSortCallback;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ColumnSortComparator;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ColumnSortStateChangedListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ISortableModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.RowHeaderForCellSortComparator;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.RowHeaderSortCallback;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.RowHeaderSortComparator;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.SortState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColumnSortHandler {

    private final CellRecyclerViewAdapter<List<ISortableModel>> mCellRecyclerViewAdapter;
    private final RowHeaderRecyclerViewAdapter<ISortableModel> mRowHeaderRecyclerViewAdapter;
    private final ColumnHeaderRecyclerViewAdapter mColumnHeaderRecyclerViewAdapter;

    private List<ColumnSortStateChangedListener> columnSortStateChangedListeners = new ArrayList<>();
    private boolean mEnableAnimation = true;

    public boolean isEnableAnimation() {
        return mEnableAnimation;
    }

    public void setEnableAnimation(boolean mEnableAnimation) {
        this.mEnableAnimation = mEnableAnimation;
    }

    public ColumnSortHandler(@NonNull ITableView tableView) {
        this.mCellRecyclerViewAdapter = (CellRecyclerViewAdapter<List<ISortableModel>>) tableView.getCellRecyclerView()
                .getAdapter();

        this.mRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter<ISortableModel>) tableView
                .getRowHeaderRecyclerView().getAdapter();

        this.mColumnHeaderRecyclerViewAdapter = (ColumnHeaderRecyclerViewAdapter) tableView
                .getColumnHeaderRecyclerView().getAdapter();
    }

    public void sortByRowHeader(@NonNull final SortState sortState) {
        List<ISortableModel> originalRowHeaderList = mRowHeaderRecyclerViewAdapter.getItems();
        List<ISortableModel> sortedRowHeaderList = new ArrayList<>(originalRowHeaderList);

        List<List<ISortableModel>> originalList = mCellRecyclerViewAdapter.getItems();
        List<List<ISortableModel>> sortedList = new ArrayList<>(originalList);

        if (sortState != SortState.UNSORTED) {
            // Do descending / ascending sort
            Collections.sort(sortedRowHeaderList, new RowHeaderSortComparator(sortState));

            // Sorting Columns/Cells using the same logic has sorting DataSet
            RowHeaderForCellSortComparator rowHeaderForCellSortComparator
                    = new RowHeaderForCellSortComparator(
                    originalRowHeaderList,
                    originalList,
                    sortState);

            Collections.sort(sortedList, rowHeaderForCellSortComparator);
        }

        mRowHeaderRecyclerViewAdapter.getRowHeaderSortHelper().setSortingStatus(sortState);

        // Set sorted data list
        swapItems(originalRowHeaderList, sortedRowHeaderList, sortedList, sortState);
    }

    public void sort(int column, @NonNull SortState sortState) {
        List<List<ISortableModel>> originalList = mCellRecyclerViewAdapter.getItems();
        List<List<ISortableModel>> sortedList = new ArrayList<>(originalList);

        List<ISortableModel> originalRowHeaderList
                = mRowHeaderRecyclerViewAdapter.getItems();
        List<ISortableModel> sortedRowHeaderList
                = new ArrayList<>(originalRowHeaderList);

        if (sortState != SortState.UNSORTED) {
            // Do descending / ascending sort
            Collections.sort(sortedList, new ColumnSortComparator(column, sortState));

            // Sorting RowHeader using the same logic has sorting DataSet
            ColumnForRowHeaderSortComparator columnForRowHeaderSortComparator
                    = new ColumnForRowHeaderSortComparator(
                    originalRowHeaderList,
                    originalList,
                    column,
                    sortState);

            Collections.sort(sortedRowHeaderList, columnForRowHeaderSortComparator);
        }

        // Update sorting list of column headers
        mColumnHeaderRecyclerViewAdapter.getColumnSortHelper().setSortingStatus(column, sortState);

        // Set sorted data list
        swapItems(originalList, sortedList, column, sortedRowHeaderList, sortState);
    }

    private void swapItems(@NonNull List<ISortableModel> oldRowHeader,
                           @NonNull List<ISortableModel> newRowHeader,
                           @NonNull List<List<ISortableModel>> newColumnItems,
                           @NonNull SortState sortState
    ) {

        mRowHeaderRecyclerViewAdapter.setItems(newRowHeader, !mEnableAnimation);
        mCellRecyclerViewAdapter.setItems(newColumnItems, !mEnableAnimation);

        if (mEnableAnimation) {
            final RowHeaderSortCallback diffCallback = new RowHeaderSortCallback(oldRowHeader, newRowHeader);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

            diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);
            diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
        }

        for (ColumnSortStateChangedListener listener : columnSortStateChangedListeners) {
            listener.onRowHeaderSortStatusChanged(sortState);
        }
    }

    private void swapItems(@NonNull List<List<ISortableModel>> oldItems, @NonNull List<List<ISortableModel>>
            newItems, int column, @NonNull List<ISortableModel> newRowHeader, @NonNull SortState sortState) {

        mCellRecyclerViewAdapter.setItems(newItems, !mEnableAnimation);
        mRowHeaderRecyclerViewAdapter.setItems(newRowHeader, !mEnableAnimation);

        if (mEnableAnimation) {
            final ColumnSortCallback diffCallback = new ColumnSortCallback(oldItems, newItems, column);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

            diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
            diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);
        }

        for (ColumnSortStateChangedListener listener : columnSortStateChangedListeners) {
            listener.onColumnSortStatusChanged(column, sortState);
        }
    }

    public void swapItems(@NonNull List<List<ISortableModel>> newItems, int column) {
        List<List<ISortableModel>> oldItems = mCellRecyclerViewAdapter.getItems();

        mCellRecyclerViewAdapter.setItems(newItems, !mEnableAnimation);

        if (mEnableAnimation) {
            final ColumnSortCallback diffCallback = new ColumnSortCallback(oldItems, newItems, column);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

            diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
            diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);
        }
    }

    @NonNull
    public SortState getSortingStatus(int column) {
        return mColumnHeaderRecyclerViewAdapter.getColumnSortHelper().getSortingStatus(column);
    }

    @Nullable
    public SortState getRowHeaderSortingStatus() {
        return mRowHeaderRecyclerViewAdapter.getRowHeaderSortHelper().getSortingStatus();
    }

    public void addColumnSortStateChangedListener(@NonNull ColumnSortStateChangedListener listener) {
        if (columnSortStateChangedListeners == null) {
            columnSortStateChangedListeners = new ArrayList<>();
        }

        columnSortStateChangedListeners.add(listener);
    }
}