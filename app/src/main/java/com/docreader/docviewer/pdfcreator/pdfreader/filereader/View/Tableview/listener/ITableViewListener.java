package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener;

import androidx.recyclerview.widget.RecyclerView;

public interface ITableViewListener {
    void onCellClicked(RecyclerView.ViewHolder viewHolder, int i, int i2);

    void onCellDoubleClicked(RecyclerView.ViewHolder viewHolder, int i, int i2);

    void onCellLongPressed(RecyclerView.ViewHolder viewHolder, int i, int i2);

    void onColumnHeaderClicked(RecyclerView.ViewHolder viewHolder, int i);

    void onColumnHeaderDoubleClicked(RecyclerView.ViewHolder viewHolder, int i);

    void onColumnHeaderLongPressed(RecyclerView.ViewHolder viewHolder, int i);

    void onRowHeaderClicked(RecyclerView.ViewHolder viewHolder, int i);

    void onRowHeaderDoubleClicked(RecyclerView.ViewHolder viewHolder, int i);

    void onRowHeaderLongPressed(RecyclerView.ViewHolder viewHolder, int i);
}
