package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class TextChangeListener implements TextWatcher {
    public void afterTextChanged(Editable editable) {
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public abstract void onTextChanged(CharSequence charSequence, int i, int i2, int i3);
}
