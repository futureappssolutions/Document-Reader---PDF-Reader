package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;

public interface ITableAdapter<CH, RH, C> {
    void addAdapterDataSetChangedListener(AdapterDataSetChangedListener<CH, RH, C> adapterDataSetChangedListener);

    int getCellItemViewType(int i);

    int getColumnHeaderItemViewType(int i);

    View getCornerView();

    int getRowHeaderItemViewType(int i);

    ITableView getTableView();

    void onBindCellViewHolder(AbstractViewHolder abstractViewHolder, C c, int i, int i2);

    void onBindColumnHeaderViewHolder(AbstractViewHolder abstractViewHolder, CH ch, int i);

    void onBindRowHeaderViewHolder(AbstractViewHolder abstractViewHolder, RH rh, int i);

    AbstractViewHolder onCreateCellViewHolder(ViewGroup viewGroup, int i);

    AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup viewGroup, int i);

    View onCreateCornerView(ViewGroup viewGroup);

    AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup viewGroup, int i);
}
