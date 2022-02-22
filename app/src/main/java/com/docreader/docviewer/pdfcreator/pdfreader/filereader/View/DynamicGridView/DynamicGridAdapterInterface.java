package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.DynamicGridView;

public interface DynamicGridAdapterInterface {
    boolean canReorder(int i);

    int getColumnCount();

    void reorderItems(int i, int i2);
}
