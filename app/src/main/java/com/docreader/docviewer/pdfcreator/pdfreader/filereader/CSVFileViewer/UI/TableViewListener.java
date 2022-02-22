package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.UI;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.PopUp.ColumnHeaderLongPressPopup;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.PopUp.RowHeaderLongPressPopup;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.ViewHolder.ColumnHeaderViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.TableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.ITableViewListener;

public class TableViewListener implements ITableViewListener {
    private final Context mContext;
    private final TableView mTableView;

    public TableViewListener(TableView tableView) {
        this.mContext = tableView.getContext();
        this.mTableView = tableView;
    }

    public void onCellClicked(RecyclerView.ViewHolder viewHolder, int i, int i2) {
        showToast("Cell " + i + " " + i2 + " has been clicked.");
    }

    public void onCellDoubleClicked(RecyclerView.ViewHolder viewHolder, int i, int i2) {
        showToast("Cell " + i + " " + i2 + " has been double clicked.");
    }

    public void onCellLongPressed(RecyclerView.ViewHolder viewHolder, int i, int i2) {
        showToast("Cell " + i + " " + i2 + " has been long pressed.");
    }

    public void onColumnHeaderClicked(RecyclerView.ViewHolder viewHolder, int i) {
        showToast("Column header  " + i + " has been clicked.");
    }

    public void onColumnHeaderDoubleClicked(RecyclerView.ViewHolder viewHolder, int i) {
        showToast("Column header  " + i + " has been double clicked.");
    }

    public void onColumnHeaderLongPressed(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ColumnHeaderViewHolder) {
            new ColumnHeaderLongPressPopup((ColumnHeaderViewHolder) viewHolder, this.mTableView).show();
        }
    }

    public void onRowHeaderClicked(RecyclerView.ViewHolder viewHolder, int i) {
        showToast("Row header " + i + " has been clicked.");
    }

    public void onRowHeaderDoubleClicked(RecyclerView.ViewHolder viewHolder, int i) {
        showToast("Row header " + i + " has been double clicked.");
    }

    public void onRowHeaderLongPressed(RecyclerView.ViewHolder viewHolder, int i) {
        new RowHeaderLongPressPopup(viewHolder, this.mTableView).show();
    }

    private void showToast(String str) {
        Toast.makeText(this.mContext, str, Toast.LENGTH_SHORT).show();
    }
}
