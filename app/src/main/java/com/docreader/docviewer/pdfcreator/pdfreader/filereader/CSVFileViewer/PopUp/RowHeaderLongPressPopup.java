package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.PopUp;

import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.TableView;

public class RowHeaderLongPressPopup extends PopupMenu implements PopupMenu.OnMenuItemClickListener {
    private final int mRowPosition;
    private final TableView mTableView;

    private void createMenuItem() {
    }

    public RowHeaderLongPressPopup(RecyclerView.ViewHolder viewHolder, TableView tableView) {
        super(viewHolder.itemView.getContext(), viewHolder.itemView);
        mTableView = tableView;
        mRowPosition = viewHolder.getAdapterPosition();
        initialize();
    }

    private void initialize() {
        createMenuItem();
        setOnMenuItemClickListener(this);
    }

    public boolean onMenuItemClick(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 1) {
            mTableView.scrollToColumnPosition(15);
        } else if (itemId != 2) {
            if (itemId == 3) {
                mTableView.getAdapter().removeRow(mRowPosition);
            }
        } else if (mTableView.isColumnVisible(1)) {
            mTableView.hideColumn(1);
        } else {
            mTableView.showColumn(1);
        }
        return true;
    }
}
