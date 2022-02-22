package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.itemclick;

import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;

public class RowHeaderRecyclerViewItemClickListener extends AbstractItemClickListener {
    public RowHeaderRecyclerViewItemClickListener(CellRecyclerView cellRecyclerView, ITableView iTableView) {
        super(cellRecyclerView, iTableView);
    }


    public boolean clickAction(RecyclerView recyclerView, MotionEvent motionEvent) {
        View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (findChildViewUnder == null) {
            return false;
        }
        AbstractViewHolder abstractViewHolder = (AbstractViewHolder) this.mRecyclerView.getChildViewHolder(findChildViewUnder);
        int adapterPosition = abstractViewHolder.getAdapterPosition();
        if (!this.mTableView.isIgnoreSelectionColors()) {
            this.mSelectionHandler.setSelectedRowPosition(abstractViewHolder, adapterPosition);
        }
        getTableViewListener().onRowHeaderClicked(abstractViewHolder, adapterPosition);
        return true;
    }


    public void longPressAction(MotionEvent motionEvent) {
        View findChildViewUnder;
        if (this.mRecyclerView.getScrollState() == 0 && (findChildViewUnder = this.mRecyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY())) != null) {
            RecyclerView.ViewHolder childViewHolder = this.mRecyclerView.getChildViewHolder(findChildViewUnder);
            getTableViewListener().onRowHeaderLongPressed(childViewHolder, childViewHolder.getAdapterPosition());
        }
    }


    public boolean doubleClickAction(MotionEvent motionEvent) {
        View findChildViewUnder = this.mRecyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (findChildViewUnder == null) {
            return false;
        }
        AbstractViewHolder abstractViewHolder = (AbstractViewHolder) this.mRecyclerView.getChildViewHolder(findChildViewUnder);
        int adapterPosition = abstractViewHolder.getAdapterPosition();
        if (!this.mTableView.isIgnoreSelectionColors()) {
            this.mSelectionHandler.setSelectedRowPosition(abstractViewHolder, adapterPosition);
        }
        getTableViewListener().onRowHeaderDoubleClicked(abstractViewHolder, adapterPosition);
        return true;
    }
}
