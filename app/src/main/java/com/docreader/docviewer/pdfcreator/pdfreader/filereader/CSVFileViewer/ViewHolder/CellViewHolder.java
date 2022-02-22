package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.Cell;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;

public class CellViewHolder extends AbstractViewHolder {
    private final LinearLayout cell_container;
    private final TextView cell_textview;

    public CellViewHolder(View view) {
        super(view);
        cell_textview = view.findViewById(R.id.cell_data);
        cell_container = view.findViewById(R.id.cell_container);
    }

    public void setCell(Cell cell) {
        cell_textview.setText(String.valueOf(cell.getData()));
        cell_container.getLayoutParams().width = -2;
        cell_textview.requestLayout();
    }
}
