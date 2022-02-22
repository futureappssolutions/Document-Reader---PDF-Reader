package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.UI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.ViewHolder.CellViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.ViewHolder.ColumnHeaderViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.ViewHolder.RowHeaderViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.Cell;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.ColumnHeader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.RowHeader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.AbstractTableAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.SortState;

public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {
    public int getCellItemViewType(int i) {
        return 0;
    }

    public int getColumnHeaderItemViewType(int i) {
        return 0;
    }

    public int getRowHeaderItemViewType(int i) {
        return 0;
    }

    public AbstractViewHolder onCreateCellViewHolder(ViewGroup viewGroup, int i) {
        return new CellViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_view_cell_layout, viewGroup, false));
    }

    public void onBindCellViewHolder(AbstractViewHolder abstractViewHolder, Cell cell, int i, int i2) {
        ((CellViewHolder) abstractViewHolder).setCell(cell);
    }

    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup viewGroup, int i) {
        return new ColumnHeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_view_column_header_layout, viewGroup, false), getTableView());
    }

    public void onBindColumnHeaderViewHolder(AbstractViewHolder abstractViewHolder, ColumnHeader columnHeader, int i) {
        ((ColumnHeaderViewHolder) abstractViewHolder).setColumnHeader(columnHeader);
    }

    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup viewGroup, int i) {
        return new RowHeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_view_row_header_layout, viewGroup, false));
    }

    public void onBindRowHeaderViewHolder(AbstractViewHolder abstractViewHolder, RowHeader rowHeader, int i) {
        ((RowHeaderViewHolder) abstractViewHolder).row_header_textview.setText(String.valueOf(rowHeader.getData()));
    }

    public View onCreateCornerView(ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_view_corner_layout, viewGroup, false);
        inflate.setOnClickListener(view -> {
            if (getTableView().getRowHeaderSortingStatus() != SortState.ASCENDING) {
                Log.d("TableViewAdapter", "Order Ascending");
                getTableView().sortRowHeader(SortState.ASCENDING);
                return;
            }
            Log.d("TableViewAdapter", "Order Descending");
            getTableView().sortRowHeader(SortState.DESCENDING);
        });
        return inflate;
    }
}
