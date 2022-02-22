package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
    private SelectionState m_eState = SelectionState.UNSELECTED;

    public enum SelectionState {
        SELECTED,
        UNSELECTED,
        SHADOWED
    }

    public boolean onFailedToRecycleView() {
        return false;
    }

    public void onViewRecycled() {
    }

    public AbstractViewHolder(View view) {
        super(view);
    }

    public void setSelected(SelectionState selectionState) {
        this.m_eState = selectionState;
        if (selectionState == SelectionState.SELECTED) {
            this.itemView.setSelected(true);
        } else if (selectionState == SelectionState.UNSELECTED) {
            this.itemView.setSelected(false);
        }
    }

    public boolean isSelected() {
        return this.m_eState == SelectionState.SELECTED;
    }

    public boolean isShadowed() {
        return this.m_eState == SelectionState.SHADOWED;
    }

    public void setBackgroundColor(int i) {
        this.itemView.setBackgroundColor(i);
    }
}
