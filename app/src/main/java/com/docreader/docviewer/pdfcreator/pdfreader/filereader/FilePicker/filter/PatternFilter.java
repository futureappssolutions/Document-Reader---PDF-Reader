package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter;

import java.io.File;
import java.util.regex.Pattern;

public class PatternFilter implements FileFilter {
    private final boolean mDirectoriesFilter;
    private final Pattern mPattern;

    public PatternFilter(Pattern pattern, boolean z) {
        this.mPattern = pattern;
        this.mDirectoriesFilter = z;
    }

    public boolean accept(File file) {
        return (file.isDirectory() && !this.mDirectoriesFilter) || this.mPattern.matcher(file.getName()).matches();
    }
}
