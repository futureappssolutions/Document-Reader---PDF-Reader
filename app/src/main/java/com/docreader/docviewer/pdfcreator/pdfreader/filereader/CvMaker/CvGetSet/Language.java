package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet;

import android.os.Parcel;
import android.os.Parcelable;

public class Language implements Parcelable {
    public static final Parcelable.Creator<Language> CREATOR = new Parcelable.Creator<Language>() {
        public Language createFromParcel(Parcel parcel) {
            return new Language(parcel);
        }

        public Language[] newArray(int i) {
            return new Language[i];
        }
    };
    private String name;
    private int ratting;

    @Override
    public int describeContents() {
        return 0;
    }

    public Language() {
        this("English", 5);
    }

    public Language(String str, int i) {
        this.name = str;
        this.ratting = i;
    }

    protected Language(Parcel parcel) {
        this.name = parcel.readString();
        this.ratting = parcel.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeInt(this.ratting);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getRatting() {
        return this.ratting;
    }

    public void setRatting(int i) {
        this.ratting = i;
    }
}
