package com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BookMetadata {
    private Map<String, String> alldata = new LinkedHashMap<>();
    private String author;
    private String filename;
    private String title;

    public final String getTitle() {
        return this.title;
    }

    public final void setTitle(String str) {
        this.title = str;
    }

    public final String getAuthor() {
        return this.author;
    }

    public final void setAuthor(String str) {
        this.author = str;
    }

    public final String getFilename() {
        return this.filename;
    }

    public final void setFilename(String str) {
        this.filename = str;
    }

    public final Map<String, String> getAlldata() {
        return this.alldata;
    }

    public final void setAlldata(Map<String, String> map) {
        this.alldata = map;
    }
}
