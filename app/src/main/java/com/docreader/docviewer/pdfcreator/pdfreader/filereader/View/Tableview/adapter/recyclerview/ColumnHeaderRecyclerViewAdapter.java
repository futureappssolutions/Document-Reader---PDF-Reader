package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.ITableAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ColumnSortHelper;

import java.util.List;

public class ColumnHeaderRecyclerViewAdapter<CH> extends AbstractRecyclerViewAdapter<CH> {
    private ColumnSortHelper mColumnSortHelper;
    private ITableAdapter mTableAdapter;
    private ITableView mTableView;

    public ColumnHeaderRecyclerViewAdapter(Context context, List<CH> list, ITableAdapter iTableAdapter) {
        super(context, list);
        this.mTableAdapter = iTableAdapter;
        this.mTableView = iTableAdapter.getTableView();
    }

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return this.mTableAdapter.onCreateColumnHeaderViewHolder(viewGroup, i);
    }

    public void onBindViewHolder(AbstractViewHolder abstractViewHolder, int i) {
        this.mTableAdapter.onBindColumnHeaderViewHolder(abstractViewHolder, getItem(i), i);
    }

    public int getItemViewType(int i) {
        return this.mTableAdapter.getColumnHeaderItemViewType(i);
    }

    public void onViewAttachedToWindow(AbstractViewHolder abstractViewHolder) {
        super.onViewAttachedToWindow(abstractViewHolder);
        AbstractViewHolder.SelectionState columnSelectionState = this.mTableView.getSelectionHandler().getColumnSelectionState(abstractViewHolder.getAdapterPosition());
        if (!this.mTableView.isIgnoreSelectionColors()) {
            this.mTableView.getSelectionHandler().changeColumnBackgroundColorBySelectionStatus(abstractViewHolder, columnSelectionState);
        }
        abstractViewHolder.setSelected(columnSelectionState);
        if (this.mTableView.isSortable() && (abstractViewHolder instanceof AbstractSorterViewHolder)) {
            ((AbstractSorterViewHolder) abstractViewHolder).onSortingStatusChanged(getColumnSortHelper().getSortingStatus(abstractViewHolder.getAdapterPosition()));
        }
    }

    public ColumnSortHelper getColumnSortHelper() {
        if (this.mColumnSortHelper == null) {
            this.mColumnSortHelper = new ColumnSortHelper(this.mTableView.getColumnHeaderLayoutManager());
        }
        return this.mColumnSortHelper;
    }
}
