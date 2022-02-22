package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.PopUp;

import android.view.MenuItem;
import android.widget.PopupMenu;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.ViewHolder.ColumnHeaderViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.TableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.SortState;

public class ColumnHeaderLongPressPopup extends PopupMenu implements PopupMenu.OnMenuItemClickListener {
    private final TableView mTableView;
    private final int mXPosition;

    public ColumnHeaderLongPressPopup(ColumnHeaderViewHolder columnHeaderViewHolder, TableView tableView) {
        super(columnHeaderViewHolder.itemView.getContext(), columnHeaderViewHolder.itemView);
        mTableView = tableView;
        mXPosition = columnHeaderViewHolder.getAdapterPosition();
        initialize();
    }

    private void initialize() {
        createMenuItem();
        changeMenuItemVisibility();
        setOnMenuItemClickListener(this);
    }

    private void createMenuItem() {
        mTableView.getContext();
    }

    private void changeMenuItemVisibility() {
        SortState sortingStatus = mTableView.getSortingStatus(mXPosition);
        if (sortingStatus != SortState.UNSORTED) {
            if (sortingStatus == SortState.DESCENDING) {
                getMenu().getItem(1).setVisible(false);
            } else if (sortingStatus == SortState.ASCENDING) {
                getMenu().getItem(0).setVisible(false);
            }
        }
    }

    public boolean onMenuItemClick(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 1) {
            mTableView.sortColumn(mXPosition, SortState.ASCENDING);
        } else if (itemId == 2) {
            mTableView.sortColumn(mXPosition, SortState.DESCENDING);
        } else if (itemId == 3) {
            mTableView.hideRow(5);
        } else if (itemId == 4) {
            mTableView.showRow(5);
        } else if (itemId == 5) {
            mTableView.scrollToRowPosition(5);
        }
        return true;
    }
}
