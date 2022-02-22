package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.ITableAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.RowHeaderSortHelper;

import java.util.List;

public class RowHeaderRecyclerViewAdapter<RH> extends AbstractRecyclerViewAdapter<RH> {
    private RowHeaderSortHelper mRowHeaderSortHelper;
    private ITableAdapter mTableAdapter;
    private ITableView mTableView;

    public RowHeaderRecyclerViewAdapter(Context context, List<RH> list, ITableAdapter iTableAdapter) {
        super(context, list);
        this.mTableAdapter = iTableAdapter;
        this.mTableView = iTableAdapter.getTableView();
    }

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return this.mTableAdapter.onCreateRowHeaderViewHolder(viewGroup, i);
    }

    public void onBindViewHolder(AbstractViewHolder abstractViewHolder, int i) {
        this.mTableAdapter.onBindRowHeaderViewHolder(abstractViewHolder, getItem(i), i);
    }

    public int getItemViewType(int i) {
        return this.mTableAdapter.getRowHeaderItemViewType(i);
    }

    public void onViewAttachedToWindow(AbstractViewHolder abstractViewHolder) {
        super.onViewAttachedToWindow(abstractViewHolder);
        AbstractViewHolder.SelectionState rowSelectionState = this.mTableView.getSelectionHandler().getRowSelectionState(abstractViewHolder.getAdapterPosition());
        if (!this.mTableView.isIgnoreSelectionColors()) {
            this.mTableView.getSelectionHandler().changeRowBackgroundColorBySelectionStatus(abstractViewHolder, rowSelectionState);
        }
        abstractViewHolder.setSelected(rowSelectionState);
    }

    public RowHeaderSortHelper getRowHeaderSortHelper() {
        if (this.mRowHeaderSortHelper == null) {
            this.mRowHeaderSortHelper = new RowHeaderSortHelper();
        }
        return this.mRowHeaderSortHelper;
    }
}
