package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {
    public int compare(File file, File file2) {
        if (file == file2) {
            return 0;
        }
        if (file.isDirectory() && file2.isFile()) {
            return -1;
        }
        if (!file.isFile() || !file2.isDirectory()) {
            return file.getName().compareToIgnoreCase(file2.getName());
        }
        return 1;
    }
}
