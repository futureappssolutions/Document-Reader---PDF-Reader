package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.ViewHolder;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.ColumnHeader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.SortState;

public class ColumnHeaderViewHolder extends AbstractSorterViewHolder {
    private static final String LOG_TAG = "ColumnHeaderViewHolder";
    private final LinearLayout column_header_container;
    private final ImageButton column_header_sortButton;
    private final TextView column_header_textview;
    private final ITableView tableView;

    public ColumnHeaderViewHolder(View view, ITableView iTableView) {
        super(view);
        tableView = iTableView;
        column_header_textview = view.findViewById(R.id.column_header_textView);
        column_header_container = view.findViewById(R.id.column_header_container);
        column_header_sortButton = view.findViewById(R.id.column_header_sortButton);
        
        column_header_sortButton.setOnClickListener(v -> {
            if (getSortState() == SortState.ASCENDING) {
                tableView.sortColumn(getAdapterPosition(), SortState.DESCENDING);
            } else if (getSortState() == SortState.DESCENDING) {
                tableView.sortColumn(getAdapterPosition(), SortState.ASCENDING);
            } else {
                tableView.sortColumn(getAdapterPosition(), SortState.DESCENDING);
            }
        });
    }

    public void setColumnHeader(ColumnHeader columnHeader) {
        column_header_textview.setText(String.valueOf(columnHeader.getData()));
        column_header_container.getLayoutParams().width = -2;
        column_header_textview.requestLayout();
    }

    public void onSortingStatusChanged(SortState sortState) {
        Log.e(LOG_TAG, " + onSortingStatusChanged : x:  " + getAdapterPosition() + " old state " + getSortState() + " current state : " + sortState + " visiblity: " + column_header_sortButton.getVisibility());
        column_header_container.getLayoutParams().width = -2;
        controlSortState(sortState);
        Log.e(LOG_TAG, " - onSortingStatusChanged : x:  " + getAdapterPosition() + " old state " + getSortState() + " current state : " + sortState + " visiblity: " + column_header_sortButton.getVisibility());
        column_header_textview.requestLayout();
        column_header_sortButton.requestLayout();
        column_header_container.requestLayout();
        itemView.requestLayout();
        super.onSortingStatusChanged(sortState);
    }

    private void controlSortState(SortState sortState) {
        if (sortState == SortState.ASCENDING) {
            column_header_sortButton.setVisibility(View.VISIBLE);
            column_header_sortButton.setImageResource(R.drawable.ic_arrow_down);
        } else if (sortState == SortState.DESCENDING) {
            column_header_sortButton.setVisibility(View.VISIBLE);
            column_header_sortButton.setImageResource(R.drawable.ic_arrow_up);
        } else {
            column_header_sortButton.setVisibility(View.INVISIBLE);
        }
    }
}
