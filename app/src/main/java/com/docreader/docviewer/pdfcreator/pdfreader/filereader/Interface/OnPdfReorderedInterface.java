package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface;

import android.graphics.Bitmap;

import java.util.List;

public interface OnPdfReorderedInterface {
    void onPdfReorderCompleted(List<Bitmap> list);

    void onPdfReorderFailed();

    void onPdfReorderStarted();
}
