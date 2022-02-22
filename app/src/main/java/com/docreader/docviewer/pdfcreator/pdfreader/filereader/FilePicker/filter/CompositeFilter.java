package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter;

import java.io.File;
import java.util.List;

public class CompositeFilter implements FileFilter {
    private final List<FileFilter> mFilters;

    public CompositeFilter(List<FileFilter> list) {
        mFilters = list;
    }

    public boolean accept(File file) {
        for (FileFilter accept : mFilters) {
            if (!accept.accept(file)) {
                return false;
            }
        }
        return true;
    }
}
