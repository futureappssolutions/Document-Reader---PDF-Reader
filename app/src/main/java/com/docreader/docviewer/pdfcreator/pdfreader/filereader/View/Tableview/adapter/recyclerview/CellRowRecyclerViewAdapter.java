package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.ITableAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.List;

public class CellRowRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {
    private ITableAdapter mTableAdapter;
    private ITableView mTableView;
    private int mYPosition;

    public CellRowRecyclerViewAdapter(Context context, ITableView iTableView) {
        super(context, (List) null);
        this.mTableAdapter = iTableView.getAdapter();
        this.mTableView = iTableView;
    }

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return this.mTableAdapter.onCreateCellViewHolder(viewGroup, i);
    }

    public void onBindViewHolder(AbstractViewHolder abstractViewHolder, int i) {
        this.mTableAdapter.onBindCellViewHolder(abstractViewHolder, getItem(i), i, this.mYPosition);
    }

    public int getYPosition() {
        return this.mYPosition;
    }

    public void setYPosition(int i) {
        this.mYPosition = i;
    }

    public int getItemViewType(int i) {
        return this.mTableAdapter.getCellItemViewType(i);
    }

    public void onViewAttachedToWindow(AbstractViewHolder abstractViewHolder) {
        super.onViewAttachedToWindow(abstractViewHolder);
        AbstractViewHolder.SelectionState cellSelectionState = this.mTableView.getSelectionHandler().getCellSelectionState(abstractViewHolder.getAdapterPosition(), this.mYPosition);
        if (!this.mTableView.isIgnoreSelectionColors()) {
            if (cellSelectionState == AbstractViewHolder.SelectionState.SELECTED) {
                abstractViewHolder.setBackgroundColor(this.mTableView.getSelectedColor());
            } else {
                abstractViewHolder.setBackgroundColor(this.mTableView.getUnSelectedColor());
            }
        }
        abstractViewHolder.setSelected(cellSelectionState);
    }

    public boolean onFailedToRecycleView(AbstractViewHolder abstractViewHolder) {
        return abstractViewHolder.onFailedToRecycleView();
    }

    public void onViewRecycled(AbstractViewHolder abstractViewHolder) {
        super.onViewRecycled(abstractViewHolder);
        abstractViewHolder.onViewRecycled();
    }
}
