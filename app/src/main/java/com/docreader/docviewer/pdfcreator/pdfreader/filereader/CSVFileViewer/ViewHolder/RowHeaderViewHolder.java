package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;

public class RowHeaderViewHolder extends AbstractViewHolder {
    public final TextView row_header_textview;

    public RowHeaderViewHolder(View view) {
        super(view);
        row_header_textview = view.findViewById(R.id.row_header_textview);
    }
}
