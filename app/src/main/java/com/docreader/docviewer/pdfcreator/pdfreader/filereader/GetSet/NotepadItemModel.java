package com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet;

public final class NotepadItemModel {
    private String content;
    private String date;
    private int f144id;
    private int pin;
    private int them;
    private String title;

    public NotepadItemModel(int i, String str, String str2, String str3, int i2, int i3) {
        this.f144id = i;
        this.title = str;
        this.content = str2;
        this.date = str3;
        this.them = i2;
        this.pin = i3;
    }

    public final String getContent() {
        return this.content;
    }

    public final void setContent(String str) {
        this.content = str;
    }

    public final String getDate() {
        return this.date;
    }

    public final void setDate(String str) {
        this.date = str;
    }

    public final int getId() {
        return this.f144id;
    }

    public final void setId(int i) {
        this.f144id = i;
    }

    public final int getPin() {
        return this.pin;
    }

    public final void setPin(int i) {
        this.pin = i;
    }

    public final int getThem() {
        return this.them;
    }

    public final void setThem(int i) {
        this.them = i;
    }

    public final String getTitle() {
        return this.title;
    }

    public final void setTitle(String str) {
        this.title = str;
    }
}
