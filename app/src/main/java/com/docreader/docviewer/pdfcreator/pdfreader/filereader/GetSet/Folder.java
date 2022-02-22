package com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet;

import java.util.ArrayList;

public final class Folder {
    private String folderName;
    private ArrayList<ImageModel> images = new ArrayList<>();

    public Folder(String str) {
        this.folderName = str;
    }

    public final String getFolderName() {
        return this.folderName;
    }

    public final void setFolderName(String str) {
        this.folderName = str;
    }

    public final ArrayList<ImageModel> getImages() {
        return this.images;
    }

    public final void setImages(ArrayList<ImageModel> arrayList) {
        this.images = arrayList;
    }
}
