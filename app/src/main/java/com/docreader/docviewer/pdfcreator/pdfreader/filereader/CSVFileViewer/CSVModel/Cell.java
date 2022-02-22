package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter.IFilterableModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.ISortableModel;

public class Cell implements ISortableModel, IFilterableModel {
    private Object mData;
    private final String mFilterKeyword;
    private final String mId;

    public Cell(String str, Object obj) {
        this.mId = str;
        this.mData = obj;
        this.mFilterKeyword = String.valueOf(obj);
    }

    public String getId() {
        return this.mId;
    }

    public Object getContent() {
        return this.mData;
    }

    public Object getData() {
        return this.mData;
    }

    public void setData(Object obj) {
        this.mData = obj;
    }

    public String getFilterableKeyword() {
        return this.mFilterKeyword;
    }
}
