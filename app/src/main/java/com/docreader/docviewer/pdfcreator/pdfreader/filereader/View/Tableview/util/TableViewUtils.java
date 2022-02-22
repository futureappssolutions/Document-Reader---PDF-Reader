package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.util;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class TableViewUtils {
    public static void setWidth(View view, int i) {
        ((RecyclerView.LayoutParams) view.getLayoutParams()).width = i;
        view.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824));
        view.requestLayout();
    }
}
