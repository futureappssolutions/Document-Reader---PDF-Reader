package com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet;

public final class FileModel {
    private int fileType;
    private boolean isFavorite;
    private boolean isSelected;
    private long modifiedDate;
    private String name;
    private String path;
    private String size;

    public final String getName() {
        return this.name;
    }

    public final void setName(String str) {
        this.name = str;
    }

    public final String getPath() {
        return this.path;
    }

    public final void setPath(String str) {
        this.path = str;
    }

    public final String getSize() {
        return this.size;
    }

    public final void setSize(String str) {
        this.size = str;
    }

    public final long getModifiedDate() {
        return this.modifiedDate;
    }

    public final void setModifiedDate(long j) {
        this.modifiedDate = j;
    }

    public final int getFileType() {
        return this.fileType;
    }

    public final void setFileType(int i) {
        this.fileType = i;
    }

    public final boolean isSelected() {
        return this.isSelected;
    }

    public final void setSelected(boolean z) {
        this.isSelected = z;
    }

    public final boolean isFavorite() {
        return this.isFavorite;
    }

    public final void setFavorite(boolean z) {
        this.isFavorite = z;
    }
}
