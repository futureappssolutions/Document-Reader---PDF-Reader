package com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet;


public final class DocumentFiles {
    private int fileType;
    private int f142id;
    private String createdAt;
    private String fileContent;
    private String fileName;
    private String updatedAt;

    public final int getId() {
        return this.f142id;
    }

    public final void setId(int i) {
        this.f142id = i;
    }

    public final int getFileType() {
        return this.fileType;
    }

    public final void setFileType(int i) {
        this.fileType = i;
    }

    public final String getFileName() {
        return this.fileName;
    }

    public final void setFileName(String str) {
        this.fileName = str;
    }

    public final String getFileContent() {
        return this.fileContent;
    }

    public final void setFileContent(String str) {
        this.fileContent = str;
    }

    public final String getCreatedAt() {
        return this.createdAt;
    }

    public final void setCreatedAt(String str) {
        this.createdAt = str;
    }

    public final String getUpdatedAt() {
        return this.updatedAt;
    }

    public final void setUpdatedAt(String str) {
        this.updatedAt = str;
    }
}
