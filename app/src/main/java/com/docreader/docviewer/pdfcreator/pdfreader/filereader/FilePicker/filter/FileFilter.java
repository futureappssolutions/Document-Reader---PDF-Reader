package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter;

import java.io.File;
import java.io.Serializable;

public interface FileFilter extends Serializable {
    boolean accept(File file);
}
