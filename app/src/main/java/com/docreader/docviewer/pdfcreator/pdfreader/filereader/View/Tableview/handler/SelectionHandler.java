package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.CellLayoutManager;

public class SelectionHandler {
    public static final int UNSELECTED_POSITION = -1;
    private CellLayoutManager mCellLayoutManager;
    private CellRecyclerView mColumnHeaderRecyclerView;
    private AbstractViewHolder mPreviousSelectedViewHolder;
    private CellRecyclerView mRowHeaderRecyclerView;
    private int mSelectedColumnPosition = -1;
    private int mSelectedRowPosition = -1;
    private ITableView mTableView;
    private boolean shadowEnabled = true;

    public SelectionHandler(ITableView iTableView) {
        this.mTableView = iTableView;
        this.mColumnHeaderRecyclerView = iTableView.getColumnHeaderRecyclerView();
        this.mRowHeaderRecyclerView = this.mTableView.getRowHeaderRecyclerView();
        this.mCellLayoutManager = this.mTableView.getCellLayoutManager();
    }

    public boolean isShadowEnabled() {
        return this.shadowEnabled;
    }

    public void setShadowEnabled(boolean z) {
        this.shadowEnabled = z;
    }

    public void setSelectedCellPositions(AbstractViewHolder abstractViewHolder, int i, int i2) {
        setPreviousSelectedView(abstractViewHolder);
        this.mSelectedColumnPosition = i;
        this.mSelectedRowPosition = i2;
        if (this.shadowEnabled) {
            selectedCellView();
        }
    }

    public void setSelectedColumnPosition(AbstractViewHolder abstractViewHolder, int i) {
        setPreviousSelectedView(abstractViewHolder);
        this.mSelectedColumnPosition = i;
        selectedColumnHeader();
        this.mSelectedRowPosition = -1;
    }

    public int getSelectedColumnPosition() {
        return this.mSelectedColumnPosition;
    }

    public void setSelectedRowPosition(AbstractViewHolder abstractViewHolder, int i) {
        setPreviousSelectedView(abstractViewHolder);
        this.mSelectedRowPosition = i;
        selectedRowHeader();
        this.mSelectedColumnPosition = -1;
    }

    public int getSelectedRowPosition() {
        return this.mSelectedRowPosition;
    }

    public void setPreviousSelectedView(AbstractViewHolder abstractViewHolder) {
        restorePreviousSelectedView();
        AbstractViewHolder abstractViewHolder2 = this.mPreviousSelectedViewHolder;
        if (abstractViewHolder2 != null) {
            abstractViewHolder2.setBackgroundColor(this.mTableView.getUnSelectedColor());
            this.mPreviousSelectedViewHolder.setSelected(AbstractViewHolder.SelectionState.UNSELECTED);
        }
        AbstractViewHolder cellViewHolder = this.mCellLayoutManager.getCellViewHolder(getSelectedColumnPosition(), getSelectedRowPosition());
        if (cellViewHolder != null) {
            cellViewHolder.setBackgroundColor(this.mTableView.getUnSelectedColor());
            cellViewHolder.setSelected(AbstractViewHolder.SelectionState.UNSELECTED);
        }
        this.mPreviousSelectedViewHolder = abstractViewHolder;
        abstractViewHolder.setBackgroundColor(this.mTableView.getSelectedColor());
        this.mPreviousSelectedViewHolder.setSelected(AbstractViewHolder.SelectionState.SELECTED);
    }

    private void restorePreviousSelectedView() {
        int i = this.mSelectedColumnPosition;
        if (i != -1 && this.mSelectedRowPosition != -1) {
            unselectedCellView();
        } else if (i != -1) {
            unselectedColumnHeader();
        } else if (this.mSelectedRowPosition != -1) {
            unselectedRowHeader();
        }
    }

    private void selectedRowHeader() {
        changeVisibleCellViewsBackgroundForRow(this.mSelectedRowPosition, true);
        if (this.shadowEnabled) {
            changeSelectionOfRecyclerView(this.mColumnHeaderRecyclerView, AbstractViewHolder.SelectionState.SHADOWED, this.mTableView.getShadowColor());
        }
    }

    private void unselectedRowHeader() {
        changeVisibleCellViewsBackgroundForRow(this.mSelectedRowPosition, false);
        changeSelectionOfRecyclerView(this.mColumnHeaderRecyclerView, AbstractViewHolder.SelectionState.UNSELECTED, this.mTableView.getUnSelectedColor());
    }

    private void selectedCellView() {
        int shadowColor = this.mTableView.getShadowColor();
        AbstractViewHolder abstractViewHolder = (AbstractViewHolder) this.mRowHeaderRecyclerView.findViewHolderForAdapterPosition(this.mSelectedRowPosition);
        if (abstractViewHolder != null) {
            abstractViewHolder.setBackgroundColor(shadowColor);
            abstractViewHolder.setSelected(AbstractViewHolder.SelectionState.SHADOWED);
        }
        AbstractViewHolder abstractViewHolder2 = (AbstractViewHolder) this.mColumnHeaderRecyclerView.findViewHolderForAdapterPosition(this.mSelectedColumnPosition);
        if (abstractViewHolder2 != null) {
            abstractViewHolder2.setBackgroundColor(shadowColor);
            abstractViewHolder2.setSelected(AbstractViewHolder.SelectionState.SHADOWED);
        }
    }

    private void unselectedCellView() {
        int unSelectedColor = this.mTableView.getUnSelectedColor();
        AbstractViewHolder abstractViewHolder = (AbstractViewHolder) this.mRowHeaderRecyclerView.findViewHolderForAdapterPosition(this.mSelectedRowPosition);
        if (abstractViewHolder != null) {
            abstractViewHolder.setBackgroundColor(unSelectedColor);
            abstractViewHolder.setSelected(AbstractViewHolder.SelectionState.UNSELECTED);
        }
        AbstractViewHolder abstractViewHolder2 = (AbstractViewHolder) this.mColumnHeaderRecyclerView.findViewHolderForAdapterPosition(this.mSelectedColumnPosition);
        if (abstractViewHolder2 != null) {
            abstractViewHolder2.setBackgroundColor(unSelectedColor);
            abstractViewHolder2.setSelected(AbstractViewHolder.SelectionState.UNSELECTED);
        }
    }

    private void selectedColumnHeader() {
        changeVisibleCellViewsBackgroundForColumn(this.mSelectedColumnPosition, true);
        changeSelectionOfRecyclerView(this.mRowHeaderRecyclerView, AbstractViewHolder.SelectionState.SHADOWED, this.mTableView.getShadowColor());
    }

    private void unselectedColumnHeader() {
        changeVisibleCellViewsBackgroundForColumn(this.mSelectedColumnPosition, false);
        changeSelectionOfRecyclerView(this.mRowHeaderRecyclerView, AbstractViewHolder.SelectionState.UNSELECTED, this.mTableView.getUnSelectedColor());
    }

    public boolean isCellSelected(int i, int i2) {
        return (getSelectedColumnPosition() == i && getSelectedRowPosition() == i2) || isColumnSelected(i) || isRowSelected(i2);
    }

    public AbstractViewHolder.SelectionState getCellSelectionState(int i, int i2) {
        if (isCellSelected(i, i2)) {
            return AbstractViewHolder.SelectionState.SELECTED;
        }
        return AbstractViewHolder.SelectionState.UNSELECTED;
    }

    public boolean isColumnSelected(int i) {
        return getSelectedColumnPosition() == i && getSelectedRowPosition() == -1;
    }

    public boolean isColumnShadowed(int i) {
        return (getSelectedColumnPosition() == i && getSelectedRowPosition() != -1) || (getSelectedColumnPosition() == -1 && getSelectedRowPosition() != -1);
    }

    public boolean isAnyColumnSelected() {
        return getSelectedColumnPosition() != -1 && getSelectedRowPosition() == -1;
    }

    public AbstractViewHolder.SelectionState getColumnSelectionState(int i) {
        if (isColumnShadowed(i)) {
            return AbstractViewHolder.SelectionState.SHADOWED;
        }
        if (isColumnSelected(i)) {
            return AbstractViewHolder.SelectionState.SELECTED;
        }
        return AbstractViewHolder.SelectionState.UNSELECTED;
    }

    public boolean isRowSelected(int i) {
        return getSelectedRowPosition() == i && getSelectedColumnPosition() == -1;
    }

    public boolean isRowShadowed(int i) {
        return (getSelectedRowPosition() == i && getSelectedColumnPosition() != -1) || (getSelectedRowPosition() == -1 && getSelectedColumnPosition() != -1);
    }

    public AbstractViewHolder.SelectionState getRowSelectionState(int i) {
        if (isRowShadowed(i)) {
            return AbstractViewHolder.SelectionState.SHADOWED;
        }
        if (isRowSelected(i)) {
            return AbstractViewHolder.SelectionState.SELECTED;
        }
        return AbstractViewHolder.SelectionState.UNSELECTED;
    }

    private void changeVisibleCellViewsBackgroundForRow(int i, boolean z) {
        int unSelectedColor = this.mTableView.getUnSelectedColor();
        AbstractViewHolder.SelectionState selectionState = AbstractViewHolder.SelectionState.UNSELECTED;
        if (z) {
            unSelectedColor = this.mTableView.getSelectedColor();
            selectionState = AbstractViewHolder.SelectionState.SELECTED;
        }
        CellRecyclerView cellRecyclerView = (CellRecyclerView) this.mCellLayoutManager.findViewByPosition(i);
        if (cellRecyclerView != null) {
            changeSelectionOfRecyclerView(cellRecyclerView, selectionState, unSelectedColor);
        }
    }

    private void changeVisibleCellViewsBackgroundForColumn(int i, boolean z) {
        int unSelectedColor = this.mTableView.getUnSelectedColor();
        AbstractViewHolder.SelectionState selectionState = AbstractViewHolder.SelectionState.UNSELECTED;
        if (z) {
            unSelectedColor = this.mTableView.getSelectedColor();
            selectionState = AbstractViewHolder.SelectionState.SELECTED;
        }
        for (int findFirstVisibleItemPosition = this.mCellLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition < this.mCellLayoutManager.findLastVisibleItemPosition() + 1; findFirstVisibleItemPosition++) {
            AbstractViewHolder abstractViewHolder = (AbstractViewHolder) ((CellRecyclerView) this.mCellLayoutManager.findViewByPosition(findFirstVisibleItemPosition)).findViewHolderForAdapterPosition(i);
            if (abstractViewHolder != null) {
                abstractViewHolder.setBackgroundColor(unSelectedColor);
                abstractViewHolder.setSelected(selectionState);
            }
        }
    }

    public void changeRowBackgroundColorBySelectionStatus(AbstractViewHolder abstractViewHolder, AbstractViewHolder.SelectionState selectionState) {
        if (this.shadowEnabled && selectionState == AbstractViewHolder.SelectionState.SHADOWED) {
            abstractViewHolder.setBackgroundColor(this.mTableView.getShadowColor());
        } else if (selectionState == AbstractViewHolder.SelectionState.SELECTED) {
            abstractViewHolder.setBackgroundColor(this.mTableView.getSelectedColor());
        } else {
            abstractViewHolder.setBackgroundColor(this.mTableView.getUnSelectedColor());
        }
    }

    public void changeColumnBackgroundColorBySelectionStatus(AbstractViewHolder abstractViewHolder, AbstractViewHolder.SelectionState selectionState) {
        if (this.shadowEnabled && selectionState == AbstractViewHolder.SelectionState.SHADOWED) {
            abstractViewHolder.setBackgroundColor(this.mTableView.getShadowColor());
        } else if (selectionState == AbstractViewHolder.SelectionState.SELECTED) {
            abstractViewHolder.setBackgroundColor(this.mTableView.getSelectedColor());
        } else {
            abstractViewHolder.setBackgroundColor(this.mTableView.getUnSelectedColor());
        }
    }

    public void changeSelectionOfRecyclerView(CellRecyclerView cellRecyclerView, AbstractViewHolder.SelectionState selectionState, int i) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) cellRecyclerView.getLayoutManager();
        for (int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition < linearLayoutManager.findLastVisibleItemPosition() + 1; findFirstVisibleItemPosition++) {
            AbstractViewHolder abstractViewHolder = (AbstractViewHolder) cellRecyclerView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
            if (abstractViewHolder != null) {
                if (!this.mTableView.isIgnoreSelectionColors()) {
                    abstractViewHolder.setBackgroundColor(i);
                }
                abstractViewHolder.setSelected(selectionState);
            }
        }
    }

    public void clearSelection() {
        unselectedRowHeader();
        unselectedCellView();
        unselectedColumnHeader();
    }

    public void setSelectedRowPosition(int i) {
        this.mSelectedRowPosition = i;
    }

    public void setSelectedColumnPosition(int i) {
        this.mSelectedColumnPosition = i;
    }
}
