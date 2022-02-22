package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.itemclick;

import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRowRecyclerViewAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;

public class CellRecyclerViewItemClickListener extends AbstractItemClickListener {
    private CellRecyclerView mCellRecyclerView;

    public CellRecyclerViewItemClickListener(CellRecyclerView cellRecyclerView, ITableView iTableView) {
        super(cellRecyclerView, iTableView);
        this.mCellRecyclerView = iTableView.getCellRecyclerView();
    }


    public boolean clickAction(RecyclerView recyclerView, MotionEvent motionEvent) {
        View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (findChildViewUnder == null) {
            return false;
        }
        AbstractViewHolder abstractViewHolder = (AbstractViewHolder) this.mRecyclerView.getChildViewHolder(findChildViewUnder);
        int adapterPosition = abstractViewHolder.getAdapterPosition();
        int yPosition = ((CellRowRecyclerViewAdapter) this.mRecyclerView.getAdapter()).getYPosition();
        if (!this.mTableView.isIgnoreSelectionColors()) {
            this.mSelectionHandler.setSelectedCellPositions(abstractViewHolder, adapterPosition, yPosition);
        }
        getTableViewListener().onCellClicked(abstractViewHolder, adapterPosition, yPosition);
        return true;
    }


    public void longPressAction(MotionEvent motionEvent) {
        View findChildViewUnder;
        if (this.mRecyclerView.getScrollState() == 0 && this.mCellRecyclerView.getScrollState() == 0 && (findChildViewUnder = this.mRecyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY())) != null) {
            RecyclerView.ViewHolder childViewHolder = this.mRecyclerView.getChildViewHolder(findChildViewUnder);
            getTableViewListener().onCellLongPressed(childViewHolder, childViewHolder.getAdapterPosition(), ((CellRowRecyclerViewAdapter) this.mRecyclerView.getAdapter()).getYPosition());
        }
    }


    public boolean doubleClickAction(MotionEvent motionEvent) {
        View findChildViewUnder = this.mRecyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (findChildViewUnder == null) {
            return false;
        }
        AbstractViewHolder abstractViewHolder = (AbstractViewHolder) this.mRecyclerView.getChildViewHolder(findChildViewUnder);
        int adapterPosition = abstractViewHolder.getAdapterPosition();
        int yPosition = ((CellRowRecyclerViewAdapter) this.mRecyclerView.getAdapter()).getYPosition();
        if (!this.mTableView.isIgnoreSelectionColors()) {
            this.mSelectionHandler.setSelectedCellPositions(abstractViewHolder, adapterPosition, yPosition);
        }
        getTableViewListener().onCellDoubleClicked(abstractViewHolder, adapterPosition, yPosition);
        return true;
    }
}
