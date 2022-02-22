package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter;

import java.io.File;

public class HiddenFilter implements FileFilter {
    public boolean accept(File file) {
        return !file.isHidden();
    }
}
